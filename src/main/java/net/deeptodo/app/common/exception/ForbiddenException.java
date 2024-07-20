package net.deeptodo.app.common.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends GlobalException {

    public ForbiddenException(ErrorCode<?> errorCode) {
        super(errorCode, HttpStatus.FORBIDDEN);
    }

}
