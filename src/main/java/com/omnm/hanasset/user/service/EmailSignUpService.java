package com.omnm.hanasset.user.service;

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
public class EmailSignUpService {
    @Autowired
    UserRepository userRepository;
    final PasswordEncoder passwordEncoder; // 반드시 final로 선언

    @Transactional
    public void signUp(EmailSignUpRequest emailSignUpRequest) {
        isEmailExists(emailSignUpRequest.getEmail()); // 이미 존재하는 이메일인지 확인

        userRepository.save(User.builder()
                .email(emailSignUpRequest.getEmail())
                .password(passwordEncoder.encode(emailSignUpRequest.getPassword()))
                .name(emailSignUpRequest.getName())
                .build());
    }

    private void isEmailExists (String email) {
        if (userRepository.findByEmail(email).isPresent()) {
//            throw new UserException(ErrorCode.EXISTS_EMAIL);
            throw new IllegalArgumentException("이미 회원가입된 이메일입니다.");
        }
    }
}
