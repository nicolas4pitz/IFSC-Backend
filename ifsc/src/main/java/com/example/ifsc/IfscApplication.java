package com.example.ifsc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@SpringBootApplication
@RestController
public class IfscApplication {

    public static void main(String[] args) {
        SpringApplication.run(IfscApplication.class, args);
    }

    HashMap<Integer, String> messages = new HashMap();

    @PostMapping("/mensageiro/publica/{CHAVE}")
    public String tempMessage(@PathVariable("CHAVE") Integer chave, @RequestParam String message){
        messages.put(chave, message);
        return chave + "" + message;
    }

    @GetMapping("/mensageiro/acessa/{CHAVE}")
    public String tempMessageAcess(@PathVariable("CHAVE") Integer chave){
        return messages.get(chave++);
    }

    //@GetMapping("/api/verificador/{CPF}")
    //public String verificarCPF(@PathVariable("CPF") String cpf){

//        System.out.println(cpf.length());
//        String result = validador(cpf);
//        return result;

    //}

//    public String validador(String cpf){
//
//        if (cpf.length() != 11){
//            return "BAD REQUEST (400)";
//        }
//
//        if (!cpf.matches("\\d+")) {
//            return "BAD REQUEST (400)";
//        }
//
//        int resultOne = calc(10, cpf);
//        if (resultOne == 10) {
//            resultOne = 0;
//        }
//
//        if (resultOne != Character.getNumericValue(cpf.charAt(9))) {
//            return "BAD REQUEST (400)";
//        }
//
//        int resultTwo = calc(11, cpf);
//        if (resultTwo == 10) {
//            resultTwo = 0;
//        }
//
//        if (resultTwo != Character.getNumericValue(cpf.charAt(10))) {
//            return "BAD REQUEST (400)";
//        }
//
//        return "OK (200)";
//    }
//
//    public int calc(int num, String cpf){
//        int sum = 0;
//        int count = num - 1;
//
//        for (int i = 0; i < count; i++) {
//            int digit = Character.getNumericValue(cpf.charAt(i));
//            sum += digit * (num - i);
//        }
//
//        int remainder = sum % 11;
//        return remainder;
//    }


}
