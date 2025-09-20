package com.example.SamvaadProject.MainPackage;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ParentController {

    @GetMapping("/test")
    public String getTest() {
        return "test";
    }
}
