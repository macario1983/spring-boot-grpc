package com.grpc.client.domain.port.inbound;

import java.util.List;

import com.grpc.client.domain.model.Book;

public interface BookUseCase {
    Book getBook(String isbn);
    Book createBook(Book book);
    List<Book> listBooks();
}
