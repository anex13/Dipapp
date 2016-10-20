package com.anex13.dipapp;

import android.content.ContentUris;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ViewSwitcher;

/**
 * Created by namel on 17.05.2016.
 */
public class FragSRVAdd extends Fragment implements View.OnClickListener, View.OnFocusChangeListener {
    EditText srvname;
    EditText srvurl;
    EditText srvchkurl;
    EditText srvupdatetime;
    Switch srvalrmsw;
    Button addbutton;
    Button cancelbutton;
    ImageButton infname;
    ImageButton infurl;
    ImageButton infchk;
    ImageButton inftime;
    String servername = "";
    String serverurl = "";
    String serverchkurl = "";
    int serverupdate ;
    Switch alarmsw;
    ViewSwitcher vs1;
    ViewSwitcher vs2;
    ViewSwitcher vs3;
    ViewSwitcher vs4;
    Toast toast;
    Bundle bundle;
    int id;
    Uri uri;



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.srv_add, container, false);
        bundle=getArguments();
        srvname = (EditText) rootView.findViewById(R.id.addsrvname);
        srvname.setOnFocusChangeListener(this);
        srvurl = (EditText) rootView.findViewById(R.id.addsrvurl);
        srvurl.setOnFocusChangeListener(this);
        srvchkurl = (EditText) rootView.findViewById(R.id.addchkurl);
        srvchkurl.setOnFocusChangeListener(this);
        srvupdatetime = (EditText) rootView.findViewById(R.id.addupdatetime);
        srvupdatetime.setOnFocusChangeListener(this);
        srvalrmsw = (Switch) rootView.findViewById(R.id.addswitchalarm);
        addbutton = (Button) rootView.findViewById(R.id.addsrvbutton);
        cancelbutton = (Button) rootView.findViewById(R.id.addcancelbutton);
        infname = (ImageButton) rootView.findViewById(R.id.infname);
        infurl = (ImageButton) rootView.findViewById(R.id.infurl);
        infchk = (ImageButton) rootView.findViewById(R.id.infchk);
        inftime = (ImageButton) rootView.findViewById(R.id.inftime);
        alarmsw = (Switch) rootView.findViewById(R.id.addswitchalarm);
        addbutton.setOnClickListener(this);
        cancelbutton.setOnClickListener(this);
        infname.setOnClickListener(this);
        infurl.setOnClickListener(this);
        infchk.setOnClickListener(this);
        inftime.setOnClickListener(this);
        vs1 = (ViewSwitcher) rootView.findViewById(R.id.viewSwitchername);
        vs2 = (ViewSwitcher) rootView.findViewById(R.id.viewSwitcherurl);
        vs3 = (ViewSwitcher) rootView.findViewById(R.id.viewSwitcherchkurl);
        vs4 = (ViewSwitcher) rootView.findViewById(R.id.viewSwitchertime);
        Animation inAnim = new AlphaAnimation(0, 1);
        inAnim.setDuration(500);
        Animation outAnim = new AlphaAnimation(1, 0);
        outAnim.setDuration(500);
        vs1.setInAnimation(inAnim);
        vs1.setOutAnimation(outAnim);
        vs2.setInAnimation(inAnim);
        vs2.setOutAnimation(outAnim);
        vs3.setInAnimation(inAnim);
        vs3.setOutAnimation(outAnim);
        vs4.setInAnimation(inAnim);
        vs4.setOutAnimation(outAnim);
        if (bundle!=null){
            Server srv=new Server(bundle);
            id=srv.getId();
            srvname.setText(srv.getName());
            srvurl.setText(srv.getUrl());
            srvchkurl.setText(srv.getChkurl());
            srvupdatetime.setText(Long.toString(srv.getTime()/60000));
            alarmsw.setChecked(srv.getAlarm()!=0);
            uri = ContentUris.withAppendedId(SRVContentProvider.SERVERS_CONTENT_URI, id);
            vs1.setVisibility(View.INVISIBLE);
            vs2.setVisibility(View.INVISIBLE);
            vs3.setVisibility(View.INVISIBLE);
            vs4.setVisibility(View.INVISIBLE);
            addbutton.setText("Update");

        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.infname:
                toast = Toast.makeText(getContext(), "Name of your server", Toast.LENGTH_LONG);
                toast.show();
                break;
            case R.id.infurl:
                toast = Toast.makeText(getContext(), "Url or IP-address of your server", Toast.LENGTH_LONG);
                toast.show();
                break;
            case R.id.infchk:
                toast = Toast.makeText(getContext(), "Url or IP-address of device that locate \n in the same network as your server", Toast.LENGTH_LONG);
                toast.show();
                break;
            case R.id.inftime:
                toast = Toast.makeText(getContext(), "Smaller time intervals \n seams higher battery drain", Toast.LENGTH_LONG);
                toast.show();
                break;
            case R.id.addcancelbutton:
                ((MainActivity) getActivity()).showFragment(new FragAutoscan(), false);
                break;
            default:
                //add button
                final int switchalrm;
                long now = java.lang.System.currentTimeMillis();

               final long servernextupdate =now + (serverupdate * 1000*60);
                servername = srvname.getText().toString();
                serverurl = srvurl.getText().toString();
                serverchkurl = srvchkurl.getText().toString();
                serverupdate = Integer.parseInt(srvupdatetime.getText().toString())*60*1000;
                if (srvalrmsw.isChecked())
                switchalrm =1;
                else
                switchalrm =0;
                if (!servername.equals("") && !serverurl.equals("") && !serverchkurl.equals("") && serverupdate!=0) {
                    if (bundle==null){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Server server = new Server(servername, serverurl, serverchkurl, serverupdate,servernextupdate,switchalrm,2);
                            getActivity().getContentResolver().insert(SRVContentProvider.SERVERS_CONTENT_URI, server.toContentValues());
                        }
                    }).start();}
                    else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Server server = new Server(servername, serverurl, serverchkurl, serverupdate,servernextupdate,switchalrm,2);
                                getActivity().getContentResolver().update(uri, server.toContentValues(),null,null);
                            }
                        }).start();
                    }
                    ((MainActivity) getActivity()).showFragment(new FragAutoscan(), false);
                }
                else {
                Toast toast1 = Toast.makeText(getContext(), "Fill all fields or press cancel", Toast.LENGTH_LONG);
                toast1.show();}

                break;
        }
    }

    // TODO: 23.09.2016 см фрагавтоскан
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.addsrvname:
                if (srvname.getText().toString().equals("")){
                vs1.showNext();}
                break;
            case R.id.addsrvurl:
                if (srvurl.getText().toString().equals("")){
                vs2.showNext();}
                break;
            case R.id.addchkurl:
                if (srvchkurl.getText().toString().equals("")){
                vs3.showNext();}
                break;
            default:
                if (srvupdatetime.getText().toString().equals("")){
                vs4.showNext();}
                break;
        }
    }
}