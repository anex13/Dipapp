package com.anex13.dipapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity {
    frag1 frag1;
    frag2 frag2;
    frag3 frag3;
    frag4 frag4;
    FragmentTransaction fTrans;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }


    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        fTrans = getFragmentManager().beginTransaction();

        if (id == R.id.menuautoscan) {
            fTrans.replace(R.id.content, frag1);
        } else if (id == R.id.menuping) {
            fTrans.replace(R.id.content, frag2);
        } else if (id == R.id.menulanscan) {
            fTrans.replace(R.id.content, frag3);
        } else if (id == R.id.menuwiki) {
            fTrans.replace(R.id.content, frag4);
        }


        return false;
    }
}