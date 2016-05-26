package com.xuewen.kidsbook.service.beans;

import android.content.ContentValues;

/**
 * Created by lker_zy on 16-4-30.
 */
public class EssenceCollection {
    private int id;
    private String title;
    private String author;
    private String content;
    private ContentValues contentValues = new ContentValues();

    public ContentValues getContentValues() {
        return contentValues;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        contentValues.put("id", id);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        contentValues.put("title", title);
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
        contentValues.put("author", author);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        contentValues.put("content", content);
    }
}
