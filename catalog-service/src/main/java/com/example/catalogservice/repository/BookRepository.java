package com.example.catalogservice.repository;

import com.example.catalogservice.domian.Book;

import java.util.Optional;

public interface BookRepository {
    Iterable<Book> findAll();

    Optional<Book> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    Book save(Book book);

    void deleteByIsbn(String isbn);
}
