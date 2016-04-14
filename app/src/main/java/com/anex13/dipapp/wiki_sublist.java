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
    public static final String TAG_PAGEENUM = "pageenum";
    private Pageenum pageenum;
    // TextView tv;
    ListView lv;
    Bundle bundle;
    String recieveInfo;
    String text;
    String itemid;


    private String[] wiki_list;

    public static wiki_sublist getInstance(Pageenum pageenum) {
        wiki_sublist fragment = new wiki_sublist();
        Bundle bundle = new Bundle();
        bundle.putString(TAG_PAGEENUM,pageenum.name());
        fragment.setArguments(bundle);

        return null;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =
                inflater.inflate(R.layout.wiki_sublist, container, false);
        bundle = getArguments();
        lv = (ListView) rootView.findViewById(R.id.wikilist);


        if (bundle != null) {

            pageenum=Pageenum.valueOf(bundle.getString(TAG_PAGEENUM,Pageenum.WIN.name()));
        }
        wiki_list =getResources().getStringArray(pageenum.getList());
        lv.setAdapter(new ArrayAdapter<>(lv.getContext(), android.R.layout.simple_list_item_1, wiki_list));
        lv.setTextFilterEnabled(true);
        //Обрабатываем щелчки на элементах ListView:
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                      public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                                          switch (position) {
                                              case 0:
                                                  itemid = recieveInfo + '0';
                                                  break;
                                              case 1:
                                                  itemid = recieveInfo + '1';
                                                  break;
                                              case 2:
                                                  itemid = recieveInfo + '2';
                                                  break;
                                              case 3:
                                                  itemid = recieveInfo + '3';
                                                  break;
                                              case 4:
                                                  itemid = recieveInfo + '4';
                                                  break;
                                              case 5:
                                                  itemid = recieveInfo + '5';
                                                  break;
                                              case 6:
                                                  itemid = recieveInfo + '6';
                                                  break;
                                              case 7:
                                                  itemid = recieveInfo + '7';
                                                  break;
                                              case 8:
                                                  itemid = recieveInfo + '8';
                                                  break;
                                              case 9:
                                                  itemid = recieveInfo + '9';
                                                  break;
                                              default:
                                                  itemid = recieveInfo + "err";
                                                  break;

                                          }
                                          ((MainActivity) getActivity()).showFragment(new wikiitemview(), true);
                                      }

                                  }
        );
        //

        return rootView;
    }

}
