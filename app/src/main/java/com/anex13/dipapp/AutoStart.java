package com.anex13.dipapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class AutoStart extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Context mcontext = context;
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            SharedPreferences sPref = context.getSharedPreferences(FragPrefs.PREF_TAG, MODE_PRIVATE);
            if (sPref.getBoolean(FragPrefs.AUTO_START, false))
                IntentSrvs.checkItNow(context);
        }
    }
}
// TODO: 20.10.2016 вынести в шаред префс