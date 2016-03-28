package com.xuewen.kidsbook.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xuewen.kidsbook.R;

/**
 * Created by lker_zy on 16-3-28.
 */
public class BottomBarFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.common_bottom_bar, container, false);
    }
}
