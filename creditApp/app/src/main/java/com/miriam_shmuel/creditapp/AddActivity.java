package com.miriam_shmuel.creditapp;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.content.ContentValues.TAG;

public class AddActivity extends AppCompatActivity implements View.OnClickListener {
    private RadioButton rCredit, rGift;
    private EditText edtShopNameGC, edtCreditBarCodeIDGC, edtvalueIDGC, edtItemW, edtShopNameIDW, edtCreditBarCodeIDW, edtgiftNameIDG;
    private TextView editDateTextGC, editDateTextIDW;
    private ImageView imgPicGC, picItemIDW, picReceiptIDW;
    private Bitmap picBitmap, picBitmap1, picBitmap2;
    private FloatingActionButton fab, fabItemW, fabReceiptW;
    private Button btnSaveGC, btnPlusShopName, btnSaveW;
    private LinearLayout shops, gift_credit_view, warranty_view;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    public boolean isPICAP = false ,isPICAP1 = false, isPICAP2 = false;
    private Intent takePictureIntent;

    private Calendar calender;
    private int day, month, year;
    private int dayED, monthED, yearED;

    private String dateExp = "";
    private String type = "";

    public static final int ONETIME_ALARM_CODE = 0;
    private AlarmManager alarmManager;

    public Uri ImageUri;

    public boolean savedName = false;

    public static AddActivity instance;
    public ArrayList<Shop> shopsList;

    private StorageReference storageRef;
    ArrayList<String> diaListShopNsme = new ArrayList<String>();
    ListView listView;
    AdapterShop adapter;

    boolean printShopListFlag = false;

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
        edtgiftNameIDG = findViewById(R.id.edtGiftNameIDG);
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

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

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
                if (type == "Warranty")
                    editDateTextIDW.setText(dateExp);
                else
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
                    edtgiftNameIDG.setVisibility(View.GONE);
                    edtShopNameGC.setVisibility(View.VISIBLE);
                    type = "Credit";
                }
                break;
            case R.id.radioGiftID:
                if (checked) {
                    gift_credit_view.setVisibility(View.VISIBLE);
                    warranty_view.setVisibility(View.GONE);
                    btnPlusShopName.setVisibility(View.VISIBLE);
                    edtgiftNameIDG.setVisibility(View.VISIBLE);
                    edtShopNameGC.setVisibility(View.GONE);
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
            if(type == "Credit" || type == "Gift") {
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
                    isPICAP1 = true;
                }
                else{
                    picBitmap2 = (Bitmap) data.getExtras().get("data"); // get picture as thumbnail
                    ImageUri = data.getData();
                    picReceiptIDW.setImageBitmap(picBitmap2);
                    isPICAP2 = true;
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPlusShopNameID:
                //String edtsname = edtShopNameGC.getText().toString();
                UpdateShopName();
                //edtShopNameGC.setText(null);
                break;
        }
    }

    private void saveGC() {
        if(type == "Credit") {
            if(fullFieldsCG()){
                List_of_Credits list_of_credits = new List_of_Credits();
                ArrayList<Shop> shopName = new ArrayList<Shop>();
                Shop s = new Shop(edtShopNameGC.getText().toString());
                shopName.add(s);
                list_of_credits.iSExist(edtCreditBarCodeIDGC.getText().toString(), dateExp, shopName, edtvalueIDGC.getText().toString(), picBitmap,"add", "");
                updateHome("add credit");
                finish();
            }
        }
        if (type == "Gift") {
            if(fullFieldsCG()) {
                List_of_Gifts list_of_gifts = new List_of_Gifts();
                list_of_gifts.iSExist(edtCreditBarCodeIDGC.getText().toString(), dateExp, shopsList, edtvalueIDGC.getText().toString(), edtgiftNameIDG.getText().toString() ,picBitmap, "add", "");
                updateHome("add gift");
                finish();
            }
        }
    }

    private void updateHome(String message){
        Intent intent = new Intent();
        intent.putExtra("message", message);
        setResult(RESULT_OK, intent);
    }

    private boolean fullFieldsCG() {
        String name = edtShopNameGC.getText().toString();
        String giftName = edtgiftNameIDG.getText().toString();
        String value = edtvalueIDGC.getText().toString();
        String barCode = edtCreditBarCodeIDGC.getText().toString();
        String expDate = editDateTextGC.getText().toString();

        if(giftName.isEmpty() && (type == "Gift")){
            edtgiftNameIDG.setError("Please enter shop name");
            edtgiftNameIDG.requestFocus();
            return false;
        }
        else if(shopsList.isEmpty() && (type == "Gift"))
        {
            Toast.makeText((instance), "plese edit shoop name and press +", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(name.isEmpty() && (type == "Credit")){
            edtShopNameGC.setError("Please enter shop name");
            edtShopNameGC.requestFocus();
            return false;
        }
        else if(value.isEmpty()){
            edtvalueIDGC.setError("Please enter value credit");
            edtvalueIDGC.requestFocus();
            return false;
        }
        else if(barCode.isEmpty()){
            edtCreditBarCodeIDGC.setError("Please enter bar code credit");
            edtCreditBarCodeIDGC.requestFocus();
            return false;
        }
        else if(expDate.isEmpty()){
            Toast.makeText((instance), "please edit expiration date of the receipt credit", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(isPICAP != true){
            Toast.makeText((instance), "please take a picture of the receipt credit", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!((name.isEmpty()) && (value.isEmpty()) && (barCode.isEmpty()) && (expDate.isEmpty()) &&
                 (isPICAP != true))){
            return true;
        }
        return true;
    }

    private void saveW(){
        if(fullFieldsW()){
            List_of_Warranty list_of_warranty = new List_of_Warranty();
            list_of_warranty.iSExist(edtCreditBarCodeIDW.getText().toString(), editDateTextIDW.getText().toString(), edtShopNameIDW.getText().toString(), edtItemW.getText().toString(), picBitmap1, picBitmap2 , "add", "");
            updateHome("add warranty");
            finish();
        }
    }

    private boolean fullFieldsW(){
        String itemName = edtItemW.getText().toString();
        String shopName = edtShopNameIDW.getText().toString();
        String barCode = edtCreditBarCodeIDW.getText().toString();
        String expDate = editDateTextIDW.getText().toString();
        if(itemName.isEmpty()){
            edtItemW.setError("Please enter item name warranty");
            edtItemW.requestFocus();
            return false;
        }
        else if(shopName.isEmpty()){
            edtShopNameIDW.setError("Please enter shop name warranty");
            edtShopNameIDW.requestFocus();
            return false;
        }
        else if(barCode.isEmpty()){
            edtCreditBarCodeIDW.setError("Please enter barcode warranty");
            edtCreditBarCodeIDW.requestFocus();
            return false;
        }
        else if(expDate.isEmpty()){
            Toast.makeText((instance), "please enter expiration date of warranty", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!isPICAP1){
            Toast.makeText((instance), "please take a picture of warranty", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!isPICAP2){
            Toast.makeText((instance), "please take a picture of shop receipt", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void UpdateShopName() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddActivity.this);
        final View dialogViewList = getLayoutInflater().inflate(R.layout.dialog_list_shop_name, null);
        final EditText diaShopName = (EditText) dialogViewList.findViewById(R.id.edtDiaShopNameId);
        final Button diaBtnAddShop = (Button) dialogViewList.findViewById(R.id.btnDiaPlusShopNameID);
        listView = dialogViewList.findViewById(R.id.listViewDiaID);
        mBuilder.setView(dialogViewList);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        final View dialogViewItem = getLayoutInflater().inflate(R.layout.item_shop_name, null);
        final Button diaBtnRemoveShop = (Button) dialogViewItem.findViewById(R.id.btnRemoveId);

        adapter = new AdapterShop(this, dialog, diaListShopNsme);
        listView.setAdapter(adapter);

        if(diaListShopNsme.isEmpty()) {
            printShopListFlag = false;
        }

        /*if ((!shopNameFromEdt.isEmpty()) && (!shopNameExist(shopNameFromEdt)) && (!printShopListFlag)) {
            String shopname = edtShopNameGC.getText().toString();
            Shop shop = new Shop(shopname);
            shopsList.add(shop);
            Toast.makeText(AddActivity.this, "SHOP NAME "+shopname, Toast.LENGTH_SHORT).show();
            diaListShopNsme.add(shopNameFromEdt);
            adapter.notifyDataSetChanged();
            if ((!shopNameExist(shopNameFromEdt))) {
                Toast.makeText(AddActivity.this, "SHOP NAME EXISTS!", Toast.LENGTH_SHORT).show();
            }
        }*/

        diaBtnAddShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((!(diaShopName.getText().toString()).isEmpty())) {
                    String shopname = diaShopName.getText().toString();
                    if (!shopNameExist(shopname)) {
                        Shop shop = new Shop(shopname);
                        shopsList.add(shop);
                        diaListShopNsme.add(shopname);
                        adapter.notifyDataSetChanged();
                        printShopList();

                    }
                    else
                        Toast.makeText(AddActivity.this, "SHOP NAME EXISTS!", Toast.LENGTH_SHORT).show();
                    diaShopName.setText(null);
                }
            }
        });



    }

    public boolean shopNameExist(String shopname) {
        if (!diaListShopNsme.isEmpty()) {
            for (int i = 0; i < diaListShopNsme.size(); i++) {
                if (diaListShopNsme.get(i).equals(shopname))
                    return true;
            }
        }
        return false;
    }

    public void printShopList() {
        printShopListFlag = true;
        String str = "";
        if (shopsList.size() == 0)
            str = "SHOPNAME";
        else{
            for (int i = 0; i<shopsList.size(); i++) {
                if (i != shopsList.size() - 1)
                    str += shopsList.get(i) + ",  ";
                else
                    str += shopsList.get(i);
            }
        }
        btnPlusShopName.setText(str);
    }

    public void oneTimeAlarm(Calendar alarmTime, String key, String type) {
        Intent intent = new Intent(this, AlarmReceiver.class);
        String str = "";
        if (type.equals("credit"))
            str += "Your "+type+" from "+edtShopNameGC.getText().toString()+" is about to expire !";
        else if (type.equals("gift"))
            str += "Your "+type+" from "+edtgiftNameIDG.getText().toString()+" is about to expire !";
        intent.putExtra("title", "Credit APP!");
        intent.putExtra("msg", str);
        intent.putExtra("key", key);
        intent.putExtra("type", type);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, ONETIME_ALARM_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long wakeupTime = alarmTime.getTimeInMillis();
        Log.d(TAG, "TSG WAKEUP"+wakeupTime);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, wakeupTime, pendingIntent);
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, wakeupTime, pendingIntent);
        else
            alarmManager.set(AlarmManager.RTC_WAKEUP, wakeupTime, pendingIntent);
    }

    private void showTimeDialog(String key, String type) throws ParseException {
        //Specifying date format that matches the given date
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf.parse(dateExp));
        Log.d("debug", "TAG expDate"+sdf.parse(dateExp));

        //Number of Days to add
        calendar.add(Calendar.DAY_OF_MONTH, -5);

        oneTimeAlarm(calendar, key, type);
    }

    public void createOneTimeAlarmInPickedTime(String key, String type) throws ParseException {
        showTimeDialog(key, type);
    }

    public void sendNoti(String key, String type){
        try
        {
            createOneTimeAlarmInPickedTime(key, type);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
    }
}