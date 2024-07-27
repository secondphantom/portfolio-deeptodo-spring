package net.deeptodo.app.aop.dto.response;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Aspect
public class ResponseWrapperAspect {

    @Around("execution(* net.deeptodo..*Controller.*(..))")
    public Object wrapResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();

        if (result instanceof ResponseEntity<?>) {
            ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
            HttpStatusCode statusCode = responseEntity.getStatusCode();
            Object body = responseEntity.getBody();
            Map<String, Object> wrappedResponse = new HashMap<>();
            wrappedResponse.put("success", !(statusCode.value() >= 400));
            wrappedResponse.put("data", body);
            return ResponseEntity.status(statusCode).body(wrappedResponse);
        }
        return result;
    }
}
