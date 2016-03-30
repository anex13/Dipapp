package com.anex13.dipapp;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class frag4 extends Fragment implements View.OnClickListener {
    TextView tv;// для проверки работоспособности
    ImageButton winbtn;
    ImageButton lanbtn;
    ImageButton mobbtn;
    ImageButton nixbtn;
    // хз чо тут происходит
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =
                inflater.inflate(R.layout.frag4, container, false);

        tv = (TextView) rootView.findViewById(R.id.tv);
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
// проверка
        switch (v.getId()) {
            case R.id.nixbtn:
                tv.setText("nix");
                break;
            case R.id.winbtn:
                tv.setText("win");
                break;
            case R.id.lanbtn:
                tv.setText("lan");
                break;
            default:
                tv.setText("mob");
                break;
        }

    }




}