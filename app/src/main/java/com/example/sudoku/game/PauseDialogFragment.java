package com.example.sudoku.game;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;

public class PauseDialogFragment extends AppCompatDialogFragment {
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == Dialog.BUTTON_POSITIVE) {
                    if (getActivity().getClass() == GameActivity.class)
                        ((GameActivity) getActivity()).setPause();
                } else if (which == Dialog.BUTTON_NEGATIVE) {
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    ExitGameDialogFragment exitGameDialogFragment = new ExitGameDialogFragment();
                    exitGameDialogFragment.show(manager, "exit");
                }
            }
        };
        setCancelable(false);
        adb.setTitle("Пауза").setMessage("Игра приостановлена").
                setPositiveButton("Продолжить", myClickListener).
                setNegativeButton("Выйти", myClickListener);
        return adb.create();
    }
}
