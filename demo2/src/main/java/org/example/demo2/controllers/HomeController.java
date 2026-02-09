package org.example.demo2.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@ResponseBody
public class HomeController {

    @GetMapping
    public String getHomePage(){

        log.info("Home page API called");

        return "Welcome to Home Page";
    }

    @GetMapping("/dashboard")
    public String getDashBoard(){

        log.info("Dashboard API called - Login Successful");

        return "Login Successful!";
    }
}
