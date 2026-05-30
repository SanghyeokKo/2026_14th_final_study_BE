package com.example.demo.controller;

import com.example.demo.dto.request.LoginRequestDto;
import com.example.demo.dto.request.SignupRequestDto;
import com.example.demo.dto.response.LoginResponseDto;
import com.example.demo.dto.response.SignupResponseDto;
import com.example.demo.dto.response.UserResponseDto;
import com.example.demo.global.ApiResponse;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원 관련 기능", description = "회원가입, 로그인 및 내 정보 조회 API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor // ▼ UserService 주입을 위해 추가합니다.
public class UserController {

    private final UserService userService; // ▼ 진짜 로직을 처리해줄 서비스 연결

    // 1. 회원가입 API
    @Operation(summary = "회원가입", description = "이메일, 비밀번호, 닉네임을 받아 회원 데이터를 생성합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 (이메일 중복 등)")
    })
    @PostMapping("/signup")
    public ApiResponse<SignupResponseDto> signup(@RequestBody SignupRequestDto request) {
        // [진짜 로직 연결] 서비스에서 회원가입을 처리하고 결과 DTO를 받아옵니다.
        SignupResponseDto response = userService.signup(request);
        return new ApiResponse<>(201, "회원가입이 완료되었습니다.", response);
    }

    // 2. 로그인 API
    @Operation(summary = "로그인", description = "인증을 진행한 후 유효 한 JWT Access Token을 발급합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그인 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패: 이메일 또는 비밀번호 불일치")
    })
    @PostMapping("/login")
    public ApiResponse<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        // [진짜 로직 연결] 서비스에서 로그인 인증 후 JWT 토큰이 담긴 응답을 받아옵니다.
        LoginResponseDto response = userService.login(request);
        return new ApiResponse<>(200, "로그인 성공", response);
    }

    // 3. 내 정보 조회 API
    @Operation(summary = "내 정보 조회", description = "헤더의 Authorization 토큰을 검증하여 현재 본인의 인적사항을 반환합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패: 유효하지 않거나 만료된 토큰"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "인가 실패: 해당 자원에 접근 권한 부족")
    })
    @GetMapping("/me")
    public ApiResponse<UserResponseDto> getMyInfo(Authentication authentication) {
        // JwtFilter를 통과하며 토큰에서 꺼내둔 사용자의 이메일을 가져옵니다.
        String email = authentication.getName();

        // [진짜 로직 연결] 가져온 이메일로 DB에서 유저 정보를 조회해 반환합니다.
        UserResponseDto response = userService.getMyInfo(email);
        return new ApiResponse<>(200, "내 정보 조회가 완료되었습니다.", response);
    }
}