package com.thoughtworks.rslist.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@ControllerAdvice
public class ExceptionHandler {
    Logger logger = LogManager.getLogger(ExceptionHandler.class);

    @org.springframework.web.bind.annotation.ExceptionHandler({
        InvalidIndexException.class,
        InvalidRequestParamException.class,
        InvalidParamException.class,
        InvalidUserException.class
    })
    public ResponseEntity<CommentError> handleException(Exception error) {
        CommentError commentError = new CommentError();
        commentError.setErrorMessage(error.getMessage());
        logger.error(error.getMessage());
        return ResponseEntity.badRequest().body(commentError);
    }
}
