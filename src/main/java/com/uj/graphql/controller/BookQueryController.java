package com.uj.graphql.controller;

import com.uj.graphql.entity.Book;
import com.uj.graphql.repository.BookRepository;
import com.uj.graphql.types.BookFilter;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Controller
public class BookQueryController {

    private final BookRepository bookRepository;

    public BookQueryController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @QueryMapping
    public Flux<Book> allBook() {
        return bookRepository.books;
    }

    @QueryMapping
    public Mono<Book> getBookByTitle(@Argument("filter") BookFilter bookFilter) {
        return bookRepository.findBookByTitle(bookFilter.getTitle());
    }

    @QueryMapping
    public Mono<Book> getBookByAuthor(@Argument("filter") BookFilter bookFilter) {
        return bookRepository.findBookByAuthor(bookFilter.getAuthor());
    }

    @QueryMapping
    public Mono<Book> getBookByAuthorAndTitle(@Argument("author") String author, @Argument("title") String title) {
        return bookRepository.findBookByAuthorAndTitle(author, title);
    }

    @MutationMapping
    public Mono<Book> addBook(@Argument("filter") BookFilter bookFilter) {
        Book book = new Book();
        book.setTitle(bookFilter.getTitle());
        book.setAuthor(bookFilter.getAuthor());
        book.setPublishedDate(LocalDate.parse(bookFilter.getPublishedDate()));
        return bookRepository.addBook(book);
    }

    @MutationMapping
    public void removeBook(@Argument("id") Integer id) {
        bookRepository.deleteById(id);
    }

    @MutationMapping
    public void editBook(@Argument Integer id, @Argument BookFilter bookFilter) {
        Book book = new Book();
        book.setId(id);
        book.setAuthor(bookFilter.getAuthor());
        book.setTitle(bookFilter.getTitle());
        bookRepository.editBook(book);

    }

}
