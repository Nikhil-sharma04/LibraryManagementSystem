package com.parkin.library.service;

import com.parkin.library.Exception.*;
import com.parkin.library.entity.Book;
import com.parkin.library.entity.Member;
import com.parkin.library.repository.BookRepository;
import com.parkin.library.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MemberService {
    private static final Logger logger = LoggerFactory.getLogger(MemberService.class);
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;

    public MemberService(MemberRepository memberRepository, BookRepository bookRepository) {
        this.memberRepository = memberRepository;
        this.bookRepository = bookRepository;
    }

    // Add a new member
    public Member addMember(Member member) {
        if (memberRepository.existsByEmailAddress(member.getEmailAddress())) {
            logger.error("Email address {} is already in use.", member.getEmailAddress());
            throw new DuplicateEmailException("Email address already in use");
        }
        return memberRepository.save(member);
    }

    // Get all members
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    // Borrow a book for a member
    @Transactional
    public void borrowBook(Long memberId, UUID bookId) {
        logger.info("borrowBook()");
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with ID: " + memberId));

        if (member.getBorrowedBooks().size() >= 3) {
            throw new LimitExceededException("A member can only borrow up to 3 books.");
        }

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + bookId));

        if (!book.isAvailable()) {
            throw new BookUnavailableException("Book is already borrowed.");
        }

        book.setAvailable(false);
        member.getBorrowedBooks().add(book);

        bookRepository.save(book);
        memberRepository.save(member);
    }

    // Return a book from a member
    public void returnBook(Long memberId, UUID bookId) {
        logger.info("returnBook()");
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with ID: " + memberId));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + bookId));

        if (!member.getBorrowedBooks().contains(book)) {
            throw new BookNotFoundException("This book is not borrowed by the member.");
        }

        member.getBorrowedBooks().remove(book);
        book.setAvailable(true);

        bookRepository.save(book);
        memberRepository.save(member);
    }

    // Get member by ID
    public Member getMemberById(Long memberId) {
        logger.info("getMemberById()");
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with ID: " + memberId));
    }
}
