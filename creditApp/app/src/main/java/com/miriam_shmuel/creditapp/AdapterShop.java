package com.miriam_shmuel.creditapp;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AdapterShop extends ArrayAdapter<String> {

    Context context;
    ArrayList<String> rows;
    AlertDialog dialog;

    AdapterShop(Context c, AlertDialog dialog, ArrayList<String> rows) {
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
                AddActivity.instance.loo = DelItemFromList(position);
                AddActivity.instance.UpdateShopName("");
                dialog.cancel();
            }
        });
        return item;
    }

    public ArrayList<String> DelItemFromList(int line) {
        Boolean find=false;
        ArrayList<String> newlistArray = new ArrayList<String>(rows.size() - 1);
        for(int i = 0; i < rows.size() ;i++) {
            if(i != line){
                newlistArray.add(rows.get(i));
            }
        }
        return newlistArray;
    }
}


