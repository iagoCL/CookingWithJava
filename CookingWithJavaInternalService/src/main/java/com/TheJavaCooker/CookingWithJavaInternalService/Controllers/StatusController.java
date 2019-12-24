package com.TheJavaCooker.CookingWithJavaInternalService.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class StatusController {
  @RequestMapping(value = "")
  @ResponseBody
  public String status() {
    return "All ok";
  }
}
