package com.TheJavaCooker.CookingWithJava.DataBase;

public enum DifficultyLevel {
    UNDEFINED("Undefined"), EASY("Easy"), INTERMEDIATE("Intermediate"), DIFFICULT("Difficult"), EXPERT("Expert"),
    PROFESSIONAL("Professional");

    private String text;

    @Override
    public String toString() {
        return text;
    }

    DifficultyLevel(String text_) {
        text = text_;
    }

    public static DifficultyLevel fromString(String text_) {
        for (DifficultyLevel comparedDifficulty : DifficultyLevel.values()) {
            if (comparedDifficulty.text.equalsIgnoreCase(text_)) {
                return comparedDifficulty;
            }
        }
        return UNDEFINED;
    }
}