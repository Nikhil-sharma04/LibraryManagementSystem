package com.parkin.library.controller;

import com.parkin.library.entity.Member;
import com.parkin.library.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/library/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Member> addMember(@Valid @RequestBody Member member) {
        return ResponseEntity.ok(memberService.addMember(member));
    }

    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getMemberById(id));
    }

    @PostMapping("/{memberId}/borrow/{bookId}")
    public ResponseEntity<String> borrowBook(@PathVariable Long memberId, @PathVariable UUID bookId) {
        memberService.borrowBook(memberId, bookId);
        return ResponseEntity.ok("Book borrowed successfully");
    }

    @PostMapping("/{memberId}/return/{bookId}")
    public ResponseEntity<String> returnBook(@PathVariable Long memberId, @PathVariable UUID bookId) {
        memberService.returnBook(memberId, bookId);
        return ResponseEntity.ok("Book returned successfully");
    }
}
