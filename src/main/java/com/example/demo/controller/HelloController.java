package com.example.demo.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    private static final Logger log = LoggerFactory.getLogger(HelloController.class);
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @GetMapping("/hello")
    public String hello(Model model) {
        String now = LocalDateTime.now().format(FORMAT);
        log.info("GET /hello at {}", now);
        model.addAttribute("now", now);
        return "hello";
    }
}
