package com.lee.wait.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2015/12/30.
 */
public class NoteDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "notedatabase.db"; //数据库名称
    private static final int version = 1; //数据库版本
    private String tbContentSql ;
    public NoteDatabaseHelper(Context context) {
        super(context, DB_NAME, null, version);
        // TODO Auto-generated constructor stub
        tbContentSql  = NoteContent.tbContentSql;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tbContentSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }
}
