package com.TheJavaCooker.CookingWithJava;

import com.TheJavaCooker.CookingWithJava.DataBase.DatabaseManager;
import com.TheJavaCooker.CookingWithJava.DataBase.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class CookingWithJavaApplication {
	@Autowired
	private  DatabaseManager databaseManager;

	public static void main(String[] args) {
		SpringApplication.run(CookingWithJavaApplication.class, args);
	}

	@PostConstruct
	public void init() {
		databaseManager.crearUsuariosEjemplo(15);
		Usuario u = databaseManager.getUsuarioRepository().buscarPorCorreoElectronico("correo1@example.com");
		System.out.println(u);
	}

}

