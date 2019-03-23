package com.TheJavaCooker.CookingWithJavaInternalService.Controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/crearTXT")
public class CrearTXTController {

    @PostMapping(name = "/", consumes = "application/json")
    @ResponseBody
    public String crearTXT(@RequestBody String recetaJSON) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(recetaJSON);
            JsonNode creadorNode = rootNode.get("creador");
            int numeroPasos = rootNode.get("numero_pasos").asInt();

            String recetaEnTXT ="Receta: "
                    + "\nNombre de receta: " + rootNode.get("nombre_receta").asText()
                    + "\nTipo plato: " + rootNode.get("tipo_plato").asText()
                    + "\nNivel de dificultad: " + rootNode.get("nivel_de_dificultad").asText()
                    + "\nNumero de comentarios: " + rootNode.get("numero_comentarios").asText()
                    + "\nNumero de favoritos: " + rootNode.get("numero_favoritos").asText()
                    + "\nDuracion total: " + rootNode.get("duracion_total").asText()
                    + "\n\nCreador: "
                    + "\n\tNombre de usuario: " + creadorNode.get("nombre_usuario").asText()
                    + "\n\tNombre apellidos: " + creadorNode.get("nombre_apellidos").asText()
                    + "\n\tCorreo electronico: " + creadorNode.get("correo_electronico").asText()
                    +"\n\nPasos: " + numeroPasos+":";
            JsonNode pasosNode = rootNode.get("pasos");
            for (int i = 1; i <= numeroPasos; ++i) {
                JsonNode paso = pasosNode.get("paso-" + i);
                recetaEnTXT += "\nPaso - " + i + ":"
                        + "\n\tDuracion: " + paso.get("duracion").asText()
                        + "\n\tDescripcion: " + paso.get("descripcion").asText();
            }

            int numeroIngredientes = rootNode.get("numero_ingredientes").asInt();
            recetaEnTXT +="\n\nIngredientes: " + numeroIngredientes+":";
            JsonNode ingredientesNode = rootNode.get("ingredientes");
            for (int i = 1; i <= numeroIngredientes; ++i) {
                JsonNode ingrediente = ingredientesNode.get("ingrediente-" + i);
                recetaEnTXT += "\nIngrediente-" + i+":"
                        + "\n\tNombre de ingrediente: " + ingrediente.get("nombre_ingrediente").asText()
                        + "\n\tCantidad de ingrediente: " + ingrediente.get("cantidad_ingrediente").asText();
            }

            int numeroUtensilios = rootNode.get("numero_utensilios").asInt();
            recetaEnTXT += "\n\nUtensilios: " + numeroPasos+":";
            JsonNode utensiliosNode = rootNode.get("utensilios");
            for (int i = 1; i <= numeroUtensilios; ++i) {
                JsonNode utensilio = utensiliosNode.get("utensilio-" + i);
                recetaEnTXT += "\nUtensilio-" + i+":"
                        + "\n\tNombre de utensilio: " + utensilio.get("nombre_utensilio").asText()
                        + "\n\tNivel de dificultad: " + utensilio.get("nivel_de_dificultad").asText();

            }
            return recetaEnTXT;
        } catch (Exception e) {
            return "";
        }
    }
}
