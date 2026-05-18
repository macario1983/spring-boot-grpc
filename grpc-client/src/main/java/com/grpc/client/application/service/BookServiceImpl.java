package com.grpc.client.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.grpc.client.adapter.outbound.grpc.BookGrpcClient;
import com.grpc.client.domain.model.Book;
import com.grpc.client.domain.port.inbound.BookUseCase;

@Service
public class BookServiceImpl implements BookUseCase {

    private final BookGrpcClient client;

    public BookServiceImpl(BookGrpcClient client) {
        this.client = client;
    }

    @Override
    public Book getBook(String isbn) {
        return client.getBook(isbn);
    }

    @Override
    public Book createBook(Book book) {
        return client.createBook(book);
    }

    @Override
    public List<Book> listBooks() {
        return client.listBooks();
    }
}
