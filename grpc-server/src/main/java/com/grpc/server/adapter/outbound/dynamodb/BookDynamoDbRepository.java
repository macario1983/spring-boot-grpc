package com.grpc.server.adapter.outbound.dynamodb;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.grpc.server.domain.model.Book;
import com.grpc.server.domain.port.outbound.BookRepository;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
public class BookDynamoDbRepository implements BookRepository {

    private final DynamoDbTable<BookEntity> table;

    public BookDynamoDbRepository(DynamoDbEnhancedClient client) {
        this.table = client.table("books", TableSchema.fromBean(BookEntity.class));
    }

    @Override
    public Book save(Book book) {
        BookEntity entity = new BookEntity(book);
        table.putItem(entity);
        return book;
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        BookEntity entity = table.getItem(Key.builder().partitionValue(isbn).build());
        return Optional.ofNullable(entity).map(BookEntity::toDomain);
    }

    @Override
    public List<Book> findAll() {
        return table.scan().items().stream()
                .map(BookEntity::toDomain)
                .toList();
    }
}
