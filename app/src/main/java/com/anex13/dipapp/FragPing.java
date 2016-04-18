package com.anex13.dipapp;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FragPing extends Fragment implements View.OnClickListener {
    TextView tv;
    String ans;
    Button serchbtn;
    EditText pingurl;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ping, container, false);
        tv= (TextView)rootView.findViewById(R.id.textView2);
        serchbtn = (Button) rootView.findViewById(R.id.button);
        pingurl = (EditText)rootView.findViewById(R.id.pingurl);
        serchbtn.setOnClickListener(this);
        return rootView;
    }
    public void onClick(View v) {
        String url;
        url = pingurl.getText().toString();
        tv.setText("Please wait");
        new PingTask().execute(url);
    }
    private class PingTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            return ping(urls[0]);
        }
        protected void onPostExecute(String result) {
            tv.setText(result);
        }
    }
    //new tread new runable, service content provider
    //бд-> content provider -> ui
    //intent service несколько экшенов
    //broadcast reciever
    public String ping(String url) {
        String str = "";
        try {
            Process process = Runtime.getRuntime().exec(
                    "/system/bin/ping -c 4 " + url);
            //
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));
            int i;
            char[] buffer = new char[4096];
            StringBuffer output = new StringBuffer();
            while ((i = reader.read(buffer)) > 0)
                output.append(buffer, 0, i);
            reader.close();
            str = output.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }
}