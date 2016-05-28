package com.xuewen.kidsbook.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.xuewen.kidsbook.KidsBookApplication;
import com.xuewen.kidsbook.service.beans.User;
import com.xuewen.kidsbook.utils.LogUtil;

/**
 * Created by lker_zy on 16-5-28.
 */
public class UserService {
    private static String TAG = UserService.class.getSimpleName();
    private static String TABLE = "user";

    private User userObject = new User();

    public void update(User user) {
        Context context = KidsBookApplication.getInstance().getApplicationContext();

        LogUtil.i(TAG, "update user: ");
        LogUtil.i(TAG, "       nickName: " + user.getNickname());
        LogUtil.i(TAG, "       birthday: " + user.getBirthday());

        SharedPreferences userPreferences = context.getSharedPreferences(TABLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userPreferences.edit();
        editor.putLong("userid", user.getId());
        editor.putString("mobilephone", user.getPhonenum());
        editor.putString("birthday", user.getBirthday());
        editor.putString("nickname", user.getNickname());
        editor.commit();

        load();
    }

    public void load() {
        Context context = KidsBookApplication.getInstance().getApplicationContext();

        SharedPreferences userPreferences = context.getSharedPreferences(TABLE, Context.MODE_PRIVATE);
        userObject.setBirthday(userPreferences.getString("birthday", ""));
        userObject.setId(userPreferences.getLong("id", -1));
        userObject.setNickname(userPreferences.getString("nickname", ""));

        LogUtil.i(TAG, "load   user: ");
        LogUtil.i(TAG, "       nickName: " + userObject.getNickname());
        LogUtil.i(TAG, "       birthday: " + userObject.getBirthday());
    }

    public User user() {
        return userObject;
    }
}
