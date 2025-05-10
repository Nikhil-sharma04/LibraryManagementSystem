package com.parkin.library.repository;

import com.parkin.library.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member,Long> {
    boolean existsByEmailAddress(String emailAddress);
}
