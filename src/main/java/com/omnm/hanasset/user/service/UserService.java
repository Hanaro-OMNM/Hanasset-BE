package com.omnm.hanasset.user.service;

import com.omnm.hanasset.global.config.security.TokenProvider;
import com.omnm.hanasset.user.dto.EmailSignInRequest;
import com.omnm.hanasset.user.dto.EmailSignUpRequest;
import com.omnm.hanasset.user.entity.User;
import com.omnm.hanasset.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    final PasswordEncoder passwordEncoder; // 반드시 final로 선언; 인증과 인가에서 사용될 패스워드의 인코딩 방식을 지정; PasswordConfig 파일 확인
    final TokenProvider tokenProvider;

    @Transactional
    public void signUp(EmailSignUpRequest emailSignUpRequest) {
        isEmailExists(emailSignUpRequest.getEmail()); // 이미 존재하는 이메일인지 확인

        userRepository.save(User.builder()
                .email(emailSignUpRequest.getEmail())
                .password(passwordEncoder.encode(emailSignUpRequest.getPassword()))
                .name(emailSignUpRequest.getName())
                .build());
    }

    @Transactional
    public String signIn(EmailSignInRequest emailSignInRequest) {
        User user = userRepository.findByEmail(emailSignInRequest.getEmail()).orElseThrow(IllegalArgumentException::new);

        isMatchPassword(emailSignInRequest.getPassword(), user.getPassword()); // 비밀번호 일치하는지 체크

        return tokenProvider.generateToken(emailSignInRequest.getEmail());
    }

    private void isEmailExists (String email) {
        if (userRepository.findByEmail(email).isPresent()) {
//            throw new UserException(ErrorCode.EXISTS_EMAIL);
            throw new IllegalArgumentException("이미 회원가입된 이메일입니다.");
        }
    }

    private void isMatchPassword (String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new IllegalArgumentException("비밀번호가 틀립니다.");
        }
    }
}
