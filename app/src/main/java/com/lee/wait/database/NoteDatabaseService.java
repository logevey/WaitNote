package com.lee.wait.database;


/**
 * Created by Administrator on 2015/12/30.
 */

import java.util.ArrayList;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.database.Cursor;


public class NoteDatabaseService {

    private NoteDatabaseHelper ndbHelper;

    // private Context context;

    public NoteDatabaseService(Context context) {
        // this.context = context;
        ndbHelper = new NoteDatabaseHelper(context);
    }

    public boolean insert(NoteContent noteContent) {
        SQLiteDatabase database = ndbHelper.getWritableDatabase();
        database.beginTransaction();

        Cursor cursor = database.rawQuery("select * from tb_content where category=?", new String[]{noteContent.getCategory()});
        if (cursor.getCount() != 0 &&noteContent.getContent() == ""){
            database.endTransaction();
            return false;
        }else{
            database.execSQL("insert into tb_content(category,content,time)values(?,?,?)",
                    new Object[]{noteContent.getCategory(), noteContent.getContent(), noteContent.getTime()});
            // database.close();可以不关闭数据库，他里面会缓存一个数据库对象，如果以后还要用就直接用这个缓存的数据库对象。但通过
            // context.openOrCreateDatabase(arg0, arg1, arg2)打开的数据库必须得关闭
            database.setTransactionSuccessful();
            database.endTransaction();
            return true;
        }
    }

    public void update(NoteContent noteContent) {
        SQLiteDatabase database = ndbHelper.getWritableDatabase();
        database.execSQL(
                "update tb_content set category=?,content=? ,time= ? where id=?",
                new Object[]{noteContent.getCategory(), noteContent.getContent(), noteContent.getTime(), noteContent.getId()});
    }

    public NoteContent find(Integer id) {
        SQLiteDatabase database = ndbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from tb_content where id=?", new String[]{String.valueOf(id)});
        if (cursor.moveToNext()) {
            return new NoteContent(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
        }
        return null;
    }

    public void delete(Integer... ids) {
        if (ids.length > 0) {
            StringBuffer sb = new StringBuffer();
            for (Integer id : ids) {
                sb.append('?').append(',');
            }
            sb.deleteCharAt(sb.length() - 1);
            SQLiteDatabase database = ndbHelper.getWritableDatabase();
            database.execSQL(
                    "delete from tb_content where id in(" + sb.toString() + ")", ids);
        }
    }
    public void delete(String strLimit) {
            SQLiteDatabase database = ndbHelper.getWritableDatabase();
            database.execSQL("delete from tb_content " +strLimit);
    }

//    public List<NoteContent> getScrollData(int startResult, int maxResult) {
//        List<NoteContent> noteContents = new ArrayList<NoteContent>();
//        SQLiteDatabase database = ndbHelper.getReadableDatabase();
//        Cursor cursor = database.rawQuery(
//                "select * from tb_content limit ?,?",
//                new String[]{String.valueOf(startResult),
//                        String.valueOf(maxResult)});
//        while (cursor.moveToNext()) {
//            noteContents.add(new NoteContent(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
//        }
//        return noteContents;
//    }


    public List<NoteContent> getScrollData(String strLimit) {
        List<NoteContent> noteContents = new ArrayList<NoteContent>();
        SQLiteDatabase database = ndbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(
                "select * from tb_content " + strLimit , null);
        while (cursor.moveToNext()) {
            noteContents.add(new NoteContent(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
        }
        return noteContents;
    }

//    // 获取分页数据，提供给SimpleCursorAdapter使用。
//    public Cursor getRawScrollData(int startResult, int maxResult) {
//        List<NoteContent> noteContents = new ArrayList<NoteContent>();
//        SQLiteDatabase database = ndbHelper.getReadableDatabase();
//        return database.rawQuery(
//                "select id as _id ,category ,content,time from tb_content order by time DESC where limit ?,?",
//                new String[]{String.valueOf(startResult),
//                        String.valueOf(maxResult)});
//    }

    // 获取分页数据，提供给SimpleCursorAdapter使用。
    public Cursor getRawScrollData(String strLimit) {
        List<NoteContent> noteContents = new ArrayList<NoteContent>();
        SQLiteDatabase database = ndbHelper.getReadableDatabase();
        return database.rawQuery("select id as _id ,category ,content,time from tb_content  " + strLimit , null);
    }


    public int getCount() {
        SQLiteDatabase database = ndbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select count(*) from tb_content", null);
        if (cursor.moveToNext()) {
            return cursor.getInt(0);
        }
        return 0;
    }
}
