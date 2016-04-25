package com.anex13.dipapp;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by namel on 21.04.2016.
 */
public class IntentSrvs extends IntentService {

    private static final String ACTION_TRACE = "ACTION_TRACE";
    private static final String ACTION_PING = "ACTION_PING";
    private static final String PARAM_URL = "url";
    public static final String ANSVER = "ansver";

    public IntentSrvs() {
        super("PingSrvc");
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
        String action = intent.getAction();
        String url = intent.getStringExtra(PARAM_URL);
        switch (action) {
            case ACTION_PING:
                String pingResult = ping(url, 54, 4);
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(FragPing.BROADCAST_ACTION);
                broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                broadcastIntent.putExtra(ANSVER, pingResult);
                sendBroadcast(broadcastIntent);
                break;
            case ACTION_TRACE:
                String last = "";
                String now = "";
                int ttl1 = 1;
                do {
                    last = now;
                    String[] splitedstr = ping(url, ttl1, 1).split("\n");
                    now = splitedstr[1].replaceFirst(":(.*)exceeded", "").replaceFirst(":(.*)ms", "");
                    String result = "step " + ttl1 + " " + now;
                    ttl1 = ttl1 + 1;
                    Intent broadcastIntent1 = new Intent();
                    broadcastIntent1.setAction(FragPing.BROADCAST_ACTION);
                    broadcastIntent1.addCategory(Intent.CATEGORY_DEFAULT);
                    broadcastIntent1.putExtra(ANSVER, result);
                    sendBroadcast(broadcastIntent1);
                } while (!now.equals(last) && !TextUtils.isEmpty(now));
                break;
        }
    }

    public static void startPing(Context context, String url) {
        Intent pingIntent = new Intent(context, IntentSrvs.class);
        pingIntent.setAction(ACTION_PING);
        pingIntent.putExtra(PARAM_URL, url);
        context.startService(pingIntent);
    }

    public static void startTrace(Context context, String url) {
        Intent pingIntent = new Intent(context, IntentSrvs.class);
        pingIntent.setAction(ACTION_TRACE);
        pingIntent.putExtra(PARAM_URL, url);
        context.startService(pingIntent);
    }
}
