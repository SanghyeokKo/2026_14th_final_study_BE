package com.example.demo.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequestDto(
        @Schema(description = "사용자 로그인 이메일", example = "user@example.com")
        String email,

        @Schema(description = "사용자 비밀번호", example = "password123")
        String password
) {}