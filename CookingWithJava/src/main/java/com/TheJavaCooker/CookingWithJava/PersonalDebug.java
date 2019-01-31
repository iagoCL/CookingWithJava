package com.TheJavaCooker.CookingWithJava;

public class PersonalDebug {
    private static boolean activarDebug;
    public static void setDebug(boolean activarDebug_){
        PersonalDebug.activarDebug=activarDebug_;
    }
    public static void imprimir(String texto)
    {
        if(activarDebug)
        {
            System.out.println(texto);
        }
    }
}
