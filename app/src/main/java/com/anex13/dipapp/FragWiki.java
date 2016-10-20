package com.anex13.dipapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import static com.anex13.dipapp.Pageenum.LAN;
import static com.anex13.dipapp.Pageenum.MOB;
import static com.anex13.dipapp.Pageenum.NIX;
import static com.anex13.dipapp.Pageenum.WIN;

public class FragWiki extends Fragment implements View.OnClickListener {
    ImageButton winbtn;
    ImageButton lanbtn;
    ImageButton mobbtn;
    ImageButton nixbtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.wiki, container, false);
        winbtn = (ImageButton) rootView.findViewById(R.id.winbtn);
        lanbtn = (ImageButton) rootView.findViewById(R.id.lanbtn);
        mobbtn = (ImageButton) rootView.findViewById(R.id.mobbtn);
        nixbtn = (ImageButton) rootView.findViewById(R.id.nixbtn);
        nixbtn.setOnClickListener(this);
        winbtn.setOnClickListener(this);
        lanbtn.setOnClickListener(this);
        mobbtn.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        Pageenum pageenum;
        switch (v.getId()) {
            case R.id.nixbtn:
                pageenum = NIX;
                break;
            case R.id.winbtn:
                pageenum = WIN;
                break;
            case R.id.lanbtn:
                pageenum = LAN;
                break;
            default:
                pageenum = MOB;
                break;
        }

        ((MainActivity) getActivity()).showFragment( WikiSublist.getInstance(pageenum), true);
    }

}
// TODO: 20.10.2016 заполнить разделы 