package com.xuewen.kidsbook.utils;

import android.content.Context;
import android.text.style.ClickableSpan;
import android.view.View;
import android.webkit.URLUtil;

/**
 * Created by lker_zy on 16-5-8.
 */
public class MyURLSpan extends ClickableSpan {
    private Context context = null;
    private String mUrl     = null;;

    public MyURLSpan(Context context,String url) {
        this.context = context;
        this.mUrl = url;
    }

    @Override
    public void onClick(View widget) {
        if (URLUtil.isNetworkUrl(mUrl)) {
        }
    }
}
