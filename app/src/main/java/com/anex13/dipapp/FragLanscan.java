package com.anex13.dipapp;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FragLanscan extends Fragment implements View.OnClickListener {
    BroadcastReceiver receiver;
    TextView tv;
    String ans;
    Button btnscn;
    EditText scanurl;
    public final static String ANSVER = "ansver";
    public final static String BROADCAST_ACTION = "com.anex13.dipapp";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.lanscan, container, false);
        tv = (TextView) rootView.findViewById(R.id.scanresult);
        btnscn = (Button) rootView.findViewById(R.id.buttonscn);
        scanurl = (EditText) rootView.findViewById(R.id.editText);
        btnscn.setOnClickListener(this);
        receiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                ans = intent.getStringExtra(ANSVER);
                tv.setText(ans+"\n" + tv.getText());
            }
        };


        return rootView;
    }

    @Override
    public void onClick(View v) {
        String url;
        url = scanurl.getText().toString();
        tv.setText("Please wait");
        IntentSrvs.startScan(getActivity(), url);
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
}