package com.anex13.dipapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


public class FragPing extends Fragment implements View.OnClickListener {
    BroadcastReceiver receiver;
    TextView tv;
    String ans;
    Button pingbtn;
    Button tracebtn;
    EditText pingurl;
    ProgressBar pBar;
    public final static String ANSVER = "ansver";
    public final static String BROADCAST_ACTION = "com.anex13.dipapp.ping";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ping, container, false);
        tv = (TextView) rootView.findViewById(R.id.textView2);
        pBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        pBar.setVisibility(View.INVISIBLE);
        pingbtn = (Button) rootView.findViewById(R.id.buttonping);
        tracebtn = (Button) rootView.findViewById(R.id.buttontrace);
        pingurl = (EditText) rootView.findViewById(R.id.pingurl);
        pingbtn.setOnClickListener(this);
        tracebtn.setOnClickListener(this);
        receiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                ans = intent.getStringExtra(ANSVER);
                tv.setText(ans+"\n" + tv.getText());
                pBar.setVisibility(View.INVISIBLE);

            }
        };

        return rootView;
    }

    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(BROADCAST_ACTION);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().registerReceiver(receiver, filter);

    }

    public void onPause() {
        super.onPause();
        IntentFilter filter = new IntentFilter(BROADCAST_ACTION);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().unregisterReceiver(receiver);
    }

    public void onClick(View v) {
        String url;
        url = pingurl.getText().toString();
        tv.setText("Please wait");
        switch (v.getId()) {
            case R.id.buttonping:
                IntentSrvs.startPing(getActivity(), url);
                pBar.setVisibility(View.VISIBLE);
                break;
            default:
                IntentSrvs.startTrace(getActivity(), url);
                pBar.setVisibility(View.VISIBLE);
                break;
        }
    }
}
//todo viewswitcher refs
// TODO: 23.09.2016  сделать чтото с кнопками? тулбар ? автозаполнение едита текстом?