package com.anex13.dipapp;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import static com.anex13.dipapp.Pageenum.*;

public class frag_wiki extends Fragment implements View.OnClickListener {
    ImageButton winbtn;
    ImageButton lanbtn;
    ImageButton mobbtn;
    ImageButton nixbtn;

    // хз чо тут происходит
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
                pageenum =WIN;
                break;
            case R.id.winbtn:
                pageenum =NIX;
                break;
            case R.id.lanbtn:
                pageenum =LAN;
                break;
            default:
                pageenum =MOB;
                break;
        }
        ((MainActivity) getActivity()).showFragment( wiki_sublist.getInstance(pageenum), true);


    }

}