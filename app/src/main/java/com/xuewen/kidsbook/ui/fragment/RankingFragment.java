package com.xuewen.kidsbook.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import com.xuewen.kidsbook.R;
import com.xuewen.kidsbook.view.ScrollGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by lker_zy on 16-3-28.
 */
public class RankingFragment extends BaseFragment {
    @Bind(R.id.act_ranking_scroll_grid_view)
    ScrollGridView rankGridView;

    private SimpleAdapter gridAdapter;
    private int[] icon = { R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher };
    private String[] iconName = { "test1", "test2", "test3"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initViews();
        return view;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ranking;
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> data_list = new ArrayList<>();

        for(int i = 0; i < 3; i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", R.drawable.ic_launcher);
            map.put("author", "test_" + i);
            data_list.add(map);
        }

        return data_list;
    }

    private void initViews() {
        gridAdapter = new SimpleAdapter(getActivity(), getData(), R.layout.book_item_large_view,
                new String[] {"image", "author"}, new int[] {R.id.book_image, R.id.book_title});
        rankGridView.setAdapter(gridAdapter);
    }

    private void dynamicAdjustGridView(ScrollGridView view) {
        SimpleAdapter adapter = (SimpleAdapter) view.getAdapter();
        if (adapter == null) {
            return;
        }

        int col = 3;

    }

}
