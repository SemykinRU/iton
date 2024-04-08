package ru.iton.test.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import ru.iton.test.server.service.NumberServiceExt;

import java.io.IOException;

public class Main {
    private static final Integer SERVER_PORT = 8080;

    public static void main(String[] args) throws IOException, InterruptedException {
        final Server server = ServerBuilder
            .forPort(SERVER_PORT)
            .addService(new NumberServiceExt())
            .build();
        server.start();
        server.awaitTermination();
    }
}
