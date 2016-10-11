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

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.autoscan, container, false);
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        lv = (ListView) rootView.findViewById(R.id.srvlist);
        Button btn = (Button) rootView.findViewById(R.id.buttonchkitnow) ;
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
// TODO: 23.09.2016 допилить наконецто это гавно!!!!!
// создать курсор адаптер
//     https://www.javacodegeeks.com/2013/09/android-viewholder-pattern-example.html

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
    public void onClick(View v) {switch (v.getId()){
        case R.id.fab:
        ((MainActivity) getActivity()).showFragment(new FragSRVAdd(), true);
        break;
        case R.id.buttonchkitnow:
            SRVChecker.checkItNow();
            break;
    }
    }
}
//контекстное меню - удалить\редактировать\обновить
//аларм манагер
//собсна сама проверка
//pull to refresh