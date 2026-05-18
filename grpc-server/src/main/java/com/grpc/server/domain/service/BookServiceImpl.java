package com.grpc.server.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.grpc.server.domain.model.Book;
import com.grpc.server.domain.port.inbound.BookUseCase;
import com.grpc.server.domain.port.outbound.BookRepository;

@Service
public class BookServiceImpl implements BookUseCase {

    private final BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Book getBook(String isbn) {
        return repository.findByIsbn(isbn)
                .orElseThrow(() -> new IllegalArgumentException("Book not found: " + isbn));
    }

    @Override
    public Book createBook(Book book) {
        return repository.save(book);
    }

    @Override
    public List<Book> listBooks() {
        return repository.findAll();
    }
}
