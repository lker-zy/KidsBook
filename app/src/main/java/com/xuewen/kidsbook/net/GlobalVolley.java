package com.xuewen.kidsbook.net;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.xuewen.kidsbook.KidsBookApplication;

/**
 * Created by lker_zy on 16-3-7.
 */
public class GlobalVolley {
    private static RequestQueue requestQueue = null;

    public static void initialize() {
        requestQueue = Volley.newRequestQueue(KidsBookApplication.getInstance().getContext());
        requestQueue.start();
    }

    public static void add(Request request) {
        requestQueue.add(request);
    }

    public static RequestQueue requestQueue() {
        return  requestQueue;
    }

    public static void destroy() {
        requestQueue.cancelAll(null);
    }
}
