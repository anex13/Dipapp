package com.anex13.dipapp;


import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;


public class FragPrefs extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener, AdapterView.OnItemSelectedListener {

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
    public static final String DEF_SCR = "def scr";
    Switch autostart, useMail, useGmail, starTLS, authNeeded, wifionly, oncharge;
    SeekBar pingcount;
    SharedPreferences sPref;
    EditText email, password, smtpurl, smtpPort, mailto, mailheader;
    Button mailSave;
    TextView gmailref;
    View mailGroup, ownsrvGroup;
    Spinner defScreen;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.prefs_frag, container, false);
        sPref = getActivity().getSharedPreferences(PREF_TAG, MODE_PRIVATE);
        mailGroup =(View) rootView.findViewById(R.id.mail_group);
        ownsrvGroup =(View) rootView.findViewById(R.id.notGmail_group);
        gmailref = (TextView) rootView.findViewById(R.id.gmail_ref);
        autostart = (Switch) rootView.findViewById(R.id.swonboot);
        autostart.setChecked(sPref.getBoolean(AUTO_START, false));
        autostart.setOnCheckedChangeListener(this);
        useMail = (Switch) rootView.findViewById(R.id.sw_email);
        useMail.setOnCheckedChangeListener(this);
        useMail.setChecked(sPref.getBoolean(USE_MAIL, false));
        useGmail = (Switch) rootView.findViewById(R.id.sw_gmail);
        useGmail.setChecked(sPref.getBoolean(USE_GMAIL, false));
        useGmail.setOnCheckedChangeListener(this);
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
        defScreen = (Spinner) rootView.findViewById(R.id.def_scr);
        defScreen.setOnItemSelectedListener(this);
        String[] data = {"Monitoring", "Ping and trace", "Lan scaner", "Wiki", "Port scaner"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        defScreen.setAdapter(adapter);
        defScreen.setSelection(sPref.getInt(DEF_SCR,1)); //взять из шаред преф.
        defScreen.setPrompt("Default screen");

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            oncharge.setVisibility(View.GONE);
            wifionly.setVisibility(View.GONE);
        }
        if (!sPref.getBoolean(USE_MAIL, false)) {
            mailGroup.setVisibility(View.GONE);
        } else {
            if (sPref.getBoolean(USE_GMAIL, false)) {
                ownsrvGroup.setVisibility(View.GONE);
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
                ed = sPref.edit();
                ed.putBoolean(USE_AUTH, buttonView.isChecked());
                ed.apply();
                break;
            case R.id.sw_tls:
                ed = sPref.edit();
                ed.putBoolean(USE_TLS, buttonView.isChecked());
                ed.apply();
                break;
            case R.id.sw_wifi_only:
                ed = sPref.edit();
                ed.putBoolean(USE_WIFI, buttonView.isChecked());
                ed.apply();
                break;
            case R.id.sw_on_charge:
                ed = sPref.edit();
                ed.putBoolean(USE_CHARGE, buttonView.isChecked());
                ed.apply();
                break;
            default:
                //
        }

    }

    private void setVisibilityMail() {
        if (!sPref.getBoolean(USE_MAIL, false)) {
        mailGroup.setVisibility(View.GONE);
        } else {
            mailGroup.setVisibility(View.VISIBLE);
        }
    }

    private void setVisibilityGmail() {
        if (sPref.getBoolean(USE_MAIL, false)) {
            if (sPref.getBoolean(USE_GMAIL, false)) {
                gmailref.setVisibility(View.VISIBLE);
                ownsrvGroup.setVisibility(View.GONE);
            } else {
                gmailref.setVisibility(View.GONE);
                ownsrvGroup.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mailprefssave:
                SharedPreferences.Editor ed;
                ed = sPref.edit();
                ed.putString(MAIL_FROM, email.getText().toString());
                ed.putString(MAIL_PASS, password.getText().toString());
                ed.putString(MAIL_TO, mailto.getText().toString());
                ed.putString(MAIL_HEAD, mailheader.getText().toString());
                ed.putString(MAIL_URL, smtpurl.getText().toString());
                ed.putString(MAIL_PORT, smtpPort.getText().toString());
                ed.apply();
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        SharedPreferences.Editor ed;
        ed = sPref.edit();
        ed.putInt(DEF_SCR, i);
        ed.apply();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

// TODO: 26.10.2016 https://www.google.com/settings/u/1/security/lesssecureapps