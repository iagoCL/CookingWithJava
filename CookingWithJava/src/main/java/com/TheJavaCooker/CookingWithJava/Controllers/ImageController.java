package com.TheJavaCooker.CookingWithJava.Controllers;

import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Imagedb;
import com.TheJavaCooker.CookingWithJava.DataBase.Services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ImageController {
    @Autowired
    private ImageService imageService;

    @RequestMapping(value = { "/image/{imageId}",
            "/image/{imageId}.jpg" }, method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImageBytes(@PathVariable long imageId) {
        Imagedb u = imageService.searchById(imageId);
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        if (u != null) {
            return new ResponseEntity<>(u.getImage(), headers, HttpStatus.OK);
        } else
            return new ResponseEntity<>(null, headers, HttpStatus.BAD_REQUEST);
    }
}