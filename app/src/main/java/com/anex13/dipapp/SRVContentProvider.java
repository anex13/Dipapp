package com.anex13.dipapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.content.ContentUris;
import android.content.Context;
import android.content.UriMatcher;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

/**
 * Created by namel on 06.05.2016.
 */
public class SRVContentProvider extends android.content.ContentProvider {
    static final String DB_NAME = "mydb";
    static final int DB_VERSION = 2;

    // Таблица
    static final String SRV_TABLE = "servers";

    // Поля
    static final String SERVER_ID = "_id";
    static final String SERVER_NAME = "name";
    static final String SERVER_URL = "url";
    static final String SERVER_CHKURL = "checkurl";
    static final String UPDATE_TIME = "updatetime";
    static final String SERVER_NEXT_CHECK = "nextcheck";
    static final String FAIL_NOTIFICATION = "notification";
    static final String SERVER_STATE = "state";


    // Скрипт создания таблицы
    static final String DB_CREATE = "create table " + SRV_TABLE + "("
            + SERVER_ID + " integer primary key autoincrement, "
            + SERVER_NAME + " text, " + SERVER_URL + " text," + SERVER_CHKURL
            + " text," + UPDATE_TIME + " text," + SERVER_NEXT_CHECK + " text,"
            + FAIL_NOTIFICATION + " integer,"+ SERVER_STATE + " integer" + ");";

    // // Uri
    // authority
    static final String AUTHORITY = "com.anex13.providers.SRVContentProvider";

    // path
    static final String SRV_PATH = "servers";

    // Общий Uri
    public static final Uri SERVERS_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + SRV_PATH);

    // Типы данных
    // набор строк
    static final String SERVERS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + SRV_PATH;

    // одна строка
    static final String SERVERS_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + SRV_PATH;

    //// UriMatcher
    // общий Uri
    static final int URI_SERVERS = 1;

    // Uri с указанным ID
    static final int URI_SERVER_ID = 2;

    // описание и создание UriMatcher
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, SRV_PATH, URI_SERVERS);
        uriMatcher.addURI(AUTHORITY, SRV_PATH + "/#", URI_SERVER_ID);
    }

    DBHelper dbHelper;
    SQLiteDatabase db;

    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    // чтение
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // проверяем Uri
        switch (uriMatcher.match(uri)) {
            case URI_SERVERS: // общий Uri
                // если сортировка не указана, ставим свою - по имени
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = SERVER_NAME + " ASC";
                }
                break;
            case URI_SERVER_ID: // Uri с ID
                String id = uri.getLastPathSegment();
                // добавляем ID к условию выборки
                if (TextUtils.isEmpty(selection)) {
                    selection = SERVER_ID + " = " + id;
                } else {
                    selection = selection + " AND " + SERVER_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(SRV_TABLE, projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),
                SERVERS_CONTENT_URI);
        return cursor;
    }

    public Uri insert(Uri uri, ContentValues values) {
        if (uriMatcher.match(uri) != URI_SERVERS)
            throw new IllegalArgumentException("Wrong URI: " + uri);

        db = dbHelper.getWritableDatabase();
        long rowID = db.insert(SRV_TABLE, null, values);
        Uri resultUri = ContentUris.withAppendedId(SERVERS_CONTENT_URI, rowID);
        // уведомляем ContentResolver, что данные по адресу resultUri изменились
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case URI_SERVERS:
                break;
            case URI_SERVER_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = SERVER_ID + " = " + id;
                } else {
                    selection = selection + " AND " + SERVER_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.delete(SRV_TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case URI_SERVERS:

                break;
            case URI_SERVER_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = SERVER_ID + " = " + id;
                } else {
                    selection = selection + " AND " + SERVER_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.update(SRV_TABLE, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case URI_SERVERS:
                return SERVERS_CONTENT_TYPE;
            case URI_SERVER_ID:
                return SERVERS_CONTENT_ITEM_TYPE;
        }
        return null;
    }


    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL ("DROP TABLE " + SRV_TABLE);
            onCreate(db);
        }
    }
}
