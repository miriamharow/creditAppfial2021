package com.miriam_shmuel.creditapp;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterEdit extends ArrayAdapter<String> {

    Context context;
    ArrayList<String> rows;
    AlertDialog dialog;

    AdapterEdit(Context c, AlertDialog dialog, ArrayList<String> rows) {
        super(c, R.layout.item_shop_name, R.id.itemId, rows);
        this.context = c;
        this.dialog = dialog;
        this.rows = rows;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item = layoutInflater.inflate(R.layout.item_shop_name, parent, false);
        TextView shopName = item.findViewById(R.id.itemId);
        shopName.setText(rows.get(position));

        Button btnRow = item.findViewById(R.id.btnRemoveId);
        btnRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditItemActivity.instance.diaListShopNsme = DelItemFromList(position);
                EditItemActivity.instance.shopsList = DelItemFromListShop(position);
                EditItemActivity.instance.UpdateShopName();

                dialog.cancel();
            }
        });
        return item;
    }

    public ArrayList<String> DelItemFromList(int line) {
        Boolean find=false;
        ArrayList<String> newListArray = new ArrayList<String>(rows.size() - 1);
        for(int i = 0; i < rows.size() ;i++) {
            if(i != line){
                newListArray.add(rows.get(i));
            }
        }
        return newListArray;
    }

    public ArrayList<Shop> DelItemFromListShop(int line) {
        Boolean find=false;
        ArrayList<Shop> newListArray = new ArrayList<Shop>(rows.size() - 1);
        for(int i = 0; i < rows.size() ;i++) {
            if(i != line){
                Shop shop = new Shop(rows.get(i));
                newListArray.add(shop);
            }
        }
        return newListArray;
    }
}


