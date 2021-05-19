package com.etimokhov.funsymusic.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

//@Controller
public class CustomErrorHandler implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletResponse response, Model model) {
        String errorMessage;
        switch (response.getStatus()) {
            case 400:
                errorMessage = "Something is wrong with your request.";
                break;
            case 403:
                errorMessage = "You are not permitted to see this page.";
                break;
            case 404:
                errorMessage = "The page you are looking doesn't exist.";
                break;
            case 500:
                errorMessage = "Something went wrong. Internal server error.";
                break;
            default:
                errorMessage = "Something went wrong.";
        }
        model.addAttribute("errorMessage", errorMessage);
        return "error";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
