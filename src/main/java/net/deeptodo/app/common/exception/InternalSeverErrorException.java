package net.deeptodo.app.common.exception;

import org.springframework.http.HttpStatus;

public class InternalSeverErrorException extends GlobalException {

    public InternalSeverErrorException(ErrorCode<?> errorCode) {
        super(errorCode, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
