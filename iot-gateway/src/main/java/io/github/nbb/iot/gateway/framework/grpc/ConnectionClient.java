package io.github.nbb.iot.gateway.framework.grpc;

import cn.hutool.core.lang.UUID;
import io.github.nbb.iot.grpcapi.ConnectionServiceGrpc;
import io.github.nbb.iot.grpcapi.IotGrpcService.ClientList;
import io.github.nbb.iot.grpcapi.IotGrpcService.HeartbeatRequest;
import io.github.nbb.iot.grpcapi.IotGrpcService.HeartbeatResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ConnectionClient {
    private final ManagedChannel channel;
    private final ConnectionServiceGrpc.ConnectionServiceStub asyncStub;
    private final String clientId;
    private StreamObserver<HeartbeatRequest> requestObserver;
    private final CountDownLatch finishLatch = new CountDownLatch(1);

    public ConnectionClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build());
    }

    private ConnectionClient(ManagedChannel channel) {
        this.channel = channel;
        this.asyncStub = ConnectionServiceGrpc.newStub(channel);
        this.clientId = UUID.randomUUID().toString();
    }

    public void shutdown() throws InterruptedException {
        if (requestObserver != null) {
            requestObserver.onCompleted();
        }
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        finishLatch.await(1, TimeUnit.SECONDS);
    }

    public void startConnection() {
        requestObserver = asyncStub.connect(new StreamObserver<HeartbeatResponse>() {
            @Override
            public void onNext(HeartbeatResponse response) {
                log.info("Received heartbeat response at: " + response.getTimestamp());
            }

            @Override
            public void onError(Throwable t) {
                finishLatch.countDown();
            }

            @Override
            public void onCompleted() {
                log.info("Server closed the connection");
                finishLatch.countDown();
            }
        });

        // 启动心跳发送线程
        Thread heartbeatThread = new Thread(() -> {
            try {
                int counter = 0;
                while (finishLatch.getCount() > 0) {
                    HeartbeatRequest request = HeartbeatRequest.newBuilder()
                            .setClientId(clientId)
                            .setMetadata("Client metadata " + counter++)
                            .build();
                    requestObserver.onNext(request);
                    Thread.sleep(5000); // 每5秒发送一次心跳
                }
            } catch (RuntimeException e) {
                log.error("Exception while sending heartbeat", e);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                try {
                    requestObserver.onCompleted();
                } catch (RuntimeException e) {
                    log.error("Error closing request observer", e);
                }
            }
        });

        heartbeatThread.setDaemon(true);
        heartbeatThread.start();
    }


    public static void main(String[] args) throws Exception {
        ConnectionClient client = new ConnectionClient("localhost", 50051);

        try {
            client.startConnection();

            System.out.println("Client started. Press Enter to exit...");
            System.in.read();
        } finally {
            client.shutdown();
        }
    }
}