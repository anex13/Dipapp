package com.anex13.dipapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

public class FragAutoscan extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {
    ListView lv;
    Button newsrvbtn;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.autoscan, container, false);
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
       // newsrvbtn = (Button) rootView.findViewById(R.id.addsrvbutton);
        //newsrvbtn.setOnClickListener(this);
        lv = (ListView) rootView.findViewById(R.id.srvlist);
        registerForContextMenu(lv);
        fab.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(123, null, this);
        super.onActivityCreated(savedInstanceState);
    }

// создать курсор адаптер
//     https://www.javacodegeeks.com/2013/09/android-viewholder-pattern-example.html

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(getContext(), SRVContentProvider.SERVERS_CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        lv.setAdapter(new SRVadapter(getActivity(), data));
        //диствью сет адаптер (тнью май курсор адаптер (курсор))
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                //update srv state
                ((MainActivity) getActivity()).showFragment(new FragSRVAdd(), true);
                break;
            case R.id.addsrvbutton:
                //((MainActivity) getActivity()).showFragment(new FragSRVAdd(), true);
                break;
        }
    }
}
//переделать лаяут итемов листа
//фаб сделать в адд серв
//контекстное меню - удалить\редактировать\обновить
//аларм манагер
//собсна сама проверка
//pull to refresh