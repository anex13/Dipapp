package com.anex13.dipapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by namel on 06.05.2016.
 */
public class FragSrvItem extends Fragment {

    String Title="title";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.autoscan, container, false);

        return rootView;
    }
    public String getTitle() {
        return Title;
    }
}
