package com.grpc.client.adapter.outbound.grpc;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.grpc.client.BookResponse;
import com.grpc.client.BookServiceGrpc.BookServiceBlockingStub;
import com.grpc.client.CreateBookRequest;
import com.grpc.client.GetBookRequest;
import com.grpc.client.ListBooksRequest;
import com.grpc.client.domain.model.Book;

@Component
public class BookGrpcClient {

    private static final Logger log = LoggerFactory.getLogger(BookGrpcClient.class);

    private final BookServiceBlockingStub stub;

    public BookGrpcClient(BookServiceBlockingStub stub) {
        this.stub = stub;
    }

    public Book getBook(String isbn) {
        log.debug("gRPC GetBook isbn={}", isbn);
        BookResponse response = stub.getBook(GetBookRequest.newBuilder().setIsbn(isbn).build());
        log.debug("gRPC GetBook isbn={} -> {}", isbn, response.getTitle());
        return toDomain(response);
    }

    public Book createBook(Book book) {
        log.debug("gRPC CreateBook isbn={}", book.isbn());
        var request = CreateBookRequest.newBuilder()
                .setIsbn(book.isbn())
                .setTitle(book.title())
                .setAuthor(book.author())
                .setPages(book.pages())
                .setYear(book.year())
                .build();
        Book result = toDomain(stub.createBook(request));
        log.debug("gRPC CreateBook isbn={} -> ok", book.isbn());
        return result;
    }

    public List<Book> listBooks() {
        log.debug("gRPC ListBooks");
        var books = stub.listBooks(ListBooksRequest.getDefaultInstance())
                .getBooksList().stream()
                .map(BookGrpcClient::toDomain)
                .toList();
        log.debug("gRPC ListBooks -> {} results", books.size());
        return books;
    }

    private static Book toDomain(BookResponse r) {
        return new Book(r.getIsbn(), r.getTitle(), r.getAuthor(), r.getPages(), r.getYear());
    }
}
