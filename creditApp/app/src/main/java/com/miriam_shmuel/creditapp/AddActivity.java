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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class AddActivity extends AppCompatActivity  implements View.OnClickListener {
    private RadioButton rCredit, rGift;
    private EditText edtShopNameGC, edtCreditBarCodeIDGC, edtvalueIDGC, edtItemW, edtShopNameIDW, edtCreditBarCodeIDW;
    private TextView editDateTextGC, editDateTextIDW;
    private ImageView imgPicGC, picItemIDW, picReceiptIDW;
    private Bitmap picBitmap, picBitmap1, picBitmap2;
    private FloatingActionButton fab, fabItemW, fabReceiptW;
    private Button btnSaveGC, btnPlusShopName, btnSaveW;
    private LinearLayout shops, gift_credit_view, warranty_view;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    public boolean isPICAP = false;
    private Intent takePictureIntent;

    private Calendar calender;
    private int day, month, year;
    private int dayED, monthED, yearED;

    private String dateExp = "";
    private String type = "";

    public Uri ImageUri;

    public boolean savedName = false;

    public static AddActivity instance;
    public ArrayList<Shop> shopsList;

    private StorageReference storageRef;
    ArrayList<String> loo = new ArrayList<String>();
    ListView listView;
    AdapterShop adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        rCredit = findViewById(R.id.radioCreditID);
        rGift = findViewById(R.id.radioGiftID);
        edtShopNameGC = findViewById(R.id.edtShopNameIDGC);
        edtCreditBarCodeIDGC = findViewById(R.id.edtCreditBarCodeIDGC);
        edtvalueIDGC = findViewById(R.id.valueIDGC);
        editDateTextGC = findViewById(R.id.editDateTextIDGC);
        edtItemW = findViewById(R.id.itemW);
        edtShopNameIDW = findViewById(R.id.edtShopNameIDW);
        edtCreditBarCodeIDW = findViewById(R.id.edtCreditBarCodeIDW);
        editDateTextIDW = findViewById(R.id.editDateTextIDW);
        imgPicGC = findViewById(R.id.imgPicIDGC);
        picItemIDW = findViewById(R.id.picItemIDW);
        picReceiptIDW = findViewById(R.id.picReceiptIDW);
        shops = findViewById(R.id.shops);
        gift_credit_view = findViewById(R.id.gift_credit_layout);
        warranty_view = findViewById(R.id.warranty_layout);

        btnSaveGC = findViewById(R.id.btnSaveIDGC);
        btnSaveGC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveGC();
            }
        });

        btnSaveW = findViewById(R.id.btnSaveIDW);
        btnSaveW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveW();
            }
        });

        btnPlusShopName = findViewById(R.id.btnPlusShopNameID);
        btnPlusShopName.setOnClickListener(this);

        fab = findViewById(R.id.fabID);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });

        fabItemW = findViewById(R.id.fabItemW);
        fabItemW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });

        fabReceiptW = findViewById(R.id.fabReceiptW);
        fabReceiptW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });

        shopsList = new ArrayList<Shop>();
        instance = this;

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
            public void onClick(View v) {makeDate();}});
        editDateTextIDW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {makeDate();}});
        //---------------------------------------------------
    }

    public void makeDate(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(AddActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateExp = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                editDateTextGC.setText(dateExp);

                dayED = day;
                monthED = month + 1;
                yearED = year;
            }
        }, year, month, day);
        // disable dates before today
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radioCreditID:
                if (checked) {
                    gift_credit_view.setVisibility(View.VISIBLE);
                    warranty_view.setVisibility(View.GONE);
                    btnPlusShopName.setVisibility(View.GONE);
                    type = "Credit";
                }
                break;
            case R.id.radioGiftID:
                if (checked) {
                    gift_credit_view.setVisibility(View.VISIBLE);
                    warranty_view.setVisibility(View.GONE);
                    btnPlusShopName.setVisibility(View.VISIBLE);
                    type = "Gift";
                }
                break;
            case R.id.radioWarrantyID:
                if (checked) {
                    gift_credit_view.setVisibility(View.GONE);
                    warranty_view.setVisibility(View.VISIBLE);
                    type = "Warranty";
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
            ActivityCompat.requestPermissions(AddActivity.this, new String[]{Manifest.permission.CAMERA}, 111);
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if(type == "Credit" || type == "Gift1") {
                picBitmap = (Bitmap) data.getExtras().get("data"); // get picture as thumbnail
                ImageUri = data.getData();
                imgPicGC.setImageBitmap(picBitmap);
                isPICAP = true;
            }
            else
            {
                if (picBitmap1 == null){
                    picBitmap1 = (Bitmap) data.getExtras().get("data"); // get picture as thumbnail
                    ImageUri = data.getData();
                    picItemIDW.setImageBitmap(picBitmap1);
                    isPICAP = true;
                }
                else{
                    picBitmap2 = (Bitmap) data.getExtras().get("data"); // get picture as thumbnail
                    ImageUri = data.getData();
                    picReceiptIDW.setImageBitmap(picBitmap1);
                    isPICAP = true;
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPlusShopNameID:
                String shopname = edtShopNameGC.getText().toString();
                Shop shop = new Shop(shopname);
                shopsList.add(shop);
                loo.add(shopname);
                UpdateShopName(edtShopNameGC.getText().toString());
                edtShopNameGC.setText(null);
                break;
        }
    }

    private void saveGC() {
        if(type == "Credit")
        {
            List_of_Credits list_of_credits = new List_of_Credits();
            ArrayList<Shop> ls = new ArrayList<Shop>();
            Shop s = new Shop(edtShopNameGC.getText().toString());
            ls.add(s);
            String key = list_of_credits.addCredit(null, edtCreditBarCodeIDGC.getText().toString(), dateExp, ls, edtvalueIDGC.getText().toString());
            savePic(key, picBitmap);

        }
        if (type == "Gift") {
            List_of_Gifts list_of_gifts = new List_of_Gifts();
            ArrayList<Shop> ls = new ArrayList<Shop>();
            ls = shopsList;
            String key = list_of_gifts.addGift(null, edtCreditBarCodeIDGC.getText().toString(), dateExp, ls, edtvalueIDGC.getText().toString());
            savePic(key, picBitmap);
        }
    }

    private void saveW(){
        List_of_Warranty list_of_warranty = new List_of_Warranty();
        ArrayList<Shop> ls = new ArrayList<Shop>();
        Shop s = new Shop(edtShopNameGC.getText().toString());
        ls.add(s);
        String key = list_of_warranty.addWarranty(null, null, edtCreditBarCodeIDGC.getText().toString(), dateExp, ls, edtItemW.getText().toString());
        savePic(key + "itemReceipt", picBitmap1);
        savePic(key + "shopReceipt", picBitmap2);
    }



    private void printShopList(){
        String str = "";
        for (int i = 0; i<shopsList.size(); i++){
            if (i != shopsList.size()-1)
                str += shopsList.get(i)+"; ";
            else
                str += shopsList.get(i);
        }
        edtShopNameGC.setText(str);

    }

    private void savePic(String key, Bitmap bitmap) {
        // Create a storage reference from our app
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        // Create a reference to "mountains.jpg"
        StorageReference mountainsRef = storageRef.child(key + ".jpg");
        // Create a reference to 'images/mountains.jpg'
        StorageReference mountainImagesRef = storageRef.child("images/" + key + ".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            }
        });
    }

    public void UpdateShopName(String shopNameFromEdt) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddActivity.this);
        final View dialogViewList = getLayoutInflater().inflate(R.layout.dialog_list_shop_name, null);
        final EditText diaShopName = (EditText) dialogViewList.findViewById(R.id.edtDiaShopNameId);
        final Button diaBtnAddShop = (Button) dialogViewList.findViewById(R.id.btnDiaPlusShopNameID);
        final Button btnSaveShops = (Button) dialogViewList.findViewById(R.id.saveShops);
        listView = dialogViewList.findViewById(R.id.listViewDiaID);
        mBuilder.setView(dialogViewList);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        final View dialogViewItem = getLayoutInflater().inflate(R.layout.item_shop_name, null);
        final Button diaBtnRemoveShop = (Button) dialogViewItem.findViewById(R.id.btnRemoveId);

        adapter = new AdapterShop(this, dialog, loo);
        listView.setAdapter(adapter);

        if ((!shopNameFromEdt.isEmpty()) && (!shopNameExist(shopNameFromEdt))) {
            loo.add(shopNameFromEdt);
            adapter.notifyDataSetChanged();
        }
        else {
            //Toast.makeText(AddActivity.this, "SHOP NAME EXISTS!", Toast.LENGTH_SHORT).show();
        }

        diaBtnAddShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!diaShopName.getText().toString().isEmpty()) {
                    String shopname = diaShopName.getText().toString();
                    if (!shopNameExist(shopname)) {
                        Shop shop = new Shop(shopname);
                        shopsList.add(shop);
                        loo.add(shopname);
                        adapter.notifyDataSetChanged();
                    }
                    else
                        Toast.makeText(AddActivity.this, "SHOP NAME EXISTS!", Toast.LENGTH_SHORT).show();
                    diaShopName.setText(null);
                }
            }
        });
        adapter.notifyDataSetChanged();
        btnSaveShops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shopname = diaShopName.getText().toString();
                if (!shopNameExist(shopname) && !shopname.contains(";")) {
                    Shop shop = new Shop(shopname);
                    shopsList.add(shop);
                    loo.add(shopname);
                    savedName = true;
                }
                dialog.dismiss();
                printShopList();

            }
        });

    }


    public boolean shopNameExist(String shopname) {
        if (!loo.isEmpty()) {
            for (int i = 0; i < loo.size(); i++) {
                if (loo.get(i).equals(shopname))
                    return true;
            }
        }
        return false;
    }
}