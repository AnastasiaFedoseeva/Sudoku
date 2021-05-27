package com.example.sudoku.authorization;

public class User {
    String name;
    String bestTime = "00:00:00";
    int allCol, easyCol, mediumCol, hardCol;

    public User() {
    }

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getBestTime() {
        return bestTime;
    }

    public int getAllCol() {
        return allCol;
    }

    public int getEasyCol() {
        return easyCol;
    }

    public int getMediumCol() {
        return mediumCol;
    }

    public int getHardCol() {
        return hardCol;
    }
}
