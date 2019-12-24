package com.TheJavaCooker.CookingWithJava.Controllers;

import java.util.Map;
import java.util.Set;

import com.hazelcast.spring.cache.HazelcastCache;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CacheController {
    @Autowired
    private CacheManager cacheManager;

    @RequestMapping(value = "/cache", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getCacheContent() {
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        String text = "COMPLETE CACHE:\n\n";
        for (String cacheName : cacheManager.getCacheNames()) {
            text += getCacheStringContent(cacheName);
        }

        return new ResponseEntity<>(text, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/cache/{cacheName}", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getCacheContent(@PathVariable String cacheName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());

        String text = getCacheStringContent(cacheName);
        return new ResponseEntity<>(text, headers, HttpStatus.OK);
    }

    private String getCacheStringContent(@PathVariable String cacheName) {
        HazelcastCacheManager cacheMgr = (HazelcastCacheManager) cacheManager;
        HazelcastCache cache = (HazelcastCache) cacheMgr.getCache(cacheName);
        Map<Object, Object> nativeCache = cache.getNativeCache();
        Set<Object> keys = nativeCache.keySet();
        String text = cacheName.toUpperCase() + ":\n{\n";
        for (Object key : keys) {
            text += "\t{\n\t\tKEY:\t" + key.toString() + "\n\t\tVALUE:\t" + nativeCache.get(key).toString() + "\n\t}\n";
        }
        text += "}\n";
        return text;
    }
}