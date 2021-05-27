package com.example.sudoku.game;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.sudoku.R;

public class GameOverDialogFragment extends AppCompatDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == Dialog.BUTTON_POSITIVE) {
                    StartDialogFragment startDialogFragment = new StartDialogFragment();
                    startDialogFragment.show(getActivity().getSupportFragmentManager(), "startGameDialog");
                } else if (which == Dialog.BUTTON_NEGATIVE) getActivity().finish();
            }
        };
        setCancelable(false);
        adb.setTitle(R.string.title_game_over_d).setMessage("Вы совершили много ошибок. Попробуете снова?")
                .setPositiveButton(R.string.game_over_new, myClickListener)
                .setNegativeButton(R.string.game_over_exit, myClickListener);
        return adb.create();
    }
}
