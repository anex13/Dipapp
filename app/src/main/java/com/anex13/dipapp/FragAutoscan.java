package com.anex13.dipapp;

import android.app.Activity;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;

public class FragAutoscan extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener  {
    private static final int MENU_EDIT_SERVER = 1;
    private static final int MENU_DEL_SERVER = 2;
    private static final String LOGTAG = "my log";
    private Animation fabOpenAnimation,fabOpenAnimation1,fabOpenAnimation2;
    private Animation fabCloseAnimation, fabCloseAnimation1, fabCloseAnimation2;
    private static boolean menuisopen = false;
    ListView lv;
    Server menusrv;
    FragSRVAdd editfrag = new FragSRVAdd();
    Bundle args = new Bundle();
    FloatingActionButton fabmain, fabadd, fabrefresh, stopalrm;
    View viewadd,viewrefresh,viewstop;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.autoscan, container, false);

        fabmain = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fabadd = (FloatingActionButton) rootView.findViewById(R.id.fabaddsrv);
        fabrefresh = (FloatingActionButton) rootView.findViewById(R.id.fabrefresh);
        stopalrm = (FloatingActionButton) rootView.findViewById(R.id.fabstopalrm);
        fabadd.setOnClickListener(this);
        fabrefresh.setOnClickListener(this);
        stopalrm.setOnClickListener(this);
        viewadd= (View) rootView.findViewById(R.id.fabaddgroup);
        viewrefresh= (View) rootView.findViewById(R.id.fabrefreshgroup);
        viewstop= (View) rootView.findViewById(R.id.fabstopalrmgroup);
        viewadd.setVisibility(View.INVISIBLE);
        viewrefresh.setVisibility(View.INVISIBLE);
        viewstop.setVisibility(View.INVISIBLE);
        lv = (ListView) rootView.findViewById(R.id.srvlist);
        menuisopen=false;
        fabmain.setOnClickListener(this);
        registerForContextMenu(lv);
        getAnimations();
        return rootView;
    }

    private void getAnimations() {

        fabOpenAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.open_fab_menu);
        fabOpenAnimation1 = AnimationUtils.loadAnimation(getContext(), R.anim.open_fab_menu1);
        fabOpenAnimation2 = AnimationUtils.loadAnimation(getContext(), R.anim.open_fab_menu2);

        fabCloseAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.close_fab_menu);
        fabCloseAnimation1 = AnimationUtils.loadAnimation(getContext(), R.anim.close_fab_menu1);
        fabCloseAnimation2 = AnimationUtils.loadAnimation(getContext(), R.anim.close_fab_menu2);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(123, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(getContext(), SRVContentProvider.SERVERS_CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        lv.setAdapter(new SRVadapter(getActivity(), data));

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        Cursor c = ((Cursor) lv.getAdapter().getItem(info.position));
        menusrv = new Server(c);
        String name = menusrv.getName();
        menu.setHeaderTitle(name);
        menu.add(Menu.NONE, MENU_EDIT_SERVER, Menu.NONE, "Edit server");
        menu.add(Menu.NONE, MENU_DEL_SERVER, Menu.NONE, "Delete server");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_EDIT_SERVER:
                args = menusrv.toBundle();
                editfrag.setArguments(args);
                ((MainActivity) getActivity()).showFragment(editfrag, true);
                return true;
            case MENU_DEL_SERVER:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Uri uri = ContentUris.withAppendedId(SRVContentProvider.SERVERS_CONTENT_URI, menusrv.getId());
                        getActivity().getContentResolver().delete(uri, null, null);
                    }
                }).start();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                if (!menuisopen)
                    openMenu();
                else
                    closeMenu();
                break;
            case R.id.fabaddsrv:
                ((MainActivity) getActivity()).showFragment(new FragSRVAdd(), true);
                break;
            case R.id.fabrefresh:
                IntentSrvs.checkItNow(getContext());
                break;
            case R.id.fabstopalrm:
                IntentSrvs.cancelALRM(getContext());
                break;
        }
    }


    public void openMenu() {
        viewadd.startAnimation(fabOpenAnimation);
        viewrefresh.startAnimation(fabOpenAnimation1);
        viewstop.startAnimation(fabOpenAnimation2);
        menuisopen = true;
    }

    public void closeMenu() {
        viewadd.startAnimation(fabCloseAnimation2);
        viewrefresh.startAnimation(fabCloseAnimation1);
        viewstop.startAnimation(fabCloseAnimation);
        menuisopen=false;
    }

}
// TODO: 20.10.2016 пулл ту рефрэш , разобраться с эдит срв , отображать время след алярма и состояние проверки, покрутить лайаут 