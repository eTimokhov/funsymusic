package com.etimokhov.funsymusic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomePageController {

    @GetMapping(value = {"/", "/home"})
    public String home() {
        return "home";
    }
}
