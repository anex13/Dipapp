package com.anex13.dipapp;

import android.app.IntentService;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
    private static final String ACTION_MONITORNOW = "checkitnow";
    private static final String ACTION_MONITORALRM = "alrmcheck";
    private static Context mcontext;
    private static Cursor c;
    static String selection = null;
    static String[] selectionArgs = null;
    static String sortOrder = null;
    static String[] projection = null;
    static long fivemins = 5 * 60 * 1000;
    private final static String TAG = "HardwareAddress";
    private final static String MAC_RE = "^%s\\s+0x1\\s+0x2\\s+([:0-9a-fA-F]+)\\s+\\*\\s+\\w+$";
    private final static int BUF = 8 * 1024;
    private static final String ACTION_TRACE = "ACTION_TRACE";
    private static final String ACTION_PING = "ACTION_PING";
    private static final String ACTION_SCAN = "ACTION_SCAN";
    private static final String ACTION_MONITOR = "ACTION_MONITOR";
    private static final String PARAM_URL = "url";
    private static final String PARAM_CHKURL = "chkurl";
    public static final String ANSVER = "ansver";
    final static String LOG_TAG = "myLogs";
    private static final int NB_THREADS = 10;
    String hostname = "";
    String mac;

    public IntentSrvs() {
        super("PingSrvc");
    }

    public static String ping(String url, int ttl, int count) {
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

    public static Boolean statechk(String url) {
        boolean str;
        String pingansver = ping(url, 54, 1);
        str = !pingansver.contains("100% packet loss");
        Log.i(LOG_TAG, "state chk " + url + "  ansv   " + str);
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
                String[] splurl = url.split("\\.");
                String lanurl = splurl[0] + "." + splurl[1] + "." + splurl[2] + ".";
                for (int dest = 1; dest < 255; dest++) {
                    String host = lanurl + dest;
                    executor.execute(pingRunnable(host));
                }
                Log.i(LOG_TAG, "Waiting for executor to terminate...");
                executor.shutdown();
                try {
                    executor.awaitTermination(60 * 1000, TimeUnit.MILLISECONDS);
                } catch (InterruptedException ignored) {
                }
                Log.i(LOG_TAG, "Scan finished");
                Intent broadcastIntentfinish = new Intent();
                broadcastIntentfinish.setAction(FragPing.BROADCAST_ACTION);
                broadcastIntentfinish.addCategory(Intent.CATEGORY_DEFAULT);
                String[] scanfinish = new String[1];
                scanfinish[0] = "scanfinish";
                broadcastIntentfinish.putExtra(ANSVER, scanfinish);
                sendBroadcast(broadcastIntentfinish);
                break;
            case ACTION_MONITORNOW:
                selection = SRVContentProvider.FAIL_NOTIFICATION + " <> ?";
                selectionArgs = new String[]{"0"};
                c = mcontext.getContentResolver().query(SRVContentProvider.SERVERS_CONTENT_URI, projection, selection, selectionArgs, sortOrder, null);
                try {
                    check(c);
                    Log.i(LOG_TAG, "check kursor");
                } finally {
                    c.close();
                    Log.i(LOG_TAG, "cursor close");
                }
                //// TODO: 12.10.2016 add code here
                setalrm();
                break;


            case ACTION_MONITORALRM:

                long timenow = java.lang.System.currentTimeMillis();
                Alarm.cancelAlarm(mcontext);
                selection = SRVContentProvider.FAIL_NOTIFICATION + " <> ? AND " + SRVContentProvider.SERVER_NEXT_CHECK + " <?";
                selectionArgs = new String[]{"0", Long.toString(timenow + fivemins)};
                c = mcontext.getContentResolver().query(SRVContentProvider.SERVERS_CONTENT_URI, projection, selection, selectionArgs, sortOrder, null);
                //todo запустить следующий алярм
                try {
                    check(c);
                    Log.i(LOG_TAG, "check kursor");
                } finally {
                    c.close();
                    Log.i(LOG_TAG, "cursor close");
                }
                setalrm();
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

    private static void setalrm() {
        long timenow = java.lang.System.currentTimeMillis();
        selection = SRVContentProvider.FAIL_NOTIFICATION + " <> ?";
        selectionArgs = new String[]{"0"};
        sortOrder = SRVContentProvider.SERVER_NEXT_CHECK;
        c = mcontext.getContentResolver().query(SRVContentProvider.SERVERS_CONTENT_URI, projection, selection, selectionArgs, sortOrder, null);
        try {
            Log.i(LOG_TAG, "cursor for next alrm");
            c.moveToFirst();
            Server nalrm = new Server(c);
            Alarm alarm =new Alarm();
            alarm.setAlarm(mcontext, nalrm.getNextchktime());

        } finally {
            c.close();
            Log.i(LOG_TAG, "close alrm cursr");
        }
    }


    public static void checkItNow(Context context) {
        Intent monallIntent = new Intent(context, IntentSrvs.class);
        monallIntent.setAction(ACTION_MONITORNOW);
        context.startService(monallIntent);
        mcontext = context;
        Log.i(LOG_TAG, "check it now intent start");

    }

    public static void alrmCheck(Context context) {
        Intent monalrmIntent = new Intent(context, IntentSrvs.class);
        monalrmIntent.setAction(ACTION_MONITORALRM);
        context.startService(monalrmIntent);
        mcontext = context;
        Log.i(LOG_TAG, "alrm check intent start");
    }

    static void check(Cursor c) {
        final long now = java.lang.System.currentTimeMillis();
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    final Server crsrSRV = new Server(c);
                    int state;
                    if (statechk(crsrSRV.getUrl())) {
                        Log.i(LOG_TAG, "1chk ");
                        state = 1;
                    } else if (statechk(crsrSRV.getChkurl())) {
                        state = 0;
                    } else
                        state = 2;
                    Log.i(LOG_TAG, "state = " + state);
                    crsrSRV.setState(state);
                    crsrSRV.setNextchktime(now + crsrSRV.getTime());
                    final Uri uri = ContentUris.withAppendedId(SRVContentProvider.SERVERS_CONTENT_URI, crsrSRV.getId());
                    Log.i(LOG_TAG, "update db entry");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mcontext.getContentResolver().update(uri, crsrSRV.toContentValues(), null, null);
                        }
                    }).start();
                } while (c.moveToNext());
            }
        } else {
            Log.d(LOG_TAG, "Cursor is null");
        }
        //установить следующий аларм взять первый серв из (FAIL_NOTIFICATION != 0) и сортировать по SERVER_NEXT_CHECK
    }

    public static void startmonitor(Context context, String url, String chkurl) {
        Intent pingIntent = new Intent(context, IntentSrvs.class);
        pingIntent.setAction(ACTION_MONITOR);
        pingIntent.putExtra(PARAM_URL, url);
        pingIntent.putExtra(PARAM_CHKURL, chkurl);
        context.startService(pingIntent);
        Log.i(LOG_TAG, "start mon url chkurl" + url + "  " + chkurl);
    }

    public void onDestroy() {
        super.onDestroy();
    }


    private Runnable pingRunnable(final String host) {
        return new Runnable() {
            public void run() {
                String[] scanresult = new String[4];
                boolean state = statechk(host);
                if (state == true) {
                    try {
                        hostname = InetAddress.getByName(host).getHostName();
                        mac = getHardwareAddress(host);
                    } catch (UnknownHostException e) {
                        Log.e(LOG_TAG, "Not found", e);
                    }
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