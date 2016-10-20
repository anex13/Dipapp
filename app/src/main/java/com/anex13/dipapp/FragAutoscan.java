package com.anex13.dipapp;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class FragAutoscan extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {
    private static final int MENU_EDIT_SERVER =1 ;
    private static final int MENU_DEL_SERVER =2 ;
    private static final String LOGTAG ="my log" ;
    ListView lv;
    Server menusrv;
    FragSRVAdd editfrag=new FragSRVAdd();
    Bundle args=new Bundle();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.autoscan, container, false);
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        lv = (ListView) rootView.findViewById(R.id.srvlist);
        Button btn = (Button) rootView.findViewById(R.id.buttonchkitnow);
        btn.setOnClickListener(this);
        fab.setOnClickListener(this);
        registerForContextMenu(lv);
        return rootView;
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
        Cursor c  = ((Cursor) lv.getAdapter().getItem(info.position));
        menusrv=new Server(c);
        String name  = menusrv.getName();
        menu.setHeaderTitle(name);
        menu.add(Menu.NONE, MENU_EDIT_SERVER, Menu.NONE,"Edit server");
        menu.add(Menu.NONE, MENU_DEL_SERVER, Menu.NONE,"Delete server");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_EDIT_SERVER:
                args= menusrv.toBundle();
                editfrag.setArguments(args);
                ((MainActivity) getActivity()).showFragment(editfrag, true);
                return true;
            case MENU_DEL_SERVER:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                      Uri uri = ContentUris.withAppendedId(SRVContentProvider.SERVERS_CONTENT_URI, menusrv.getId());
                        getActivity().getContentResolver().delete(uri,null,null);
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
                ((MainActivity) getActivity()).showFragment(new FragSRVAdd(), true);
                break;
            case R.id.buttonchkitnow:
                IntentSrvs.checkItNow(getContext());
                break;
        }
    }
}
// TODO: 20.10.2016 пулл ту рефрэш , разобраться с эдит срв , отображать время след алярма и состояние проверки, покрутить лайаут 