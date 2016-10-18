package com.anex13.dipapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

public class Alarm extends BroadcastReceiver {
    final static String LOG_TAG = "myLogs";

    @Override
    public void onReceive(Context context, Intent intent) {

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        Log.i(LOG_TAG, "alrms tart chck");
        IntentSrvs.alrmCheck(context);

        wl.release();
    }

    public static void setAlarm(Context context, long next) {

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context,Alarm.class);
        i.setAction("com.anex13.dipapp.MYALRM");
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setWindow(AlarmManager.RTC_WAKEUP, next,5000, pi);
        Log.i(LOG_TAG, "alrm set");
    }

    public static void cancelAlarm(Context context) {
        Intent i = new Intent(context,Alarm.class);
        i.setAction("com.anex13.dipapp.MYALRM");
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, i, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
        Log.i(LOG_TAG, "alrm canceled");
    }
}