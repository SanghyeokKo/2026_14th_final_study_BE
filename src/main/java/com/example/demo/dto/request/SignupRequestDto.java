package com.example.demo.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record SignupRequestDto(
        @Schema(description = "사용자 이메일", example = "user@example.com")
        String email,

        @Schema(description = "비밀번호", example = "password123")
        String password,

        @Schema(description = "닉네임", example = "아기사자")
        String nickname
) {}