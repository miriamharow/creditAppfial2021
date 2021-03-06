package com.miriam_shmuel.creditapp;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditItemActivity extends AppCompatActivity implements View.OnClickListener{
    private LinearLayout creditgiftlayout, warrantylayout, giftNameField;
    private EditText CedtGiftName, CedtShopName, Cedtvalue, CedtexpDate, Cedtbarcode, Wedtitem, WedtShopName, WedtexpDate, Wedtbarcode;
    private ImageView CimageView, WWarrantyPic, WReceiptPic;
    private Button CbtnSave, WbtnSave, btnPlusShopName;
    public static EditItemActivity instance;
    public ArrayList<Shop> shopsList;

    String type;
    Gift_Credit gift_credit;
    Gift_Credit newgc;
    Warranty warranty;
    Warranty neww;

    private FirebaseFirestore db;
    private FirebaseUser user;
    private String email;

    public static final int ONETIME_ALARM_CODE = 0;
    private AlarmManager alarmManager;

    private Calendar calender;
    private int day, month, year;
    private int dayED, monthED, yearED;
    private String dateExp = "";

    ArrayList<String> diaListShopName = new ArrayList<String>();
    ListView listView;
    AdapterEdit adapter;

    public EditItemActivity(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        email = user.getEmail();
        db = FirebaseFirestore.getInstance();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        creditgiftlayout = findViewById(R.id.creditgiftlayout);
        warrantylayout = findViewById(R.id.warrantylayout);
        CedtGiftName = findViewById(R.id.CedtGiftNameID);
        CedtShopName = findViewById(R.id.CedtShopNameID);
        Cedtvalue = findViewById(R.id.CedtvalueID);
        CedtexpDate = findViewById(R.id.CedtexpDateID);
        Cedtbarcode = findViewById(R.id.CedtbarcodeID);
        Wedtitem = findViewById(R.id.WedtitemID);
        WedtShopName = findViewById(R.id.WedtShopNameID);
        WedtexpDate = findViewById(R.id.WedtexpDateID);
        Wedtbarcode = findViewById(R.id.WedtbarcodeID);
        CimageView = findViewById(R.id.CimageView);
        WWarrantyPic = findViewById(R.id.WWarrantyPic);
        WReceiptPic = findViewById(R.id.WReceiptPic);
        CbtnSave = findViewById(R.id.CbtnSave);
        WbtnSave = findViewById(R.id.WbtnSave);
        giftNameField = findViewById(R.id.giftNameField);
        btnPlusShopName = findViewById(R.id.btnPlusShopNameID);
        btnPlusShopName.setOnClickListener(this);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        shopsList = new ArrayList<Shop>();
        instance = this;

        type = getIntent().getExtras().getString("type");
        if (type.equals("credit") || type.equals("gift")) {
            if(type.equals("gift")){
                btnPlusShopName.setVisibility(View.VISIBLE);
            }
            else
                btnPlusShopName.setVisibility(View.GONE);
            gift_credit = (Gift_Credit) getIntent().getSerializableExtra("obj");
            creditgiftlayout.setVisibility(View.VISIBLE);
            enterGiftCreditInfo();
        }
        else {
            warranty = (Warranty) getIntent().getSerializableExtra("obj");
            warrantylayout.setVisibility(View.VISIBLE);
            enterWarrantyInfo();
        }

        //-----------------CURRENT DATE---------------------
        calender = Calendar.getInstance();
        day = calender.get(Calendar.DAY_OF_MONTH);
        month = calender.get(Calendar.MONTH);
        year = calender.get(Calendar.YEAR);
        //---------------------------------------------------


        //-----------------DATE PICKER DIALOG----------------
        CedtexpDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {makeDate();}});
        WedtexpDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {makeDate();}});
        //---------------------------------------------------

        CbtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!inValidGC()) {
                    //Toast.makeText(EditItemActivity.this, "ok", Toast.LENGTH_SHORT).show();
                    confirmationData();
                }
                else
                    enterGiftCreditInfo();
            }
        });

        WbtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!inValidW()) {
                    //Toast.makeText(EditItemActivity.this, "ok", Toast.LENGTH_SHORT).show();
                    confirmationData();
                }
                else
                    enterWarrantyInfo();
            }
        });
    }

    public void makeDate(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(EditItemActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateExp = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                if (type.equals("warranty"))
                    WedtexpDate.setText(dateExp);
                else
                    CedtexpDate.setText(dateExp);

                dayED = day;
                monthED = month + 1;
                yearED = year;
            }
        }, year, month, day);

        // disable dates before today
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void confirmationData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditItemActivity.this);
        builder.setMessage("Are you sure to want to update the details of the receipt?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        update();//updateData(); check isExist by name+barcode-> delete & add
                        dialog.cancel();
                        Intent intent = new Intent();
                        intent.putExtra("type", type);
                        if (type.equals("credit") || type.equals("gift"))
                            intent.putExtra("object", (Serializable) newgc);
                        else
                            intent.putExtra("object", (Serializable) neww);
                        setResult(RESULT_OK, intent);
                        finish();

                    }
                });

        builder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void update() {
        if (type.equals("credit")) {
            ArrayList<Shop> shop = new ArrayList<Shop>();
            Shop s = new Shop(CedtShopName.getText().toString());
            shop.add(s);
            String newKeyC = shop.get(0).getName() + Cedtbarcode.getText().toString();
            List_of_Credits lc = new List_of_Credits();
            Gift_Credit newCredit = new Gift_Credit(newKeyC, Cedtbarcode.getText().toString(),
                    CedtexpDate.getText().toString(), shop, "credit", Cedtvalue.getText().toString(), null,gift_credit.getPicture());
            if(newKeyC.equals(gift_credit.getKey()))
            {
                lc.delete(gift_credit.getKey());
                lc.addCredit(newCredit);
                sendNoti(newCredit.getKey(), newCredit.getType());
                newgc = newCredit;
                Toast.makeText(instance, "save", Toast.LENGTH_SHORT).show();
            }
            else
            {
                //Toast.makeText(instance, "no = ", Toast.LENGTH_SHORT).show();
                Bitmap picBitmap = null;
                newgc = lc.iSExist(Cedtbarcode.getText().toString(),  CedtexpDate.getText().toString(), shop, Cedtvalue.getText().toString(), picBitmap ,"update", gift_credit.getKey());
            }
        }
        else if (type.equals("gift")) {
            String newKeyG = CedtGiftName.getText().toString() + Cedtbarcode.getText().toString();
            List_of_Gifts lg = new List_of_Gifts();
            Gift_Credit newGift = new Gift_Credit(newKeyG, Cedtbarcode.getText().toString(),
                    CedtexpDate.getText().toString(), shopsListNew() , "gift", Cedtvalue.getText().toString(), CedtGiftName.getText().toString(),gift_credit.getPicture());
            if(newKeyG.equals(gift_credit.getKey()))
            {
                lg.delete(gift_credit.getKey());
                lg.addGift(newGift);
                sendNoti(newGift.getKey(), newGift.getType());
                newgc = newGift;
                Toast.makeText(instance, "save", Toast.LENGTH_SHORT).show();
            }
            else
            {
                //Toast.makeText(instance, "no = ", Toast.LENGTH_SHORT).show();
                Bitmap picBitmap = null;
                newgc = lg.iSExist(Cedtbarcode.getText().toString(), CedtexpDate.getText().toString(), shopsListNew(), Cedtvalue.getText().toString(), CedtGiftName.getText().toString(),picBitmap, "update" ,gift_credit.getKey());
            }
        }
        else if (type.equals("warranty")) {
            String newKeyW = WedtShopName.getText().toString() + Wedtbarcode.getText().toString();
            Warranty newWarranty = new Warranty(WedtShopName.getText().toString(), Wedtbarcode.getText().toString(), WedtexpDate.getText().toString(), Wedtitem.getText().toString(), newKeyW, warranty.getPictureItem(), warranty.getPictureShop(), warranty.getFolder());
            List_of_Warranty lw = new List_of_Warranty();
            if(newKeyW.equals(warranty.getKey()))
            {
                lw.delete(warranty.getKey());
                lw.addWarranty(newWarranty);
                neww = newWarranty;
                Toast.makeText(instance, "save", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Bitmap picBitmap = null;
                neww = lw.iSExist(Wedtbarcode.getText().toString(), WedtexpDate.getText().toString(),WedtShopName.getText().toString(), Wedtitem.getText().toString(), picBitmap, picBitmap, "update" ,warranty.getKey());
            }
        }
    }

    private ArrayList<Shop> shopsListNew() {
        ArrayList<Shop> shoListNew = new ArrayList<>();
        for(int i =0; i<diaListShopName.size(); i++)
        {
            Shop s = new Shop (diaListShopName.get(i));
            shoListNew.add(s);
        }
        return  shoListNew;
    }

    private boolean specialCharters(int length, String str) {
        Pattern special = Pattern.compile ("[!@#$&%*()_+=|<>?{}\\[\\]~]*");
        Matcher hasSpecial = special.matcher(str);

        Pattern space = Pattern.compile ("[\n]*");
        Matcher hasSpace = space.matcher(str);

        //Toast.makeText(EditItemActivity.this, "click" + hasSpecial.find(), Toast.LENGTH_SHORT).show();
        return hasSpecial.find() || hasSpace.find();
    }

    private boolean inValidGC() {
        if(gift_credit.getType().equals("gift") && CedtGiftName.getText().toString().equals(""))
        {
            CedtGiftName.setError("Please enter gift name");
            CedtGiftName.requestFocus();
            return true;
        }

        else if(diaListShopName.isEmpty() &&  gift_credit.getType().equals("gift"))
        {
            Toast.makeText((instance), "Please edit shop name and press +", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if(CedtShopName.getText().toString().equals("") && gift_credit.getType().equals("credit") )
        {
            CedtShopName.setError("Please enter shop name");
            CedtShopName.requestFocus();
            return true;
        }
        else if(Cedtvalue.getText().toString().equals(""))  // && valid
        {
            Cedtvalue.setError("Please enter value");
            Cedtvalue.requestFocus();
            return true;
        }
        else if(CedtexpDate.getText().toString().equals(""))
        {
            CedtexpDate.setError("Please enter expiration date dd/mm/yy");
            CedtexpDate.requestFocus();
            return true;
        }
        else if(Cedtbarcode.getText().toString().equals(""))
        {
            Cedtbarcode.setError("Please enter your name");
            Cedtbarcode.requestFocus();
            return true;
        }
        return false;
    }

    private boolean inValidW() {
        if(Wedtitem.getText().toString().equals(""))
        {
            Wedtitem.setError("Please enter item name");
            Wedtitem.requestFocus();
            return true;
        }
        else if(WedtShopName.getText().toString().equals(""))
        {
            WedtShopName.setError("Please enter shop name");
            WedtShopName.requestFocus();
            return true;
        }
        else if(WedtexpDate.getText().toString().equals(""))
        {
            WedtexpDate.setError("Please enter expiration date dd/mm/yy");
            WedtexpDate.requestFocus();
            return true;
        }
        else if(Wedtbarcode.getText().toString().equals(""))
        {
            Wedtbarcode.setError("Please enter barcode");
            Wedtbarcode.requestFocus();
            return true;
        }
        return false;
    }

    public void enterGiftCreditInfo(){
        String list = "";
        String picture = "";
        if (gift_credit.getType().equals("credit"))
        {
            if (CedtShopName.getText().toString().equals(""))
                CedtShopName.setText( gift_credit.getShopName().get(0).getName());
            list = "list of credit";
        }
        else {
            CedtShopName.setVisibility(View.GONE);
            giftNameField.setVisibility(View.VISIBLE);
            if (CedtGiftName.getText().toString().equals(""))
                CedtGiftName.setText(gift_credit.getGiftName());
            if(diaListShopName.isEmpty())
                restShopListGift();
            printShopList();
            list = "list of gift";
        }
        picture = gift_credit.getPicture();
        if (Cedtvalue.getText().toString().equals(""))
            Cedtvalue.setText(gift_credit.getValue());
        if (CedtexpDate.getText().toString().equals(""))
            CedtexpDate.setText(gift_credit.getExpirationDate());
        if (Cedtbarcode.getText().toString().equals(""))
            Cedtbarcode.setText(gift_credit.getBarCode());
        getPicture(list, picture, CimageView);
    }


    private void restShopListGift() {
        for (int i=0; i < gift_credit.getShopName().size(); i++){
            shopsList.add(gift_credit.getShopName().get(i));
            diaListShopName.add(gift_credit.getShopName().get(i).getName());
        }
    }

    public void enterWarrantyInfo(){
        if (Wedtitem.getText().toString().equals(""))
            Wedtitem.setText(warranty.getItemName());
        if (WedtShopName.getText().toString().equals(""))
            WedtShopName.setText(warranty.getShopName());
        if (WedtexpDate.getText().toString().equals(""))
            WedtexpDate.setText(warranty.getExpirationDate());
        if (Wedtbarcode.getText().toString().equals(""))
            Wedtbarcode.setText(warranty.getBarCode());

        String list = "list of warranty";
        String folder = warranty.getFolder();
        //Toast.makeText(this, folder, Toast.LENGTH_SHORT).show();
        String picture1 = folder + "/"+ folder + "itemReceipt";
        String picture2 = folder + "/"+ folder + "shopReceipt";

        getPicture(list, picture1, WReceiptPic);
        getPicture(list, picture2, WWarrantyPic);

    }

    public void getPicture(String list, String picture, final ImageView imageView){
        StorageReference mImageRef = FirebaseStorage.getInstance().getReference(email+"/"+list+"/"+picture+".jpg");
        final long FIVE_MEGABYTE = 5 * 1024 * 1024;
        mImageRef.getBytes(FIVE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>()  {
            @Override
            public void onSuccess(byte[] bytes) {
                ImageView iView = imageView;
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                iView.setImageBitmap(bm);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    public String shopList(){
        String str = "";
        ArrayList<Shop> shopName = gift_credit.getShopName();
        for (int i = 0; i < shopName.size(); i++){
            if (i != shopName.size()-1)
                str += shopName.get(i).getName().toString()+", ";
            else
                str += shopName.get(i).toString();
        }
        //Toast.makeText(EditItemActivity.this,""+str, Toast.LENGTH_LONG).show();
        return str;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPlusShopNameID:
                UpdateShopName();
                CedtShopName.setText(null);
                break;
        }
    }

    public void UpdateShopName() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(EditItemActivity.this);
        final View dialogViewList = getLayoutInflater().inflate(R.layout.dialog_list_shop_name, null);
        final EditText diaShopName = (EditText) dialogViewList.findViewById(R.id.edtDiaShopNameId);
        final Button diaBtnAddShop = (Button) dialogViewList.findViewById(R.id.btnDiaPlusShopNameID);
        final Button btnShowShops = (Button) dialogViewList.findViewById(R.id.showShops);
        listView = dialogViewList.findViewById(R.id.listViewDiaID);
        mBuilder.setView(dialogViewList);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        final View dialogViewItem = getLayoutInflater().inflate(R.layout.item_shop_name, null);
        final Button diaBtnRemoveShop = (Button) dialogViewItem.findViewById(R.id.btnRemoveId);

        adapter = new AdapterEdit(this, dialog, diaListShopName);
        listView.setAdapter(adapter);

        diaBtnAddShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((!(diaShopName.getText().toString()).isEmpty())) {
                    String shopname = diaShopName.getText().toString();
                    if (!shopNameExist(shopname)) {
                        Shop shop = new Shop(shopname);
                        shopsList.add(shop);
                        diaListShopName.add(shopname);
                        //Toast.makeText(EditItemActivity.this, shopsList.toString(), Toast.LENGTH_SHORT).show();
                        printShopList();
                        adapter.notifyDataSetChanged();
                    }
                    else
                        Toast.makeText(EditItemActivity.this, "SHOP NAME EXISTS!", Toast.LENGTH_SHORT).show();
                    diaShopName.setText(null);
                }
            }
        });

        btnShowShops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                printShopList();
            }
        });

    }

    public boolean shopNameExist(String shopname) {
        if (!diaListShopName.isEmpty()) {
            for (int i = 0; i < diaListShopName.size(); i++) {
                if (diaListShopName.get(i).equals(shopname))
                    return true;
            }
        }
        return false;
    }

    public void printShopList() {
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
            str += "Your "+type+" from "+CedtShopName.getText().toString()+" is about to expire !";
        else
            str += "Your "+type+" from "+CedtGiftName.getText().toString()+" is about to expire !";
        intent.putExtra("title", "Credit APP!");
        intent.putExtra("msg", str);
        intent.putExtra("key", key);
        intent.putExtra("type", type);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, ONETIME_ALARM_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long wakeupTime = alarmTime.getTimeInMillis();

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
        Log.d("debug", "TAG"+calendar.toString());

        oneTimeAlarm(calendar, key, type);
    }

    public void createOneTimeAlarmInPickedTime(String key, String type) throws ParseException {
        Log.d("debug", "createOneTimeAlarmInPickedTime()");
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
