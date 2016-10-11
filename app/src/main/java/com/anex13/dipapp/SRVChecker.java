package com.anex13.dipapp;

import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import java.util.Date;

/**
 * Created by it.zavod on 07.10.2016.
 */

public class SRVChecker {
    private static Context mcontext;
    private static Cursor c;
    static String selection = null;
    static String[] selectionArgs = null;
    static String sortOrder = null;
    static String[] projection = null;
    static long fivemins = 5 * 60 * 1000;
    static BroadcastReceiver receiver;
    public static final String ANSVER = "ansver";
    public final static String BROADCAST_ACTION = "com.anex13.dipapp";
    static String ans;
    final static String LOG_TAG = "myLogs";

    public SRVChecker(Context context) {
        this.mcontext = context;


    }

    public static void checkItNow() {
        // выбрать все серваки с (FAIL_NOTIFICATION != 0)
        selection = SRVContentProvider.FAIL_NOTIFICATION + " <> ?";
        selectionArgs = new String[]{"0"};
        c = mcontext.getContentResolver().query(SRVContentProvider.SERVERS_CONTENT_URI, projection, selection, selectionArgs, sortOrder, null);
        check(c);
        Log.i(LOG_TAG, "chck it now");
    }

    public static void alrmCheck() {
        long now = java.lang.System.currentTimeMillis();
        selection = SRVContentProvider.FAIL_NOTIFICATION + " <>? AND "+SRVContentProvider.SERVER_NEXT_CHECK+" <?";
        selectionArgs = new String[]{"0",Long.toString(now+fivemins)};
        c = mcontext.getContentResolver().query(SRVContentProvider.SERVERS_CONTENT_URI, projection, selection, selectionArgs, sortOrder, null);
        //todo запустить следующий алярм
        check(c);
    }

    private static void check(Cursor c) {
        final long now=java.lang.System.currentTimeMillis();
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    final Server crsrSRV = new Server(c);
                    IntentSrvs.startmonitor(mcontext, crsrSRV.getUrl(),crsrSRV.getChkurl());
                    receiver = new BroadcastReceiver() {
                        public void onReceive(Context context, Intent intent) {
                            ans = intent.getStringExtra(ANSVER);
                            Log.i(LOG_TAG, "ansver="+ans);
                            crsrSRV.setState(Integer.parseInt(ans));//todo вернуть стэйт с проверки
                            crsrSRV.setNextchktime(now+crsrSRV.getTime());
                            final Uri uri = ContentUris.withAppendedId(SRVContentProvider.SERVERS_CONTENT_URI, crsrSRV.getId());
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    mcontext.getContentResolver().update(uri, crsrSRV.toContentValues(), null, null);
                                }
                            }).start();
                        }
                    };

                } while (c.moveToNext());
            }
            c.close();
        } else {
             Log.d(LOG_TAG, "Cursor is null");
        }



        //установить следующий аларм взять первый серв из (FAIL_NOTIFICATION != 0) и сортировать по SERVER_NEXT_CHECK
    }


}
