package com.trade.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping
    public  String home(){
        return "Welcome to trading!!...";
    }

    @GetMapping("/api")
    public  String secure(){
        return "Secure";
    }
}
