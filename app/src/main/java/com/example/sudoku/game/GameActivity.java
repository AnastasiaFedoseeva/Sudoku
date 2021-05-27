package com.example.sudoku.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.sudoku.Inf;
import com.example.sudoku.R;
import com.example.sudoku.records.Record;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GameActivity extends AppCompatActivity {
    ButtonsFragment buttonsFragment;
    SudokuView sudokuView;
    TextView timeView, difficultView, errorsView;
    Handler timeHandler;
    StringBuilder time;
    Sudoku sudoku;
    ExitGameDialogFragment exitGameDialogFragment;
    FragmentManager manager = getSupportFragmentManager();
    int h, m, s;
    int dif;
    static boolean isPause = false;
    SharedPreferences sharedPreferences;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    String difficult;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        timeView = (TextView) findViewById(R.id.timeView);
        Intent intent = getIntent();
        dif = intent.getIntExtra("dif", 0);
        difficultView = (TextView) findViewById(R.id.difficultView);
        errorsView = (TextView) findViewById(R.id.errorsView);
        errorsView.setText(getString(R.string.errors, 0));
        switch (dif) {
            case 0:
                difficultView.setText(R.string.training);
                errorsView.setText("");
                break;
            case 1:
                difficultView.setText(R.string.easy_name);
                break;
            case 2:
                difficultView.setText(R.string.medium_name);
                break;
            case 3:
                difficultView.setText(R.string.hard_name);
                break;
        }
        sudokuView = (SudokuView) findViewById(R.id.sudokuView);
        sudoku = new Sudoku(dif, this);
        SudokuView.sudoku = this.sudoku;
        timeHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (!isPause) {
                    s++;
                    if (s >= 60) {
                        s = 0;
                        m++;
                    }
                    if (m >= 60) {
                        m = 0;
                        h++;
                    }
                    time = new StringBuilder();
                    if (h < 10) time.append("0").append(h);
                    else time.append(h);
                    time.append(":");
                    if (m < 10) time.append("0").append(m);
                    else time.append(m);
                    time.append(":");
                    if (s < 10) time.append("0").append(s);
                    else time.append(s);
                    timeView.setText(time.toString());
                    if (!Sudoku.is_finish) timeHandler.sendEmptyMessageDelayed(0, 1000);
                }
            }

        };
        timeHandler.sendEmptyMessage(0);
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        buttonsFragment = new ButtonsFragment();
        buttonsFragment.setRetainInstance(true);
        fragmentTransaction.add(R.id.buttons_fragment, buttonsFragment, null);
        fragmentTransaction.commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    void saveRecord() {
        sharedPreferences = getSharedPreferences(Inf.PREF_NAME_RECORD, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String[] lastTimeRecord = sharedPreferences.getString(Inf.TIME_RECORD, "00:00:00").split(":");
        String[] nowTimeRecord = time.toString().split(":");
        if ((Integer.parseInt(lastTimeRecord[0]) == 0 && (Integer.parseInt(lastTimeRecord[1]) == 0)
                && (Integer.parseInt(lastTimeRecord[2]) == 0)) ||
                ((Integer.parseInt(lastTimeRecord[0]) * 60 * 60) +
                        (Integer.parseInt(lastTimeRecord[1]) * 60) +
                        (Integer.parseInt(lastTimeRecord[2]))) >=
                        ((Integer.parseInt(nowTimeRecord[0]) * 60 * 60) +
                                (Integer.parseInt(nowTimeRecord[1]) * 60) +
                                (Integer.parseInt(nowTimeRecord[2]))))
            editor.putString(Inf.TIME_RECORD, time.toString());
        if (dif == 1) editor.putString(Inf.EASY_RECORD,
                String.valueOf(Integer.parseInt(sharedPreferences.getString(Inf.EASY_RECORD,
                        "0")) + 1));
        if (dif == 2) editor.putString(Inf.MEDIUM_RECORD,
                String.valueOf(Integer.parseInt(sharedPreferences.getString(Inf.MEDIUM_RECORD,
                        "0")) + 1));
        if (dif == 3) editor.putString(Inf.HARD_RECORD,
                String.valueOf(Integer.parseInt(sharedPreferences.getString(Inf.HARD_RECORD,
                        "0")) + 1));
        editor.putString(Inf.ALL_RECORD,
                String.valueOf(Integer.parseInt(sharedPreferences.getString(Inf.ALL_RECORD,
                        "0")) + 1));
        editor.apply();
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            if (dif == 1) difficult = Inf.EASY;
            else if (dif == 2) difficult = Inf.MEDIUM;
            else if (dif == 3) difficult = Inf.HARD;
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("bestTime")
                    .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(),
                                "Не удалось записать рекорд в профиль", Toast.LENGTH_LONG)
                                .show();
                    } else {
                        String[] userTimeRecord = String.valueOf(task.getResult().getValue())
                                .split(":");
                        ArrayList<String> records = new ArrayList<>();
                        mDatabase.child("records").child(difficult).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()) {
                                    mDatabase.child("records").child(difficult).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            records.clear();
                                            for (DataSnapshot data : snapshot.getChildren()) {
                                                System.out.println(data.child("time").getValue());
                                                records.add(data.child("time").getValue().toString());
                                            }
                                            Context context = getApplicationContext();
                                            if (context != null) {
                                                Collections.sort(records, new Comparator<String>() {
                                                    @Override
                                                    public int compare(String t1, String t2) {
                                                        String[] time1 = t1.split(":");
                                                        String[] time2 = t2.split(":");
                                                        if (Integer.valueOf(time1[0]) - Integer.valueOf(time2[0]) >= 0) {
                                                            if (Integer.valueOf(time1[1]) - Integer.valueOf(time2[1]) >= 0) {
                                                                return Integer.valueOf(time1[2]) - Integer.valueOf(time2[2]);
                                                            } else return -1;
                                                        }
                                                        return -1;
                                                    }
                                                });
                                            }
                                            if (records.size() >= 10) {
                                                String[] t1 = records.get(records.size() - 1).split(":");
                                                String[] t2 = time.toString().split(":");
                                                if (Integer.valueOf(t2[0]) <= Integer.valueOf(t1[0])) {
                                                    if (Integer.valueOf(t2[1]) <= Integer.valueOf(t1[1])) {
                                                        if (Integer.valueOf(t2[2]) <= Integer.valueOf(t1[2]))
                                                            mDatabase.child("records").child(difficult).push().setValue(new Record(mAuth.getCurrentUser()
                                                                    .getDisplayName(), time.toString()));
                                                    }
                                                }
                                            } else {
                                                mDatabase.child("records").child(difficult).push().setValue(new Record(mAuth.getCurrentUser()
                                                        .getDisplayName(), time.toString()));
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                        }
                                    });
                                }
                            }
                        });

                        if (dif == 1) setColUser("easyCol");
                        if (dif == 2) setColUser("mediumCol");
                        if (dif == 3) setColUser("hardCol");
                        setColUser("allCol");
                        if ((Integer.parseInt(userTimeRecord[0]) == 0 && (Integer.parseInt(userTimeRecord[1]) == 0)
                                && (Integer.parseInt(userTimeRecord[2]) == 0)) ||
                                ((Integer.parseInt(userTimeRecord[0]) * 60 * 60) +
                                        (Integer.parseInt(userTimeRecord[1]) * 60) +
                                        (Integer.parseInt(userTimeRecord[2]))) >=
                                        ((Integer.parseInt(nowTimeRecord[0]) * 60 * 60) +
                                                (Integer.parseInt(nowTimeRecord[1]) * 60) +
                                                (Integer.parseInt(nowTimeRecord[2]))))
                            mDatabase.child("users").child(mAuth.getCurrentUser().getUid())
                                    .child("bestTime").setValue(time.toString())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (!task.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(),
                                                        "Не удалось записать рекорд в профиль",
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                    }
                }
            });
        }
    }

    private void setColUser(String colName) {
        mDatabase.child("users").child(mAuth
                .getCurrentUser().getUid()).child(colName).get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),
                                    "Не удалось записать рекорд в профиль",
                                    Toast.LENGTH_LONG).show();
                            System.out.println(colName);
                        } else {
                            int col = Integer.valueOf(task.getResult().getValue().toString());
                            mDatabase.child("users").child(mAuth.getCurrentUser().getUid())
                                    .child(colName).setValue(String.valueOf(col + 1))
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (!task.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(),
                                                        "Не удалось записать рекорд в профиль",
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    public void gameOver() {
        GameOverDialogFragment gameOverDialogFragment = new GameOverDialogFragment();
        gameOverDialogFragment.show(getSupportFragmentManager(), "game_over_dialog");
    }

    public void setPause() {
        isPause = !isPause;
        if (!isPause) timeHandler.sendEmptyMessage(0);
        else {
            PauseDialogFragment pauseDialogFragment = new PauseDialogFragment();
            pauseDialogFragment.show(manager, "pause");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            exitGameDialogFragment = new ExitGameDialogFragment();
            exitGameDialogFragment.show(manager, "exitGameDialog");
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        exitGameDialogFragment = new ExitGameDialogFragment();
        exitGameDialogFragment.show(manager, "exitGameDialog");
    }
}
