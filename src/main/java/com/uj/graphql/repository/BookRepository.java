package com.uj.graphql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uj.graphql.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

	Book findBookByTitle(String title);

	Book findBookByAuthor(String author);

	Book findBookByAuthorAndTitle(String author, String title);
}
