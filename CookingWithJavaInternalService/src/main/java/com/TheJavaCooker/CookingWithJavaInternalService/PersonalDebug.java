package com.TheJavaCooker.CookingWithJavaInternalService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersonalDebug {
    private static boolean enableDebug;
    private static Logger logger = LoggerFactory.getLogger(PersonalDebug.class);

    public static void setDebug(boolean enableDebug_) {
        PersonalDebug.enableDebug = enableDebug_;
    }

    public static void printMsg(String text) {
        if (enableDebug) {
            logger.warn(text);
        }
    }
}