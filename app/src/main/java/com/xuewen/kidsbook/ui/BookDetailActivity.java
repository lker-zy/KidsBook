package com.xuewen.kidsbook.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuewen.kidsbook.R;

public class BookDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() { return R.layout.activity_book_detail;}

    @Override
    protected void initTitleView() {
        TextView title_text = (TextView) findViewById(R.id.common_title_text);
        title_text.setText("Book: ");
        title_text.setVisibility(View.VISIBLE);

        ((LinearLayout) findViewById(R.id.common_title_left_btn)).setVisibility(View.VISIBLE);

        ImageView left_img = (ImageView) findViewById(R.id.common_title_left_btn_icon);
        left_img.setBackgroundResource(R.drawable.common_title_qr);
        left_img.setVisibility(View.VISIBLE);
    }
}
