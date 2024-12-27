package com.omnm.hanasset.user.service;

import com.omnm.hanasset.global.config.RedisHandler;
import com.omnm.hanasset.global.config.security.TokenProvider;
import com.omnm.hanasset.global.exception.CustomException;
import com.omnm.hanasset.global.exception.code.ErrorCode;
import com.omnm.hanasset.user.dto.*;
import com.omnm.hanasset.user.entity.Property;
import com.omnm.hanasset.user.entity.User;
import com.omnm.hanasset.user.repository.PropertyRepository;
import com.omnm.hanasset.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;
    private final PasswordEncoder passwordEncoder; // 반드시 final로 선언; 인증과 인가에서 사용될 패스워드의 인코딩 방식을 지정; PasswordConfig 파일 확인
    private final TokenProvider tokenProvider; // 반드시 final로 선언; JWT 발급
    private final RedisHandler redisHandler;

    @Transactional
    public void signUp(EmailSignUpRequest emailSignUpRequest) {
        isEmailExists(emailSignUpRequest.getEmail()); // 이미 존재하는 이메일인지 확인
        isEmailVerified(emailSignUpRequest.getEmail()); // 메일 인증이 완료된 이메일인지 확인

        User user = userRepository.save(User.builder()
                .email(emailSignUpRequest.getEmail())
                .password(passwordEncoder.encode(emailSignUpRequest.getPassword()))
                .name(emailSignUpRequest.getName())
                .build());

        propertyRepository.save(
                Property.builder()
                            .user(user)
                            .income(0)
                            .capital(0)
                            .hasHouse(false)
                            .annualInterest(0)
                            .annualPrinciple(0)
                            .isAbnormalHouse(false)
                            .isHousingFraudVictim(false)
                            .stressDsr(0.0)
                        .build()
        );
    }

    @Transactional
    public List<String> signIn(EmailSignInRequest emailSignInRequest) {
        User user = userRepository.findByEmail(emailSignInRequest.getEmail()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        isPasswordMatches(emailSignInRequest.getPassword(), user.getPassword()); // 비밀번호 일치하는지 체크

        List<String> tokensList = new ArrayList<>();

        String accessToken = tokenProvider.generateAccessToken(emailSignInRequest.getEmail());
        String refreshToken = tokenProvider.generateRefreshToken(emailSignInRequest.getEmail());

        tokensList.add(accessToken);
        tokensList.add(refreshToken);

        return tokensList;
    }

    @Transactional
    public void setBirthDate(BirthRequest birth) {
        User user = userRepository.findByEmail(birth.getEmail()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.updateBirthDate(birth.getBirthDate());
    }

    public String logout(HttpServletRequest request) {
        String accessToken = tokenProvider.resolveTokenFromRequest(request);
        String refreshToken = tokenProvider.resolveRefreshTokenFromCookie(request);

        if (StringUtils.hasText(accessToken) && StringUtils.hasText(refreshToken)) {
            tokenProvider.destroyToken(accessToken, refreshToken);
            return "로그아웃 성공";
        }

        return "ERROR";
    }

    @Transactional
    public void withdrawUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Property property = propertyRepository.findByUser_UserId(userId).orElseThrow(() -> new CustomException(ErrorCode.PROPERTY_NOT_FOUND));

        propertyRepository.delete(property);
        userRepository.delete(user);
    }

    @Transactional
    public UserInfoResponse getUserInfo(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return UserInfoResponse.builder().name(user.getName()).email(user.getEmail()).birthDate(user.getBirthDate()).build();
    }

    @Transactional
    public void updateUserInfo(Long userId, UserInfoRequest userInfoRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.updateName(userInfoRequest.getName()); // 유저 이름 수정

        isPasswordMatches(userInfoRequest.getCurrPassword(), user.getPassword()); // 기존 비밀번호와 일치하는 지 체크

        user.updatePassword(passwordEncoder.encode(userInfoRequest.getNewPassword())); // 비밀번호 업데이트
    }

    private void isEmailExists (String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_REGISTERED_EMAIL);
        }
    }

    private void isPasswordMatches (String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new CustomException(ErrorCode.INCORRECT_PASSWORD);
        }
    }

    private void isEmailVerified (String email) {
        String confirmStatus = redisHandler.getValue(email);
        if (confirmStatus == null || !confirmStatus.equals("confirmed")) {
            throw new CustomException(ErrorCode.NOT_VERIFIED_EMAIL);
        }
    }
}
