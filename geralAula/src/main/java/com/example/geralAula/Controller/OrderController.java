package com.example.geralAula.Controller;

import com.example.geralAula.Service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class OrderController {

    OrderService orderService;

    @RequestMapping("/order")
    public String order(){
        String text = orderService.getOrder();
        return text;
    }

}
