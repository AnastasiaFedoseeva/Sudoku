package com.example.sudoku.game;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.sudoku.R;

public class FinishGameDialogFragment extends AppCompatDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == Dialog.BUTTON_POSITIVE) {
                    StartDialogFragment startDialogFragment = new StartDialogFragment();
                    startDialogFragment.show(getActivity().getSupportFragmentManager(), "startGameDialog");
                } else if (which == Dialog.BUTTON_NEGATIVE) {
                    getActivity().finish();
                }
            }
        };
        setCancelable(false);
        adb.setTitle(R.string.title_finish_d).setMessage("Время прохождения: " + ((GameActivity) getActivity()).
                time.toString()).setPositiveButton(R.string.finish_game_new, myClickListener).
                setNegativeButton(R.string.finish_game_end, myClickListener);
        if (((GameActivity) getActivity()).dif != 0) ((GameActivity) getActivity()).saveRecord();
        return adb.create();
    }
}
