package com.anex13.dipapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;


public class Wiki_item_view extends Fragment {
    WebView webView;
    Bundle bundle;
    public static final String TAG_PAGEENUM = "pageenum";
    static Pageenum pageenum1=Pageenum.WIN;
    static int position1;


    public static Wiki_item_view getInstance(Pageenum pageenum, int position) {
        position1=position;
        pageenum1=pageenum;
        return new Wiki_item_view();
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =
                inflater.inflate(R.layout.wikiitemview, container, false);

        bundle = getArguments();

        if (bundle != null) {
            pageenum1 = Pageenum.valueOf(bundle.getString(TAG_PAGEENUM, Pageenum.WIN.name()));
        } else if (savedInstanceState != null && savedInstanceState.containsKey(TAG_PAGEENUM)) {
            pageenum1 = Pageenum.valueOf(savedInstanceState.getString(TAG_PAGEENUM, Pageenum.WIN.name()));
        }
        webView = (WebView) rootView.findViewById(R.id.webView);
        webView.loadUrl("file:///android_res/raw/w" + pageenum1.getPosition()+position1 + ".htm");

        return rootView;

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TAG_PAGEENUM, pageenum1.name());
    }
}
