package com.anex13.dipapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
    ProgressBar pBar;
    String[] ans;
    FloatingActionButton  btnscn;
    final static String TAG= "mylog";
    EditText scanurl;
    public final static String ANSVER = "ansver";
    public final static String BROADCAST_ACTION = "com.anex13.dipapp";
    LinearLayout linLayout = null;
    Toast toast2;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        WifiManager wifiMgr = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        String ipAddress = Formatter.formatIpAddress(ip);
        final View rootView = inflater.inflate(R.layout.lanscan, container, false);
        pBar = (ProgressBar) rootView.findViewById(R.id.lanpb);
        pBar.setVisibility(View.INVISIBLE);
        btnscn = (FloatingActionButton) rootView.findViewById(R.id.buttonscn);
        scanurl = (EditText) rootView.findViewById(R.id.editText);
        btnscn.setOnClickListener(this);
        scanurl.setText(ipAddress);

        linLayout = (LinearLayout) rootView.findViewById(R.id.linrnet);

        receiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                ans = intent.getStringArrayExtra(ANSVER);
                switch (ans[0]){
                    case "scanfinish":
                        // scan finished actions
                        Toast toast1 = Toast.makeText(getContext(), "Network scan is finished", Toast.LENGTH_LONG);
                        toast1.show();
                        pBar.setVisibility(View.INVISIBLE);
                        break;
                    default:
                LayoutInflater ltInflater = getActivity().getLayoutInflater();
                View item = ltInflater.inflate(R.layout.lan_item, linLayout, false);
                TextView tvName = (TextView) item.findViewById(R.id.textHostname);
                tvName.setText(ans[0]);
                TextView tvip = (TextView) item.findViewById(R.id.textIP);
                tvip.setText(ans[1]);
                TextView tvmac = (TextView) item.findViewById(R.id.textMacAddr);
                tvmac.setText("mac: " + ans[2]);
                TextView tvvendor = (TextView) item.findViewById(R.id.textVendor);
                tvvendor.setText("vendor: " + ans[3]);
                item.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
                item.setTag(ans[1]);
                linLayout.addView(item);
               item.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       //сюда запилить фраг портов
                       Log.d(TAG,"id"+v.getTag());
                   }
               });}
            }

        };


        return rootView;
    }

    // TODO: 23.09.2016 запилить прогрессбар черным цветом на фаб
    @Override
    public void onClick(View v) {
        String url;
        if (pBar.getVisibility()!=View.VISIBLE){
        pBar.setVisibility(View.VISIBLE);
        url = scanurl.getText().toString();
        linLayout.removeAllViews();
        IntentSrvs.startScan(getActivity(), url);}
        else {
            toast2 = Toast.makeText(getContext(), "Please wait", Toast.LENGTH_SHORT);
            toast2.show();
        }
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