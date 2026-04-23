package com.example.baeldungbasics;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@org.springframework.stereotype.Controller
public class Controller {

    @Value("${spring.application.name}")
    String appName;

    @RequestMapping(value = "/")
    public String homePage(Model model){
        model.addAttribute("appName", appName);
        return "home";
    }

}
