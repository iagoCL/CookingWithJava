package com.TheJavaCooker.CookingWithJava;

import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Receta;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class InternalServiceCliente {

    private static String uriPDF;
    private static String uriTXT;

    public static void setURL( String url)
    {
        uriPDF = url+"/crearPDF";
        uriTXT = url+"/crearTXT";
    }

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
            PersonalDebug.imprimir("PDF creado.");
            return result;
        } catch (Exception e) {
            PersonalDebug.imprimir("Error creando PDF: "+e.toString()+" - URL: "+uriPDF);
            return null;
        }
    }

    public String obtenerTXT() {
        try {
            RestTemplate rt = new RestTemplate();
            rt.getMessageConverters().add(new StringHttpMessageConverter());
            String result = null;
            result = rt.postForObject(uriTXT, mapaReceta, String.class);
            PersonalDebug.imprimir("TXT creado.");
            return result;
        } catch (Exception e) {
            PersonalDebug.imprimir("Error creando TXT: "+e.toString()+" - URL: "+uriPDF);
            return null;
        }
    }
}
