package com.TheJavaCooker.CookingWithJava.Controllers;

import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Imagendb;
import com.TheJavaCooker.CookingWithJava.DataBase.Services.ImagenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ImagenController {
    @Autowired
    private ImagenService imagenService;

    @RequestMapping(value = {"/image/{imageId}", "/image/{imageId}.jpg"})
    @ResponseBody
    public byte[] getImageUsuario(@PathVariable long imageId) {
        Imagendb u = imagenService.buscarPorId(imageId);
        if (u != null) {
            return u.getImagen();
        } else
            return null;
    }
}
