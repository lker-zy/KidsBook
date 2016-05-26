package com.xuewen.kidsbook.ui;

import android.media.Image;
import android.os.Bundle;
import android.text.Html;
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
public class ActivityDetailActivity extends BaseActivity {
    private static String TAG = ActivityDetailActivity.class.getSimpleName();

    private String start_time;
    private String address;
    private String content;
    private String name;
    private String image_url;

    @Bind(R.id.common_title_left_btn) LinearLayout left_btn;
    @Bind(R.id.common_title_right_btn) LinearLayout right_btn;

    @Bind(R.id.activity_address) TextView addr_tv;
    @Bind(R.id.activity_time) TextView time_tv;
    @Bind(R.id.activity_content) TextView content_tv;
    @Bind(R.id.activity_name) TextView name_tv;
    @Bind(R.id.activity_detail_image) ImageView image_iv;

    private ImageLoader imageLoader;
    private DisplayImageOptions displayImageOptions;
    public void initImageLoader() {
        // 使用DisplayImageOptions.Builder()创建DisplayImageOptions
        displayImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.essence_item_image_bg) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.essence_item_image_bg) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.essence_item_image_bg) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 构建完成

        imageLoader = ImageLoader.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initImageLoader();

        Bundle bundle = this.getIntent().getExtras();
        start_time = bundle.getString("start_time");
        name = bundle.getString("name");
        content = bundle.getString("content");
        address = bundle.getString("address");
        image_url = bundle.getString("image_url");

        addr_tv.setText(address);
        name_tv.setText(name);
        content_tv.setText(Html.fromHtml(content));
        time_tv.setText(start_time);
        imageLoader.displayImage(image_url, image_iv, displayImageOptions);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail;
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
}
