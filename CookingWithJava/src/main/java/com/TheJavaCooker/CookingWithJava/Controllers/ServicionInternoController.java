package com.TheJavaCooker.CookingWithJava.Controllers;

import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Receta;import com.TheJavaCooker.CookingWithJava.DataBase.Services.RecetaService;
import com.TheJavaCooker.CookingWithJava.InternalServiceCliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ServicionInternoController {
    @Autowired
    private RecetaService recetaService;
    @Autowired
    private UsuariosController usuariosController;
    @Autowired
    private WebController webController;

    @RequestMapping(value = {"/pdf/{recetaId}",
            "/pdf/{recetaId}.pdf"},
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> crearPDF(@PathVariable long recetaId) {
        Receta receta = recetaService.buscarPorId(recetaId);
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());

        // Conexión por sockets con el servicio interno
        InternalServiceCliente cliente = new InternalServiceCliente(receta);
        byte[] u = cliente.obtenerPDF();
        if (u != null) {
            return new ResponseEntity<>(u, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, headers, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = {"/txt/{recetaId}",
            "/txt/{recetaId}.txt"},
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> crearTXT(@PathVariable long recetaId) {
        Receta receta = recetaService.buscarPorId(recetaId);
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());

        // Conexión por sockets con el servicio interno
        InternalServiceCliente cliente = new InternalServiceCliente(receta);
        String text = cliente.obtenerTXT();
        if (text != null) {
            return new ResponseEntity<>(text, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, headers, HttpStatus.BAD_REQUEST);
        }
    }
}
