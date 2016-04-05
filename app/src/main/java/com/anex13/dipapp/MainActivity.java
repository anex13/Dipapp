package com.anex13.dipapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        showFragment(new frag_wiki(), false,"0","0");
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        Fragment fragment = null;
        switch (id) {
            case R.id.menuautoscan:
                fragment = new frag_autoscan();
                toolbar.setTitle(R.string.frag1);
              //toolbar.setBackgroundColor(Color.RED);
                break;
            case R.id.menuping:
                fragment = new frag_ping();
                toolbar.setTitle(R.string.frag2);
              //toolbar.setBackgroundColor(Color.MAGENTA);
                break;
            case R.id.menulanscan:
                fragment = new frag_lanscan();
                toolbar.setTitle(R.string.frag3);
              //toolbar.setBackgroundColor(Color.BLUE);
                break;
            default:
                fragment = new frag_wiki();
                toolbar.setTitle(R.string.frag4);
              //toolbar.setBackgroundColor(Color.GREEN);
                break;
        }
        showFragment(fragment, false,"0","0");
        return true;
    }

    public void showFragment(Fragment fragment, boolean addToBackStack,String tag, String arg) {
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        Bundle bundle = new Bundle();
        bundle.putString(tag, arg);
        fragment.setArguments(bundle);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
        drawer.closeDrawers();
    }
}