package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class OkApplication {
    @RequestMapping("/hehe")
    public String gg(){
        return "hello";
    }
}
