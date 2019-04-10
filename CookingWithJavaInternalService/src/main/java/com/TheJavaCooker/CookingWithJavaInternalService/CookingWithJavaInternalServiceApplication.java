package com.TheJavaCooker.CookingWithJavaInternalService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CookingWithJavaInternalServiceApplication {
	
	private static int puerto = 7001;

	public static void main(String[] args) {
		for (int i = 0; i < args.length; i++) {
			if(args[i].equals("-d"))
			{
				PersonalDebug.setDebug(true);
			} else if (args[i].startsWith("-puertoWeb")) {				
                puerto=Integer.parseInt(args[i].substring("-puertoWeb".length()));
            }
		}
		System.getProperties().put( "server.port", puerto );
		SpringApplication.run(CookingWithJavaInternalServiceApplication.class, args);
	}

}
