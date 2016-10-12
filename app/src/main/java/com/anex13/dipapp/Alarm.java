package com.anex13.dipapp;

        import android.app.AlarmManager;
        import android.app.PendingIntent;
        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.os.PowerManager;
        import android.provider.Settings;
        import android.util.Log;
        import android.widget.Toast;

public class Alarm extends BroadcastReceiver
{
    final static String LOG_TAG = "myLogs";
    @Override
    public void onReceive(Context context, Intent intent)
    {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        Log.i(LOG_TAG, "alrms tart chck");
        IntentSrvs.alrmCheck(context);

        wl.release();
    }

    public static void setAlarm(Context context,long next)
    {Log.i(LOG_TAG, "alrm set"+next+ "   current time  " + System.currentTimeMillis());
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, Alarm.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),next-System.currentTimeMillis(), pi); // Millisec * Second * Minute
    }

    public static void cancelAlarm(Context context)
    {
        Intent intent = new Intent(context, Alarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
        Log.i(LOG_TAG, "alrm canceled");
    }
}