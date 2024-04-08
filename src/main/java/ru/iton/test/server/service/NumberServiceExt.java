package ru.iton.test.server.service;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import ru.iton.test.grpc.NumberRequest;
import ru.iton.test.grpc.NumberResponse;
import ru.iton.test.grpc.NumberServiceGrpc;

import java.util.concurrent.TimeUnit;

@Slf4j
public class NumberServiceExt extends NumberServiceGrpc.NumberServiceImplBase {
    private static final Integer TWO_SECOND_SLEEP = 2;

    @Override
    public void getValue(NumberRequest request, StreamObserver<NumberResponse> responseObserver) {
        final int firstValue = request.getFirstValue();
        final int lastValue = request.getLastValue();
        int currentValue = firstValue;
        for (int i = firstValue; currentValue < lastValue; ) {
            try {
                TimeUnit.SECONDS.sleep(TWO_SECOND_SLEEP);
                currentValue = ++i + 1;
                final NumberResponse response =
                    NumberResponse.newBuilder().setValue(currentValue).build();
                responseObserver.onNext(response);
            } catch (InterruptedException e) {
                log.error("Interrupted exception", e);
                throw new RuntimeException("interrupted_error");
            }
        }
        responseObserver.onCompleted();
    }
}
