package com.xuewen.kidsbook.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuewen.kidsbook.R;

import butterknife.Bind;

/**
 * Created by lker_zy on 16-4-25.
 */
public class SuggestDetailActivity extends BaseActivity {
    private static String TAG = SuggestDetailActivity.class.getSimpleName();

    private String bookName;
    private String bookAuthor;
    private String sugContent;
    private String imageUrl;
    private String content;

    @Bind(R.id.common_title_left_btn) LinearLayout left_btn;
    @Bind(R.id.common_title_right_btn) LinearLayout right_btn;

    @Bind(R.id.title) TextView titleTextView;
    @Bind(R.id.content) TextView contentTextView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_suggest_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getIntent().getExtras();
        bookName = bundle.getString("name");
        bookAuthor = bundle.getString("author");
        content = bundle.getString("desc");

        initSimpleView();
        loadData();
    }

    @Override
    protected void initTitleView() {
        left_btn.setVisibility(View.VISIBLE);

        ImageView left_img = (ImageView) findViewById(R.id.common_title_left_btn_icon);
        left_img.setBackgroundResource(R.drawable.commont_title_back);
        left_img.setVisibility(View.VISIBLE);

        left_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        right_btn.setVisibility(View.VISIBLE);

        ImageView right_img1 = (ImageView) findViewById(R.id.common_title_right_btn_image_1);
        right_img1.setBackgroundResource(R.drawable.ic_menu_favorite);
        right_img1.setVisibility(View.VISIBLE);

        ImageView right_img2 = (ImageView) findViewById(R.id.common_title_right_btn_image_2);
        right_img2.setBackgroundResource(R.drawable.ic_menu_share);
        right_img2.setVisibility(View.VISIBLE);
    }

    private void initSimpleView() {
        titleTextView.setText(bookName);
        contentTextView.setText(content);
    }

    private void loadData() {

    }
}
