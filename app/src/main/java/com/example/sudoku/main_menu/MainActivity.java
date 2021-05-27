package com.example.sudoku.main_menu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.sudoku.Inf;
import com.example.sudoku.R;
import com.example.sudoku.authorization.AuthorizationActivity;
import com.example.sudoku.game.StartDialogFragment;
import com.example.sudoku.records.RecordsActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    Button start_btn, records_btn;
    TextView user_inf, exit_login;
    Intent intent;
    FirebaseAuth mAuth;
    FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        manager = getSupportFragmentManager();
        mAuth = FirebaseAuth.getInstance();
        user_inf = (TextView) findViewById(R.id.user_inf);
        exit_login = (TextView) findViewById(R.id.exit_login);
        if (Inf.isAuthorized()) {
            SharedPreferences sharedPreferences = getSharedPreferences(Inf.PREF_NAME_USER, MODE_PRIVATE);
            user_inf.setText(sharedPreferences.getString(Inf.NAME_USER, ""));
            exit_login.setText("Сменить пользователя");
        } else {
            user_inf.setText("");
            exit_login.setText("Вход");
        }
        exit_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this,
                        AuthorizationActivity.class);
                startActivity(intent);
            }
        });
        start_btn = (Button) findViewById(R.id.start_button);
        records_btn = (Button) findViewById(R.id.records_button);
        StartDialogFragment startDialogFragment = new StartDialogFragment();
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (manager.findFragmentByTag("startGameDialog") == null)
                startDialogFragment.show(manager, "startGameDialog");
            }
        });
        records_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, RecordsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}
