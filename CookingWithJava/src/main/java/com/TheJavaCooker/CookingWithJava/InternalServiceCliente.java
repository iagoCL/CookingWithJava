package com.TheJavaCooker.CookingWithJava;

import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Ingrediente;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Paso;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Receta;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Utensilio;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.JsonbHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class InternalServiceCliente {

    private final static String uri = "http://127.0.0.1:9000/crearPDF";
    private DataInputStream input;
    private PrintStream output;

    private String nombre;
    private Receta receta;
    private String tipo;
    private String duracion;
    private String nombre_creador;
    private Set<Ingrediente> ingredientes;
    private Set<Utensilio> utensilios;
    private Set<Paso> pasos;

    public InternalServiceCliente(Receta receta) {
        this.receta = receta;
    }

    public byte[] obtenerPDF() {
        try {

            Map<String,Object> mapa = this.receta.toJSON();

            RestTemplate rt = new RestTemplate();
            rt.getMessageConverters().add(new StringHttpMessageConverter());
            byte[] result = null;
            result = rt.postForObject(uri, mapa, byte[].class);
            return result;
        } catch (Exception e) {
            return null;
        }
    }
}
