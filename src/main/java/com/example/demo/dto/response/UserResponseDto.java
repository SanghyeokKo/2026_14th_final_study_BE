package com.example.demo.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserResponseDto(
        @Schema(description = "사용자 이메일", example = "user@example.com")
        String email,

        @Schema(description = "닉네임", example = "아기사자")
        String nickname,

        @Schema(description = "접근 권한 등급", example = "USER")
        String role
) {}