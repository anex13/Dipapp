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
import android.widget.ViewSwitcher;

import static android.content.Context.MODE_PRIVATE;


public class FragPrefs extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    public static final String AUTO_START = "autostart";
    public static final String PREF_TAG = "dipappprefs";
    public static final String USE_MAIL ="use mail" ;
    public static final String USE_GMAIL ="use GMail" ;
    public static final String USE_TLS = "Star tls needed" ;
    public static final String USE_AUTH = "use smtp auth";
    public static final String PING_COUNT = "ping count";
    private static final String MAIL_FROM = "my mail";
    private static final String MAIL_PASS = "my pass";
    private static final String MAIL_TO ="mail to" ;
    private static final String MAIL_HEAD ="mail header" ;
    private static final String MAIL_URL = "smtp server url";
    private static final String MAIL_PORT ="smtp port" ;
    Switch autostart, useMail, useGmail, starTLS, authNeeded;
    SeekBar pingcount;
    SharedPreferences sPref;
    EditText email, password, smtpurl, smtpPort, mailto, mailheader;
    ViewSwitcher wsapi, wsmail, wsgmail;
    Button mailSave;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.prefs_frag, container, false);
        sPref = getActivity().getSharedPreferences(PREF_TAG, MODE_PRIVATE);
        autostart = (Switch) rootView.findViewById(R.id.swonboot);
        autostart.setChecked(sPref.getBoolean(AUTO_START,false));
        autostart.setOnCheckedChangeListener(this);
        useMail= (Switch) rootView.findViewById(R.id.sw_email);
        useMail.setOnCheckedChangeListener(this);
        useMail.setChecked(sPref.getBoolean(USE_MAIL,false));
        useGmail = (Switch) rootView.findViewById(R.id.sw_gmail);
        useGmail.setOnCheckedChangeListener(this);
        useGmail.setChecked(sPref.getBoolean(USE_GMAIL,false));
        starTLS = (Switch) rootView.findViewById(R.id.sw_tls);
        starTLS.setOnCheckedChangeListener(this);
        starTLS.setChecked(sPref.getBoolean(USE_TLS,false));
        authNeeded = (Switch) rootView.findViewById(R.id.sw_auth);
        authNeeded.setChecked(sPref.getBoolean(USE_AUTH,false));
        authNeeded.setOnCheckedChangeListener(this);
        mailSave=(Button) rootView.findViewById(R.id.mailprefssave);
        mailSave.setOnClickListener(this);
        pingcount=(SeekBar) rootView.findViewById(R.id.pingcount);
        pingcount.setOnSeekBarChangeListener(this);
        pingcount.setProgress(sPref.getInt(PING_COUNT,3));
        email=(EditText) rootView.findViewById(R.id.edit_email);
        email.setText(sPref.getString(MAIL_FROM,""));
        password=(EditText) rootView.findViewById(R.id.edit_pass);
        password.setText(sPref.getString(MAIL_PASS,""));
        mailto=(EditText) rootView.findViewById(R.id.edit_mailto);
        mailto.setText(sPref.getString(MAIL_TO,""));
        mailheader=(EditText) rootView.findViewById(R.id.edit_mailheader);
        mailheader.setText(sPref.getString(MAIL_HEAD,""));
        smtpurl=(EditText) rootView.findViewById(R.id.edit_smtpsrv);
        smtpurl.setText(sPref.getString(MAIL_URL,""));
        smtpPort=(EditText) rootView.findViewById(R.id.edit_smtpport);
        smtpPort.setText(sPref.getString(MAIL_PORT,""));
        wsapi= (ViewSwitcher) rootView.findViewById(R.id.wsJsheduler);
        wsmail= (ViewSwitcher) rootView.findViewById(R.id.ws_email);
        wsgmail= (ViewSwitcher) rootView.findViewById(R.id.ws_Gmail);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wsapi.showNext();
                    }
       // if (sPref.getBoolean(USE_MAIL,false)){
        //    wsmail.showNext();
       //     if (sPref.getBoolean(USE_GMAIL,false))
       //         wsgmail.showNext();
      //  }



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
                wsmail.showNext();
                ed = sPref.edit();
                ed.putBoolean(USE_MAIL, buttonView.isChecked());
                ed.apply();
                break;
            case R.id.sw_gmail:
                wsgmail.showNext();
                ed = sPref.edit();
                ed.putBoolean(USE_GMAIL, buttonView.isChecked());
                ed.apply();
                break;
            case R.id.sw_auth:
                break;
            case R.id.sw_tls:
                break;
            default:
                //
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
