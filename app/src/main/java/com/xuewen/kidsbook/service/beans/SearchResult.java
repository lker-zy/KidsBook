package com.xuewen.kidsbook.service.beans;

import java.util.List;

/**
 * Created by lker_zy on 16-4-24.
 */
public class SearchResult {
    private int errno;
    private String errmsg;

    private int num;
    private int total;

    private List<SearchBookItem> books;

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<SearchBookItem> getBooks() {
        return books;
    }

    public void setBooks(List<SearchBookItem> books) {
        this.books = books;
    }
}
