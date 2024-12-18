package com.omnm.hanasset.user.exception;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class EmailException extends RuntimeException {
    private String errorMessage;

    public EmailException (String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
