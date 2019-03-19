package com.TheJavaCooker.CookingWithJavaInternalService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
public class ApiRestController {
    @PostMapping(name = "/crearPDF", consumes = "application/json")
    @ResponseBody
    public byte[] crearPDF(@RequestBody String recetaJSON) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode rootNode = objectMapper.readTree(recetaJSON);




            PersonalDebug.imprimir("\nreceta: "
                    + "\n\ttipo_plato: " + rootNode.get("tipo_plato").asText()
                    + "\n\tnombre_receta: " + rootNode.get("nombre_receta").asText()
                    + "\n\tnivel_de_dificultad: " + rootNode.get("nivel_de_dificultad").asText()
                    + "\n\tnumero_comentarios: " + rootNode.get("numero_comentarios").asText()
                    + "\n\tnumero_favoritos: " + rootNode.get("numero_favoritos").asText()
                    + "\n\tduracion_total: " + rootNode.get("duracion_total").asText());

            JsonNode creadorNode = rootNode.get("creador");

            PersonalDebug.imprimir("\nCreador: "
                    + "\n\tnombre_usuario: " + creadorNode.get("nombre_usuario").asText()
                    + "\n\tnombre_apellidos: " + creadorNode.get("nombre_apellidos").asText()
                    + "\n\tcorreo_electronico: " + creadorNode.get("correo_electronico").asText());

            int numeroPasos = rootNode.get("numero_pasos").asInt();
            PersonalDebug.imprimir("\nPasos: "+numeroPasos);
            JsonNode pasosNode = rootNode.get("pasos");
            for(int i = 1; i<=numeroPasos;++i){
                JsonNode paso = pasosNode.get("paso-"+i);
                PersonalDebug.imprimir("\nPaso: "+i
                        + "\n\tduracion: " + paso.get("duracion").asText()
                        + "\n\tdescripcion: " + paso.get("descripcion").asText());
            }

            int numeroIngredientes = rootNode.get("numero_ingredientes").asInt();
            PersonalDebug.imprimir("\nIngredientes: "+numeroIngredientes);
            JsonNode ingredientesNode = rootNode.get("ingredientes");
            for(int i = 1; i<=numeroPasos;++i){
                JsonNode ingrediente = ingredientesNode.get("ingrediente-"+i);
                PersonalDebug.imprimir("\nIngrediente: "+i
                        + "\n\tnombre_ingrediente: " + ingrediente.get("nombre_ingrediente").asText()
                        + "\n\tcantidad_ingrediente: " + ingrediente.get("cantidad_ingrediente").asText());
            }

            int numeroUtensilios = rootNode.get("numero_utensilios").asInt();
            PersonalDebug.imprimir("\nUtensilios: "+numeroPasos);
            JsonNode utensiliosNode = rootNode.get("utensilios");
            for(int i = 1; i<=numeroPasos;++i){
                JsonNode utensilio = utensiliosNode.get("utensilio-"+i);
                PersonalDebug.imprimir("\nUtensilio: "+i
                        + "\n\tnombre_utensilio: " + utensilio.get("nombre_utensilio").asText()
                        + "\n\tnivel_de_dificultad: " + utensilio.get("nivel_de_dificultad").asText());
            }

            return PDFCreator.createPDF(rootNode.get("nombre_receta").asText(), "Paso 1", "Paso 2");
        } catch (Exception e) {
            return new byte[0];
        }
    }
}
