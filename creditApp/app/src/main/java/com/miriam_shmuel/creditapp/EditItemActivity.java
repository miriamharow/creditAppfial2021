package com.miriam_shmuel.creditapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditItemActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Gift_Credit test = (Gift_Credit) getIntent().getSerializableExtra("obj");
        String key = test.getKey();
        Toast.makeText(this, key, Toast.LENGTH_SHORT).show();
    }

}
