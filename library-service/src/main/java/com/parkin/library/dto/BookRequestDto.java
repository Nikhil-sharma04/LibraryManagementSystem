package com.parkin.library.dto;

import jakarta.validation.constraints.NotBlank;

public class BookRequestDto {
    @NotBlank
    private String title;
    @NotBlank
    private String author;
}
