package com.grpc.client.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

import com.grpc.client.BookServiceGrpc;
import com.grpc.client.BookServiceGrpc.BookServiceBlockingStub;

@Configuration
public class GrpcClientConfig {

    @Bean
    public BookServiceBlockingStub bookServiceBlockingStub(GrpcChannelFactory factory) {
        return BookServiceGrpc.newBlockingStub(factory.createChannel("default"));
    }
}
