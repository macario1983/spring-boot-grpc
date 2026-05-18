package com.grpc.client.adapter.inbound.rest;

import java.util.List;

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

    private final BookUseCase useCase;

    public BookController(BookUseCase useCase) {
        this.useCase = useCase;
    }

    @GetMapping("/{isbn}")
    public Book getBook(@PathVariable String isbn) {
        return useCase.getBook(isbn);
    }

    @PostMapping
    public Book createBook(@RequestBody Book book) {
        return useCase.createBook(book);
    }

    @GetMapping
    public List<Book> listBooks() {
        return useCase.listBooks();
    }
}
