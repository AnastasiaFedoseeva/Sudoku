package com.example.sudoku.game;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.sudoku.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SudokuView extends SurfaceView implements SurfaceHolder.Callback {
    static Sudoku sudoku;
    DrawThread drawThread;
    Cell[][] table = new Cell[9][9];
    int on_touch_i, on_touch_j;
    private boolean doNote = false;

    public SudokuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    public boolean isDoNote() {
        return doNote;
    }

    public void setDoNote(boolean doNote) {
        this.doNote = doNote;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.drawThread = new DrawThread(holder);
        drawThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        this.drawThread.requestToStop();
        boolean retry = true;
        while (retry) {
            try {
                drawThread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void checkErrors(int cell_i, int cell_j) {
        int value;
        clearErrors();
        boolean isError = false;
        if (cell_i < 9 && cell_j < 9 && table[cell_i][cell_j].isIs_open()) {
            value = table[cell_i][cell_j].getValue();
            //проверка по вертикали и горизонтали
            for (int i = 0; i < 9; i++) {
                if (value == table[i][cell_j].getValue() && i != cell_i && table[i][cell_j].isIs_open()) {
                    if (!table[i][cell_j].isIs_error() && !isError) {
                        isError = true;
                        if (sudoku.isCheckErrors()) sudoku.setErrors();
                        ((GameActivity) getContext()).errorsView.setText(Sudoku.
                                getActivity().getString(R.string.errors,
                                sudoku.getErrors()));
                    }
                    table[cell_i][cell_j].setIs_error(true);
                    table[i][cell_j].setIs_error(true);
                }
                if (value == table[cell_i][i].getValue() && i != cell_j && table[cell_i][i].isIs_open()) {
                    if (!table[cell_i][i].isIs_error() && !isError) {
                        isError = true;
                        if (sudoku.isCheckErrors()) sudoku.setErrors();
                        ((GameActivity) getContext()).errorsView.setText(Sudoku.
                                getActivity().getString(R.string.errors,
                                sudoku.getErrors()));
                    }
                    table[cell_i][cell_j].setIs_error(true);
                    table[cell_i][i].setIs_error(true);
                }
            }
            //определяет где начать и закончить проверку
            int start_border_i;
            int finish_border_i;
            int start_border_j;
            int finish_border_j;
            if (cell_i < 3) {
                start_border_i = 0;
                finish_border_i = 3;
            } else if (cell_i < 6) {
                start_border_i = 3;
                finish_border_i = 6;
            } else {
                start_border_i = 6;
                finish_border_i = 9;
            }
            if (cell_j < 3) {
                start_border_j = 0;
                finish_border_j = 3;
            } else if (cell_j < 6) {
                start_border_j = 3;
                finish_border_j = 6;
            } else {
                start_border_j = 6;
                finish_border_j = 9;
            }
            //проверка в квадрате 3x3
            for (int i = start_border_i; i < finish_border_i; i++) {
                for (int j = start_border_j; j < finish_border_j; j++) {
                    if (!(i == cell_i && j == cell_j)) {
                        if (value == table[i][j].getValue() && table[i][j].isIs_open()) {
                            if (!table[cell_i][cell_j].isIs_error() && !isError) {
                                isError = true;
                                if (sudoku.isCheckErrors()) sudoku.setErrors();
                                ((GameActivity) getContext()).errorsView.setText(Sudoku.
                                        getActivity().getString(R.string.errors,
                                        sudoku.getErrors()));
                            }
                            table[cell_i][cell_j].setIs_error(true);
                            table[i][j].setIs_error(true);
                        }
                    }
                }
            }
        }
        if (sudoku.getErrors() > 3) {
            ((GameActivity) getContext()).gameOver();
        } else if (isError) {
            ((GameActivity) getContext()).s += 3;
            Toast.makeText(getContext(), R.string.toast_3_sec, Toast.LENGTH_LONG).show();
        }
    }

    public void clearErrors() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) table[i][j].setIs_error(false);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {//"выбирает" нажатую клетку
        float x = event.getX();
        float y = event.getY();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                table[i][j].setIs_touch(false);
                if (x > table[i][j].getX1() && x < table[i][j].getX2()) {
                    if (y > table[i][j].getY1() && y < table[i][j].getY2()) {
                        table[i][j].setIs_touch(true);
                        on_touch_i = i;
                        on_touch_j = j;
                    }
                }
            }
        }
        return true;
    }

    public void setNewValue(int value) {//меняет значение клетки после нажатия кнопки
        if (!table[on_touch_i][on_touch_j].isStart_open()) {
            table[on_touch_i][on_touch_j].setIs_open(true);
            table[on_touch_i][on_touch_j].setValue(value);
            checkErrors(on_touch_i, on_touch_j);
            checkFinish();
        }
    }

    public void clearValue() {
        if (!table[on_touch_i][on_touch_j].isStart_open()) {
            if (!table[on_touch_i][on_touch_j].isIs_open()) {
                if (!table[on_touch_i][on_touch_j].getNotes().isEmpty()) clearNotes();
            } else table[on_touch_i][on_touch_j].setIs_open(false);
            clearErrors();
        }
    }

    public void setNote(int note) {
        if (!table[on_touch_i][on_touch_j].isStart_open()) {
            if (table[on_touch_i][on_touch_j].isIs_open()) {
                table[on_touch_i][on_touch_j].setIs_open(false);
                checkErrors(on_touch_i, on_touch_j);
            }
            table[on_touch_i][on_touch_j].setNote(note);
        }
    }

    public void clearNotes() {
        if (table[on_touch_i][on_touch_j].isIs_open() && !table[on_touch_i][on_touch_j].isStart_open())
            clearValue();
        else table[on_touch_i][on_touch_j].getNotes().clear();
    }

    public void checkFinish() {
        boolean finish_game = true;
        outer:
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (!table[i][j].isIs_open() || table[i][j].isIs_error()) {
                    finish_game = false;
                    break outer;
                }
            }
        }
        if (finish_game) this.finish();
    }

    public void finish() {
        Sudoku.finish();
        drawThread.working = false;
    }

    class DrawThread extends Thread {
        private final SurfaceHolder surfaceHolder;
        private boolean working = true;

        public DrawThread(SurfaceHolder surfaceHolder) {
            this.surfaceHolder = surfaceHolder;
        }

        public void requestToStop() {
            working = false;
        }

        @Override
        public void run() {
            Cell cell;
            Paint paint = new Paint();
            float textSize = 50f;
            boolean is_gen = false;
            float cell_coordx = 0, cell_coordy = 0;
            Canvas canvas;
            while (working) {
                canvas = surfaceHolder.lockCanvas();
                if (canvas != null) {
                    try {
                        float cell_w = canvas.getWidth() / 9f;
                        float cell_h = canvas.getHeight() / 9f;
                        try {
                            table = sudoku.sudokuGen.get();
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (!is_gen) {
                            for (int i = 0; i < 9; i++) {
                                for (int j = 0; j < 9; j++) {
                                    table[i][j].setCoord(cell_coordx, cell_coordx + cell_w,
                                            cell_coordy, cell_coordy + cell_h);
                                    cell_coordx += cell_w;
                                }
                                cell_coordy += cell_h;
                                cell_coordx = 0;
                            }
                            is_gen = true;
                        }
                        paint.setStyle(Paint.Style.FILL);
                        paint.setColor(Color.WHITE);
                        canvas.drawPaint(paint);
                        paint.setColor(Color.BLACK);
                        paint.setStyle(Paint.Style.STROKE);
                        float x = cell_w;
                        float y = cell_h;
                        for (int i = 1; i < 9; i++) { //отрисовка линий
                            if (i % 3 == 0) {
                                paint.setStrokeWidth(5);
                                canvas.drawLine(x, 0, x, canvas.getHeight(), paint);
                                canvas.drawLine(0, y, canvas.getWidth(), y, paint);
                                paint.setStrokeWidth(1);
                            } else {
                                canvas.drawLine(x, 0, x, canvas.getHeight(), paint);
                                canvas.drawLine(0, y, canvas.getWidth(), y, paint);
                            }
                            x += canvas.getWidth() / 9f;
                            y += canvas.getHeight() / 9f;
                        }
                        //отрисовка цифр
                        paint.setStrokeWidth(5);
                        canvas.drawRect(2, 2, canvas.getWidth() - 2,
                                canvas.getHeight() - 2, paint);

                        paint.setStrokeWidth(2);
                        paint.setTextSize(textSize);
                        x = canvas.getWidth() / 18f;
                        y = canvas.getHeight() / 18f;
                        for (int i = 0; i < 9; i++) {
                            for (int j = 0; j < 9; j++) {
                                cell = table[i][j];
                                paint.setColor(Color.BLACK);
                                if (cell.isIs_error()) paint.setColor(Color.RED);
                                if (cell.isIs_open()) {
                                    if (cell.isStart_open()) {//если клетка открыта изначально
                                        paint.setStyle(Paint.Style.FILL);
                                        canvas.drawText(Integer.toString(cell.getValue()),
                                                x - textSize / 4, y + textSize / 4, paint);
                                        paint.setStyle(Paint.Style.STROKE);
                                    } else {
                                        if (!cell.isIs_error())
                                            paint.setColor(getResources().getColor(R.color.purple_700));
                                        canvas.drawText(Integer.toString(cell.getValue()),//если заполнена пользователем
                                                x - textSize / 4, y + textSize / 4, paint);
                                    }
                                } else {
                                    ArrayList<Integer> notes = cell.getNotes();
                                    if (!notes.isEmpty()) {//отрисовка заметок
                                        float noteTextSize;
                                        if (getResources().getConfiguration().orientation
                                                == Configuration.ORIENTATION_PORTRAIT)
                                            noteTextSize = cell_h / 4f;
                                        else noteTextSize = cell_w / 4f;
                                        paint.setStyle(Paint.Style.FILL);
                                        paint.setColor(Color.rgb(100, 100, 100));
                                        paint.setTextSize(noteTextSize);
                                        float note_x = x - cell_w / 9f;
                                        float note_y = y - noteTextSize / 4f + noteTextSize / 10;
                                        for (int i1 = 0; i1 < notes.size(); i1++) {
                                            canvas.drawText(Integer.toString(notes.get(i1)),
                                                    note_x - noteTextSize,
                                                    note_y - noteTextSize,
                                                    paint);
                                            note_x += cell_w / 3f;
                                            if ((i1 + 1) % 3 == 0) {
                                                note_x = x - cell_w / 10f;
                                                note_y += cell_h / 3f;
                                            }
                                        }
                                        paint.setTextSize(textSize);
                                    }
                                }
                                if (cell.isIs_touch()) {//если выбранна
                                    paint.setStyle(Paint.Style.FILL);
                                    paint.setARGB(100, 10, 20, 255);
                                    canvas.drawRect(cell.getX1(), cell.getY1(), cell.getX2(),
                                            cell.getY2(), paint);
                                    paint.setColor(Color.BLACK);
                                    paint.setStyle(Paint.Style.STROKE);
                                }
                                x += canvas.getWidth() / 9f;
                            }
                            y += canvas.getHeight() / 9f;
                            x = canvas.getWidth() / 18f;
                        }
                    } finally {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }
    }
}
