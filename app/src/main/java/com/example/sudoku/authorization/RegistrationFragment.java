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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class RegistrationFragment extends Fragment {
    EditText emailView, nameView, passwdView;
    Button btnregister, for_login, cancel;
    FirebaseAuth mAuth;
    SharedPreferences sharedPreferences;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registration_fragment, container, false);
        mAuth = FirebaseAuth.getInstance();
        emailView = (EditText) view.findViewById(R.id.email_registration);
        nameView = (EditText) view.findViewById(R.id.name_registration);
        passwdView = (EditText) view.findViewById(R.id.password_registration);
        for_login = (Button) view.findViewById(R.id.for_login);
        btnregister = (Button) view.findViewById(R.id.btnregister);
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
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

    private void registerNewUser() {
        String email, password, username;
        email = emailView.getText().toString();
        password = passwdView.getText().toString();
        username = nameView.getText().toString();
        if (email.isEmpty()) {
            Toast.makeText(getContext(), "Пожалуйста, введите email",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(getContext(), "Пожалуйста, введите пароль",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (username.isEmpty()) {
            Toast.makeText(getContext(), "Пожалуйста, введите имя",
                    Toast.LENGTH_LONG).show();
            return;
        } else {

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(),
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(getContext(),
                                        "Что-то пошло не так :(\nПопробуйте снова!",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                //addUserNameToUser(task.getResult().getUser(), username);
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(username).build();
                                if (task.getResult().getUser().updateProfile(profileUpdates).isSuccessful()) {
                                    Toast.makeText(getContext(), "Пользователь создан",
                                            Toast.LENGTH_LONG).show();
                                }
                                sharedPreferences = getActivity().getSharedPreferences(Inf.PREF_NAME_USER,
                                        MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.apply();
                                mAuth.signInWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    writeNewUser(Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName());
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
                    });
        }
    }

    public void writeNewUser(String name) {
        User user = new User(name);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mDatabase.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).setValue(user);
    }
}
