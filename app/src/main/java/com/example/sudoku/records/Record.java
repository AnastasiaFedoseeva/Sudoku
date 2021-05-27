package com.example.sudoku.records;

public class Record implements Comparable<Record> {
    String userName, time;

    public Record() {
    }

    public Record(String userName, String time) {
        this.userName = userName;
        this.time = time;
    }

    public String getUserName() {
        return userName;
    }

    public String getTime() {
        return time;
    }

    @Override
    public int compareTo(Record o) {
        return getTime().compareTo(o.getTime());
    }
}
