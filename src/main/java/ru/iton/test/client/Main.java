package ru.iton.test.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import ru.iton.test.grpc.NumberRequest;
import ru.iton.test.grpc.NumberResponse;
import ru.iton.test.grpc.NumberServiceGrpc;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Main {
    private static final Integer BATCH_SIZE = 50;
    private static final Integer ONE_SECOND_SLEEP = 1;
    private static final Integer SERVER_PORT = 8080;
    private static final Integer LAST_VALUE = 30;

    public static void main(String[] args) throws InterruptedException {
        final ManagedChannel channel =
            ManagedChannelBuilder.forAddress("localhost", SERVER_PORT)
                .usePlaintext()
                .build();
        final NumberServiceGrpc.NumberServiceBlockingStub stub =
            NumberServiceGrpc.newBlockingStub(channel);
        int currentValue = 0;
        log.info("numbers Client is starting...");
        final Iterator<NumberResponse> value = stub.getValue(NumberRequest.newBuilder()
            .setFirstValue(0)
            .setLastValue(LAST_VALUE)
            .build());
        int valueFromServer;
        for (int i = 0; i < BATCH_SIZE; i++) {
            if (value.hasNext()) {
                log.info("currentValue:" + (++currentValue));
                valueFromServer = value.next().getValue();
                log.info("new value: " + valueFromServer);

            } else {
                break;
            }
            TimeUnit.SECONDS.sleep(ONE_SECOND_SLEEP);
            if (value.hasNext()) {
                log.info("currentValue:" + (currentValue += valueFromServer + 1));
            }
        }
        log.info("request completed");
        channel.shutdown();
    }
}
