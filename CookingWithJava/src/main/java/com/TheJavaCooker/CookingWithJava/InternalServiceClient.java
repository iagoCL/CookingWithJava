package com.TheJavaCooker.CookingWithJava;

import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Recipe;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class InternalServiceClient {

    private static String uriPDF;
    private static String uriTXT;

    public static void setURL(String url) {
        uriPDF = url + "/createPDF";
        uriTXT = url + "/createTXT";
    }

    private Recipe recipe;
    Map<String, Object> recipeMap;

    public InternalServiceClient(Recipe recipe) {
        this.recipe = recipe;
        recipeMap = this.recipe.toJSON();
    }

    public byte[] obtainPDF() {
        try {
            RestTemplate rt = new RestTemplate();
            rt.getMessageConverters().add(new StringHttpMessageConverter());
            byte[] result = null;
            result = rt.postForObject(uriPDF, recipeMap, byte[].class);
            PersonalDebug.printMsg("PDF created.");
            return result;
        } catch (Exception exception) {
            PersonalDebug.printMsg("Error creating PDF: " + exception.toString() + " - URL: " + uriPDF);
            return null;
        }
    }

    public String obtainTXT() {
        try {
            RestTemplate rt = new RestTemplate();
            rt.getMessageConverters().add(new StringHttpMessageConverter());
            String result = null;
            result = rt.postForObject(uriTXT, recipeMap, String.class);
            PersonalDebug.printMsg("TXT created.");
            return result;
        } catch (Exception exception) {
            PersonalDebug.printMsg("Error creating TXT: " + exception.toString() + " - URL: " + uriPDF);
            return null;
        }
    }
}