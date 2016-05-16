package com.anex13.dipapp;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.List;

public class FragAutoscan extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private TimeAlarm alarm;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.autoscan, container, false);
        FloatingActionButton fab =(FloatingActionButton) rootView.findViewById(R.id.fab);
        alarm = new TimeAlarm();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //обновить состояние запилить
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(123,null,this);
        super.onActivityCreated(savedInstanceState);
    }

// создать курсор адаптер
//     https://www.javacodegeeks.com/2013/09/android-viewholder-pattern-example.html

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(uri,null,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
 //диствью сет адаптер (тнью май курсор адаптер (курсор))
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

