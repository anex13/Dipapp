package com.anex13.dipapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;


public class wikiitemview extends Fragment {
    WebView webView;
    Bundle bundle;
    String recieveInfo;
    String url;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =
                inflater.inflate(R.layout.wikiitemview, container, false);

        bundle = getArguments();

        if (bundle != null) {
            recieveInfo=bundle.getString("wiki1tem","0_o");
                    url="file:///android_res/raw/w"+recieveInfo+".htm";
            }
        webView = (WebView) rootView.findViewById(R.id.webView);
            webView.loadUrl(url);
           // webView.loadUrl("file:///android_assets/w1.htm");


        return rootView;

    }
}
