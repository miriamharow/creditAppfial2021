package com.miriam_shmuel.creditapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditItemActivity extends AppCompatActivity {
    private LinearLayout creditgiftlayout, warrantylayout, giftNameField;
    private EditText CedtGiftName, CedtShopName, Cedtvalue, CedtexpDate, Cedtbarcode, Wedtitem, WedtShopName, WedtexpDate, Wedtbarcode;
    private ImageView CimageView, WWarrantyPic, WReceiptPic;
    private Button CbtnSave, WbtnSave;
    String type;
    Gift_Credit gift_credit;
    Warranty warranty;

    private FirebaseFirestore db;
    private FirebaseUser user;
    private String email;

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

        type = getIntent().getExtras().getString("type");
        if (type.equals("credit") || type.equals("gift")) {
            gift_credit = (Gift_Credit) getIntent().getSerializableExtra("obj");
            creditgiftlayout.setVisibility(View.VISIBLE);
            enterGiftCreditInfo();
        }
        else {
            warranty = (Warranty) getIntent().getSerializableExtra("obj");
            warrantylayout.setVisibility(View.VISIBLE);
            enterWarrantyInfo();
        }

        CbtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!inValid()) {
                    Toast.makeText(EditItemActivity.this, "ok", Toast.LENGTH_SHORT).show();
                    confirmationData();
                }
            }
        });
    }

    private void confirmationData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditItemActivity.this);
        builder.setMessage("Are you sure to want to update the details of the receipt?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //updateData(); -> dellete & add
                        //Toast(seccused)
                        dialog.cancel();
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

    private boolean specialCharters(int length, String str) {
        Pattern special = Pattern.compile ("[!@#$&%*()_+=|<>?{}\\[\\]~]*");
        Matcher hasSpecial = special.matcher(str);

        Pattern space = Pattern.compile ("[\n]*");
        Matcher hasSpace = space.matcher(str);

        Toast.makeText(EditItemActivity.this, "click" + hasSpecial.find(), Toast.LENGTH_SHORT).show();
        return hasSpecial.find() || hasSpace.find();
    }

    private boolean inValid() {
        if(gift_credit.getType().equals("gift") && CedtGiftName.getText().toString().equals(""))
        {
            CedtGiftName.setError("Please enter  name");
            CedtGiftName.requestFocus();
            return true;
        }
        else if(CedtShopName.getText().toString().equals(""))
        {
            CedtShopName.setError("Please enter your name");
            CedtShopName.requestFocus();
            return true;
        }
        else if(Cedtvalue.getText().toString().equals(""))  // && valid
        {
            Cedtvalue.setError("Please enter your name");
            Cedtvalue.requestFocus();
            return true;
        }
        else if(CedtexpDate.getText().toString().equals(""))
        {
            CedtexpDate.setError("Please enter your name");
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

    public void enterGiftCreditInfo(){
        String list = "";
        String key = "";
        if (gift_credit.getType().equals("credit"))
        {
            CedtShopName.setText( gift_credit.getShopName().get(0).getName());
            list = "list of credits";
            key = gift_credit.getKey();
        }
        else{
            giftNameField.setVisibility(View.VISIBLE);
            CedtGiftName.setText(gift_credit.getGiftName());
            CedtShopName.setText(shopList());
            list = "list of gifts";
            key = gift_credit.getKey();
        }
        Cedtvalue.setText(gift_credit.getValue());
        CedtexpDate.setText(gift_credit.getExpirationDate());
        Cedtbarcode.setText(gift_credit.getBarCode());
        getPicture(list, key, CimageView);
    }

    public void enterWarrantyInfo(){
        Wedtitem.setText(warranty.getItemName());
        WedtShopName.setText(warranty.getShopName());
        WedtexpDate.setText(warranty.getExpirationDate());
        Wedtbarcode.setText(warranty.getBarCode());

        String list = "list of warranty";
        String key = warranty.getKey();
        Toast.makeText(this, key, Toast.LENGTH_SHORT).show();
        String key1 = key + "/"+ key + "itemReceipt";
        String key2 = key + "/"+ key + "shopReceipt";

        getPicture(list, key1, WReceiptPic);
        getPicture(list, key2, WWarrantyPic);

    }

    public void getPicture(String list, String key, final ImageView imageView){
        StorageReference mImageRef = FirebaseStorage.getInstance().getReference(email+"/"+list+"/"+key+".jpg");
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
        Toast.makeText(EditItemActivity.this,""+str, Toast.LENGTH_LONG).show();
        return str;
    }

}
