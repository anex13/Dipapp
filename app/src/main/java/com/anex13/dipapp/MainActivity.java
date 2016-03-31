package com.anex13.dipapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        Fragment fragment = new frag_wiki();
        FragmentTransaction fTrans = getFragmentManager().beginTransaction();
        fTrans.replace(R.id.content, fragment).commit();
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        FragmentTransaction fTrans = getFragmentManager().beginTransaction();

        Fragment fragment = null;
        switch (id) {
            case R.id.menuautoscan:
                fragment = new frag_autoscan();
                break;
            case R.id.menuping:
                fragment = new frag_ping();
                break;
            case R.id.menulanscan:
                fragment = new frag_lanscan();
                break;
            default:
                fragment = new frag_wiki();
                break;
        }
        fTrans.replace(R.id.content, fragment).commit();
        fTrans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        drawer.closeDrawers();
        return true;
    }
}