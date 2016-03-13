package com.xuewen.kidsbook.ui;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.xuewen.kidsbook.R;

import butterknife.Bind;

public class BookDetailActivity extends BaseActivity implements View.OnTouchListener {
    private static int DEFAULT_MAX_LINES = 5;
    private String bookName;

    @Bind(R.id.act_detail_scrolview)
    ScrollView detail_scroll_view;

    @Bind(R.id.act_detail_desc_more_layout)
    LinearLayout desc_layout;

    @Bind(R.id.act_detail_desc_more)
    TextView desc_more_text_view;

    @Bind(R.id.act_detail_book_info_introduction)
    TextView desc_text_view;

    private GestureDetector gestureDetector;

    private class DescMoreViewListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (!desc_layout.isSelected()) {
                desc_layout.setSelected(true);
                desc_more_text_view.setText("收起");
                desc_text_view.setText("a\nb\nc\nd\ne\nf\ng\nh\ni\nj\nk\nl\nm\nn\nh\ni\nj\na\nb\nc\ne\nd\nf");
                desc_text_view.setMaxLines(50);
            } else {
                desc_layout.setSelected(false);
                desc_more_text_view.setText("展开");
                desc_text_view.setMaxLines(DEFAULT_MAX_LINES);
            }
        }
    }

    private void initDetailView() {
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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle bundle = this.getIntent().getExtras();
        bookName = bundle.getString("name");

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
    protected int getLayoutId() { return R.layout.activity_book_detail;}

    @Override
    protected void initTitleView() {
        TextView title_text = (TextView) findViewById(R.id.common_title_text);
        title_text.setText(bookName);
        title_text.setVisibility(View.VISIBLE);

        ((LinearLayout) findViewById(R.id.common_title_left_btn)).setVisibility(View.VISIBLE);

        ImageView left_img = (ImageView) findViewById(R.id.common_title_left_btn_icon);
        left_img.setBackgroundResource(R.drawable.commont_title_back);
        left_img.setVisibility(View.VISIBLE);
    }
}
