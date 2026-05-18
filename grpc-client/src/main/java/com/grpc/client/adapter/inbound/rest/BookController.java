package com.grpc.client.adapter.inbound.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grpc.client.domain.model.Book;
import com.grpc.client.domain.port.inbound.BookUseCase;

@RestController
@RequestMapping("/books")
public class BookController {

    private static final Logger log = LoggerFactory.getLogger(BookController.class);

    private final BookUseCase useCase;

    public BookController(BookUseCase useCase) {
        this.useCase = useCase;
    }

    @GetMapping("/{isbn}")
    public Book getBook(@PathVariable String isbn) {
        log.info("GET /books/{}", isbn);
        Book book = useCase.getBook(isbn);
        log.info("GET /books/{} -> {}", isbn, book.title());
        return book;
    }

    @PostMapping
    public Book createBook(@RequestBody Book book) {
        log.info("POST /books {}", book.isbn());
        Book created = useCase.createBook(book);
        log.info("POST /books {} -> created", created.isbn());
        return created;
    }

    @GetMapping
    public List<Book> listBooks() {
        log.info("GET /books");
        List<Book> books = useCase.listBooks();
        log.info("GET /books -> {} results", books.size());
        return books;
    }
}
