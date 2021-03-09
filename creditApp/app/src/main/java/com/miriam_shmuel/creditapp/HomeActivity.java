package com.miriam_shmuel.creditapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

    public class HomeActivity extends AppCompatActivity {
        public static Context instance;
        private FloatingActionButton goAddPage;
        FrameLayout simpleFrameLayout;
        TabLayout tabLayout;
        EditText edtSearch;
        Fragment fragment;

        FirebaseAuth mFirebaseAuth;
        private FirebaseAuth.AuthStateListener mAuthStateListener;

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);
            //--------------------SCREEN-------------------------
            getSupportActionBar().setTitle("Credit App");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00264d")));


            fragment = new GiftFragment();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.simpleFrameLayout, fragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
            //---------------------------------------------------

            /*edtSearch = findViewById(R.id.edtSearchID);
            edtSearch.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                    // When user changed the Text
                    //HomeActivity.this.adapter.getFilter().filter(cs);
                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                              int arg3) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void afterTextChanged(Editable arg0) {
                    // TODO Auto-generated method stub
                }
            });*/

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

            simpleFrameLayout = (FrameLayout) findViewById(R.id.simpleFrameLayout);
            tabLayout = (TabLayout) findViewById(R.id.tab);

            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
// get the current selected tab's position and replace the fragment accordingly
                    fragment = null;
                    switch (tab.getPosition()) {
                        case 0:
                            fragment = new GiftFragment();
                            break;
                        case 1:
                            fragment = new CreditFragment();
                            break;
                        case 2:
                            fragment = new WarrantyFragment();
                            break;
                    }
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.simpleFrameLayout, fragment);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.commit();
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
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




