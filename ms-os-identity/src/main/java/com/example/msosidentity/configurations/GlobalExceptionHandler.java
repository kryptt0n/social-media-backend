package com.example.msosidentity.configurations;

import com.example.msosidentity.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = InvalidCredentialsException.class)
    public ProblemDetail handleFeignException(InvalidCredentialsException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(value = CacheNotFoundException.class)
    public ProblemDetail handleCacheException(CacheNotFoundException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(value = LinkingOauthException.class)
    public ProblemDetail handleLinkingException(LinkingOauthException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    public ProblemDetail handleUnauthorizedException(UnauthorizedException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(value = UsernameRequiredException.class)
    public ProblemDetail handleUsernameException(UsernameRequiredException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }
}
