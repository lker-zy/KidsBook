package com.xuewen.kidsbook.service.beans;

import android.content.ContentValues;

/**
 * Created by lker_zy on 16-4-30.
 */
public class BookCollection {
    private int id;
    private String name;
    private String author;
    private String publisher;

    private ContentValues contentValues = new ContentValues();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        contentValues.put("id", id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        contentValues.put("name", name);
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
        contentValues.put("author", author);
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
        contentValues.put("publisher", publisher);
    }

    public ContentValues getContentValues() {
        return contentValues;
    }
}
