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
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    EditText logUsername;
    EditText logPassword;
    Button loginBtnLog;
    Button registerBtnLog;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        logUsername = findViewById(R.id.regUsername);
        logPassword = findViewById(R.id.regPassword);
        loginBtnLog = findViewById(R.id.registerBtnReg);
        registerBtnLog = findViewById(R.id.registerBtnLog);
        mAuth = FirebaseAuth.getInstance();

        registerBtnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });

        loginBtnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

    }

    private void loginUser() {
        String username = logUsername.getText().toString();
        String password = logPassword.getText().toString();

        if (TextUtils.isEmpty(username)) {
            logUsername.setError("Email cannot be empty");
            logPassword.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            logUsername.setError("Password cannot be empty");
            logPassword.requestFocus();
        } else {
            mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(Login.this, "User login successfully",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Payments.class));
                    } else {
                        Toast.makeText(Login.this, "Error login",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
