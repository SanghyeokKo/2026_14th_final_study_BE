package com.example.demo.service;

import com.example.demo.domain.Role;
import com.example.demo.domain.User;
import com.example.demo.dto.request.LoginRequestDto;
import com.example.demo.dto.request.SignupRequestDto;
import com.example.demo.dto.response.LoginResponseDto;
import com.example.demo.dto.response.SignupResponseDto;
import com.example.demo.dto.response.UserResponseDto;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
@RequiredArgsConstructor
public class UserService {

    // 1단계에서 만든 DB 연결 도구
    private final UserRepository userRepository;
    // 2단계에서 만든 비밀번호 암호화 도구
    private final PasswordEncoder passwordEncoder;
    // 2단계에서 만든 통행증(JWT) 발급 도구
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 1. 회원가입 로직
     */
    @Transactional
    public SignupResponseDto signup(SignupRequestDto request) {
        // [1] 이메일 중복 검사
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        // [2] 닉네임 중복 검사
        if (userRepository.existsByNickname(request.nickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        // [3] 비밀번호 암호화 및 Entity 생성
        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password())) // 비밀번호를 암호화해서 넣습니다!
                .nickname(request.nickname())
                .role(Role.ROLE_USER) // 기본 권한 부여
                .build();

        // [4] DB에 저장
        User savedUser = userRepository.save(user);

        // [5] 캡처 화면 명세서대로 userId만 담아서 반환
        return new SignupResponseDto(savedUser.getId());
    }

    /**
     * 2. 로그인 로직
     */
    @Transactional(readOnly = true)
    public LoginResponseDto login(LoginRequestDto request) {
        // [1] 이메일로 DB에서 회원 찾기
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        // [2] 비밀번호 일치 여부 확인
        // 주의: DB에 저장된 암호화된 비밀번호와 입력받은 원본 비밀번호를 비교합니다.
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // [3] 이메일과 권한 정보를 담은 JWT 토큰 발급
        String token = jwtTokenProvider.createToken(user.getEmail(), user.getRole().name());

        // [4] 캡처 화면 명세서대로 토큰과 타입(Bearer)을 담아서 반환
        return new LoginResponseDto(token, "Bearer");
    }

    /**
     * 3. 내 정보 조회 로직
     */
    @Transactional(readOnly = true)
    public UserResponseDto getMyInfo(String email) {
        // 이메일로 사용자를 조회하고, 없으면 예외 던지기
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        // 유저 정보 DTO 생성 시 각 컴포넌트에 맞춰 값을 매핑합니다.
        // (만약 UserResponseDto가 record라면 new UserResponseDto(user.getEmail(), ...) 형태로 작성)
        return new UserResponseDto(user.getEmail(), user.getNickname(), user.getRole().name());
    }

    /**
     * 4. 특정 유저 정보 조회 로직 (403 테스트용)
     */
    @Transactional(readOnly = true)
    public UserResponseDto getUserInfo(Long requestedUserId) {
        // [1] 현재 로그인한 사람의 이메일 꺼내기 (JwtFilter에서 넣어둔 정보)
        String currentLoginEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        // [2] 요청받은 번호(ID)로 DB에서 유저 찾기
        User targetUser = userRepository.findById(requestedUserId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        // [3] 로그인한 사람과 조회하려는 사람이 다르면 403 에러 던지기!
        if (!currentLoginEmail.equals(targetUser.getEmail())) {
            throw new AccessDeniedException("본인의 정보만 조회할 수 있습니다.");
        }

        // [4] 일치하면 정보 반환 (UserResponseDto 생성자에 맞게 수정 필요 시 수정)
        return new UserResponseDto(targetUser.getEmail(), targetUser.getNickname(), targetUser.getRole().name());
    }
}