package com.xuewen.kidsbook.service.beans;

/**
 * Created by lker_zy on 16-4-24.
 */
public class SearchBookItem {
    private String name;
    private String author;
    private String brief;
    private String desc;
    private String publisher;
    private String pageNum;
    private String wordsNum;

    private String imageId;
    private String imageUrl;

    public String getName() {
        return name;
    }

    public void setName(String bookName) {
        this.name = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String bookBrief) {
        this.brief = bookBrief;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPageNum() {
        return pageNum;
    }

    public void setPageNum(String pageNum) {
        this.pageNum = pageNum;
    }

    public String getWordsNum() {
        return wordsNum;
    }

    public void setWordsNum(String wordsNum) {
        this.wordsNum = wordsNum;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
