package com.xuewen.kidsbook.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.xuewen.kidsbook.R;

import butterknife.Bind;

/**
 * Created by lker_zy on 16-4-20.
 */
public class CrawdApplyActivity extends BaseActivity {
    private static String TAG = CrawdApplyActivity.class.getSimpleName();
    private static int DEFAULT_MAX_LINES = 5;

    private String bookName;
    private String bookAuthor;
    private String bookDesc;
    private String bookImg;
    private String bookPublisher;

    @Bind(R.id.common_title_left_btn) LinearLayout left_btn;
    @Bind(R.id.common_title_right_btn) LinearLayout right_btn;

    @Bind(R.id.act_detail_desc_more_layout) LinearLayout desc_layout;
    @Bind(R.id.act_detail_desc_more) TextView desc_more_text_view;
    @Bind(R.id.act_detail_book_info_introduction) TextView desc_text_view;

    @Bind(R.id.book_author) TextView book_author_tv;
    @Bind(R.id.book_publisher) TextView book_publisher_tv;
    @Bind(R.id.book_title) TextView book_title_tv;
    @Bind(R.id.book_img) ImageView book_img_iv;

    private ImageLoader imageLoader;
    private DisplayImageOptions displayImageOptions;

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

    public void initImageLoader() {
        // 使用DisplayImageOptions.Builder()创建DisplayImageOptions
        displayImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_launcher) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.ic_launcher) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.ic_launcher) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 构建完成

        imageLoader = ImageLoader.getInstance();
    }
    private void initDetailView() {
        book_author_tv.setText(bookAuthor);
        book_title_tv.setText(bookName);
        book_publisher_tv.setText(bookPublisher);

        desc_text_view.setText(bookDesc);
        desc_text_view.setMaxLines(DEFAULT_MAX_LINES);

        desc_layout.setOnClickListener(new DescMoreViewListener());

        imageLoader.displayImage(bookImg, book_img_iv, displayImageOptions);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.crawd_apply_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initImageLoader();

        Bundle bundle = this.getIntent().getExtras();
        bookName = bundle.getString("name");
        bookAuthor = bundle.getString("author");
        bookDesc = bundle.getString("desc");
        bookPublisher = bundle.getString("publisher");
        bookImg = bundle.getString("image_url");

        initDetailView();
    }

    @Override
    protected void initTitleView() {
        TextView title_text = (TextView) findViewById(R.id.common_title_text);
        title_text.setText("试读申请");
        title_text.setVisibility(View.VISIBLE);

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

        ImageView right_img2 = (ImageView) findViewById(R.id.common_title_right_btn_image_2);
        right_img2.setBackgroundResource(R.drawable.ic_menu_share);
        right_img2.setVisibility(View.VISIBLE);
    }
}
