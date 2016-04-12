package com.xuewen.kidsbook.ui;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.xuewen.kidsbook.R;
import com.xuewen.kidsbook.view.ScrollGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

public class BookDetailActivity extends BaseActivity
        implements View.OnTouchListener, View.OnClickListener {
    private static int DEFAULT_MAX_LINES = 5;
    private String bookName;
    private String bookAuthor;
    private String bookDesc;
    private String bookImgUri;

    @Bind(R.id.act_detail_scrolview)
    ScrollView detail_scroll_view;

    @Bind(R.id.act_detail_desc_more_layout)
    LinearLayout desc_layout;

    @Bind(R.id.act_detail_desc_more)
    TextView desc_more_text_view;

    @Bind(R.id.act_detail_book_info_introduction)
    TextView desc_text_view;

    @Bind(R.id.act_detail_books_review_grid_view)
    ScrollGridView books_review_grid_view;

    @Bind(R.id.act_detail_title_book_name)  TextView book_name_view;
    @Bind(R.id.act_detail_title_book_author) TextView book_author_view;

    private GestureDetector gestureDetector;
    private SimpleAdapter gridAdapter;

    private int[] icon = { R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher };
    private String[] iconName = { "test1", "test2", "test3"};

    private class DescMoreViewListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (!desc_layout.isSelected()) {
                desc_layout.setSelected(true);
                desc_more_text_view.setText("收起");
                desc_text_view.setText(bookDesc);
                desc_text_view.setMaxLines(50);
            } else {
                desc_layout.setSelected(false);
                desc_more_text_view.setText("展开");
                desc_text_view.setMaxLines(DEFAULT_MAX_LINES);
            }
        }
    }

    private void initDetailView() {
        book_name_view.setText(bookName);
        book_author_view.setText(bookAuthor);

        desc_text_view.setText(bookDesc);
        desc_text_view.setMaxLines(DEFAULT_MAX_LINES);

        desc_layout.setOnClickListener(new DescMoreViewListener());

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e2.getRawX() - e1.getRawX() > 200) {
                    finish();
                    return true;
                }

                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });

        //detail_scroll_view.setOnTouchListener(this);

        gridAdapter = new SimpleAdapter(this, getData(), R.layout.detail_book_review_grid_item,
                new String[] {"image", "author"}, new int[] {R.id.review_img, R.id.review_author});
        books_review_grid_view.setAdapter(gridAdapter);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle bundle = this.getIntent().getExtras();
        bookName = bundle.getString("name");
        bookAuthor = bundle.getString("author");
        bookDesc = bundle.getString("desc");
        bookImgUri = bundle.getString("img");

        super.onCreate(savedInstanceState);

        initDetailView();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.common_title_left_btn:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected int getLayoutId() { return R.layout.activity_book_detail;}

    @Override
    protected void initTitleView() {
        TextView title_text = (TextView) findViewById(R.id.common_title_text);
        title_text.setText("图书详情");
        title_text.setVisibility(View.VISIBLE);

        LinearLayout title_left_btn = (LinearLayout) findViewById(R.id.common_title_left_btn);
        title_left_btn.setVisibility(View.VISIBLE);
        title_left_btn.setOnClickListener(this);

        ImageView left_img = (ImageView) findViewById(R.id.common_title_left_btn_icon);
        left_img.setBackgroundResource(R.drawable.commont_title_back);
        left_img.setVisibility(View.VISIBLE);
    }
}
