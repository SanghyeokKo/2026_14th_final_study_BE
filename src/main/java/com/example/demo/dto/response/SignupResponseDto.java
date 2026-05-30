package com.example.demo.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record SignupResponseDto(
        @Schema(description = "생성된 유저 고유 ID", example = "1")
        Long userId
) {}