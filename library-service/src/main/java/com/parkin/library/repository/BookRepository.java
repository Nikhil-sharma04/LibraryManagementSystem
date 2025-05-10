package com.parkin.library.repository;

import com.parkin.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookRepository  extends JpaRepository<Book, UUID> {
}
