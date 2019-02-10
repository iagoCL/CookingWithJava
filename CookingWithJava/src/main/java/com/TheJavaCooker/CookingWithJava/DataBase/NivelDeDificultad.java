package com.TheJavaCooker.CookingWithJava.DataBase;

public enum NivelDeDificultad {
    INDEFINIDO("Indefinido"),
    FACIL("Facil"),
    MEDIO("Medio"),
    DIFICIL("Dificil"),
    EXPERTO("Experto"),
    PROFESIONAL("Profesional");

    private String text;

    @Override
    public String toString() {
        return text;
    }

    NivelDeDificultad(String text_) {
        text = text_;
    }

    public static NivelDeDificultad fromString(String text_) {
        for (NivelDeDificultad b : NivelDeDificultad.values()) {
            if (b.text.equalsIgnoreCase(text_)) {
                return b;
            }
        }
        return INDEFINIDO;
    }
}
