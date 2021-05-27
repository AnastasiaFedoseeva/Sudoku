package com.example.sudoku.game;

import android.app.Activity;
import android.os.AsyncTask;

public class Sudoku {
    public static boolean is_finish = false;
    static int difficulty;
    private int errors;
    private boolean checkErrors;
    private static Activity activity;
    SudokuGen sudokuGen;

    public Sudoku(int dif, Activity start_activity) {
        this.checkErrors = true;
        if (dif == 0) {
            difficulty = 20;
            this.checkErrors = false;
        } else if (dif == 1) difficulty = 20;
        else if (dif == 2) difficulty = 30;
        else if (dif == 3) difficulty = 40;
        activity = start_activity;
        sudokuGen = new SudokuGen();
        is_finish = false;
        sudokuGen.execute();
    }

    public boolean isCheckErrors() {
        return checkErrors;
    }

    public int getErrors() {
        return errors;
    }

    public static Activity getActivity() {
        return activity;
    }

    public void setErrors() {
        this.errors++;
    }

    static void finish() {
        is_finish = true;
        FinishGameDialogFragment finishGameDialogFragment = new FinishGameDialogFragment();
        finishGameDialogFragment.show(((GameActivity) activity).getSupportFragmentManager(), "finish");
    }
}

class SudokuGen extends AsyncTask<Integer, Integer[][], Cell[][]> {
    int n = 3;
    Integer[][] table;
    Cell[][] cells = new Cell[9][9];
    int difficulty = Sudoku.difficulty;

    void transposing() {
        for (int i = 0; i < this.table.length; i++) {
            for (int j = i + 1; j < this.table[i].length; j++) {
                int element = this.table[i][j];
                this.table[i][j] = this.table[j][i];
                this.table[j][i] = element;
            }
        }
    }

    void swap_rows_small() {
        short area = (short) (Math.random() * this.n);
        short line1 = (short) (Math.random() * this.n);
        short n1 = (short) (area * this.n + line1);
        short line2 = (short) (Math.random() * this.n);
        while (line1 == line2) line2 = (short) (Math.random() * this.n);
        short n2 = (short) (area * this.n + line2);
        Integer[] row = this.table[n1];
        this.table[n1] = this.table[n2];
        this.table[n2] = row;
    }

    void swap_colums_small() {
        this.transposing();
        this.swap_rows_small();
        this.transposing();
    }

    void swap_rows_area() {
        short area1 = (short) (Math.random() * this.n);
        short area2 = (short) (Math.random() * this.n);
        while (area2 == area1) area2 = (short) (Math.random() * this.n);
        for (int i = 0; i < this.n; i++) {
            short n1 = (short) (area1 * this.n + i);
            short n2 = (short) (area2 * this.n + i);
            Integer[] row = this.table[n1];
            this.table[n1] = this.table[n2];
            this.table[n2] = row;
        }
    }

    void swap_colums_area() {
        this.transposing();
        this.swap_rows_area();
        this.transposing();
    }

    void mix() {
        for (int i = 0; i < 10; i++) {
            short func_n = (short) (Math.random() * 5);
            switch (func_n) {
                case 1:
                    this.transposing();
                case 2:
                    this.swap_rows_small();
                case 3:
                    this.swap_colums_small();
                case 4:
                    this.swap_rows_area();
                case 5:
                    this.swap_colums_area();
            }
        }
    }

    void makeCells() {
        //создаёт бъекты класса Cell на основе матрицы из чисел
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cells[i][j] = new Cell(table[i][j], true, true);
            }
        }
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Cell[][] doInBackground(Integer... parameter) {
        //генерирует таблицу и определяет какие клетки будут скрыты
        this.table = new Integer[n * n][n * n];
        for (int i = 0; i < n * n; i++) {
            Integer[] col = new Integer[this.n * this.n];
            for (int j = 0; j < this.n * this.n; j++) {
                col[j] = (((i * this.n + i / this.n + j) % (this.n * this.n) + 1));
            }
            this.table[i] = col;
        }
        this.mix();
        this.makeCells();
        int x = (int) (Math.random() * 9);
        int y = (int) (Math.random() * 9);
        int c = 0;
        while (c < this.difficulty) {
            if (this.cells[y][x].isStart_open()) {
                this.cells[y][x].setStart_open(false);
                this.cells[y][x].setIs_open(false);
                c++;
            }
            x = (int) (Math.random() * 9);
            y = (int) (Math.random() * 9);
        }
        return this.cells;
    }
}
