package me.p829911.blog.controller;

import lombok.extern.slf4j.Slf4j;
import me.p829911.blog.exception.BaseException;
import me.p829911.blog.exception.PostNotFound;
import me.p829911.blog.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e) {
    ErrorResponse errorResponse = ErrorResponse.builder()
        .code("400")
        .message("잘못된 요청입니다.")
        .build();

    e.getFieldErrors().forEach(fieldError -> {
      String field = fieldError.getField();
      String message = fieldError.getDefaultMessage();
      errorResponse.addValidation(field, message);
    });
    return errorResponse;
  }

  @ExceptionHandler(BaseException.class)
  public ResponseEntity<ErrorResponse> postNotFound(PostNotFound e) {
    ErrorResponse errorResponse = ErrorResponse.builder()
        .code("404")
        .message(e.getMessage())
        .build();

    return ResponseEntity.status(e.getStatusCode())
        .body(errorResponse);
  }
}
