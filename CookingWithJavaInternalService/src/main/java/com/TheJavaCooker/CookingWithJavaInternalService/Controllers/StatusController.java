package com.TheJavaCooker.CookingWithJavaInternalService.Controllers;

import com.TheJavaCooker.CookingWithJavaInternalService.PersonalDebug;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
public class StatusController {
    @RequestMapping(value = "")
    @ResponseBody
    public String status( ) {
		return "HOLA, todo ok";
    }
}
