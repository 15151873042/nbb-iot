package io.github.nbb.iot.console.framework.grpc;

import io.github.nbb.iot.grpcapi.ConnectionServiceGrpc;
import io.github.nbb.iot.grpcapi.IotGrpcService.*;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ConnectionServer {
    private final int port;
    private final Server server;
    private final ConnectionServiceImpl connectionService;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public ConnectionServer(int port) {
        this.port = port;
        this.connectionService = new ConnectionServiceImpl();
        this.server = ServerBuilder.forPort(port)
                .addService(connectionService)
                .build();

        // 启动定期检查任务
        scheduler.scheduleAtFixedRate(connectionService::checkClientStatus,
                10, 10, TimeUnit.SECONDS);
    }

    public void start() throws IOException {
        server.start();
        log.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            ConnectionServer.this.stop();
            System.err.println("*** server shut down");
        }));
    }

    public void stop() {
        if (server != null) {
            server.shutdown();
            scheduler.shutdown();
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws Exception {
        ConnectionServer server = new ConnectionServer(50051);
        server.start();
        server.blockUntilShutdown();
    }

    static class ConnectionServiceImpl extends ConnectionServiceGrpc.ConnectionServiceImplBase {
        // 存储客户端信息和对应的流观察者
        private final ConcurrentHashMap<String, ClientInfo> clients = new ConcurrentHashMap<>();

        @Override
        public StreamObserver<HeartbeatRequest> connect(StreamObserver<HeartbeatResponse> responseObserver) {
            return new StreamObserver<HeartbeatRequest>() {
                private String clientId;

                @Override
                public void onNext(HeartbeatRequest request) {
                    clientId = request.getClientId();

                    // 更新客户端信息
                    ClientInfo clientInfo = clients.computeIfAbsent(clientId,
                            k -> new ClientInfo(clientId, responseObserver));
                    clientInfo.setLastSeen(Instant.now());
                    clientInfo.setMetadata(request.getMetadata());
                    clientInfo.setOnline(true);

                    // 发送心跳响应
                    HeartbeatResponse response = HeartbeatResponse.newBuilder()
                            .setTimestamp(System.currentTimeMillis())
                            .setMessage("Heartbeat received")
                            .build();
                    responseObserver.onNext(response);

                    log.info("Heartbeat received from client: " + clientId);
                }

                @Override
                public void onError(Throwable t) {
                    if (clientId != null) {
                        log.warn("Client disconnected with error: " + clientId + ", error: " + t.getMessage());
                        clients.computeIfPresent(clientId, (k, v) -> {
                            v.setOnline(false);
                            return v;
                        });
                    }
                    responseObserver.onCompleted();
                }

                @Override
                public void onCompleted() {
                    if (clientId != null) {
                        log.info("Client disconnected: " + clientId);
                        clients.computeIfPresent(clientId, (k, v) -> {
                            v.setOnline(false);
                            return v;
                        });
                    }
                    responseObserver.onCompleted();
                }
            };
        }

        @Override
        public void getOnlineClients(Empty request, StreamObserver<ClientList> responseObserver) {
            ClientList.Builder listBuilder = ClientList.newBuilder();

            // 收集所有在线客户端
            clients.forEach((clientId, clientInfo) -> {
                listBuilder.addClients(Client.newBuilder()
                        .setClientId(clientId)
                        .setLastSeen(clientInfo.getLastSeen().toString())
                        .setMetadata(clientInfo.getMetadata())
                        .setOnline(clientInfo.isOnline())
                        .build());
            });

            responseObserver.onNext(listBuilder.build());
            responseObserver.onCompleted();
        }

        // 定期检查客户端状态
        public void checkClientStatus() {
            Instant now = Instant.now();
            List<String> inactiveClients = new ArrayList<>();

            clients.forEach((clientId, clientInfo) -> {
                // 如果超过30秒没有收到心跳，标记为离线
                if (clientInfo.isOnline() &&
                        now.minusSeconds(30).isAfter(clientInfo.getLastSeen())) {
                    clientInfo.setOnline(false);
                    inactiveClients.add(clientId);
                    log.info("Client marked as offline due to inactivity: " + clientId);
                }
            });

            // 可以选择移除长时间离线的客户端
            // inactiveClients.forEach(clients::remove);
        }
    }

    static class ClientInfo {
        private final String clientId;
        private final StreamObserver<HeartbeatResponse> responseObserver;
        private Instant lastSeen;
        private String metadata;
        private boolean online;

        public ClientInfo(String clientId, StreamObserver<HeartbeatResponse> responseObserver) {
            this.clientId = clientId;
            this.responseObserver = responseObserver;
            this.lastSeen = Instant.now();
            this.online = true;
        }

        public String getClientId() {
            return clientId;
        }

        public Instant getLastSeen() {
            return lastSeen;
        }

        public void setLastSeen(Instant lastSeen) {
            this.lastSeen = lastSeen;
        }

        public String getMetadata() {
            return metadata;
        }

        public void setMetadata(String metadata) {
            this.metadata = metadata;
        }

        public boolean isOnline() {
            return online;
        }

        public void setOnline(boolean online) {
            this.online = online;
        }
    }
}    
