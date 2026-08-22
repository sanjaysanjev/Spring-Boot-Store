package com.sanjay.store.common;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Home_Controller {
    @RequestMapping("/")
    public String login(Model model)
    {
        model.addAttribute("name","sanjay");
        return "index";
    }

    /*@RequestMapping("/hello")
    public String sayHello()
    {
        return "index.html";
    }*/
}
