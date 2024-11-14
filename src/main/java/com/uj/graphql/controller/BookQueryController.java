package com.uj.graphql.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.uj.graphql.entity.Book;
import com.uj.graphql.repository.BookRepository;
import com.uj.graphql.service.GraphQLService;
import com.uj.graphql.types.BookFilter;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Controller
public class BookQueryController {

    private final BookRepository bookRepository;
    private final GraphQLService graphQLService;

    public BookQueryController(BookRepository bookRepository, GraphQLService graphQLService) {
        this.bookRepository = bookRepository;
        this.graphQLService = graphQLService;
    }

    public static void downloadFile(String fileDownloadUri, String destinationPath) throws IOException {
        URL url = new URL(fileDownloadUri);

        try (InputStream in = url.openStream()) {
            Path destination = Path.of(destinationPath);
            Files.copy(in, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    @GetMapping("/person/{personId}")
    @ResponseBody
    public Mono<JsonNode> getPersonDetail(@PathVariable String personId) throws IOException {
        String personQuery = new String(Files.readAllBytes(Paths.get("src/main/resources/graphql/person.gql")));
        Map<String, Object> variables = new HashMap<>();
        variables.put("personId", personId);
        String path = "person";
        return graphQLService.executeGraphQLQuery(personQuery, variables, path);
    }

    @GetMapping("/v1/getAllUsers")
    public Flux<JsonNode> getAllUsers() {
        String query = """
                query{
                  getAllUsers {
                    username
                    email
                    csid
                    employeeEmailAddress
                  }
                }
                """;
        return graphQLService.getAllUsers(query).flatMapMany(Flux::fromIterable);
    }


}
