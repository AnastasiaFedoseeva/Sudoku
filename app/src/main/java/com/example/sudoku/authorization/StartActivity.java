package com.example.sudoku.authorization;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sudoku.Inf;
import com.example.sudoku.R;
import com.example.sudoku.main_menu.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class StartActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    FirebaseAuth mAuth;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences(Inf.PREF_NAME_USER, MODE_PRIVATE);
        String email, name, password;
        email = sharedPreferences.getString(Inf.EMAIL_USER, "");
        name = sharedPreferences.getString(Inf.NAME_USER, "");
        password = sharedPreferences.getString(Inf.PASSWORD_USER, "");
        if (!(email.isEmpty() && name.isEmpty() && password.isEmpty())) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Inf.setAuthorized(false);
                        Toast.makeText(getApplicationContext(), "Не удалось войти в систему", Toast.LENGTH_LONG).show();
                    } else {
                        Inf.setAuthorized(true);
                        Toast.makeText(getApplicationContext(), "Приятной игры, " + name + "!", Toast.LENGTH_LONG).show();
                    }
                    intent = new Intent(StartActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            intent = new Intent(StartActivity.this, AuthorizationActivity.class);
            startActivity(intent);
        }

    }
}