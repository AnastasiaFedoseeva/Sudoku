package com.example.sudoku.game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;

import com.example.sudoku.R;

public class ButtonsFragment extends Fragment {
    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;
    ImageButton clear_btn, pause_btn;
    ToggleButton note_btn;
    SudokuView sudokuView;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.buttons_fragment, container, false);
        btn1 = (Button) view.findViewById(R.id.one);
        btn2 = (Button) view.findViewById(R.id.two);
        btn3 = (Button) view.findViewById(R.id.three);
        btn4 = (Button) view.findViewById(R.id.four);
        btn5 = (Button) view.findViewById(R.id.five);
        btn6 = (Button) view.findViewById(R.id.six);
        btn7 = (Button) view.findViewById(R.id.seven);
        btn8 = (Button) view.findViewById(R.id.eight);
        btn9 = (Button) view.findViewById(R.id.nine);
        note_btn = (ToggleButton) view.findViewById(R.id.noteBtn);
        pause_btn = (ImageButton) view.findViewById(R.id.pauseBtn);
        clear_btn = (ImageButton) view.findViewById(R.id.clear);
        note_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (sudokuView == null) sudokuView = getActivity().findViewById(R.id.sudokuView);
                if (sudokuView != null) sudokuView.setDoNote(isChecked);
            }
        });
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sudokuView == null) sudokuView = getActivity().findViewById(R.id.sudokuView);
                if (sudokuView != null) {
                    if (v.getId() == R.id.pauseBtn) {
                        if (getActivity().getClass() == GameActivity.class)
                            ((GameActivity) getActivity()).setPause();
                    }
                    if (sudokuView.isDoNote()) {
                        switch (v.getId()) {
                            case R.id.one:
                                sudokuView.setNote(1);
                                break;
                            case R.id.two:
                                sudokuView.setNote(2);
                                break;
                            case R.id.three:
                                sudokuView.setNote(3);
                                break;
                            case R.id.four:
                                sudokuView.setNote(4);
                                break;
                            case R.id.five:
                                sudokuView.setNote(5);
                                break;
                            case R.id.six:
                                sudokuView.setNote(6);
                                break;
                            case R.id.seven:
                                sudokuView.setNote(7);
                                break;
                            case R.id.eight:
                                sudokuView.setNote(8);
                                break;
                            case R.id.nine:
                                sudokuView.setNote(9);
                                break;
                            case R.id.clear:
                                sudokuView.clearNotes();
                                break;
                        }
                    } else {
                        switch (v.getId()) {
                            case R.id.one:
                                sudokuView.setNewValue(1);
                                break;
                            case R.id.two:
                                sudokuView.setNewValue(2);
                                break;
                            case R.id.three:
                                sudokuView.setNewValue(3);
                                break;
                            case R.id.four:
                                sudokuView.setNewValue(4);
                                break;
                            case R.id.five:
                                sudokuView.setNewValue(5);
                                break;
                            case R.id.six:
                                sudokuView.setNewValue(6);
                                break;
                            case R.id.seven:
                                sudokuView.setNewValue(7);
                                break;
                            case R.id.eight:
                                sudokuView.setNewValue(8);
                                break;
                            case R.id.nine:
                                sudokuView.setNewValue(9);
                                break;
                            case R.id.clear:
                                sudokuView.clearValue();
                                break;
                        }
                    }
                }
            }
        };
        btn1.setOnClickListener(listener);
        btn2.setOnClickListener(listener);
        btn3.setOnClickListener(listener);
        btn4.setOnClickListener(listener);
        btn5.setOnClickListener(listener);
        btn6.setOnClickListener(listener);
        btn7.setOnClickListener(listener);
        btn8.setOnClickListener(listener);
        btn9.setOnClickListener(listener);
        clear_btn.setOnClickListener(listener);
        pause_btn.setOnClickListener(listener);
        return view;
    }
}
