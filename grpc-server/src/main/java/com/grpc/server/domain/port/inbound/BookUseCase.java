package com.grpc.server.domain.port.inbound;

import java.util.List;

import com.grpc.server.domain.model.Book;

public interface BookUseCase {
    Book getBook(String isbn);
    Book createBook(Book book);
    List<Book> listBooks();
}
