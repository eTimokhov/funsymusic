package com.etimokhov.funsymusic.controller.advice;

import com.etimokhov.funsymusic.exception.CannotSaveFileException;
import com.etimokhov.funsymusic.exception.InvalidImageException;
import com.etimokhov.funsymusic.exception.NotAuthenticatedException;
import com.etimokhov.funsymusic.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = NotFoundException.class)
    public String notFoundErrorHandler(HttpServletRequest req, Exception e, Model model) {
        model.addAttribute("errorMessage", "The page you are looking doesn't exist.");
        return "error";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {CannotSaveFileException.class, InvalidImageException.class})
    public String invalidFileErrorHandler(HttpServletRequest req, Exception e, Model model) {
        model.addAttribute("errorMessage", "Something is wrong with file that you uploaded.");
        return "error";
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = NotAuthenticatedException.class)
    public String forbiddenErrorHandler(HttpServletRequest req, Exception e, Model model) {
        model.addAttribute("errorMessage", "Something is wrong with file that you uploaded.");
        return "error";
    }
}
