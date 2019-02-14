package com.TheJavaCooker.CookingWithJava.DataBase;

public enum TipoDeImagen {
    INDEFINIDO("Indefinido"),
    USUARIO("Usuario"),
    RECETA("Receta");

    private String text;

    @Override
    public String toString() {
        return text;
    }

    TipoDeImagen(String text_) {
        text = text_;
    }

    public static TipoDeImagen fromString(String text_) {
        for (TipoDeImagen b : TipoDeImagen.values()) {
            if (b.text.equalsIgnoreCase(text_)) {
                return b;
            }
        }
        return INDEFINIDO;
    }
}
