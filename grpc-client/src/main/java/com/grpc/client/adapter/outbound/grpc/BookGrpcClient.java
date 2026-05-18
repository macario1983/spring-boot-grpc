package com.grpc.client.adapter.outbound.grpc;

import java.util.List;

import org.springframework.stereotype.Component;

import com.grpc.client.BookResponse;
import com.grpc.client.BookServiceGrpc.BookServiceBlockingStub;
import com.grpc.client.CreateBookRequest;
import com.grpc.client.GetBookRequest;
import com.grpc.client.ListBooksRequest;
import com.grpc.client.domain.model.Book;

@Component
public class BookGrpcClient {

    private final BookServiceBlockingStub stub;

    public BookGrpcClient(BookServiceBlockingStub stub) {
        this.stub = stub;
    }

    public Book getBook(String isbn) {
        BookResponse response = stub.getBook(GetBookRequest.newBuilder().setIsbn(isbn).build());
        return toDomain(response);
    }

    public Book createBook(Book book) {
        var request = CreateBookRequest.newBuilder()
                .setIsbn(book.isbn())
                .setTitle(book.title())
                .setAuthor(book.author())
                .setPages(book.pages())
                .setYear(book.year())
                .build();
        return toDomain(stub.createBook(request));
    }

    public List<Book> listBooks() {
        return stub.listBooks(ListBooksRequest.getDefaultInstance())
                .getBooksList().stream()
                .map(BookGrpcClient::toDomain)
                .toList();
    }

    private static Book toDomain(BookResponse r) {
        return new Book(r.getIsbn(), r.getTitle(), r.getAuthor(), r.getPages(), r.getYear());
    }
}
