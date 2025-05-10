package com.parkin.library.controller;
import com.parkin.library.entity.Member;
import com.parkin.library.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/library/borrow")
public class BorrowController {

    private final MemberService memberService;

    public BorrowController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/{memberId}/book/{bookId}")
    public ResponseEntity<String> borrowBook(@PathVariable Long memberId, @PathVariable UUID bookId) {
        memberService.borrowBook(memberId, bookId);
        return ResponseEntity.ok("Book borrowed successfully.");
    }


    @PostMapping("/{memberId}/return/{bookId}")
    public ResponseEntity<String> returnBook(@PathVariable Long memberId, @PathVariable UUID bookId) {
        memberService.returnBook(memberId, bookId);
        return ResponseEntity.ok("Book returned successfully.");
    }


    @GetMapping("/member/{memberId}")
    public ResponseEntity<Member> getMemberWithBorrowedBooks(@PathVariable Long memberId) {
        return ResponseEntity.ok(memberService.getMemberById(memberId));
    }
}
