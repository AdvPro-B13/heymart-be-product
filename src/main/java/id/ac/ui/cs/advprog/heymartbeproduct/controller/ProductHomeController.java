package id.ac.ui.cs.advprog.heymartbeproduct.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductHomeController {

    @GetMapping("/")
    public String home() {
        return "ProductHomePage";
    }
}