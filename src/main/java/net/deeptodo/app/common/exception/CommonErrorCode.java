package net.deeptodo.app.common.exception;

import lombok.Getter;

@Getter
public enum CommonErrorCode {

    INTERNAL("00000", "Internal Server Error"),
    ;

    private final String code;
    private final String message;

    CommonErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
