package com.xuewen.kidsbook.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xuewen.kidsbook.AppConfig;
import com.xuewen.kidsbook.KidsBookApplication;
import com.xuewen.kidsbook.net.GlobalVolley;
import com.xuewen.kidsbook.service.beans.User;
import com.xuewen.kidsbook.utils.LogUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lker_zy on 16-3-21.
 */
public class LoginService {
    private static String TAG = LoginService.class.getSimpleName();
    private static boolean isLogin = false;
    private static LoginService instance = null;

    private LoginListener loginListener = null;

    public LoginService() {
        Context context = KidsBookApplication.getInstance().getApplicationContext();

        SharedPreferences runDataPreferences = context.getSharedPreferences("RunData", Context.MODE_PRIVATE);
        isLogin = runDataPreferences.getBoolean("login_status", false);

        instance = this;
    }

    public static LoginService getInstance() {
        return instance;
    }

    public static void setLogin(boolean isLogin) {
        LoginService.isLogin = isLogin;
        KidsBookApplication.getInstance().APP_STATUS = KidsBookApplication.AppStatus.LOGIN_READLY;
    }

    public static boolean isLogin() {
        return LoginService.isLogin;
    }

    public void setLoginListener(LoginListener listener) {
        Log.i(TAG, "setLoginListener: ");
        loginListener = listener;
    }

    private void onLoginSucc(Map<String, Object> data) {
        if (loginListener != null) {
            setLogin(true);
            loginListener.onLoginSucc(data);

            Context context = KidsBookApplication.getInstance().getApplicationContext();

            SharedPreferences runDataPreferences = context.getSharedPreferences("RunData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = runDataPreferences.edit();
            editor.putString("mobilephone", "lker");
            editor.putString("userid", "lker");
            editor.putBoolean("login_status", true);
            editor.commit();
        }
    }

    private void onLoginFail(Map<String, Object> data) {
        String errMsg = (String)data.get("errmsg");

        if (loginListener != null) {
            loginListener.onLoginFail(errMsg);
        }
    }

    public void login(final User user) {
        StringRequest request = new StringRequest(Request.Method.POST, AppConfig.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        LogUtil.d(TAG, "receive registration response: " + response);

                        ObjectMapper objectMapper = new ObjectMapper();
                        Map<String, Object> data = null;
                        try {
                            data = objectMapper.readValue(response, Map.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        int errno = (int)data.get("errno");
                        if (errno == 0) {
                            onLoginSucc(data);
                        } else  {
                            onLoginFail(data);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (loginListener != null) {
                            loginListener.onLoginFail(error.toString());
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String>  map = new HashMap<>();
                map.put("id", String.valueOf(user.getId()));
                map.put("mobilephone", "15810535238");
                map.put("password", user.getPassword());

                return map;
            }
        };

        GlobalVolley.add(request);
    }

    public void logout() {
        // TODO: load user from native
        User user = new User();
        logout(user);
    }

    public void logout(final User user) {
        Context context = KidsBookApplication.getInstance().getApplicationContext();

        SharedPreferences runDataPreferences = context.getSharedPreferences("RunData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = runDataPreferences.edit();
        editor.putString("mobilephone", "lker");
        editor.putString("userid", "lker");
        editor.putBoolean("login_status", false);
        editor.commit();
    }

    public interface LoginListener {

        void onLoginSucc(Object data);

        void onLoginFail(Object data);
    }
}
