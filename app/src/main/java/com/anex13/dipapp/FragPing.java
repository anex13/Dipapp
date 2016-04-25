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
import android.widget.TextView;


public class FragPing extends Fragment implements View.OnClickListener {
    BroadcastReceiver receiver;
    TextView tv;
    String ans;
    Button pingbtn;
    Button tracebtn;
    EditText pingurl;
    public final static String ANSVER = "ansver";
    public final static String BROADCAST_ACTION = "com.anex13.dipapp";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ping, container, false);
        tv = (TextView) rootView.findViewById(R.id.textView2);
        pingbtn = (Button) rootView.findViewById(R.id.buttonping);
        tracebtn = (Button) rootView.findViewById(R.id.buttontrace);
        pingurl = (EditText) rootView.findViewById(R.id.pingurl);
        pingbtn.setOnClickListener(this);
        tracebtn.setOnClickListener(this);
        receiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                ans = intent.getStringExtra(ANSVER);
                tv.setText(ans+"\n" + tv.getText());
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
                Intent pingIntent = new Intent(getActivity().getApplicationContext(), IntentSrvs.class).putExtra(IntentSrvs.url, url).putExtra(IntentSrvs.action,"ping");
                getActivity().getApplicationContext().startService(pingIntent);
                break;
            default:
                tv.setText("Тут будет трасировка");
                Intent traceIntent = new Intent(getActivity().getApplicationContext(), IntentSrvs.class).putExtra(IntentSrvs.url, url).putExtra(IntentSrvs.action, "trace");
                getActivity().getApplicationContext().startService(traceIntent);
                break;
        }
    }
}