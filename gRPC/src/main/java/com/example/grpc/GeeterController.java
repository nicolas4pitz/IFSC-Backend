package com.example.grpc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeeterController {

    private final GreeterClient greeterClient;

    @Autowired
    public GeeterController(GreeterClient greeterClient) {
        this.greeterClient = greeterClient;
    }

    @GetMapping("/adiciona")
    public double greet(@RequestParam double p1, @RequestParam double p2) {
        return greeterClient.adiciona(p1, p2);
    }
}