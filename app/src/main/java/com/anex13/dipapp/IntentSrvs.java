package com.anex13.dipapp;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public Boolean statechk(String url) {
        boolean str;
        String pingansver = ping(url, 54, 1);
        str = !pingansver.contains("100% packet loss");
        Log.i(LOG_TAG, "state chk " + pingansver);
        // Некоторые девайсы не пингуются =\
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
                for (int dest = 1; dest < 255; dest++) {
                    String host = "192.168.100." + dest;
                    //забрать и обработать урлу из фрагмента
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
    String hostname = "";
    String mac;

    private Runnable pingRunnable(final String host) {
        return new Runnable() {
            public void run() {
                String[] scanresult = new String[4];
                boolean state = statechk(host);
                if (state == true) {
                    try {
                        // state=InetAddress.getByName(host).isReachable(1000);
                        hostname = InetAddress.getByName(host).getHostName();
                        mac = getHardwareAddress(host);
                    } catch (UnknownHostException e) {
                        Log.e(LOG_TAG, "Not found", e);
                    }
                    // if (state==true){
                    scanresult[0] = hostname;
                    scanresult[1] = host;
                    scanresult[2] = mac;
                    scanresult[3] = "vendor";
                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction(FragPing.BROADCAST_ACTION);
                    broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                    broadcastIntent.putExtra(ANSVER, scanresult);
                    sendBroadcast(broadcastIntent);
                }
            }
        };
    }

    private final static String TAG = "HardwareAddress";
    private final static String MAC_RE = "^%s\\s+0x1\\s+0x2\\s+([:0-9a-fA-F]+)\\s+\\*\\s+\\w+$";
    private final static int BUF = 8 * 1024;

    public static String getHardwareAddress(String ip) {
        String hw = "  no mac";
        BufferedReader bufferedReader = null;
        try {
            if (ip != null) {
                String ptrn = String.format(MAC_RE, ip.replace(".", "\\."));
                Pattern pattern = Pattern.compile(ptrn);
                bufferedReader = new BufferedReader(new FileReader("/proc/net/arp"), BUF);
                String line;
                Matcher matcher;
                while ((line = bufferedReader.readLine()) != null) {
                    matcher = pattern.matcher(line);
                    if (matcher.matches()) {
                        hw = matcher.group(1);
                        break;
                    }
                }
            } else {
                Log.e(TAG, "ip is null");
            }
        } catch (IOException e) {
            Log.e(TAG, "Can't open/read file ARP: " + e.getMessage());
            return hw;
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return hw;
    }
}
// https://code.wireshark.org/review/gitweb?p=wireshark.git;a=blob_plain;f=manuf   база mac-vendor  какнибудь запилить
// InetAddress.getByName("host ip").getHostName();  hostname по ip
// выровнять руки и переделать нормально
//добавить базу вендоров
//