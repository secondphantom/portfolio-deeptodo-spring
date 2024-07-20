package net.deeptodo.app.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorCode<T> {

    private final String code;
    private final String message;
    private T info;

    public ErrorCode(String code, String message, T info) {
        this.code = code;
        this.message = message;
        this.info = info;
    }

    public ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ErrorCode getErrorCode(CommonErrorCode errorCode){
        return new ErrorCode<>(errorCode.getCode(), errorCode.getMessage());
    }

}
