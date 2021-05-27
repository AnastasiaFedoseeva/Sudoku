package com.example.sudoku;

public class Inf {
    private static boolean Authorized;

    //firebase
    public static final String EASY = "easy";
    public static final String MEDIUM = "medium";
    public static final String HARD = "hard";

    //для локальных рекордов
    public static final String PREF_NAME_RECORD = "Records";
    public static final String TIME_RECORD = "TIME";
    public static final String EASY_RECORD = "EASY";
    public static final String MEDIUM_RECORD = "MEDIUM";
    public static final String HARD_RECORD = "HARD";
    public static final String ALL_RECORD = "ALL";

    //для автоматической аторизации при открытии приложения
    public static final String PREF_NAME_USER = "User";
    public static final String EMAIL_USER = "EMAIL";
    public static final String NAME_USER = "NAME";
    public static final String PASSWORD_USER = "PASSWORD";

    public static boolean isAuthorized() {
        return Authorized;
    }

    public static void setAuthorized(boolean Authorized) {
        Inf.Authorized = Authorized;
    }
}
