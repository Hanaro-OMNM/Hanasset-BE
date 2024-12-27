package com.omnm.hanasset.consultant.service;

import com.omnm.hanasset.consultant.dto.ConsultantInfoResponse;
import com.omnm.hanasset.consultant.dto.ConsultantSignInRequest;
import com.omnm.hanasset.consultant.entity.Consultant;
import com.omnm.hanasset.consultant.repository.ConsultantRepository;
import com.omnm.hanasset.global.config.security.TokenProvider;
import com.omnm.hanasset.global.exception.CustomException;
import com.omnm.hanasset.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ConsultantService {
    private final ConsultantRepository consultantRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Transactional
    public List<String> signIn(ConsultantSignInRequest consultantSignInRequest) {
        if (consultantRepository.findByconsultantLoginId(consultantSignInRequest.getConsultantLoginId()).isEmpty()) {
            // 임의로 데이터 삽입
            consultantRepository.save(
                    Consultant.builder()
                            .name("하나은행 김미강 상담사")
                            .consultantLoginId(consultantSignInRequest.getConsultantLoginId())
                            .password(passwordEncoder.encode(consultantSignInRequest.getPassword()))
                            .build()
            );
        }

        List<String> tokensList = new ArrayList<>();

        String accessToken = tokenProvider.generateAccessToken(consultantSignInRequest.getConsultantLoginId());
        String refreshToken = tokenProvider.generateRefreshToken(consultantSignInRequest.getConsultantLoginId());

        tokensList.add(accessToken);
        tokensList.add(refreshToken);

        return tokensList;
    }

    @Transactional
    public ConsultantInfoResponse getConsultantInfo(String consultantLoginId) {
        Consultant consultant = consultantRepository.findByconsultantLoginId(consultantLoginId).orElseThrow(() -> new CustomException(ErrorCode.CONSULTANT_NOT_FOUND));

        return ConsultantInfoResponse.builder()
                .consultantId(consultant.getConsultantId())
                .name(consultant.getName())
                .build();
    }
}
