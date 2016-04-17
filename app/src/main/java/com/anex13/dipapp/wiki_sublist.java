package com.anex13.dipapp;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Wiki_sublist extends Fragment {
    public static final String TAG_PAGEENUM = "pageenum";
    private Pageenum pageenum;
    // TextView tv;
    ListView lv;
    Bundle bundle;
    String recieveInfo;
    String text;
    String itemid;


    private String[] wiki_list;

    public static Wiki_sublist getInstance(Pageenum pageenum) {
        Wiki_sublist fragment = new Wiki_sublist();
        Bundle bundle = new Bundle();
        bundle.putString(TAG_PAGEENUM, pageenum.name());
        fragment.setArguments(bundle);

        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =
                inflater.inflate(R.layout.wiki_sublist, container, false);
        bundle = getArguments();
        lv = (ListView) rootView.findViewById(R.id.wikilist);


        if (bundle != null) {

            pageenum = Pageenum.valueOf(bundle.getString(TAG_PAGEENUM, "1"));

        } else if (savedInstanceState != null && savedInstanceState.containsKey(TAG_PAGEENUM)) {
            pageenum = Pageenum.valueOf(savedInstanceState.getString(TAG_PAGEENUM,"1" ));
        }
        //Pageenum.WIN.name()
        wiki_list = getResources().getStringArray(pageenum.getList());
        lv.setAdapter(new ArrayAdapter<>(lv.getContext(), android.R.layout.simple_list_item_1, wiki_list));
        lv.setTextFilterEnabled(true);
        //Обрабатываем щелчки на элементах ListView:
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                      public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                                          ((MainActivity) getActivity()).showFragment(Wiki_item_view.getInstance(pageenum, position), true);
                                      }
                                  }
        );
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TAG_PAGEENUM, pageenum.name());
    }
}
