package com.example.sudoku.records;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.sudoku.Inf;
import com.example.sudoku.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RecordsActivity extends AppCompatActivity {
    TextView bestTime, easyRecord, mediumRecord, hardRecord, allRecord;
    SharedPreferences sharedPreferences;
    RecordsFragment recordsFragment;
    TextView records_without_authorization;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    boolean successful = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.records_title);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        allRecord = (TextView) findViewById(R.id.allRecord);
        bestTime = (TextView) findViewById(R.id.bestTime);
        easyRecord = (TextView) findViewById(R.id.easyRecord);
        mediumRecord = (TextView) findViewById(R.id.mediumRecord);
        hardRecord = (TextView) findViewById(R.id.hardRecord);
        records_without_authorization = (TextView) findViewById(R.id.records_without_authorization);
        if (Inf.isAuthorized()) {
            mAuth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance().getReference();
            loadUserRecords();
        } else {
            loadLocalRecords();
            records_without_authorization.setText(R.string.records_without_authorization);
        }
    }

    void loadLocalRecords() {
        sharedPreferences = getSharedPreferences(Inf.PREF_NAME_RECORD, MODE_PRIVATE);
        bestTime.setText(sharedPreferences.getString(Inf.TIME_RECORD, "00:00:00"));
        easyRecord.setText(sharedPreferences.getString(Inf.EASY_RECORD, "0"));
        mediumRecord.setText(sharedPreferences.getString(Inf.MEDIUM_RECORD, "0"));
        hardRecord.setText(sharedPreferences.getString(Inf.HARD_RECORD, "0"));
        allRecord.setText(sharedPreferences.getString(Inf.ALL_RECORD, "0"));
    }


    void loadUserRecords() {
        loadRecord("bestTime");
        loadRecord("allCol");
        loadRecord("easyCol");
        loadRecord("mediumCol");
        loadRecord("hardCol");
    }


    void loadRecord(String recordName) {
        if (mAuth.getCurrentUser() != null && successful) {
            mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child(recordName).get()
                    .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(),
                                        "Не удалось загрузить рекорды пользователя\n" +
                                                "Будут показаны локальные рекорды", Toast.LENGTH_LONG)
                                        .show();
                                records_without_authorization.setText("Не удалось получить рекорды других пользователей");
                                loadLocalRecords();
                                successful = false;
                            } else {
                                switch (recordName) {
                                    case "bestTime":
                                        bestTime.setText(task.getResult().getValue().toString());
                                        break;
                                    case "allCol":
                                        allRecord.setText(task.getResult().getValue().toString());
                                        break;
                                    case "easyCol":
                                        easyRecord.setText(task.getResult().getValue().toString());
                                        break;
                                    case "mediumCol":
                                        mediumRecord.setText(task.getResult().getValue().toString());
                                        break;
                                    case "hardCol":
                                        hardRecord.setText(task.getResult().getValue().toString());
                                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                        recordsFragment = new RecordsFragment();
                                        recordsFragment.setRetainInstance(true);
                                        fragmentTransaction.add(R.id.records, recordsFragment, null);
                                        try {
                                            fragmentTransaction.commit();
                                        } catch (Exception e) {
                                        }
                                        break;
                                }
                            }
                        }
                    });
        }
    }
}
