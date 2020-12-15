package com.miriam_shmuel.creditapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

    public class HomeActivity extends AppCompatActivity {
        private FloatingActionButton goAddPage;
        Button btnLogout;

        FirebaseAuth mFirebaseAuth;
        private FirebaseAuth.AuthStateListener mAuthStateListener;

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);
           //--------------------SCREEN-------------------------
            getSupportActionBar().setTitle("My Credit");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00264d")));

            //---------------------------------------------------


            //--------------------GO ADD PAGE--------------------
            goAddPage = findViewById(R.id.goAddPageID);
            goAddPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //threadOff = false;
                    Intent intent = new Intent(HomeActivity.this, AddCreditOrGiftActivity.class);
                    startActivity(intent);
                }
            });
            //---------------------------------------------------

            //--------------------LOG OUT------------------------
            btnLogout = findViewById(R.id.logout);
            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(HomeActivity.this,"log out!",Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    Intent intToLogIn = new Intent(HomeActivity.this, LogInActivity.class);
                    startActivity(intToLogIn);
                }
            });
            //---------------------------------------------------
        }
    }



