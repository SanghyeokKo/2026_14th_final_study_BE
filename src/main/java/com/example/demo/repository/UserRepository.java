package com.example.demo.repository;

import com.example.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 이메일로 회원을 찾는 기능 (로그인 및 중복 가입 방지용)
    Optional<User> findByEmail(String email);

    // 닉네임 중복 검사용
    boolean existsByNickname(String nickname);
}