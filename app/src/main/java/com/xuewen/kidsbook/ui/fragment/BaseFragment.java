package com.xuewen.kidsbook.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.xuewen.kidsbook.R;

import butterknife.ButterKnife;

/**
 * Created by lker_zy on 16-3-29.
 */
public abstract class BaseFragment extends Fragment {
    protected View container_view;

    protected Handler handler;
    protected RequestQueue requestQueue;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void setVolley(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container_view = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, container_view);

        init();
        //initTitleView();

        return container_view;
    }

    protected View getBaseView() {
        return container_view;
    }

    abstract protected int getLayoutId();

    protected void init() {}

    protected void initTitleView() {}
}
