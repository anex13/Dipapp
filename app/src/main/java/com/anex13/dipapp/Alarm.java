package com.anex13.dipapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

public class Alarm extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();
        IntentSrvs.alrmCheck(context);
        wl.release();
    }

    public static void setAlarm(Context context, long next) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context,Alarm.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setWindow(AlarmManager.RTC_WAKEUP, next,5000, pi);
    }

    public static void cancelAlarm(Context context) {
        Intent i = new Intent(context,Alarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, i, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}

// TODO: 20.10.2016 отображать время следующего алярма 