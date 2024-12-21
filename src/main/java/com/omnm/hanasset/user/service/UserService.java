package com.omnm.hanasset.user.service;

import com.omnm.hanasset.global.config.RedisHandler;
import com.omnm.hanasset.global.config.security.TokenProvider;
import com.omnm.hanasset.user.dto.EmailSignInRequest;
import com.omnm.hanasset.user.dto.EmailSignUpRequest;
import com.omnm.hanasset.user.entity.User;
import com.omnm.hanasset.user.exception.EmailException;
import com.omnm.hanasset.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // 반드시 final로 선언; 인증과 인가에서 사용될 패스워드의 인코딩 방식을 지정; PasswordConfig 파일 확인
    private final TokenProvider tokenProvider; // 반드시 final로 선언; JWT 발급
    private final RedisHandler redisHandler;

    @Transactional
    public void signUp(EmailSignUpRequest emailSignUpRequest) {
        isEmailExists(emailSignUpRequest.getEmail()); // 이미 존재하는 이메일인지 확인
        isEmailVerified(emailSignUpRequest.getEmail()); // 메일 인증이 완료된 이메일인지 확인

        userRepository.save(User.builder()
                .email(emailSignUpRequest.getEmail())
                .password(passwordEncoder.encode(emailSignUpRequest.getPassword()))
                .name(emailSignUpRequest.getName())
                .build());
    }

    @Transactional
    public List<String> signIn(EmailSignInRequest emailSignInRequest) {
        User user = userRepository.findByEmail(emailSignInRequest.getEmail()).orElseThrow(IllegalArgumentException::new);

        isPasswordMatches(emailSignInRequest.getPassword(), user.getPassword()); // 비밀번호 일치하는지 체크

        List<String> tokensList = new ArrayList<>();

        String accessToken = tokenProvider.generateAccessToken(emailSignInRequest.getEmail());
        String refreshToken = tokenProvider.generateRefreshToken(emailSignInRequest.getEmail());

        tokensList.add(accessToken);
        tokensList.add(refreshToken);

        return tokensList;
    }

    private void isEmailExists (String email) {
        if (userRepository.findByEmail(email).isPresent()) {
//            throw new UserException(ErrorCode.EXISTS_EMAIL);
            throw new IllegalArgumentException("이미 회원가입된 이메일입니다.");
        }
    }

    private void isPasswordMatches (String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new IllegalArgumentException("비밀번호가 틀립니다.");
        }
    }

    private void isEmailVerified (String email) {
        String confirmStatus = redisHandler.getValue(email);
        if (confirmStatus == null || !confirmStatus.equals("confirmed")) {
            throw new EmailException("이메일 인증이 완료되지 않았습니다.");
        }
    }
}
