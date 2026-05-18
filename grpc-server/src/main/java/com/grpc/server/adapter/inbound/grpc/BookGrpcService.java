package com.grpc.server.adapter.inbound.grpc;

import org.springframework.grpc.server.service.GrpcService;

import com.grpc.server.BookResponse;
import com.grpc.server.BookServiceGrpc;
import com.grpc.server.CreateBookRequest;
import com.grpc.server.GetBookRequest;
import com.grpc.server.ListBooksRequest;
import com.grpc.server.ListBooksResponse;
import com.grpc.server.domain.model.Book;
import com.grpc.server.domain.port.inbound.BookUseCase;

import io.grpc.stub.StreamObserver;

@GrpcService
public class BookGrpcService extends BookServiceGrpc.BookServiceImplBase {

    private final BookUseCase useCase;

    public BookGrpcService(BookUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public void getBook(GetBookRequest request, StreamObserver<BookResponse> responseObserver) {
        Book book = useCase.getBook(request.getIsbn());
        responseObserver.onNext(toResponse(book));
        responseObserver.onCompleted();
    }

    @Override
    public void createBook(CreateBookRequest request, StreamObserver<BookResponse> responseObserver) {
        Book book = new Book(request.getIsbn(), request.getTitle(), request.getAuthor(),
                request.getPages(), request.getYear());
        Book created = useCase.createBook(book);
        responseObserver.onNext(toResponse(created));
        responseObserver.onCompleted();
    }

    @Override
    public void listBooks(ListBooksRequest request, StreamObserver<ListBooksResponse> responseObserver) {
        var response = ListBooksResponse.newBuilder()
                .addAllBooks(useCase.listBooks().stream()
                        .map(BookGrpcService::toResponse)
                        .toList())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private static BookResponse toResponse(Book book) {
        return BookResponse.newBuilder()
                .setIsbn(book.isbn())
                .setTitle(book.title())
                .setAuthor(book.author())
                .setPages(book.pages())
                .setYear(book.year())
                .build();
    }
}
