package com.example.demo.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponseDto(
        @Schema(description = "발급된 JWT Access Token", example = "mock_jwt_access_token_value_12345")
        String accessToken,

        @Schema(description = "토큰 타입", example = "Bearer")
        String tokenType
) {}