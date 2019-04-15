package com.TheJavaCooker.CookingWithJavaInternalService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CookingWithJavaInternalServiceApplication {
	public static void main(String[] args) {
		for (int i = 0; i < args.length; i++) {
			if(args[i].equals("-d"))
			{
				PersonalDebug.setDebug(true);
			}
		}
		SpringApplication.run(CookingWithJavaInternalServiceApplication.class, args);
	}

}
