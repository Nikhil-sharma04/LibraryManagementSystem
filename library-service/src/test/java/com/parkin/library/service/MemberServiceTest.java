package com.parkin.library.service;

import com.parkin.library.entity.Book;
import com.parkin.library.entity.Member;
import com.parkin.library.Exception.BookAlreadyBorrowedException;
import com.parkin.library.Exception.BookNotFoundException;
import com.parkin.library.Exception.LimitExceededException;
import com.parkin.library.Exception.MemberNotFoundException;
import com.parkin.library.repository.BookRepository;
import com.parkin.library.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private MemberService memberService;

    private Member member;
    private Book book;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        member = new Member("Alice");
        member.setMemberId(1L);  // memberId is now a Long

        book = new Book("Clean Code", "Robert Martin");
        book.setBookId(UUID.randomUUID());
    }

    @Test
    void testAddMember() {
        when(memberRepository.save(any(Member.class))).thenReturn(member);
        Member saved = memberService.addMember(member);
        assertEquals("Alice", saved.getName());
    }

    @Test
    void testBorrowBook_Success() {
        when(memberRepository.findById(member.getMemberId())).thenReturn(Optional.of(member));
        when(bookRepository.findById(book.getBookId())).thenReturn(Optional.of(book));
        memberService.borrowBook(member.getMemberId(), book.getBookId());
        assertFalse(book.isAvailable());
        assertEquals(1, member.getBorrowedBooks().size());
    }

    @Test
    void testBorrowBook_BookAlreadyBorrowed() {
        book.setAvailable(false);
        when(memberRepository.findById(member.getMemberId())).thenReturn(Optional.of(member));
        when(bookRepository.findById(book.getBookId())).thenReturn(Optional.of(book));
        assertThrows(BookAlreadyBorrowedException.class,
                () -> memberService.borrowBook(member.getMemberId(), book.getBookId()));
    }

    @Test
    void testBorrowBook_LimitExceeded() {
        member.getBorrowedBooks().addAll(List.of(new Book(), new Book(), new Book()));
        when(memberRepository.findById(member.getMemberId())).thenReturn(Optional.of(member));
        when(bookRepository.findById(book.getBookId())).thenReturn(Optional.of(book));
        assertThrows(LimitExceededException.class,
                () -> memberService.borrowBook(member.getMemberId(), book.getBookId()));
    }

    @Test
    void testReturnBook_Success() {
        member.getBorrowedBooks().add(book);
        book.setAvailable(false);
        when(memberRepository.findById(member.getMemberId())).thenReturn(Optional.of(member));
        when(bookRepository.findById(book.getBookId())).thenReturn(Optional.of(book));
        memberService.returnBook(member.getMemberId(), book.getBookId());
        assertTrue(book.isAvailable());
        assertTrue(member.getBorrowedBooks().isEmpty());
    }

    @Test
    void testReturnBook_NotFound() {
        UUID fakeBookId = UUID.randomUUID();
        when(memberRepository.findById(member.getMemberId())).thenReturn(Optional.of(member));
        when(bookRepository.findById(fakeBookId)).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class,
                () -> memberService.returnBook(member.getMemberId(), fakeBookId));
    }

    @Test
    void testGetMemberById_Success() {
        when(memberRepository.findById(member.getMemberId())).thenReturn(Optional.of(member));
        Member result = memberService.getMemberById(member.getMemberId());
        assertEquals("Alice", result.getName());
    }

    @Test
    void testGetMemberById_NotFound() {
        Long fakeId = 999L;
        when(memberRepository.findById(fakeId)).thenReturn(Optional.empty());
        assertThrows(MemberNotFoundException.class,
                () -> memberService.getMemberById(fakeId));
    }
}
