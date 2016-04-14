package com.anex13.dipapp;
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

public class frag_ping extends Fragment implements View.OnClickListener {
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
        ans = ping(url);
        //сюда какой нибудь прогресбар надо
        if (ans != null) {
            tv.setText(ans);
        }
    }
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
            //body.append(output.toString()+"\n");
            str = output.toString();
            //Log.d(TAG, str);
        } catch (IOException e) {
            // body.append("Error\n");
            e.printStackTrace();
        }
        return str;
    }
}