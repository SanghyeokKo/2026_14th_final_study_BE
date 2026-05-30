package com.example.demo.controller;

import com.example.demo.dto.request.LoginRequestDto;
import com.example.demo.dto.request.SignupRequestDto;
import com.example.demo.dto.response.LoginResponseDto;
import com.example.demo.dto.response.SignupResponseDto;
import com.example.demo.dto.response.UserResponseDto;
import com.example.demo.global.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원 관련 기능", description = "회원가입, 로그인 및 내 정보 조회 API")
@RestController
@RequestMapping("/api/users")
public class UserController {

    // 1. 회원가입 API
    @Operation(summary = "회원가입", description = "이메일, 비밀번호, 닉네임을 받아 회원 데이터를 생성합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 (이메일 중복 등)")
    })
    @PostMapping("/signup")
    public ApiResponse<SignupResponseDto> signup(@RequestBody SignupRequestDto request) {
        // [빈 껍데기] 나중에 Service 로직 연결 예정
        SignupResponseDto mockResponse = new SignupResponseDto(1L);
        return new ApiResponse<>(201, "회원가입이 완료되었습니다.", mockResponse);
    }

    // 2. 로그인 API (기존 유지)
    @Operation(summary = "로그인", description = "인증을 진행한 후 유효한 JWT Access Token을 발급합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그인 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패: 이메일 또는 비밀번호 불일치")
    })
    @PostMapping("/login")
    public ApiResponse<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        // [빈 껍데기] 나중에 Token Provider 연결 예정
        LoginResponseDto mockResponse = new LoginResponseDto("mock_jwt_access_token_value_12345", "Bearer");
        return new ApiResponse<>(200, "로그인 성공", mockResponse);
    }

    // 3. 내 정보 조회 API
    @Operation(summary = "내 정보 조회", description = "헤더의 Authorization 토큰을 검증하여 현재 본인의 인적사항을 반환합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패: 유효하지 않거나 만료된 토큰"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "인가 실패: 해당 자원에 접근 권한 부족")
    })
    @GetMapping("/me")
    public ApiResponse<UserResponseDto> getMyInfo() {
        // [빈 껍데기] 나중에 SecurityContextHolder 연동 예정
        UserResponseDto mockResponse = new UserResponseDto("user@example.com", "아기사자", "USER");
        return new ApiResponse<>(200, "내 정보 조회가 완료되었습니다.", mockResponse);
    }
}