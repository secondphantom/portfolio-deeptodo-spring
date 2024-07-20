package net.deeptodo.app.common.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends GlobalException {

    public NotFoundException(ErrorCode<?> errorCode) {
        super(errorCode, HttpStatus.NOT_FOUND);
    }

}
