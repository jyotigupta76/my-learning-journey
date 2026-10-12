package com.security.demo;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/hello")
    public String hello(){
        return "Hello World";
    }


    @GetMapping("/admin/hello")
    public String adminHello(){
        return "Hello Admin";
    }

    @GetMapping("/user/hello")
    public String userHello(){
        return "Hello User";
    }
}
