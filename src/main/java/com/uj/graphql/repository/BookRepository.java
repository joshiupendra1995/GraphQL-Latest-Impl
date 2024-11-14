package com.uj.graphql.repository;

import com.uj.graphql.entity.Book;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@Repository
public class BookRepository {

    public Flux<Book> books = Flux.fromIterable(List.of(new Book(1, "title1", "author1", LocalDate.now().minusDays(1), true), new Book(2, "title2", "author2", LocalDate.now().minusDays(2), true), new Book(3, "title3", "author3", LocalDate.now().minusDays(3), true)));

    public Mono<Book> findBookByTitle(String title) {
        return books.filter(b -> b.getTitle().equalsIgnoreCase(title)).singleOrEmpty();

    }

    public Mono<Book> findBookByAuthor(String author) {
        return books.filter(b -> b.getAuthor().equalsIgnoreCase(author)).singleOrEmpty();
    }

    public Mono<Book> findBookByAuthorAndTitle(String author, String title) {
        return books.filter(b -> b.getAuthor().equalsIgnoreCase(author) && b.getTitle().equalsIgnoreCase(title)).singleOrEmpty();

    }

    public Mono<Book> addBook(Book book) {
        Mono<Book> bookMono = Mono.just(book);
        books.concatWith(bookMono);
        return bookMono;
    }

    public void deleteById(Integer id) {
        books.filter(b -> b.getId().equals(id)).map(book -> {
            book.setActive(false);
            return book;
        });
    }

    public void editBook(Book book) {
        books.filter(b -> b.getId().equals(book.getId())).map(b -> {
            b.setActive(book.isActive());
            b.setTitle(book.getTitle());
            b.setAuthor(book.getAuthor());
            b.setPublishedDate(book.getPublishedDate());
            return b;
        });
    }
}
