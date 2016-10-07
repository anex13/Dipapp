package com.anex13.dipapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoStart extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Context mcontext = context;
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {

            SRVChecker srvChecker = new SRVChecker(mcontext);
            srvChecker.checkItNow();
        }
    }
}