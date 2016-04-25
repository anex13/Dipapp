package com.anex13.dipapp;

import android.app.IntentService;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * Created by namel on 21.04.2016.
 */
public class IntentSrvs extends IntentService {


    public static String action = "action";
    public static String url = "url";

    public IntentSrvs() {
        super("PingSrvc");
    }

    String result = "";
    public static final String ANSVER = "ansver";

    public void onCreate() {
        super.onCreate();
    }

    public String ping(String url, int ttl, int count) {
        String str = "^_^error";
        try {

            Process process = Runtime.getRuntime().exec(
                    "/system/bin/ping -c " + count + " -t " + ttl + " " + url);
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
        return str;
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        String url = intent.getStringExtra("url");
        String action = intent.getStringExtra("action");
        switch (action) {
            case "ping":
                result = ping(url, 54, 4);
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(FragPing.BROADCAST_ACTION);
                broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                broadcastIntent.putExtra(ANSVER, result);
                sendBroadcast(broadcastIntent);
                break;
            default:
                String last = "";
                String now = "";
                int ttl1 = 1;
                do {
                    last = now;
                    String[] splitedstr = ping(url, ttl1, 1).split("\n");
                    now = splitedstr[1].replaceFirst(":(.*)exceeded", "").replaceFirst(":(.*)ms", "");
                    result = "step " + ttl1 + " " + now;
                    ttl1 = ttl1 + 1;
                    Intent broadcastIntent1 = new Intent();
                    broadcastIntent1.setAction(FragPing.BROADCAST_ACTION);
                    broadcastIntent1.addCategory(Intent.CATEGORY_DEFAULT);
                    broadcastIntent1.putExtra(ANSVER, result);
                    sendBroadcast(broadcastIntent1);
                }
                while (now != last );
                break;
        }


    }
    //kontent provider

    public void onDestroy() {
        super.onDestroy();

    }
}
