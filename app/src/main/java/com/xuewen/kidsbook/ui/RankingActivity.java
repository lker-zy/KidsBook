package com.xuewen.kidsbook.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.xuewen.kidsbook.R;
import com.xuewen.kidsbook.view.ScrollGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

public class RankingActivity extends BaseActivity {
    @Bind(R.id.act_ranking_scroll_grid_view)
    ScrollGridView rankGridView;

    private SimpleAdapter gridAdapter;
    private int[] icon = { R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher };
    private String[] iconName = { "test1", "test2", "test3"};

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
        gridAdapter = new SimpleAdapter(this, getData(), R.layout.detail_book_review_grid_item,
                new String[] {"image", "author"}, new int[] {R.id.review_img, R.id.review_author});
        rankGridView.setAdapter(gridAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitleView();
        initViews();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ranking;
    }

    @Override
    protected void initTitleView() {
        TextView title_text = (TextView) findViewById(R.id.common_title_text);
        title_text.setText("总榜");
        title_text.setVisibility(View.VISIBLE);

        ((LinearLayout) findViewById(R.id.common_title_left_btn)).setVisibility(View.VISIBLE);

        ImageView left_img = (ImageView) findViewById(R.id.common_title_left_btn_icon);
        left_img.setBackgroundResource(R.drawable.commont_title_back);
        left_img.setVisibility(View.VISIBLE);

        left_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
