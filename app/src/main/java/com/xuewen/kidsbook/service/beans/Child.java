package com.xuewen.kidsbook.service.beans;

import com.xuewen.kidsbook.utils.Sex;

/**
 * Created by lker_zy on 16-3-22.
 */
public class Child {
    private int age;
    private Sex sex;

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
