package com.lee.wait.database;

import java.util.Date;

/**
 * Created by Administrator on 2015/12/30.
 */
public class NoteContent {
    static String  tbContentSql = "create table if not exists tb_content(id integer primary key autoincrement , category text not null , content text , time text);";
    private int id;
    private String category;
    private String content;
    private String time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    //构造函数
    public NoteContent(){
        this("所有","","");
    };

    public NoteContent(String content,String time){
        this("所有",content,time);
    }



    public NoteContent(String category, String content, String time) {
        this.category = category;
        this.content = content;
        this.time = time;
    }

    public NoteContent(int id, String category, String content, String time) {
        this.id = id;
        this.category = category;
        this.content = content;
        this.time = time;
    }
}
