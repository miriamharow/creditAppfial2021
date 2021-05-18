package com.miriam_shmuel.creditapp;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

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
    int previousPosition, newPosition;

    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //--------------------SCREEN-------------------------
        getSupportActionBar().setTitle("Credit App");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00264d")));
        previousPosition = 4;
        newPosition = 0;
        openTab(0);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("message_subject_intent"));

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
                previousPosition = newPosition;
                Intent intent = new Intent(HomeActivity.this, AddActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        //---------------------------------------------------


        //---------------------------------------------------

        simpleFrameLayout = (FrameLayout) findViewById(R.id.simpleFrameLayout);
        tabLayout = (TabLayout) findViewById(R.id.tab);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            // get the current selected tab's position and replace the fragment accordingly
                fragment = null;
                newPosition = tab.getPosition();
                openTab(newPosition);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    public void openTab(int tab) {
        switch (tab) {
            case 0:
                fragment = new GiftFragment();
                break;
            case 1:
                fragment = new CreditFragment();
                break;
            case 2:
                fragment = new WarrantyFragment();
                break;
            case 3:
                fragment = new MapViewFragment();
                break;
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.simpleFrameLayout, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra("type");
            switch (type) {
                case "gift":
                    tabLayout.getTabAt(0).select();
                    openTab(newPosition);
                    break;
                case "credit":
                    tabLayout.getTabAt(1).select();
                    openTab(newPosition);
                    break;
                case "warranty":
                    tabLayout.getTabAt(2).select();
                    openTab(newPosition);
                    break;
            }
        }
    };


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 1
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == 1) {
                final String message = data.getStringExtra("message");
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        switch (message) {
                            case "add gift":
                                tabLayout.getTabAt(0).select();
                                if (newPosition == previousPosition)
                                    openTab(newPosition);
                                previousPosition = newPosition;
                                break;
                            case "add credit":
                                tabLayout.getTabAt(1).select();
                                if (newPosition == previousPosition)
                                    openTab(newPosition);
                                previousPosition = newPosition;
                                break;
                            case "add warranty":
                                tabLayout.getTabAt(2).select();
                                if (newPosition == previousPosition)
                                    openTab(newPosition);
                                previousPosition = newPosition;
                                break;
                        }
                        //get the result
                    }
                }, 2000);
            }
        }
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




