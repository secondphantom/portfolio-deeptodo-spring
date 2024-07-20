package net.deeptodo.app.aop.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import net.deeptodo.app.aop.exception.dto.ErrorResponse;
import net.deeptodo.app.common.exception.ErrorCode;
import net.deeptodo.app.common.exception.GlobalException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private final ObjectMapper objectMapper;

    public GlobalExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ErrorResponse> handle(
            GlobalException exception,
            HttpServletRequest request
    ) throws JsonProcessingException {
        String exceptionSource = extractExceptionSource(exception);
        ErrorCode<?> errorCode = exception.getErrorCode();

        log.warn(
                "source = {} \n {} = {} \n code = {} \n message = {} \n info = {}",
                exceptionSource,
                request.getMethod(), request.getRequestURI(),
                errorCode.getCode(), errorCode.getMessage(),
                objectMapper.writeValueAsString(errorCode.getInfo())
        );

        return ResponseEntity.status(exception.getStatus()).body(ErrorResponse.from(errorCode));
    }

    private String extractExceptionSource(Exception exception) {
        StackTraceElement[] stackTrace = exception.getStackTrace();
        if (stackTrace.length > 0) {
            return stackTrace[0].toString();
        }
        return "Unknown Source";
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleServerException(Exception exception, HttpServletRequest request) {
        String exceptionSource = extractExceptionSource(exception);

        log.error(
                "source = {} \n {} = {}",
                exceptionSource,
                request.getMethod(), request.getRequestURI(),
                exception
        );

        return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
    }
}
