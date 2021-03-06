package com.miriam_shmuel.creditapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends Activity {
    EditText UserEmail;
    Button btnSenedPwd;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpwd);

        UserEmail = findViewById(R.id.email);
        btnSenedPwd = findViewById(R.id.btnSenedNewPwd);
        progressBar = findViewById(R.id.progressBar);

        firebaseAuth = firebaseAuth.getInstance();

        btnSenedPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = UserEmail.getText().toString();
                if (!checkEmail(email))
                    Toast.makeText(ResetPasswordActivity.this, "Require format exemple@exemple.com", Toast.LENGTH_SHORT).show();
                else{
                    progressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful())
                                        Toast.makeText(ResetPasswordActivity.this, "Reset password invitation sent to your email", Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(ResetPasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }

            }
        });
    }

    private boolean checkEmail(String email) {
        if (!email.contains("@") || !email.contains("."))
            return false;
        return true;
    }
}


