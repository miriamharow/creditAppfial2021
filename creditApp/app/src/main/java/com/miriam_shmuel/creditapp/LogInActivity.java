package com.miriam_shmuel.creditapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class LogInActivity extends Activity {
    EditText emailId, password;
    Button btnSignIn, enter;
    TextView tvSignUp ,tvForgotPwd;
    FirebaseUser mFirebaseUser;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseFirestore db;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnSignIn = findViewById(R.id.btnSignIn);
        tvSignUp = findViewById(R.id.tvSignUp);
        tvForgotPwd = findViewById(R.id.tvForgotPwd);

        enter = findViewById(R.id.btnenter);
        db = FirebaseFirestore.getInstance();

        final Map<String, Object> newUser = new HashMap<>();
        newUser.put("first", "Ada");
        newUser.put("last", "Lovelace");
        newUser.put("born", 1815);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if (mFirebaseUser != null) {
                    Toast.makeText(LogInActivity.this, "You are logged in", Toast.LENGTH_SHORT).show();
                    finish();
                    Intent intToHome = new Intent(LogInActivity.this, HomeActivity.class);
                    startActivity(intToHome);
                } else {
                    Toast.makeText(LogInActivity.this, "Please Login", Toast.LENGTH_SHORT).show();
                }
            }
        };

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();
                if (email.isEmpty()) {
                    emailId.setError("Please enter your Email");
                    emailId.requestFocus();
                } else if (pwd.isEmpty()) {
                    password.setError("Please enter your Password");
                    password.requestFocus();
                } else if (email.isEmpty() && pwd.isEmpty()) {
                    Toast.makeText(LogInActivity.this, "Fields Are Empty!", Toast.LENGTH_SHORT).show();
                } else if (!(email.isEmpty() && pwd.isEmpty())) {
                    mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LogInActivity.this, "Login Error, Please Login Again", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LogInActivity.this, "enter", Toast.LENGTH_SHORT).show();
                                finish();
                                Intent intToHome = new Intent(LogInActivity.this, HomeActivity.class);
                                startActivity(intToHome);
                            }
                        }
                    });
                } else {
                    Toast.makeText(LogInActivity.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intToSignIn = new Intent(LogInActivity.this, SignUpActivity.class);
                startActivity(intToSignIn);
            }
        });

        tvForgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intToResetPwd = new Intent(LogInActivity.this, ResetPasswordActivity.class);
                startActivity(intToResetPwd);
            }
        });

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LogInActivity.this, "enter in", Toast.LENGTH_LONG).show();
                db.collection("users")
                        .add(newUser)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(LogInActivity.this, "add ", Toast.LENGTH_LONG).show();
                                Log.d("", "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LogInActivity.this, "error", Toast.LENGTH_LONG).show();
                                Log.w("", "Error adding document", e);
                            }
                        });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    public void ShowHidePass(View view) {
        if(view.getId() == R.id.show_pass_btn) {
            if (password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                //Show Password
                ((ImageView)(view)).setImageResource(R.drawable.hide_password);
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else {
                //Hide Password
                ((ImageView)(view)).setImageResource(R.drawable.show_password);
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }
}


