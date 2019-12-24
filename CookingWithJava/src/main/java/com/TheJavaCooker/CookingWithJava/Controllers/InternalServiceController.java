package com.TheJavaCooker.CookingWithJava.Controllers;

import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Recipe;
import com.TheJavaCooker.CookingWithJava.DataBase.Services.RecipeService;
import com.TheJavaCooker.CookingWithJava.InternalServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class InternalServiceController {
    @Autowired
    private RecipeService recipeService;

    @RequestMapping(value = { "/pdf/{recipeId}",
            "/pdf/{recipeId}.pdf" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> createPDF(@PathVariable long recipeId) {
        Recipe recipe = recipeService.searchById(recipeId);
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());

        // Socket connection with the Internal Service
        InternalServiceClient client = new InternalServiceClient(recipe);
        byte[] pdfBytes = client.obtainPDF();
        if (pdfBytes != null) {
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, headers, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = { "/txt/{recipeId}",
            "/txt/{recipeId}.txt" }, method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> createTXT(@PathVariable long recipeId) {
        Recipe recipe = recipeService.searchById(recipeId);
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());

        // Socket connection with the Internal Service
        InternalServiceClient client = new InternalServiceClient(recipe);
        String text = client.obtainTXT();
        if (text != null) {
            return new ResponseEntity<>(text, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, headers, HttpStatus.BAD_REQUEST);
        }
    }
}