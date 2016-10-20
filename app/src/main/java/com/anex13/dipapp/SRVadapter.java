package com.anex13.dipapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Created by namel on 16.05.2016.
 */
public class SRVadapter extends CursorAdapter{
    private Context mContext;
    public SRVadapter(Context context, Cursor c) {
        super(context, c,0);
        this.mContext=context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view= LayoutInflater.from(context).inflate(R.layout.srv_list_item,parent,false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
    final Server server = new Server(cursor);
        ConstraintLayout item =(ConstraintLayout) view.findViewById(R.id.itemlayout) ;
        Switch switch1=(Switch) view.findViewById(R.id.switch1);
        ((TextView) view.findViewById(R.id.srvname)).setText(server.getName());
        ((TextView) view.findViewById(R.id.srvurl)).setText(server.getUrl());
        ((ImageView) view.findViewById(R.id.srvState)).setColorFilter(server.getcolor());
        switch1.setChecked(server.getAlarm()!=0);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (server.getAlarm()!=0){
                    server.setAlarm(0);
                    server.setState(2);}
                else
                    server.setAlarm(1);
                final Uri uri = ContentUris.withAppendedId(SRVContentProvider.SERVERS_CONTENT_URI, server.getId());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mContext.getContentResolver().update(uri,server.toContentValues(),null,null);
                    }
                }).start();
            }
        });
    }
}
