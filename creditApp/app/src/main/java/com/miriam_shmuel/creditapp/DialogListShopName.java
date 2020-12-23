package com.miriam_shmuel.creditapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DialogListShopName extends ArrayAdapter<String> {

    Context context;

    DialogListShopName(Context c, ArrayList<String> rows) {
        super(c, R.layout.item_shop_name, R.id.itemId, rows);
        this.context = c;
    }
}


