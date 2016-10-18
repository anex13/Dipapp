package com.anex13.dipapp;

import android.database.Cursor;
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
    ListView lv;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.autoscan, container, false);
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        lv = (ListView) rootView.findViewById(R.id.srvlist);
        Button btn = (Button) rootView.findViewById(R.id.buttonchkitnow);
        btn.setOnClickListener(this);
        registerForContextMenu(lv);
        fab.setOnClickListener(this);
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
        ListView lvm=(ListView)v;
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        //int id  = ((Server) lvm.getAdapter().getItem(info.position)).getId();
        String name  = ((Server) lvm.getAdapter().getItem(info.position)).getName();
        menu.setHeaderTitle(name);
        menu.add(Menu.NONE, MENU_EDIT_SERVER, Menu.NONE,"Edit server");
        menu.add(Menu.NONE, MENU_DEL_SERVER, Menu.NONE,"Delete server");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_EDIT_SERVER:
                //AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
               // Log.d(TAG, "removing item pos=" + info.position);
               // lv.getAdapter().remove(info.position);
                return true;
            case MENU_DEL_SERVER:



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
//контекстное меню - удалить\редактировать\
//pull to refresh