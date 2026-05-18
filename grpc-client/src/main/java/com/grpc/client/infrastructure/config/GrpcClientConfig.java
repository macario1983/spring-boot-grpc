package com.grpc.client.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.grpc.client.BookServiceGrpc;
import com.grpc.client.BookServiceGrpc.BookServiceBlockingStub;

import io.grpc.ManagedChannelBuilder;

@Configuration
public class GrpcClientConfig {

    @Bean
    public BookServiceBlockingStub bookServiceBlockingStub() {
        var channel = ManagedChannelBuilder
                .forAddress("127.0.0.1", 8080)
                .usePlaintext()
                .build();
        return BookServiceGrpc.newBlockingStub(channel);
    }
}
