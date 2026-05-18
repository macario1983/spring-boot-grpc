package com.grpc.server.domain.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.grpc.server.domain.model.Book;
import com.grpc.server.domain.port.inbound.BookUseCase;
import com.grpc.server.domain.port.outbound.BookRepository;

@Service
public class BookServiceImpl implements BookUseCase {

    private static final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);

    private final BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Book getBook(String isbn) {
        log.debug("Find book isbn={}", isbn);
        return repository.findByIsbn(isbn)
                .orElseThrow(() -> {
                    log.warn("Book not found isbn={}", isbn);
                    return new IllegalArgumentException("Book not found: " + isbn);
                });
    }

    @Override
    public Book createBook(Book book) {
        log.debug("Save book isbn={}", book.isbn());
        Book saved = repository.save(book);
        log.debug("Book saved isbn={}", saved.isbn());
        return saved;
    }

    @Override
    public List<Book> listBooks() {
        log.debug("Find all books");
        List<Book> books = repository.findAll();
        log.debug("Found {} books", books.size());
        return books;
    }
}
