package com.zrj.arbitrarynote.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/8/7.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_ARBITRARY_NOTE = "create table ArbitraryNote ("
            +"id integer primary key autoincrement,"
            +"create_time text,"
            +"modify_time text,"
            +"content text)";

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DB_ARBITRARY_NOTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
