package com.xuewen.kidsbook.service;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xuewen.kidsbook.net.GlobalVolley;
import com.xuewen.kidsbook.service.beans.User;
import com.xuewen.kidsbook.utils.LogUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lker_zy on 16-3-21.
 */
public class LoginService {
    private static String TAG = LoginService.class.getSimpleName();
    private static String login_url = "http://180.76.176.277/user/login";
    private static boolean isLogin = false;

    private LoginListener loginListener = null;

    public static void setLogin(boolean isLogin) {
        LoginService.isLogin = isLogin;
    }

    public static boolean isLogin() {
        return LoginService.isLogin;
    }

    public void setLoginListener(LoginListener listener) {
        Log.i(TAG, "setLoginListener: ");
        loginListener = listener;
    }

    public void login(final User user) {
        StringRequest request = new StringRequest(Request.Method.POST, login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        LogUtil.d(TAG, "receive registration response");

                        setLogin(true);
                        if (loginListener != null) {
                            loginListener.onLoginSucc(response);
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
                map.put("password", user.getPassword());

                return map;
            }
        };

        GlobalVolley.add(request);
    }

    public interface LoginListener {

        void onLoginSucc(Object data);

        void onLoginFail(Object data);
    }
}
