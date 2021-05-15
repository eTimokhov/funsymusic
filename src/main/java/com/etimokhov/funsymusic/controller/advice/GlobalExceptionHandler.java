package com.etimokhov.funsymusic.controller.advice;

import com.etimokhov.funsymusic.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

    private class JsonErrorResponse {
        String status;
        String message;

        public JsonErrorResponse() {
        }

        public JsonErrorResponse(String message) {
            super();
            this.status = "error";
            this.message = message;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<JsonErrorResponse> handleAuthException(AccessDeniedException e) {
        return new ResponseEntity<>(new JsonErrorResponse("You are not authorized for this!"), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<JsonErrorResponse> handleNotFoundException(NotFoundException e) {
        return new ResponseEntity<>(new JsonErrorResponse("The requested resource not found!"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<JsonErrorResponse> handleException(Exception e) {
        return new ResponseEntity<>(new JsonErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
