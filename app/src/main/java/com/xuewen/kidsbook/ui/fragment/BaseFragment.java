package com.xuewen.kidsbook.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;

import butterknife.ButterKnife;

/**
 * Created by lker_zy on 16-3-29.
 */
public abstract class BaseFragment extends Fragment {
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
        View view = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    abstract protected int getLayoutId();
}
