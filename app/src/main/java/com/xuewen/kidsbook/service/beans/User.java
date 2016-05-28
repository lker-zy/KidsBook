package com.xuewen.kidsbook.service.beans;

/**
 * Created by lker_zy on 16-3-21.
 */
public class User {
    private Long    id;
    private String  nickname;
    private String  phonenum;
    private String  password;
    private Child   child;
    private String  birthday;
    private int     birYear;
    private int     birMonth;
    private int     birDay;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Child getChild() {
        return child;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    public int getBirYear() {
        return birYear;
    }

    public void setBirYear(int birYear) {
        this.birYear = birYear;
    }

    public int getBirMonth() {
        return birMonth;
    }

    public void setBirMonth(int birMonth) {
        this.birMonth = birMonth;
    }

    public int getBirDay() {
        return birDay;
    }

    public void setBirDay(int birDay) {
        this.birDay = birDay;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;

        String [] arr = birthday.split("\\-");

        if (arr.length != 3) {
            return;
        }

        setBirYear(Integer.parseInt(arr[0]));
        setBirMonth(Integer.parseInt(arr[1]));
        setBirDay(Integer.parseInt(arr[2]));
    }
}
