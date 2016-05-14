package com.anex13.dipapp;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by namel on 21.04.2016.
 */
public class IntentSrvs extends IntentService {

    private static final String ACTION_TRACE = "ACTION_TRACE";
    private static final String ACTION_PING = "ACTION_PING";
    private static final String ACTION_SCAN = "ACTION_SCAN";
    private static final String PARAM_URL = "url";
    public static final String ANSVER = "ansver";
    final static String LOG_TAG = "myLogs";

    public IntentSrvs() {
        super("PingSrvc");
    }

    public String ping(String url, int ttl, int count) {
        String str = "^_^error";
        try {

            Process process = Runtime.getRuntime().exec(
                    "/system/bin/ping -W 1 -c " + count + " -t " + ttl + " " + url);
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
                String last = "0";
                String now = "";
                int ttl1 = 1;
                String result = "tracing";
                do {
                    Intent broadcastIntent1 = new Intent();
                    broadcastIntent1.setAction(FragPing.BROADCAST_ACTION);
                    broadcastIntent1.addCategory(Intent.CATEGORY_DEFAULT);
                    broadcastIntent1.putExtra(ANSVER, result);
                    sendBroadcast(broadcastIntent1);
                    last = now;
                    String[] splitedstr = ping(url, ttl1, 1).split("\n");
                    now = splitedstr[1].replaceFirst(":(.*)exceeded", "").replaceFirst(":(.*)ms", "");
                    if (now == "") {
                        last = String.valueOf(ttl1);
                    }
                    result = "step " + ttl1 + " answer " + now;
                    ttl1 = ttl1 + 1;

                } while (!now.equals(last) && ttl1 <= 54);
                Intent broadcastIntent1 = new Intent();
                broadcastIntent1.setAction(FragPing.BROADCAST_ACTION);
                broadcastIntent1.addCategory(Intent.CATEGORY_DEFAULT);
                broadcastIntent1.putExtra(ANSVER, "\n Tracing finished \n");
                sendBroadcast(broadcastIntent1);
                break;
            case ACTION_SCAN:
                ExecutorService executor = Executors.newFixedThreadPool(NB_THREADS);
                for (int dest = 0; dest < 255; dest++) {
                    String host = "192.168.100." + dest;
                    executor.execute(pingRunnable(host));
                }
                Log.i(LOG_TAG, "Waiting for executor to terminate...");
                executor.shutdown();
                try {
                    executor.awaitTermination(60 * 1000, TimeUnit.MILLISECONDS);
                } catch (InterruptedException ignored) {
                }
                Log.i(LOG_TAG, "Scan finished");
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

    public static void startScan(Context context, String url) {
        Intent pingIntent = new Intent(context, IntentSrvs.class);
        pingIntent.setAction(ACTION_SCAN);
        pingIntent.putExtra(PARAM_URL, url);
        context.startService(pingIntent);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    private static final int NB_THREADS = 10;


    private Runnable pingRunnable(final String host) {
        return new Runnable() {
            public void run() {
                String scanresult="";
                try {
                    InetAddress inet = InetAddress.getByName(host);
                    boolean reachable = inet.isReachable(1000);
                    scanresult= "host " +host+ (reachable ? "reachable" : "not reachable");
                } catch (UnknownHostException e) {
                    Log.e(LOG_TAG, "Not found", e);
                } catch (IOException e) {
                    Log.e(LOG_TAG, "IO Error", e);
                }
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(FragPing.BROADCAST_ACTION);
                broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                broadcastIntent.putExtra(ANSVER, scanresult);
                sendBroadcast(broadcastIntent);
            }
        };
    }
}
