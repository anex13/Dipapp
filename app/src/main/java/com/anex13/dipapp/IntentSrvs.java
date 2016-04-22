package com.anex13.dipapp;

import android.app.IntentService;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by namel on 21.04.2016.
 */
public class IntentSrvs extends IntentService {


    public static int ttl=54;
    public static int count=1;
    public static String url ="url";
    public IntentSrvs() {super("PingSrvc");    }
    String str = "";
    public static final String ANSVER = "ansver";

    public void onCreate() {super.onCreate();    }


    @Override
    protected void onHandleIntent(Intent intent) {
        String url = intent.getStringExtra("url");
        ttl = intent.getIntExtra("ttl", 54);
        count = intent.getIntExtra("count", 4);

        try {

            Process process = Runtime.getRuntime().exec(
                    "/system/bin/ping -c "+count+" -t " + ttl + " " + url);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));
            int i;
            char[] buffer = new char[4096];
            StringBuffer output = new StringBuffer();
            while ((i = reader.read(buffer)) > 0)
                output.append(buffer, 0, i);
            reader.close();
            str = output.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(FragPing.BROADCAST_ACTION);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra(ANSVER, str);
        sendBroadcast(broadcastIntent);
    }
    //kontent provider
    
    public void onDestroy() {
        super.onDestroy();

    }
}
