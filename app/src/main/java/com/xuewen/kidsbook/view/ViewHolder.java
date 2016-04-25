package com.xuewen.kidsbook.view;

import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by lker_zy on 16-4-15.
 */
public class ViewHolder {
    public ViewHolder(View view) {
        ButterKnife.bind(this, view);
    }

    public void render(Object data) {
    }
}
