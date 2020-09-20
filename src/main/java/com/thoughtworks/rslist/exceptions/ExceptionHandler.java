package com.thoughtworks.rslist.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler({
        InvalidIndexException.class,
        InvalidRequestParamException.class,
        InvalidParamException.class,
        InvalidUserException.class
    })
    public ResponseEntity<CommentError> handleException(Exception error) {
        CommentError commentError = new CommentError();
        commentError.setErrorMessage(error.getMessage());
        return ResponseEntity.badRequest().body(commentError);
    }
}
