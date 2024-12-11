package com.nhnacademy.bookstoregateway.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/first-service/")
public class FirstServiceController {

    @GetMapping("/welcome")
    public String welcome() {

        return "Welcome to the First service.";
    }
}