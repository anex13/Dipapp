package com.anex13.dipapp;


import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;


public class FragPrefs extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    public static final String AUTO_START = "autostart";
    public static final String PREF_TAG = "dipappprefs";
    public static final String USE_MAIL = "use mail";
    public static final String USE_GMAIL = "use GMail";
    public static final String USE_TLS = "Star tls needed";
    public static final String USE_AUTH = "use smtp auth";
    public static final String PING_COUNT = "ping count";
    public static final String MAIL_FROM = "my mail";
    public static final String MAIL_PASS = "my pass";
    public static final String MAIL_TO = "mail to";
    public static final String MAIL_HEAD = "mail header";
    public static final String MAIL_URL = "smtp server url";
    public static final String MAIL_PORT = "smtp port";
    public static final String USE_WIFI = "wifi only";
    public static final String USE_CHARGE = "on charge only";
    Switch autostart, useMail, useGmail, starTLS, authNeeded, wifionly, oncharge;
    SeekBar pingcount;
    SharedPreferences sPref;
    EditText email, password, smtpurl, smtpPort, mailto, mailheader;
    Button mailSave;
    TextView gmailref, tvset;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.prefs_frag, container, false);
        sPref = getActivity().getSharedPreferences(PREF_TAG, MODE_PRIVATE);
        gmailref = (TextView) rootView.findViewById(R.id.gmail_ref);
        tvset = (TextView) rootView.findViewById(R.id.tv_email_set);
        autostart = (Switch) rootView.findViewById(R.id.swonboot);
        autostart.setChecked(sPref.getBoolean(AUTO_START, false));
        autostart.setOnCheckedChangeListener(this);
        useMail = (Switch) rootView.findViewById(R.id.sw_email);
        useMail.setOnCheckedChangeListener(this);
        useMail.setChecked(sPref.getBoolean(USE_MAIL, false));
        useGmail = (Switch) rootView.findViewById(R.id.sw_gmail);
        useGmail.setOnCheckedChangeListener(this);
        useGmail.setChecked(sPref.getBoolean(USE_GMAIL, false));
        starTLS = (Switch) rootView.findViewById(R.id.sw_tls);
        starTLS.setOnCheckedChangeListener(this);
        starTLS.setChecked(sPref.getBoolean(USE_TLS, false));
        authNeeded = (Switch) rootView.findViewById(R.id.sw_auth);
        authNeeded.setChecked(sPref.getBoolean(USE_AUTH, false));
        authNeeded.setOnCheckedChangeListener(this);
        wifionly = (Switch) rootView.findViewById(R.id.sw_wifi_only);
        wifionly.setChecked(sPref.getBoolean(USE_WIFI, false));
        wifionly.setOnCheckedChangeListener(this);
        oncharge = (Switch) rootView.findViewById(R.id.sw_on_charge);
        oncharge.setChecked(sPref.getBoolean(USE_CHARGE, false));
        oncharge.setOnCheckedChangeListener(this);
        mailSave = (Button) rootView.findViewById(R.id.mailprefssave);
        mailSave.setOnClickListener(this);
        pingcount = (SeekBar) rootView.findViewById(R.id.pingcount);
        pingcount.setOnSeekBarChangeListener(this);
        pingcount.setProgress(sPref.getInt(PING_COUNT, 3));
        email = (EditText) rootView.findViewById(R.id.edit_email);
        email.setText(sPref.getString(MAIL_FROM, ""));
        password = (EditText) rootView.findViewById(R.id.edit_pass);
        password.setText(sPref.getString(MAIL_PASS, ""));
        mailto = (EditText) rootView.findViewById(R.id.edit_mailto);
        mailto.setText(sPref.getString(MAIL_TO, ""));
        mailheader = (EditText) rootView.findViewById(R.id.edit_mailheader);
        mailheader.setText(sPref.getString(MAIL_HEAD, ""));
        smtpurl = (EditText) rootView.findViewById(R.id.edit_smtpsrv);
        smtpurl.setText(sPref.getString(MAIL_URL, ""));
        smtpPort = (EditText) rootView.findViewById(R.id.edit_smtpport);
        smtpPort.setText(sPref.getString(MAIL_PORT, ""));
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            oncharge.setVisibility(View.GONE);
            wifionly.setVisibility(View.GONE);
        }
        if (!sPref.getBoolean(USE_MAIL, false)) {
            tvset.setVisibility(View.GONE);
            useGmail.setVisibility(View.GONE);
            email.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            gmailref.setVisibility(View.GONE);
            smtpurl.setVisibility(View.GONE);
            smtpPort.setVisibility(View.GONE);
            authNeeded.setVisibility(View.GONE);
            starTLS.setVisibility(View.GONE);
            mailto.setVisibility(View.GONE);
            mailheader.setVisibility(View.GONE);
            mailSave.setVisibility(View.GONE);
        } else {
            if (sPref.getBoolean(USE_GMAIL, false)) {
                smtpurl.setVisibility(View.GONE);
                smtpPort.setVisibility(View.GONE);
                authNeeded.setVisibility(View.GONE);
                starTLS.setVisibility(View.GONE);
            } else
                gmailref.setVisibility(View.GONE);
        }


        return rootView;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences.Editor ed;
        switch (buttonView.getId()) {
            case R.id.swonboot:
                ed = sPref.edit();
                ed.putBoolean(AUTO_START, buttonView.isChecked());
                ed.apply();
                break;
            case R.id.sw_email:
                ed = sPref.edit();
                ed.putBoolean(USE_MAIL, buttonView.isChecked());
                ed.apply();
                setVisibilityMail();
                setVisibilityGmail();
                break;
            case R.id.sw_gmail:
                ed = sPref.edit();
                ed.putBoolean(USE_GMAIL, buttonView.isChecked());
                ed.apply();
                setVisibilityGmail();
                break;
            case R.id.sw_auth:
                break;
            case R.id.sw_tls:
                break;
            default:
                //
        }

    }

    private void setVisibilityMail() {
        if (!sPref.getBoolean(USE_MAIL, false)) {
            tvset.setVisibility(View.GONE);
            useGmail.setVisibility(View.GONE);
            email.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            gmailref.setVisibility(View.GONE);
            smtpurl.setVisibility(View.GONE);
            smtpPort.setVisibility(View.GONE);
            authNeeded.setVisibility(View.GONE);
            starTLS.setVisibility(View.GONE);
            mailto.setVisibility(View.GONE);
            mailheader.setVisibility(View.GONE);
            mailSave.setVisibility(View.GONE);
        } else {
            tvset.setVisibility(View.VISIBLE);
            useGmail.setVisibility(View.VISIBLE);
            email.setVisibility(View.VISIBLE);
            password.setVisibility(View.VISIBLE);
            mailto.setVisibility(View.VISIBLE);
            mailheader.setVisibility(View.VISIBLE);
            mailSave.setVisibility(View.VISIBLE);
        }
    }

    private void setVisibilityGmail() {
        if (sPref.getBoolean(USE_MAIL, false)) {
            if (sPref.getBoolean(USE_GMAIL, false)) {
                gmailref.setVisibility(View.VISIBLE);
                smtpurl.setVisibility(View.GONE);
                smtpPort.setVisibility(View.GONE);
                authNeeded.setVisibility(View.GONE);
                starTLS.setVisibility(View.GONE);
            } else {
                gmailref.setVisibility(View.GONE);
                smtpurl.setVisibility(View.VISIBLE);
                smtpPort.setVisibility(View.VISIBLE);
                authNeeded.setVisibility(View.VISIBLE);
                starTLS.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mailprefssave:
                //write mail prefs
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
