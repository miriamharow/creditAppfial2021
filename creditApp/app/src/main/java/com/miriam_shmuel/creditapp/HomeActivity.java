package com.miriam_shmuel.creditapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
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
                    Intent intent = new Intent(HomeActivity.this, AddActivity.class);
                    startActivity(intent);
                }
            });
            //---------------------------------------------------
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            super.onCreateOptionsMenu(menu);
            MenuItem menuAbout = menu.add("About");
            MenuItem menuExit = menu.add("Exit");

            menuAbout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    showAboutDialog();
                    return true;
                }
            });

            menuExit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    showExitDialog();
                    return true;
                }
            });
            return true;
        }

        private void showAboutDialog() {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("About CreditApp");
            alertDialog.setMessage("This app implements the CreditApp.\n\nBy Miriam Harow & Samuel Nakash (c)");
            alertDialog.show();
        }

        private void showExitDialog() {
            Toast.makeText(HomeActivity.this, "log out!", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            finish();
            Intent intToLogIn = new Intent(HomeActivity.this, LogInActivity.class);
            startActivity(intToLogIn);
        }
    }




