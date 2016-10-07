package com.anex13.dipapp;

import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;

import java.util.Date;

/**
 * Created by it.zavod on 07.10.2016.
 */

public class SRVChecker {
    private static Context mcontext;

    public SRVChecker(Context context) {
        this.mcontext = context;


    }

    public static void checkItNow() {

        //ручная проверка с переустановкой аларма
        // выбрать все серваки с (FAIL_NOTIFICATION != 0)
        Server[] servers = new Server[9];// todo не забыть
        for (Server srv : servers) {
            check(srv);
        }

    }

    public static void alrmCheck() {

        //запуск с таймера автоматом
        // выбрать все серваки с (FAIL_NOTIFICATION != 0)&&(SERVER_NEXT_CHECK > NOW-10sec)&&(SERVER_NEXT_CHECK < NOW+5 min)
        Server[] servers = new Server[9];// todo не забыть
        for (Server srv : servers) {
            check(srv);
        }


    }

    private static void check(Server server) {
        final Server srv = server;
        final Uri uri = ContentUris.withAppendedId(SRVContentProvider.SERVERS_CONTENT_URI, server.getId());
        //Проверить
        //записать в дб SERVER_STATE и установить SERVER_NEXT_CHECK= NOW+UPDATE_TIME


        new Thread(new Runnable() {
            @Override
            public void run() {
                mcontext.getContentResolver().update(uri, srv.toContentValues(), null, null);
            }
        }).start();


        //установить следующий аларм взять первый серв из (FAIL_NOTIFICATION != 0) и сортировать по SERVER_NEXT_CHECK
    }


}
