package com.parkin.library.service;
import com.parkin.library.entity.Book;
import com.parkin.library.Exception.BookNotFoundException;
import com.parkin.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book sampleBook;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleBook = new Book("Effective Java", "Joshua Bloch");
        sampleBook.setBookId(UUID.randomUUID());
    }

    @Test
    void testAddBook() {
        when(bookRepository.save(any(Book.class))).thenReturn(sampleBook);
        Book saved = bookService.addBook(sampleBook);
        assertEquals("Effective Java", saved.getTitle());
    }

    @Test
    void testGetAllBooks() {
        when(bookRepository.findAll()).thenReturn(List.of(sampleBook));
        List<Book> books = bookService.getAllBooks();
        assertEquals(1, books.size());
    }

    @Test
    void testGetAvailableBooks() {
        when(bookRepository.findAll()).thenReturn(List.of(sampleBook));
        List<Book> availableBooks = bookService.getAvailableBooks();
        assertEquals(1, availableBooks.size());
    }

    @Test
    void testGetBookById_Success() {
        when(bookRepository.findById(sampleBook.getBookId())).thenReturn(Optional.of(sampleBook));
        Book found = bookService.getBookById(sampleBook.getBookId());
        assertNotNull(found);
        assertEquals(sampleBook.getTitle(), found.getTitle());
    }

    @Test
    void testGetBookById_NotFound() {
        UUID fakeId = UUID.randomUUID();
        when(bookRepository.findById(fakeId)).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(fakeId));
    }
}
