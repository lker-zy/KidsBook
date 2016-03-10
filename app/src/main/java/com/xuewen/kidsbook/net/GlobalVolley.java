package com.xuewen.kidsbook.net;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by lker_zy on 16-3-7.
 */
public class GlobalVolley {
    private static RequestQueue requestQueue = null;

    public void initialize() {
        requestQueue = Volley.newRequestQueue(null);
        requestQueue.start();
    }
}
