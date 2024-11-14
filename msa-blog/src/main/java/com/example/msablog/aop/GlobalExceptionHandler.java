package com.example.msablog.aop;

import com.example.msablog.post.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = RuntimeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseDto handleNotFound(RuntimeException e) {
        return ResponseDto.fail(404, e.getMessage());
    }

//    @ExceptionHandler(value = Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ResponseDto handleInternalServer(Exception e) {
//        return ResponseDto.fail(500, e.getMessage());
//    }

}
