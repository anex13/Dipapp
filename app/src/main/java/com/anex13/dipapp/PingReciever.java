package com.anex13.dipapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by namel on 21.04.2016.
 */
public class PingReciever extends BroadcastReceiver {
    public static final String ACTION_RESP =
            "com.mamlambo.intent.action.MESSAGE_PROCESSED";

    @Override
    public void onReceive(Context context, Intent intent) {
        String text = intent.getStringExtra(IntentSrvs.PARAM_OUT_MSG);
    }
}

