package com.parkin.library.entity;
import jakarta.persistence.*;
import java.util.*;

@Entity
public class Member {
    @Id
    @GeneratedValue
    private Long memberId;

    private String name;

    @Column(unique = true)
    private String emailAddress;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Book> borrowedBooks = new ArrayList<>();

    public Member() {}

    public Member(String name) {
        this.name = name;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }
    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
    public void setBorrowedBooks(List<Book> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }
}
