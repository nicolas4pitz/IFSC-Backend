package com.example.ifsc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class IfscApplication {

    public static void main(String[] args) {
        SpringApplication.run(IfscApplication.class, args);
    }

    @GetMapping("/api/verificador/{CPF}")
    public String verificarCPF(@PathVariable("CPF") String cpf){

        System.out.println(cpf.length());
        String result = validador(cpf);
        return result;

    }

    public String validador(String cpf){
        
        if (cpf.length() > 11){
            return "BAD REQUEST (400) TRESTE";
        }

        
        int resultOne = calc(10, cpf);
        if (resultOne == 10) {
            resultOne = 0;
        }

        if (resultOne != cpf.indexOf(9)) {
            return "BAD REQUEST (400) PIIPIP" + cpf;
        }

        int resultTwo = calc(11, cpf);
        if (resultTwo == 10) {
            resultTwo = 0;
        }

        if (resultTwo != cpf.indexOf(10)) {
            return "BAD REQUEST (400) PUUPUP";
        }

        return "OK (200)";
    }

    public int calc(int num, String cpf){
        int sum = 0;
        int inter;
        int c = 0;
        char tt = cpf.charAt(9);
        for (int i = num; i > 2; i--) {
            inter = i * cpf.charAt(c);
            sum += inter;
        }
        return sum;
    }


}
