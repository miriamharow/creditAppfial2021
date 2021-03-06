package com.miriam_shmuel.creditapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
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
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity  {
    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore db;

    EditText userName, emailId , password , pwdauth;
    TextView pwdNote;
    Button btnSignUp;

    private boolean threadOff = false;
    private int i = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        db=FirebaseFirestore.getInstance();

        mFirebaseAuth = FirebaseAuth.getInstance();

        userName = findViewById(R.id.username);
        emailId = findViewById(R.id.email);
        password  = findViewById(R.id.password);
        pwdauth = findViewById(R.id.pwdAuth);
        pwdNote  = findViewById(R.id.textView_PwdNote);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = userName.getText().toString();
                final String email = emailId.getText().toString();
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
                    pwdauth.setError("Please enter again password");
                    pwdauth.requestFocus();
                }
                else  if(name.isEmpty() && email.isEmpty() && pwd.isEmpty() && pwdAuth.isEmpty()){
                    Toast.makeText(SignUpActivity.this,"Fields Are Empty!", Toast.LENGTH_SHORT).show();
                }
                else if(!checkEmail(email)){
                    emailId.setError("Require format exemple@exemple.com");
                    emailId.requestFocus();
                }
                else if(pwd.length()<6){
                    password.setError("Require at least 6 characters");
                    password.requestFocus();
                }
                else if((!(name.isEmpty() && email.isEmpty() && pwd.isEmpty() && pwdAuth.isEmpty())) && (pwd.equals(pwdAuth))){

                    mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            final String semail = email;

                            if(!task.isSuccessful()){
                                Toast.makeText(SignUpActivity.this,"SignUp Unsuccessful, Please Try Again",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                User user = new  User (name , email);
                                addNewUserToDb(user);
                                threadOff = true;
                                Toast.makeText(SignUpActivity.this,"Success !",Toast.LENGTH_SHORT).show();
                                finish();
                                Intent intToHome = new Intent(SignUpActivity.this, HomeActivity.class);
                                intToHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                        Intent.FLAG_ACTIVITY_NEW_TASK);
                               //intToHome.putExtra("currentUser", (Parcelable) user);
                                startActivity(intToHome);
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(SignUpActivity.this,"Error Occurred!",Toast.LENGTH_SHORT).show();
                }
            }

            private boolean checkEmail(String email) {
                if (!email.contains("@") || !email.contains("."))
                    return false;
                return true;
            }
        });

        threadAuthPwd();
    }

    public void addNewUserToDb(User user) {
        final Map<String, Object> newUser = new HashMap<>();
        newUser.put("user",user);
        db.collection("user").document(user.getEmail())
                .set(newUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(SignUpActivity.this, "create new user", Toast.LENGTH_LONG).show();
                        //Log.d("", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpActivity.this, "error", Toast.LENGTH_LONG).show();
                        Log.w("", "Error adding document", e);
                    }
                });
    }

    public void threadAuthPwd() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (i == 1) {
                    if (!threadOff) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                validatePwd();
                                SystemClock.sleep(10);    // sleep for 1000ms = 1sec
                            }
                        });
                        SystemClock.sleep(1000);    // sleep for 1000ms = 1sec
                    }
                }
            }
        }).start();
    }

    public void validatePwd() {
        if(!(password.getText().toString().isEmpty() && pwdauth.getText().toString().isEmpty())) {
            if(password.getText().toString().equals(pwdauth.getText().toString())) {
                pwdNote.setText("");
            }
            else if (!(password.getText().toString().equals(pwdauth.getText().toString()))){
                pwdNote.setText("not match!");
            }
        }
    }

    public void ShowHidePass(View view) {
        if (view.getId() == R.id.show_password_btn) {
            if (password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                //Show Password
                ((ImageView) (view)).setImageResource(R.drawable.hide_password);
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else {
                //Hide Password
                ((ImageView) (view)).setImageResource(R.drawable.show_password);
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
        if (view.getId() == R.id.show_pwdAuth_btn) {
            if (pwdauth.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                //Show Password
                ((ImageView) (view)).setImageResource(R.drawable.hide_password);
                pwdauth.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else {
                //Hide Password
                ((ImageView) (view)).setImageResource(R.drawable.show_password);
                pwdauth.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

}