package com.anex13.dipapp;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
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
    private static final String ACTION_MONITORING = "srvmonitor";
    private static final String PARAM_ID = "serverID";
    private static final String PARAM_NEXTTIME = "nextALRMtime";
    private static final String PARAM_UPDTIME = "timing";
    private static final String PARAM_NAME = "SRVName";
    private static final String PARAM_ACT = "alrm";
    private static final String PARAM_PORT = "port";
    private static final String ACTION_PORT = "portscan";
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

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        String url = intent.getStringExtra(PARAM_URL);
        switch (action) {
            case ACTION_PING: //simple ping
                Log.i(LOG_TAG, url);
                String pingResult = ping(url, 54, 4);
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(FragPing.BROADCAST_ACTION);
                broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                broadcastIntent.putExtra(ANSVER, pingResult);
                sendBroadcast(broadcastIntent);
                Log.i(LOG_TAG, "ping broadcast  " + pingResult);
                break;
            case ACTION_TRACE: //traceroute
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
            case ACTION_SCAN: //lan scaner
                ExecutorService executor = Executors.newFixedThreadPool(NB_THREADS);
                String[] splurl = url.split("\\.");
                String lanurl = splurl[0] + "." + splurl[1] + "." + splurl[2] + ".";
                for (int dest = 1; dest < 255; dest++) {
                    String host = lanurl + dest;
                    executor.execute(lanScanerRunnable(host));
                }
                Log.i(LOG_TAG, "Waiting for executor to terminate...");
                executor.shutdown();
                try {
                    executor.awaitTermination(60 * 1000, TimeUnit.MILLISECONDS);
                } catch (InterruptedException ignored) {
                }
                Log.i(LOG_TAG, "Scan finished");
                Intent broadcastIntentfinish = new Intent();
                broadcastIntentfinish.setAction(FragLanscan.BROADCAST_ACTION);
                broadcastIntentfinish.addCategory(Intent.CATEGORY_DEFAULT);
                String[] scanfinish = new String[1];
                scanfinish[0] = FragLanscan.TAGSCANFINISH;
                broadcastIntentfinish.putExtra(ANSVER, scanfinish);
                sendBroadcast(broadcastIntentfinish);
                break;
            case ACTION_MONITORING:  //auto monitoring
                int state;
                switch (intent.getStringExtra(PARAM_ACT)) { //check for action in intents queue
                    case "no":
                        if (statechk(intent.getStringExtra(PARAM_URL))) { //check server and other same network host
                            Log.i(LOG_TAG, "1chk ");
                            state = 1;
                        } else if (statechk(intent.getStringExtra(PARAM_CHKURL))) {
                            state = 0;
                        } else
                            state = 2;
                        Log.i(LOG_TAG, "state = " + state);
                        final Server server = new Server(intent.getStringExtra(PARAM_NAME),
                                intent.getStringExtra(PARAM_URL),
                                intent.getStringExtra(PARAM_CHKURL),
                                Long.parseLong(intent.getStringExtra(PARAM_UPDTIME)),
                                Long.parseLong(intent.getStringExtra(PARAM_NEXTTIME)),
                                1, 1);

                        server.setNextchktime(System.currentTimeMillis() + Long.parseLong(intent.getStringExtra(PARAM_UPDTIME)));
                        server.setState(state);
                        final Uri uri = ContentUris.withAppendedId(SRVContentProvider.SERVERS_CONTENT_URI, Integer.parseInt(intent.getStringExtra(PARAM_ID)));
                        Log.i(LOG_TAG, "update db entry");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                getApplicationContext().getContentResolver().update(uri, server.toContentValues(), null, null);
                            }
                        }).start();
                        Log.i(LOG_TAG, "update db entry finish");
                        break;
                    case "alrm":
                        setalrm(getApplicationContext());
                        break;
                    case "notification check":
                        notificationCheck(getApplicationContext());
                        break;
                }
                break;
            case ACTION_PORT://// TODO: 09.11.2016 дописать тут
                String portResult = ping(url, 54, 4);
                Intent portansIntent = new Intent();
                portansIntent.setAction(FragPortScan.BROADCAST_ACTION);
                portansIntent.addCategory(Intent.CATEGORY_DEFAULT);
                portansIntent.putExtra(ANSVER, portResult);
                sendBroadcast(portansIntent);
                Log.i(LOG_TAG, "port broadcast  " + portResult);
                break;
        }

    }

    public static void startPing(Context context, String url) {
        Intent pingIntent = new Intent(context, IntentSrvs.class);
        pingIntent.setAction(ACTION_PING);
        pingIntent.putExtra(PARAM_URL, url);
        context.startService(pingIntent);
        Log.i(LOG_TAG, "startping method");
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

    //set alarm for nearest check
    private static void setalrm(Context context) {
        Server nalrm = null;
        selection = SRVContentProvider.FAIL_NOTIFICATION + " <> ?";
        selectionArgs = new String[]{"0"};
        sortOrder = SRVContentProvider.SERVER_NEXT_CHECK;
        c = context.getContentResolver().query(SRVContentProvider.SERVERS_CONTENT_URI, projection, selection, selectionArgs, sortOrder, null);
        try {
            if (c != null) {
                Log.i(LOG_TAG, "cursor for next alrm");
                c.moveToFirst();
                nalrm = new Server(c);
                Log.i(LOG_TAG, nalrm.getId() + "   " + nalrm.getState());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //запилить джоб шедулер
                JobScheduler jobScheduler = (JobScheduler) MainActivity.getContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
                JobInfo.Builder builder = new JobInfo.Builder(1, new ComponentName(context.getPackageName(),
                        JobSrvs.class.getName()));
                Log.i(LOG_TAG, "jobsheduled for " + ((nalrm.getNextchktime() - System.currentTimeMillis()) / 1000) + "sec");
                builder.setPeriodic(nalrm.getNextchktime() - System.currentTimeMillis());
                builder.setRequiresDeviceIdle(true);
                jobScheduler.schedule(builder.build());

            } else {
                if (nalrm != null) {
                    Alarm alarm = new Alarm();
                    alarm.setAlarm(context, nalrm.getNextchktime());
                }
            }
        } finally {
            if (c != null)
                c.close();

            Log.i(LOG_TAG, "close alrm cursr");
        }
    }

    //cancel all alarms
    public static void cancelALRM(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            JobScheduler jobScheduler = (JobScheduler) MainActivity.getContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
            jobScheduler.cancelAll();
        } else {
            Alarm alarm = new Alarm();
            Alarm.cancelAlarm(context);
        }

    }

    //servers check called manually (all servers checked)
    public static void checkItNow(Context context) {
        cancelALRM(context);
        selection = SRVContentProvider.FAIL_NOTIFICATION + " <> ?";
        selectionArgs = new String[]{"0"};
        sendIntents(context, null, selection, selectionArgs, null);
        Log.i(LOG_TAG, "check it now intent start");


    }

    //servers check called by alarm (time dependent)
    public static void alrmCheck(Context context) {
        cancelALRM(context);
        selection = SRVContentProvider.FAIL_NOTIFICATION + " <> ? AND " + SRVContentProvider.SERVER_NEXT_CHECK + " <?";
        selectionArgs = new String[]{"0", Long.toString(System.currentTimeMillis() + fivemins)};
        sendIntents(context, null, selection, selectionArgs, null);
        Log.i(LOG_TAG, "alrm check intent start");
    }

    //generating intents queue for server monitoring
    static void sendIntents(Context context, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        try {
            c = context.getContentResolver().query(SRVContentProvider.SERVERS_CONTENT_URI, projection, selection, selectionArgs, sortOrder, null);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {

                        Server crsrSRV = new Server(c);
                        Intent pingIntent = new Intent(context, IntentSrvs.class);

                        pingIntent.setAction(ACTION_MONITORING);
                        pingIntent.putExtra(PARAM_ACT, "no");
                        pingIntent.putExtra(PARAM_ID, Integer.toString(crsrSRV.getId()));
                        pingIntent.putExtra(PARAM_NAME, crsrSRV.getName());
                        pingIntent.putExtra(PARAM_URL, crsrSRV.getUrl());
                        pingIntent.putExtra(PARAM_CHKURL, crsrSRV.getChkurl());
                        pingIntent.putExtra(PARAM_UPDTIME, Long.toString(crsrSRV.getTime()));
                        pingIntent.putExtra(PARAM_NEXTTIME, Long.toString(crsrSRV.getNextchktime()));
                        context.startService(pingIntent);


                    } while (c.moveToNext());
                    Intent pingIntent = new Intent(context, IntentSrvs.class);
                    pingIntent.setAction(ACTION_MONITORING);
                    pingIntent.putExtra(PARAM_ACT, "alrm");
                    context.startService(pingIntent);
                    pingIntent.putExtra(PARAM_ACT, "notification check");
                    context.startService(pingIntent);

                }
            } else {
                Log.d(LOG_TAG, "Cursor is null");
            }

        } finally {
            c.close();
            Log.i(LOG_TAG, "close srvers cursr");
        }
    }

    //generating broadcast for lan scaner
    private Runnable lanScanerRunnable(final String host) {
        return new Runnable() {
            public void run() {
                String[] scanresult = new String[4];
                boolean state = statechk(host);
                if (state) {
                    try {
                        hostname = InetAddress.getByName(host).getHostName();
                        mac = getHardwareAddress(host);
                        Log.i(LOG_TAG, hostname);
                    } catch (UnknownHostException e) {
                        Log.e(LOG_TAG, "Not found", e);
                    }
                    scanresult[0] = hostname;
                    scanresult[1] = host;
                    scanresult[2] = mac;
                    scanresult[3] = "vendor";
                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction(FragLanscan.BROADCAST_ACTION);
                    broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                    broadcastIntent.putExtra(ANSVER, scanresult);
                    sendBroadcast(broadcastIntent);
                    Log.i(LOG_TAG, "radcast sent");
                }
            }
        };
    }

    //get mac-adr by ip
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

    //notification on state is bad
    public static void notificationCheck(Context context) {
        try {
            c = context.getContentResolver().query(SRVContentProvider.SERVERS_CONTENT_URI, projection, selection, selectionArgs, sortOrder, null);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {

                        Server crsrSRV = new Server(c);

                        Intent notificationIntent = new Intent(context, MainActivity.class);
                        PendingIntent contentIntent = PendingIntent.getActivity(context,
                                0, notificationIntent,
                                PendingIntent.FLAG_CANCEL_CURRENT);

                        Resources res = context.getResources();
                        Notification.Builder builder = new Notification.Builder(context);

                        builder.setContentIntent(contentIntent)
                                .setSmallIcon(R.mipmap.ic_launcher1)
                                //.setTicker(res.getString(R.string.warning)) // текст в строке состояния
                                .setTicker("Houston we have a problem")
                                .setWhen(System.currentTimeMillis())
                                .setAutoCancel(true)
                                //.setContentTitle(res.getString(R.string.notifytitle)) // Заголовок уведомления
                                .setContentTitle("Some server unreachable")
                                //.setContentText(res.getString(R.string.notifytext))
                                .setContentText("Server " + crsrSRV.getName() + " is unreachable"); // Текст уведомления

                        // Notification notification = builder.getNotification(); // до API 16
                        Notification notification = builder.build();
                        Uri ringURI =
                                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        notification.sound = ringURI;
                        NotificationManager notificationManager = (NotificationManager) context
                                .getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify(742, notification);
                        sendMail(context);
                    } while (c.moveToNext());

                }
            } else {
                Log.d(LOG_TAG, "Cursor is null");
            }

        } finally {
            c.close();

            // TODO: 20.10.2016 прикрутить мыло с настройкой в префс
        }

    }

    public static void sendMail(Context context){
        SharedPreferences spref =context.getSharedPreferences(FragPrefs.PREF_TAG,MODE_PRIVATE);
        if (spref.getBoolean(FragPrefs.USE_MAIL,false)){
            String emailBody = "test msg from monitoring app";
            // TODO: 26.10.2016 запилить тело мыла
            try {
                Log.i("SendMailTask", "About to instantiate GMail...");
                GMail androidEmail = new GMail(emailBody,context);
                androidEmail.createEmailMessage();
                androidEmail.sendEmail();
                Log.i("SendMailTask", "Mail Sent.");
            } catch (Exception e) {
                Log.e("SendMailTask", e.getMessage(), e);
            }
        }

    }

    //core ping utility impl
    public String ping(String url, int ttl, int count) {
        Log.i(LOG_TAG, "ping method");
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
            Log.i(LOG_TAG, "exception" + e.getMessage());
            e.printStackTrace();
        }
        return str;
    }

    //ping to bool
    public Boolean statechk(String url) {
        boolean str;
        String pingansver = ping(url, 54, 1);
        str = !pingansver.contains("100% packet loss");
        Log.i(LOG_TAG, "state chk " + url + "  ansv   " + str);
        // Некоторые девайсы не пингуются =\
        return str;
    }

    public void onDestroy() {
        super.onDestroy();
    }

    //port scaner
    public static void startPortCheck(Context context,String url, String port){
        Intent portIntent = new Intent(context, IntentSrvs.class);
        portIntent.setAction(ACTION_PORT);
        portIntent.putExtra(PARAM_URL, url);
        portIntent.putExtra(PARAM_PORT, port);
        context.startService(portIntent);
    }
}


// https://code.wireshark.org/review/gitweb?p=wireshark.git;a=blob_plain;f=manuf   база mac-vendor  какнибудь запилить
// InetAddress.getByName("host ip").getHostName();  hostname по ip
// выровнять руки и переделать нормально
//добавить базу вендоров
//https://www.google.com/settings/u/1/security/lesssecureapps необходимо включить