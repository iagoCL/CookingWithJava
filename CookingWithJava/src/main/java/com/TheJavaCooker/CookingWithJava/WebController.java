package com.TheJavaCooker.CookingWithJava;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @GetMapping("/hello")
    public String helloWorld( )
    {
        return "hello-world.html";
    }

    @GetMapping("/hello-pepe")
    public String helloPepe( Model model)
    {
        model.addAttribute("name","Iago");
        return "hello";
    }
    @GetMapping("/about")
    public String about( Model model)
    {

        return "about.html";
    }
    @GetMapping("/index")
    public String index( Model model)
    {

        return "index.html";
    }
}
