package com.example.sudoku.game;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.sudoku.main_menu.MainActivity;

public class ExitGameDialogFragment extends AppCompatDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == Dialog.BUTTON_POSITIVE) {
                    startActivity(new Intent(getActivity(), MainActivity.class));
                } else if (which == Dialog.BUTTON_NEGATIVE) {
                    if (GameActivity.isPause) if (getActivity().getClass() == GameActivity.class)
                        ((GameActivity) getActivity()).setPause();
                    ((AlertDialog) dialog).dismiss();
                }
            }
        };
        setCancelable(false);
        adb.setTitle("Вы уверены, что хотите выйти?").setMessage("Рекорд не сохранится")
                .setPositiveButton("Да", myClickListener)
                .setNegativeButton("Отмена", myClickListener);
        return adb.create();
    }
}
