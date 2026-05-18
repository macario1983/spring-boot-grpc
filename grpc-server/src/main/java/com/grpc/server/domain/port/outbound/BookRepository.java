package com.grpc.server.domain.port.outbound;

import java.util.List;
import java.util.Optional;

import com.grpc.server.domain.model.Book;

public interface BookRepository {
    Book save(Book book);
    Optional<Book> findByIsbn(String isbn);
    List<Book> findAll();
}
