package com.example.demo.global;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "전역 공통 응답 포맷")
public record ApiResponse<T>(
        @Schema(description = "비즈니스 상태 코드", example = "200")
        int code,

        @Schema(description = "응답 메시지", example = "요청에 성공하였습니다.")
        String message,

        @Schema(description = "실제 데이터 반환 영역")
        T result
) {}