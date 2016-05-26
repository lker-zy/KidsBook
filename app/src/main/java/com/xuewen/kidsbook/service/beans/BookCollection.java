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
    private String desc;
    private String category_text;
    private int category_id;
    private Long words;
    private Float price;

    private ContentValues contentValues = new ContentValues();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Long getWords() {
        return words;
    }

    public void setWords(Long words) {
        this.words = words;
    }

    public String getCategoryText() {
        return category_text;
    }

    public void setCategoryText(String category_text) {
        this.category_text = category_text;
    }

    public int getCategoryId() {
        return category_id;
    }

    public void setCategoryId(int category_id) {
        this.category_id = category_id;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public ContentValues getContentValues() {
        contentValues.put("id", getId());
        contentValues.put("publisher", getPublisher());
        contentValues.put("author", getAuthor());
        contentValues.put("name", getName());
        contentValues.put("words", getWords());
        contentValues.put("desc", getDesc());
        //contentValues.put("category_id", getCategoryId());
        //contentValues.put("category_text", getCategoryText());
        contentValues.put("price", getPrice());

        return contentValues;
    }
}
