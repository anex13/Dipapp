package com.anex13.dipapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

/**
 * Created by namel on 16.05.2016.
 */
public class SRVadapter extends CursorAdapter{
    public SRVadapter(Context context, Cursor c) {
        super(context, c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view= LayoutInflater.from(context).inflate(R.layout.srv_list_item,parent,false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }
}
