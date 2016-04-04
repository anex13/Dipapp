package com.anex13.dipapp;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class wiki_sublist extends Fragment {
    ListView lv;
    Bundle bundle;
    String recieveInfo;
    private String[] nix_array = {
            "00. Основные команды Bash.",
            "01. Что стоит сделать после установки.", };
    private String[] win_array = {
            "00. Полезные команды .",
            "01. Настройка прав доступа.", };

    private String[] wiki_list;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =
                inflater.inflate(R.layout.wiki_sublist, container, false);
        bundle = getArguments();
        lv = (ListView) rootView.findViewById(R.id.wikilist);

        if (bundle != null) {
            recieveInfo = bundle.getString("wiki","0_0");
        }
        switch (recieveInfo) {
            case "1":
                wiki_list = nix_array;
                break;
            default:
                wiki_list = win_array;
                break;
        }
        lv.setAdapter(new ArrayAdapter<> (lv.getContext(), android.R.layout.simple_list_item_1, wiki_list));
        lv.setTextFilterEnabled(true);

        //Обрабатываем щелчки на элементах ListView:
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {

            }}

            );
        return rootView;
    }

    }
