package com.grpc.server.domain.model;

public record Book(String isbn, String title, String author, int pages, int year) {}
