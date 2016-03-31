package com.anex13.dipapp;
import android.app.Fragment;
import android.app.FragmentTransaction;
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
    public void onClick (View v) {
        FragmentTransaction fTrans = getFragmentManager().beginTransaction();
        Fragment fragment = null;

        switch (v.getId()) {
            case R.id.nixbtn:
                fragment = new wiki_web();
                break;
            case R.id.winbtn:
                fragment = new wiki_web();
                break;
            case R.id.lanbtn:
                fragment = new wiki_web();
                break;
            default:
                fragment = new wiki_web();
                break;

        }
        fTrans.add(R.id.content, fragment);
        // бэкстак почимуто не пашет или я чот не то делаю ?
      //  fTrans.addToBackStack(null);
        fTrans.commit();
        fTrans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
    }
}