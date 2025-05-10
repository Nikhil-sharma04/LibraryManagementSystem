package com.parkin.library.dto;
import jakarta.validation.constraints.NotBlank;
public class MemberRequestDto {
    @NotBlank
    private String name;
    @NotBlank
    private String emailAddress;
}
