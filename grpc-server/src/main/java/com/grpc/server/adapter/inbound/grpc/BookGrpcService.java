package com.grpc.server.adapter.inbound.grpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(BookGrpcService.class);

    private final BookUseCase useCase;

    public BookGrpcService(BookUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public void getBook(GetBookRequest request, StreamObserver<BookResponse> responseObserver) {
        log.info("gRPC GetBook isbn={}", request.getIsbn());
        Book book = useCase.getBook(request.getIsbn());
        responseObserver.onNext(toResponse(book));
        responseObserver.onCompleted();
        log.info("gRPC GetBook isbn={} -> {}", request.getIsbn(), book.title());
    }

    @Override
    public void createBook(CreateBookRequest request, StreamObserver<BookResponse> responseObserver) {
        log.info("gRPC CreateBook isbn={} title={}", request.getIsbn(), request.getTitle());
        Book book = new Book(request.getIsbn(), request.getTitle(), request.getAuthor(),
                request.getPages(), request.getYear());
        Book created = useCase.createBook(book);
        responseObserver.onNext(toResponse(created));
        responseObserver.onCompleted();
        log.info("gRPC CreateBook isbn={} -> created", created.isbn());
    }

    @Override
    public void listBooks(ListBooksRequest request, StreamObserver<ListBooksResponse> responseObserver) {
        log.info("gRPC ListBooks");
        var response = ListBooksResponse.newBuilder()
                .addAllBooks(useCase.listBooks().stream()
                        .map(BookGrpcService::toResponse)
                        .toList())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
        log.info("gRPC ListBooks -> {} results", response.getBooksCount());
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
