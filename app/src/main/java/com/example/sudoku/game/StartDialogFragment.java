package com.example.sudoku.game;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.sudoku.R;
import com.example.sudoku.main_menu.MainActivity;


public class StartDialogFragment extends AppCompatDialogFragment {
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String[] data = {"Тренировка", "Легко", "Средне", "Сложно"};
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ListView lv = ((AlertDialog) dialog).getListView();
                if (which == Dialog.BUTTON_POSITIVE) {
                    if (getActivity().getClass() == MainActivity.class) {
                        Intent game_intent = new Intent(getActivity(), GameActivity.class);
                        game_intent.putExtra("dif", lv.getCheckedItemPosition());//передаёт выбранную сложность
                        startActivity(game_intent);
                    } else if (getActivity().getClass() == GameActivity.class) {
                        Intent game_intent = new Intent(getActivity(), GameActivity.class);
                        game_intent.putExtra("dif", lv.getCheckedItemPosition());//передаёт выбранную сложность
                        getActivity().finish();
                        startActivity(game_intent);
                    }
                } else if (which == Dialog.BUTTON_NEGATIVE) {
                    if (getActivity().getClass() == GameActivity.class) getActivity().finish();
                    else ((AlertDialog) dialog).dismiss();
                }
            }
        };
        if (getActivity().getClass() == GameActivity.class) setCancelable(false);
        adb.setTitle(R.string.title_start_d).setSingleChoiceItems(data, 2, myClickListener)
                .setPositiveButton(R.string.start, myClickListener).setNegativeButton(R.string.cancel, myClickListener);
        return adb.create();
    }
}
