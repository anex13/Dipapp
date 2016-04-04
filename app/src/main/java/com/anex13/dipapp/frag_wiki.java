package com.anex13.dipapp;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

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
        Fragment fragment = null;
        String arg;
        switch (v.getId()) {
            case R.id.nixbtn:
                fragment = new wiki_sublist();
                arg ="1";
                break;
            case R.id.winbtn:
                fragment = new wiki_sublist();
                arg ="2";
                break;
            case R.id.lanbtn:
                fragment = new wiki_sublist();
                arg ="3";
                break;
            default:
                fragment = new wiki_sublist();
                arg ="4";
                break;
        }
        ((MainActivity) getActivity()).showFragment(fragment, true ,"wiki",arg);
    }

}