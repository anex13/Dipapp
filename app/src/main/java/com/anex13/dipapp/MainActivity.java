package com.anex13.dipapp;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    Toolbar toolbar;
    int color ;
    ActionBarDrawerToggle toggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        showFragment(new frag_wiki(), false);

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        switch (id) {
            case R.id.menuautoscan:
                fragment = new frag_autoscan();
                toolbar.setTitle(R.string.frag1);
                color=0xffff9800;
                break;
            case R.id.menuping:
                fragment = new frag_ping();
                toolbar.setTitle(R.string.frag2);
                color=0xff009688;
                break;
            case R.id.menulanscan:
                fragment = new frag_lanscan();
                toolbar.setTitle(R.string.frag3);
                color=0xff8bc34a;
                break;
            default:
                fragment = new frag_wiki();
                toolbar.setTitle(R.string.frag4);
                color=0xff3f51b5;
                break;
        }
        toolbar.setBackgroundColor(color);
        getWindow().setNavigationBarColor(color);
        showFragment(fragment, false);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showFragment(Fragment fragment, boolean addToBackStack) {
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        if (addToBackStack) {
            transaction.addToBackStack(null);
           // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
           // getSupportActionBar().setDisplayShowHomeEnabled(true);

        }
        transaction.commit();
        drawer.closeDrawers();
    }

}