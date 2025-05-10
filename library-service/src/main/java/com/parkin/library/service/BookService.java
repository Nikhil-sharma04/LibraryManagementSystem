package com.parkin.library.service;
import com.parkin.library.Exception.ResourceNotFoundException;
import com.parkin.library.entity.Book;
import com.parkin.library.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

import java.util.stream.Collectors;

@Service
public class BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);
    private final BookRepository bookRepository;
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book addBook(Book book) {
        logger.info("Adding new book with title: {}", book.getTitle());
        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        logger.info("getAllBooks()");
        return bookRepository.findAll();
    }

    public Book getBookById(UUID bookId) {
        logger.info("getBookById()");
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
    }

    public void updateAvailability(Book book, boolean available) {
        logger.info("updateAvailability()");
        book.setAvailable(available);
        bookRepository.save(book);
    }


    public List<Book> getAvailableBooks() {
        logger.info("getAvailableBooks()");
        return bookRepository.findAll()
                .stream()
                .filter(Book::isAvailable)
                .collect(Collectors.toList());
    }
}
