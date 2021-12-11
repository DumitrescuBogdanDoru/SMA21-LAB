package com.upt.cti.smartwallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import javax.microedition.khronos.egl.EGLDisplay;

public class Register extends AppCompatActivity {

    EditText regUsername;
    EditText regPassword;
    Button registerBtnReg;
    Button loginBtnReg;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Register");

        regUsername = findViewById(R.id.regUsername);
        regPassword = findViewById(R.id.regPassword);
        registerBtnReg = findViewById(R.id.registerBtnReg);
        loginBtnReg = findViewById(R.id.loginBtnReg);

        mAuth = FirebaseAuth.getInstance();

        loginBtnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

        registerBtnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });

    }

    private void createUser() {
        String username = regUsername.getText().toString();
        String password = regPassword.getText().toString();

        if (TextUtils.isEmpty(username)) {
            regUsername.setError("Email cannot be empty");
            regUsername.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            regPassword.setError("Password cannot be empty");
            regPassword.requestFocus();
        } else {
            mAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(Register.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Login.class));
                    } else {
                        Toast.makeText(Register.this, "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}