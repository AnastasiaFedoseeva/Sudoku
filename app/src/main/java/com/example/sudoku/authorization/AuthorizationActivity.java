package com.example.sudoku.authorization;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.sudoku.Inf;
import com.example.sudoku.R;

public class AuthorizationActivity extends AppCompatActivity {
    RegistrationFragment registrationFragment;
    LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        loginFragment = new LoginFragment();
        registrationFragment = new RegistrationFragment();
        loginFragment.setRetainInstance(true);
        fragmentTransaction.add(R.id.authorization, loginFragment, null);
        fragmentTransaction.commit();
    }

    public void change_fragment_to_registration(View view) {
        registrationFragment = new RegistrationFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.authorization, registrationFragment);
        fragmentTransaction.commit();
    }

    public void change_fragment_to_login(View view) {
        loginFragment = new LoginFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.authorization, loginFragment);
        fragmentTransaction.commit();
    }

    public static void save_authorized(String email, String name, String password, SharedPreferences.Editor editor) {
        editor.putString(Inf.EMAIL_USER, email);
        editor.putString(Inf.NAME_USER, name);
        editor.putString(Inf.PASSWORD_USER, password);
        editor.apply();
    }
}