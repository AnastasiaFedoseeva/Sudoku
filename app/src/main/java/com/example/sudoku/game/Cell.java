package com.example.sudoku.game;

import java.util.ArrayList;

class Cell {
    int value;
    private float x1, y1, x2, y2;
    private boolean start_open, is_open, is_touch, is_error;
    ArrayList<Integer> notes;

    public Cell(int value, boolean start_open,
                boolean is_open) {
        this.value = value;
        this.start_open = start_open;
        this.is_open = is_open;
        this.is_touch = false;
        this.is_error = false;
        this.notes = new ArrayList<>();
    }

    public boolean isStart_open() {
        return start_open;
    }

    public void setStart_open(boolean start_open) {
        this.start_open = start_open;
    }

    public boolean isIs_touch() {
        return is_touch;
    }

    public void setIs_touch(boolean is_touch) {
        this.is_touch = is_touch;
    }

    public boolean isIs_open() {
        return is_open;
    }

    public void setIs_open(boolean is_open) {
        this.is_open = is_open;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isIs_error() {
        return is_error;
    }

    public void setIs_error(boolean is_error) {
        this.is_error = is_error;
    }

    public float getX1() {
        return x1;
    }

    public float getY1() {
        return y1;
    }

    public float getX2() {
        return x2;
    }

    public float getY2() {
        return y2;
    }

    public void setCoord(float x1, float x2, float y1, float y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    public ArrayList<Integer> getNotes() {
        return notes;
    }

    public void setNote(Integer note) {
        if (!getNotes().contains(note)) this.notes.add(note);
        else getNotes().remove(note);
    }
}