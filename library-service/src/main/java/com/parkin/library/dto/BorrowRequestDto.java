package com.parkin.library.dto;

import jakarta.validation.constraints.NotNull;

public class BorrowRequestDto {
    @NotNull
    private Long memberId;
    @NotNull
    private Long bookId;
}