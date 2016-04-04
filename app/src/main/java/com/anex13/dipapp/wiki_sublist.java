package com.anex13.dipapp;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class wiki_sublist extends Fragment {
    TextView tv;
    Bundle bundle = getArguments();
    String recieveInfo;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =
                inflater.inflate(R.layout.wikiweb, container, false);
        tv = (TextView) rootView.findViewById(R.id.textView2);

        if (bundle != null) {
            recieveInfo = bundle.getString("wiki","0_0");
        }
        tv.setText(recieveInfo);


        return rootView;
    }

    }
