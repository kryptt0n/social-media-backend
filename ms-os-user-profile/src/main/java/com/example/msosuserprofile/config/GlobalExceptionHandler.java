package com.example.msosuserprofile.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ObjectMapper om = new ObjectMapper();

    @ExceptionHandler(value = FeignException.class)
    public ProblemDetail handleFeignException(FeignException feignException) {

        String data = feignException.contentUTF8();
        String message;

        if (data != null && !data.isBlank()) {
            try {
                ProblemDetail receivedProblem = om.readValue(data, ProblemDetail.class);
                message = receivedProblem.getDetail();

            } catch (JsonProcessingException je) {
                message = "Invalid input";
            }
        } else
            message = "Invalid input";

        HttpStatus status;
        if ((status = HttpStatus.resolve(feignException.status())) == null)
            return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, message);
        else
            return ProblemDetail.forStatusAndDetail(status, message);
    }
}
