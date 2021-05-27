package com.example.sudoku.authorization;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sudoku.Inf;
import com.example.sudoku.R;
import com.example.sudoku.main_menu.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static android.content.Context.MODE_PRIVATE;

public class LoginFragment extends Fragment {
    EditText emailView, passwdView;
    Button loginbtn, for_registration, cancel;
    FirebaseAuth mAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        mAuth = FirebaseAuth.getInstance();
        emailView = (EditText) view.findViewById(R.id.email_login);
        passwdView = (EditText) view.findViewById(R.id.password_login);
        for_registration = (Button) view.findViewById(R.id.for_registration);
        loginbtn = (Button) view.findViewById(R.id.name_login);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUserAccount();
            }
        });
        cancel = (Button) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void loginUserAccount() {
        String email, password;
        email = emailView.getText().toString();
        password = passwdView.getText().toString();
        // проверки правильности ввода адреса электронной почты и пароля
        if (email.isEmpty()) {
            Toast.makeText(getContext(), "Пожалуйста, введите email!",
                    Toast.LENGTH_LONG).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(getContext(), "Пожалуйста, введите пароль!",
                    Toast.LENGTH_LONG).show();
            return;
        } else {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Inf.PREF_NAME_USER,
                    MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            //mAuth.signOut();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Вход успешен!",
                                        Toast.LENGTH_LONG).show();
                                Inf.setAuthorized(true);
                                AuthorizationActivity.save_authorized(email,
                                        mAuth.getCurrentUser().getDisplayName(), password,
                                        sharedPreferences.edit());
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                            } else {
                                // Ошибка входа
                                Toast.makeText(getContext(),
                                        "Что-то пошло не так :(\nПопробуйте снова!",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }
}
