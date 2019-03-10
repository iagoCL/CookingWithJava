package com.TheJavaCooker.CookingWithJava.Controllers;

import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Imagendb;
import com.TheJavaCooker.CookingWithJava.DataBase.Services.ImagenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ImagenController {
    @Autowired
    private ImagenService imagenService;

    @RequestMapping(value = {"/image/{imageId}",
            "/image/{imageId}.jpg"},
            method = RequestMethod.GET,
            produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImageUsuario(@PathVariable long imageId) {
        Imagendb u = imagenService.buscarPorId(imageId);
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        if (u != null) {
            return new ResponseEntity<>(u.getImagen(), headers, HttpStatus.OK);
        } else
            return new ResponseEntity<>(null, headers, HttpStatus.BAD_REQUEST);
    }
}
