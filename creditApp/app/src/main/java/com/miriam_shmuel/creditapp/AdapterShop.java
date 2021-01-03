package com.miriam_shmuel.creditapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AdapterShop extends ArrayAdapter<String> {

    Context context;
    ArrayList <Shop> shopsList ; //A List of AndroidFlavor objects to display in a list
    int resource; // layout file

    AdapterShop(Context c, ArrayList<String> rows) {
        super(c, R.layout.item_shop_name, R.id.itemId, rows);
        this.context = c;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.item_shop_name, parent, false);
        TextView shop = row.findViewById(R.id.itemId);
        Button btnRow = row.findViewById(R.id.btnRemoveId);

        LayoutInflater layoutInflater1 = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialog = layoutInflater1.inflate(R.layout.dialog_list_shop_name, parent, false);
        EditText inputUser = (EditText) dialog.findViewById(R.id.edtDiaShopNameId);
        shop.setText(inputUser.getText().toString());
        Toast.makeText(context,inputUser.getText().toString() , Toast.LENGTH_SHORT).show();

        btnRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show();
            }
        });
        return row;
    }
}


