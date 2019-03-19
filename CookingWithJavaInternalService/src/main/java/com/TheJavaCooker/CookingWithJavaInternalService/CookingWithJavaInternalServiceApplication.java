package com.TheJavaCooker.CookingWithJavaInternalService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CookingWithJavaInternalServiceApplication {

	public static void main(String[] args) {
		if(args.length>0 &&args[0].equals("-d"))
		{
			PersonalDebug.setDebug(true);
		}
		SpringApplication.run(CookingWithJavaInternalServiceApplication.class, args);
	}

}
