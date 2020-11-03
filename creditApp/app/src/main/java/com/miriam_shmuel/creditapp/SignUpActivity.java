package com.miriam_shmuel.creditapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Random;

import static android.graphics.Color.GREEN;

public class SignUpActivity extends AppCompatActivity  {
    EditText userName, emailId , password , pwdauth;
    Button btnSignUp;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFirebaseAuth = FirebaseAuth.getInstance();
        userName = findViewById(R.id.username);
        emailId = findViewById(R.id.email);
        password  = findViewById(R.id.password);
        pwdauth = findViewById(R.id.pwdAuth);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = userName.getText().toString();
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();
                String pwdAuth = pwdauth.getText().toString();
                if(name.isEmpty()){
                    userName.setError("Please enter your name");
                    userName.requestFocus();
                }
                else if(email.isEmpty()){
                    emailId.setError("Please enter your email");
                    emailId.requestFocus();
                }
                else  if(pwd.isEmpty()){
                    password.setError("Please enter your password");
                    password.requestFocus();
                }
                else  if(pwdAuth.isEmpty()){
                    pwdauth.setError("Please enter agin password");
                    pwdauth.requestFocus();
                }
                else  if(name.isEmpty() && email.isEmpty() && pwd.isEmpty() && pwdAuth.isEmpty()){
                    Toast.makeText(SignUpActivity.this,"Fields Are Empty!", Toast.LENGTH_SHORT).show();
                }
                else  if((!(name.isEmpty() && email.isEmpty() && pwd.isEmpty() && pwdAuth.isEmpty())) && (pwd.equals(pwdAuth))){
                    mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(SignUpActivity.this,"SignUp Unsuccessful, Please Try Again",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(SignUpActivity.this,"Success !",Toast.LENGTH_SHORT).show();
                                finish();
                                Intent intToHome = new Intent(SignUpActivity.this, HomeActivity.class);
                                intToHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                        Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intToHome);
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(SignUpActivity.this,"Error Occurred!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
       validatePwd();
        setupPasswordToggleView();
    }

    private void validatePwd() {
        if(!(password.getText().toString().isEmpty() && pwdauth.getText().toString().isEmpty())){
            if(password.getText().toString().equals(pwdauth.getText().toString())){
               picV();
            }
            else{
                picX();
            }
        }
    }

    private void picV() {
    }

    private void picX() {
    }

    private void setupPasswordToggleView() {
    }
}
