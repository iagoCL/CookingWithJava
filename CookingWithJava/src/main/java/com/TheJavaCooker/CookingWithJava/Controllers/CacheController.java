package com.TheJavaCooker.CookingWithJava.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

@RestController
public class CacheController {
    @Autowired
    private CacheManager cacheManager;

    @RequestMapping(value = "/cache",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getCacheContent() {
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());

        ConcurrentMapCacheManager cacheMgr = (ConcurrentMapCacheManager) cacheManager;
        ConcurrentMapCache cache = (ConcurrentMapCache) cacheMgr.getCache("recetasCache");
        String text = "RECETAS:\n" + cache.getNativeCache().toString();

        cache = (ConcurrentMapCache) cacheMgr.getCache("usuariosCache");

        text += "\n\nUSUARIOS:\n" + cache.getNativeCache().toString();

        cache = (ConcurrentMapCache) cacheMgr.getCache("imagenCache");

        text += "\n\nIMAGENES:\n" + cache.getNativeCache().toString();

        return new ResponseEntity<>(text, headers, HttpStatus.OK);
    }
}
