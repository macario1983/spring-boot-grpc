package com.grpc.server.adapter.outbound.dynamodb;

import com.grpc.server.domain.model.Book;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class BookEntity {

    private String isbn;
    private String title;
    private String author;
    private int pages;
    private int year;

    public BookEntity() {}

    public BookEntity(Book book) {
        this.isbn = book.isbn();
        this.title = book.title();
        this.author = book.author();
        this.pages = book.pages();
        this.year = book.year();
    }

    @DynamoDbPartitionKey
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public int getPages() { return pages; }
    public void setPages(int pages) { this.pages = pages; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public Book toDomain() {
        return new Book(isbn, title, author, pages, year);
    }
}
