package com.omnm.hanasset.global.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 엔드포인트입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),
    AUTHORIZATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "인증 처리 중 오류가 발생했습니다."),
    UNAUTHORIZED_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    INVALIDATE_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    LOGOUT_TOKEN(HttpStatus.UNAUTHORIZED, "로그아웃된 토큰입니다."),

    // User ErrorCode
    EMAIL_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "메일 생성에 실패했습니다."),
    CODE_NOT_MATCHED(HttpStatus.BAD_REQUEST, "이메일 인증 코드가 일치하지 않습니다."),
    NOT_VERIFIED_EMAIL(HttpStatus.BAD_REQUEST, "이메일 인증이 완료되지 않았습니다."),
    ALREADY_REGISTERED_EMAIL(HttpStatus.BAD_REQUEST, "이미 회원가입된 이메일입니다."),
    INCORRECT_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 틀립니다."),

    // Loan ErrorCode
    LOAN_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 대출 상품이 존재하지 않습니다."),

    // RealEstate ErrorCode
    REAL_ESTATE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 부동산이 존재하지 않습니다."),

    // Consultant ErrorCode
    CONSULTANT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 상담사가 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
