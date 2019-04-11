package com.TheJavaCooker.CookingWithJava;

import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Receta;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class InternalServiceCliente {

    private final static String url = "http://InternalService:7000/";//Docker URL
    //private final static String url = "http://127.0.0.1:9000/";//local host URL
    private final static String uriPDF = url+"crearPDF";
    private final static String uriTXT = url+"crearTXT";

    private Receta receta;
    Map<String,Object> mapaReceta;

    public InternalServiceCliente(Receta receta) {
        this.receta = receta;
        mapaReceta = this.receta.toJSON();
    }

    public byte[] obtenerPDF() {
        try {
            RestTemplate rt = new RestTemplate();
            rt.getMessageConverters().add(new StringHttpMessageConverter());
            byte[] result = null;
            result = rt.postForObject(uriPDF, mapaReceta, byte[].class);
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public String obtenerTXT() {
        try {
            RestTemplate rt = new RestTemplate();
            rt.getMessageConverters().add(new StringHttpMessageConverter());
            String result = null;
            result = rt.postForObject(uriTXT, mapaReceta, String.class);
            return result;
        } catch (Exception e) {
            return null;
        }
    }
}
