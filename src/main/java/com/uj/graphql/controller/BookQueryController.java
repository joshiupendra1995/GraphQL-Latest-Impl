package com.uj.graphql.controller;

import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import com.uj.graphql.entity.Book;
import com.uj.graphql.repository.BookRepository;
import com.uj.graphql.types.BookFilter;
import org.springframework.graphql.data.method.annotation.Argument;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Controller
public class BookQueryController {

    private final BookRepository bookRepository;

    public BookQueryController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @QueryMapping
    public Flux<Book> allBook() {
        return Flux.fromIterable(bookRepository.findAll());
    }

    @QueryMapping
    public Mono<Book> getBookByTitle(@Argument("filter") BookFilter bookFilter) {
        return Mono.just(bookRepository.findBookByTitle(bookFilter.getTitle()));
    }

    @QueryMapping
    public Mono<Book> getBookByAuthor(@Argument("filter") BookFilter bookFilter) {
        return Mono.just(bookRepository.findBookByAuthor(bookFilter.getAuthor()));
    }

    @QueryMapping
    public Mono<Book> getBookByAuthorAndTitle(@Argument("author") String author, @Argument("title") String title) {
        return Mono.just(bookRepository.findBookByAuthorAndTitle(author, title));
    }

    @MutationMapping
    public Mono<Book> addBook(@Argument("filter") BookFilter bookFilter) {
        Book book = new Book();
        book.setTitle(bookFilter.getTitle());
        book.setAuthor(bookFilter.getAuthor());
        return Mono.just(bookRepository.save(book));
    }

    @MutationMapping
    public void removeBook(@Argument("id") Integer id){
         bookRepository.deleteById(id);
    }

    @MutationMapping
    public Mono<Book> editBook(@Argument Integer id, @Argument BookFilter bookFilter){
        Optional<Book> book = bookRepository.findById(id);
        if(book.isPresent()){
            book.get().setAuthor(bookFilter.getAuthor());
            book.get().setTitle(bookFilter.getTitle());
            return Mono.just(bookRepository.save(book.get()));
        }
        return Mono.empty();

    }

}
