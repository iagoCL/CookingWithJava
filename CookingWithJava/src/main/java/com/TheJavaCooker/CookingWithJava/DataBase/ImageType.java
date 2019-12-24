package com.TheJavaCooker.CookingWithJava.DataBase;

public enum ImageType {
    UNDEFINED("Undefined"), USER("User"), RECIPE("Recipe");

    private String text;

    @Override
    public String toString() {
        return text;
    }

    ImageType(String text_) {
        text = text_;
    }

    public static ImageType fromString(String text_) {
        for (ImageType b : ImageType.values()) {
            if (b.text.equalsIgnoreCase(text_)) {
                return b;
            }
        }
        return UNDEFINED;
    }
}