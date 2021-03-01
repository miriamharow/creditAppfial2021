package com.miriam_shmuel.creditapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import java.util.ArrayList;

public class ShowItemActivity extends AppCompatActivity {
    private LinearLayout creditgiftlayout, warrantylayout, giftNameField;
    private TextView CGiftNameID, CShopNameID, CvalueID, CexpDateID, CbarcodeID, WitemID, WShopNameID, WexpDateID, WbarcodeID, edit;
    private ImageView CimageView, WWarrantyPic, WReceiptPic;
    String type;
    Gift_Credit gift_credit;
    Warranty warranty;

    private FirebaseFirestore db;
    private FirebaseUser user;
    private String email;

    public ShowItemActivity(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        email = user.getEmail();
        db = FirebaseFirestore.getInstance();
    }
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_item);

        edit = findViewById(R.id.btnEditID);
        creditgiftlayout = findViewById(R.id.creditgiftlayout);
        warrantylayout = findViewById(R.id.warrantylayout);
        CGiftNameID = findViewById(R.id.CGiftNameID);
        CShopNameID = findViewById(R.id.CShopNameID);
        CvalueID = findViewById(R.id.CvalueID);
        CexpDateID = findViewById(R.id.CexpDateID);
        CbarcodeID = findViewById(R.id.CbarcodeID);
        WitemID = findViewById(R.id.WitemID);
        WShopNameID = findViewById(R.id.WShopNameID);
        WexpDateID = findViewById(R.id.WexpDateID);
        WbarcodeID = findViewById(R.id.WbarcodeID);
        CimageView = findViewById(R.id.CimageView);
        WWarrantyPic = findViewById(R.id.WWarrantyPic);
        WReceiptPic = findViewById(R.id.WReceiptPic);
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

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentObj = new Intent(ShowItemActivity.this, EditItemActivity.class);
                if (type.equals("credit") || type.equals("gift")) {
                    gift_credit = (Gift_Credit) getIntent().getSerializableExtra("obj");
                    intentObj.putExtra("type",gift_credit.getType());
                    intentObj.putExtra("obj", (Serializable) gift_credit);
                }
                else {
                    warranty = (Warranty) getIntent().getSerializableExtra("obj");
                    intentObj.putExtra("type","warranty");
                    intentObj.putExtra("obj", (Serializable)warranty);
                }
                ShowItemActivity.this.startActivity(intentObj);
            }
        });
    }


    public void enterGiftCreditInfo(){
        String list = "";
        String key = "";
        if (gift_credit.getType().equals("credit"))
        {
            CShopNameID.setText(gift_credit.getShopName().get(0).toString());
            list = "list of credits";
            key = gift_credit.getKey();
        }
        else{
            giftNameField.setVisibility(View.VISIBLE);
            CGiftNameID.setText(gift_credit.getGiftName());
            CShopNameID.setText(shopList());
            list = "list of gifts";
            key = gift_credit.getKey();
        }
        CvalueID.setText(gift_credit.getValue());
        CexpDateID.setText(gift_credit.getExpirationDate());
        CbarcodeID.setText(gift_credit.getBarCode());
        getPicture(list, key, CimageView);
    }

    public void enterWarrantyInfo(){
        WitemID.setText(warranty.getItemName());
        WShopNameID.setText(warranty.getShopName());
        WexpDateID.setText(warranty.getExpirationDate());
        WbarcodeID.setText(warranty.getBarCode());

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
        Toast.makeText(ShowItemActivity.this,""+str, Toast.LENGTH_LONG).show();
        return str;
    }
}