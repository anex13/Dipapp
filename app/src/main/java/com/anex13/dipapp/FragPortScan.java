package com.anex13.dipapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by it.zavod on 09.11.2016.
 */

public class FragPortScan extends Fragment implements View.OnClickListener {
    BroadcastReceiver receiver;
    EditText scanUrl,scanPort,scanAnsv;
    Button scanDefault, scanOwn;
    ImageView rdp, http, smb,ftp;
    String ans;
    public final static String ANSVER = "ansver";
    public final static String BROADCAST_ACTION = "com.anex13.dipapp.ports";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.port_scan, container, false);
        scanUrl=(EditText) rootView.findViewById(R.id.port_scan_url);
        scanPort=(EditText) rootView.findViewById(R.id.port_scan_portedit);
        scanAnsv=(EditText) rootView.findViewById(R.id.port_scan_port_ansv);
        scanDefault=(Button) rootView.findViewById(R.id.port_scan_scandefoult_btn);
        scanOwn=(Button) rootView.findViewById(R.id.port_scan_ownport_scan);
        rdp=(ImageView) rootView.findViewById(R.id.port_scan_imgrdp);
        http=(ImageView) rootView.findViewById(R.id.port_scan_imghttp);
        smb=(ImageView) rootView.findViewById(R.id.port_scan_imgsmb);
        ftp=(ImageView) rootView.findViewById(R.id.port_scan_imgftp);
        scanDefault.setOnClickListener(this);
        scanOwn.setOnClickListener(this);
        receiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                ans = intent.getStringExtra(ANSVER);

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
        switch (v.getId()) {
            case R.id.port_scan_scandefoult_btn:

                break;
            default:

                break;
        }
    }
}