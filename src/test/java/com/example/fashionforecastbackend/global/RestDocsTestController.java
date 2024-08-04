package com.example.fashionforecastbackend.global;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestDocsTestController {

    @GetMapping("/api/test")
    public String test() {
        return "Hello";
    }
}
