package com.TheJavaCooker.CookingWithJava;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersonalDebug {
    private static boolean activarDebug;
	private static Logger logger = LoggerFactory.getLogger(PersonalDebug.class);

    public static void setDebug(boolean activarDebug_) {
        PersonalDebug.activarDebug = activarDebug_;
    }

    public static void imprimir(String texto) {
        if (activarDebug) {
            logger.warn(texto);
        }
    }
}
