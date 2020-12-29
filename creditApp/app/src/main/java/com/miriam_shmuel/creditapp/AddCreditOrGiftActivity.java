package com.miriam_shmuel.creditapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class AddCreditOrGiftActivity extends AppCompatActivity  implements View.OnClickListener {
    private RadioButton rCredit, rGift;
    private EditText edtShopNameGC, edtCreditBarCodeIDGC;
    private TextView editDateTextGC;
    private ImageView imgPicGC;
    private Bitmap picBitmap;
    private FloatingActionButton fab;
    private Button btnSaveGC, btnPlusShopName, btnSaveW;
    private LinearLayout shops, gift_credit_view, warranty_view;


    private Calendar calender;
    private int day, month, year;
    private int dayED, monthED, yearED;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    public boolean isPICAP = false;
    private Intent takePictureIntent;

    public ArrayList<String> listShopDia;
    public ArrayAdapter<String> adapterDia;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_credit_gift);
        rCredit = findViewById(R.id.radioCreditID);
        rGift = findViewById(R.id.radioGiftID);
        edtShopNameGC = findViewById(R.id.edtShopNameIDGC);
        edtCreditBarCodeIDGC = findViewById(R.id.edtCreditBarCodeIDGC);
        editDateTextGC = findViewById(R.id.editDateTextIDGC);
        imgPicGC = findViewById(R.id.imgPicIDGC);

        btnSaveGC = findViewById(R.id.btnSaveIDGC);
        btnSaveW = findViewById(R.id.btnSaveIDW);
        //btnSave.setOnClickListener(this);

        btnPlusShopName = findViewById(R.id.btnPlusShopNameID);
        btnPlusShopName.setOnClickListener(this);

        fab = findViewById(R.id.fabID);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });

        shops = findViewById(R.id.shops);
        gift_credit_view = findViewById(R.id.gift_credit_layout);
        warranty_view = findViewById(R.id.warranty_layout);

        //--------------------FULL SCREEN--------------------
        // Hide the Activity Status Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Hide the Activity Action Bar
        getSupportActionBar().hide();
        //---------------------------------------------------


        //-----------------CURRENT DATE---------------------
        calender = Calendar.getInstance();
        day = calender.get(Calendar.DAY_OF_MONTH);
        month = calender.get(Calendar.MONTH);
        year = calender.get(Calendar.YEAR);
        //---------------------------------------------------

        //-----------------DATE PICKER DIALOG----------------
        editDateTextGC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddCreditOrGiftActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        editDateTextGC.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                        dayED = day;
                        monthED = month + 1;
                        yearED = year;
                    }
                }, year, month, day);
                // disable dates before today
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
        //---------------------------------------------------


        //------------------LIST SHOP DIALOG-----------------
        //listShopDia = new ArrayList<String>();
        //---------------------------------------------------
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioCreditID:
                if (checked) {
                    gift_credit_view.setVisibility(View.VISIBLE);
                    warranty_view.setVisibility(View.GONE);
                    btnPlusShopName.setVisibility(View.GONE);
                }
                break;
            case R.id.radioGiftID:
                if (checked) {
                    gift_credit_view.setVisibility(View.VISIBLE);
                    warranty_view.setVisibility(View.GONE);
                    btnPlusShopName.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.radioWarrantyID:
                if (checked) {
                    gift_credit_view.setVisibility(View.GONE);
                    warranty_view.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    // get a picture from camera
    public void takePicture() {
        // check CAMERA permission
        if (isCameraPermissionGranted()) {
            // create an intent to call the camera app to take a picture(image)
            takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            // check if camera app exists ?
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    // Check if CAMERA Permission granted ?
    public boolean isCameraPermissionGranted() {
        // check if permission for READ_CONTACTS is granted ?
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            // show requestPermissions dialog
            ActivityCompat.requestPermissions(AddCreditOrGiftActivity.this, new String[]{Manifest.permission.CAMERA}, 111);
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            picBitmap = (Bitmap) data.getExtras().get("data"); // get picture as thumbnail
            Uri ImageUri = data.getData();
            imgPicGC.setImageBitmap(picBitmap);
            isPICAP = true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPlusShopNameID:
                addShopName(edtShopNameGC.getText().toString());
                edtShopNameGC.setText(null);
        }
    }

    private void addShopName(final String shopNameFromEdt) {
        ListView listView;
        final DialogListShopName adapter;
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddCreditOrGiftActivity.this);
        final View dialogViewList = getLayoutInflater().inflate(R.layout.dialog_list_shop_name, null);
        mBuilder.setView(dialogViewList);
        final AlertDialog dialog = mBuilder.create();
        final EditText diaShopName = (EditText) dialogViewList.findViewById(R.id.edtDiaShopNameId);
        final Button diaBtnAddShop = (Button) dialogViewList.findViewById(R.id.btnDiaPlusShopNameID);
        final ArrayList <String> trylist = new ArrayList<String>();
        dialog.show();
        listView = dialogViewList.findViewById(R.id.listViewDiaID);
        adapter = new DialogListShopName(this, trylist);
        listView.setAdapter(adapter);
        final Button diaSaveShops = (Button) dialogViewList.findViewById(R.id.btnSaveShopsID);

        final View dialogViewItem = getLayoutInflater().inflate(R.layout.item_shop_name, null);
        final Button diaBtnRemoveShop = (Button) dialogViewItem.findViewById(R.id.btnRemoveId);

        if(!shopNameFromEdt.isEmpty()) {
            trylist.add(shopNameFromEdt);
            adapter.notifyDataSetChanged();
        }

        diaSaveShops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trylist.add(diaShopName.getText().toString());
                listShopDia = trylist;
                //dialog.cancel();
                Toast.makeText(AddCreditOrGiftActivity.this, listShopDia.toString(), Toast.LENGTH_LONG).show();
                String str = "";


            }
        });

        diaBtnAddShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!diaShopName.getText().toString().isEmpty())
                {
                    String shopname = diaShopName.getText().toString();
                    int isExist = 0;
                    if(!trylist.isEmpty()) {
                        for(int i=0; i< trylist.size(); i++)
                            if(trylist.get(i).equals(shopname)) {
                                isExist = 1;
                            }
                    }

                    if(isExist == 0) {
                        trylist.add(shopname);
                        adapter.notifyDataSetChanged();
                        diaShopName.setText(null);
                    }

                    if(isExist == 1) {
                        diaShopName.setText(null);
                        Toast.makeText(AddCreditOrGiftActivity.this, "SHOP NAME EXISTS!", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String str = trylist.get(position);
                trylist.remove(str);
                adapter.notifyDataSetChanged();
            }
        });
    }

}