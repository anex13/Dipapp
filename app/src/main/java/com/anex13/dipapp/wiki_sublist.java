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
   // TextView tv;
    ListView lv;
    Bundle bundle;
    String recieveInfo;
    String text;
    String itemid;
    private String[] nix_array = {
            "00. Основные команды Bash.",
            "01. Что стоит сделать после установки.", };
    private String[] win_array = {
            "00. Полезные команды .",
            "01. Настройка прав доступа.", };
    private String[] lan_array = {
            "00. Настройки минских провайдеров .",
            "01. основные настройки роутера.",};


    private String[] mob_array = {
            "00. Настройки белоруских операторов .",
            "01. настройка интернета в Android",
            "02. настройка интернета в iOS",
            "03. настройка интернета в Windows",};

    private String[] wiki_list;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =
                inflater.inflate(R.layout.wiki_sublist, container, false);
        bundle = getArguments();
        lv = (ListView) rootView.findViewById(R.id.wikilist);
       // tv = (TextView) rootView.findViewById(R.id.textView2);

        if (bundle != null) {
            recieveInfo = bundle.getString("wiki","0_0");
        }
        switch (recieveInfo) {
            case "1":
                wiki_list = nix_array;
                break;
            case "2":
                wiki_list = win_array;
                break;
            case "3":
                wiki_list = lan_array;
                break;
            default:
                wiki_list = mob_array;
                break;
        }
        lv.setAdapter(new ArrayAdapter<> (lv.getContext(), android.R.layout.simple_list_item_1, wiki_list));
        lv.setTextFilterEnabled(true);

        //Обрабатываем щелчки на элементах ListView:
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                      public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                                          switch (position) {
                                              case 0:
                                                  itemid = recieveInfo+'0' ;
                                                  break;
                                              case 1:
                                                  itemid = recieveInfo+'1' ;
                                                  break;
                                              case 2:
                                                  itemid = recieveInfo+'2' ;
                                                  break;
                                              case 3:
                                                  itemid = recieveInfo+'3' ;
                                                  break;
                                              case 4:
                                                  itemid = recieveInfo+'4' ;
                                                  break;
                                              case 5:
                                                  itemid = recieveInfo+'5' ;
                                                  break;
                                              case 6:
                                                  itemid = recieveInfo+'6' ;
                                                  break;
                                              case 7:
                                                  itemid = recieveInfo+'7' ;
                                                  break;
                                              case 8:
                                                  itemid = recieveInfo+'8' ;
                                                  break;
                                              case 9:
                                                  itemid = recieveInfo+'9' ;
                                                  break;
                                              default:
                                                  itemid = recieveInfo+"err" ;
                                                  break;

                                          }
                                          ((MainActivity) getActivity()).showFragment( new wikiitemview(), true ,"wiki1tem",itemid);
                                      }

                                  }
                );
      //

        return rootView;
    }

    }
