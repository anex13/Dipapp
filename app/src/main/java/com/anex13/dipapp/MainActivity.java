package com.anex13.dipapp;

import android.Manifest;
import android.app.Activity;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    Toolbar toolbar;
    int color;
    ActionBarDrawerToggle toggle;
    View hb;
    ShowcaseView scw;
    private static Context context;
// TODO: 23.09.2016 ресы повыносить разобраться с прикручиванием рекламы

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.context = getApplicationContext();
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        hb = findViewById(android.R.id.home);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        SharedPreferences sPref =getSharedPreferences(FragPrefs.PREF_TAG,MODE_PRIVATE);
        Fragment fragdef = null;
        switch (sPref.getInt(FragPrefs.DEF_SCR,1)) {
            case 0:
                fragdef = new FragAutoscan();
                toolbar.setTitle(R.string.frag1);
                color = 0xffff9800;
                //   color = R.color.colorMonitor;
                break;
            case 1:
                fragdef = new FragPing();
                toolbar.setTitle(R.string.frag2);
                color = 0xff009688;
                //  color = R.color.colorPing;
                break;
            case 2:
                fragdef = new FragLanscan();
                toolbar.setTitle(R.string.frag3);
                color = 0xff8bc34a;
                //  color = R.color.colorLanscan;
                break;
            case 4:
                fragdef= new FragPortScan();
                toolbar.setTitle("port scan");
                color = 0xff009688;
                break;
            default:
                fragdef = new FragWiki();
                toolbar.setTitle(R.string.frag4);
                color = 0xff3f51b5;
                // color = R.color.colorWiki ;
                break;
        }
            toolbar.setBackgroundColor(color);
            if (android.os.Build.VERSION.SDK_INT >= 21) {
                getWindow().setNavigationBarColor(color);
            }
            showFragment(fragdef, false);

        if (isFirstTime()) {
            Target viewTarget = new ViewTarget(getNavButtonView(toolbar));
            scw = new ShowcaseView.Builder(this)
                    .setTarget(viewTarget)
                    .hideOnTouchOutside()
                    .setContentTitle("Main Menu")
                    .setContentText("To open main menu press this button or swipe from left")
                    .setStyle(R.style.CustomShowcaseTheme)
                    .build();
            scw.setShowcase(viewTarget, true);
        }

    }
    //public static Activity getActivitymy(){
      //  Activity activity=
        //return activity;
    //}
    public static ImageButton getNavButtonView(Toolbar toolbar) {
        for (int i = 0; i < toolbar.getChildCount(); i++)
            if (toolbar.getChildAt(i) instanceof ImageButton)
                return (ImageButton) toolbar.getChildAt(i);
        return null;
    }
    public static Context getContext() {
        return MainActivity.context;
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
                fragment = new FragAutoscan();
                toolbar.setTitle(R.string.frag1);
                color = 0xffff9800;
                //   color = R.color.colorMonitor;
                break;
            case R.id.menuping:
                fragment = new FragPing();
                toolbar.setTitle(R.string.frag2);
                color = 0xff009688;
                //  color = R.color.colorPing;
                break;
            case R.id.menulanscan:
                fragment = new FragLanscan();
                toolbar.setTitle(R.string.frag3);
                color = 0xff8bc34a;
                //  color = R.color.colorLanscan;
                break;
            case R.id.menuportscan:
                fragment= new FragPortScan();
                toolbar.setTitle("port scan");
                color = 0xff009688;
                break;
            case R.id.menuexit:
                IntentSrvs.cancelALRM(getContext());
                break;
            case R.id.menusetings:
                fragment = new FragPrefs();
                toolbar.setTitle("Settings");
                color = 0x99ffffff;
                break;
            default:
                fragment = new FragWiki();
                toolbar.setTitle(R.string.frag4);
                color = 0xff3f51b5;
                // color = R.color.colorWiki ;
                break;
        }
        if (id!=R.id.menuexit){
        toolbar.setBackgroundColor(color);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(color);
        }


        showFragment(fragment, false);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);}
        return true;

    }

    public void showFragment(Fragment fragment, boolean addToBackStack) {
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        if (addToBackStack) {
            transaction.addToBackStack(null);

        }
        transaction.commit();
        drawer.closeDrawers();
    }

    private boolean isFirstTime() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean runBefore = preferences.getBoolean("RanBefore", false);
        if (!runBefore) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("RanBefore", true);
            editor.commit();
        }
        return !runBefore;
    }
}
