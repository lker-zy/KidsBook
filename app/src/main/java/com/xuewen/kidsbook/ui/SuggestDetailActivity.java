package com.xuewen.kidsbook.ui;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.xuewen.kidsbook.AppConfig;
import com.xuewen.kidsbook.R;
import com.xuewen.kidsbook.db.EssenceCollectionDatabaseHelper;
import com.xuewen.kidsbook.utils.LogUtil;

import java.io.IOException;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by lker_zy on 16-4-25.
 */
public class SuggestDetailActivity extends BaseActivity {
    private static String TAG = SuggestDetailActivity.class.getSimpleName();
    private final static int MSG_LOAD_ESSENCE_DETAIL_SUCC = 0;
    private final static int MSG_LOAD_ESSENCE_DETAIL_FAIL = 1;
    private final static int MSG_ADD_COLLECTION_SUCC    = 2;
    private final static int MSG_ADD_COLLECTION_FAIL    = 3;

    private int bookId;
    private String bookName;
    private String bookAuthor;
    private String bookPublisher;
    private String bookImg;
    private String bookDesc;
    private String essenceId;
    private String imageUrl;
    private String content;

    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private DisplayImageOptions displayImageOptions;

    @Bind(R.id.common_title_left_btn) LinearLayout left_btn;
    @Bind(R.id.common_title_right_btn) LinearLayout right_btn;

    @Bind(R.id.title) TextView titleTextView;
    @Bind(R.id.content) TextView contentTextView;
    @Bind(R.id.image) ImageView contentImage;

    @Bind(R.id.book_author) TextView book_author_tv;
    @Bind(R.id.book_publisher) TextView book_publisher_tv;
    @Bind(R.id.book_title) TextView book_title_tv;
    @Bind(R.id.book_img) ImageView book_img_iv;
    @Bind(R.id.sug_detail_book_lay) RelativeLayout book_lay;

    @Bind(R.id.common_title_right_btn_image_1) ImageView right_img1;
    @Bind(R.id.common_title_right_btn_image_2) ImageView right_img2;

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
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case MSG_LOAD_ESSENCE_DETAIL_SUCC:
                    initBooklView();
                    //contentTextView.setText(content);
                    contentTextView.setText(Html.fromHtml(content));
                    imageLoader.displayImage(imageUrl, contentImage, displayImageOptions);
                    break;
                case MSG_LOAD_ESSENCE_DETAIL_FAIL:
                    Toast.makeText(SuggestDetailActivity.this, "加载详情失败", Toast.LENGTH_SHORT).show();
                    break;
                case MSG_ADD_COLLECTION_SUCC:
                    Toast.makeText(SuggestDetailActivity.this, "成功添加至我的收藏", Toast.LENGTH_SHORT).show();
                    break;
                case MSG_ADD_COLLECTION_FAIL:
                    Toast.makeText(SuggestDetailActivity.this, "收藏失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_suggest_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getIntent().getExtras();
        bookName = bundle.getString("title");
        bookAuthor = bundle.getString("author");
        essenceId = bundle.getString("essence_id");

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.start();

        initImageLoader();

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

        right_img1.setBackgroundResource(R.drawable.ic_menu_favorite);
        right_img1.setVisibility(View.VISIBLE);

        right_img2.setBackgroundResource(R.drawable.ic_menu_share);
        right_img2.setVisibility(View.VISIBLE);
    }

    private void addCollection() {
        EssenceCollectionDatabaseHelper dbHelper = new EssenceCollectionDatabaseHelper(this, "essence_collection", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String addSql = "INSERT INTO essence_collection(id, title, author) values(" + essenceId + ", '" +
                bookName + "', '" + bookAuthor + "')";
        LogUtil.d(TAG, "sql is: " + addSql);

        db.execSQL(addSql);
        db.close();
    }

    private void initBooklView() {
        book_author_tv.setText(bookAuthor);
        book_title_tv.setText(bookName);
        book_publisher_tv.setText(bookPublisher);


        imageLoader.displayImage(bookImg, book_img_iv, displayImageOptions);

        book_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuggestDetailActivity.this, BookDetailActivity.class);
                intent.putExtra("id", bookId);
                intent.putExtra("name", bookName);
                intent.putExtra("author", bookAuthor);
                intent.putExtra("desc", bookDesc);
                intent.putExtra("img", bookImg);

                startActivity(intent);

            }
        });

        right_img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCollection();
                handler.sendEmptyMessage(MSG_ADD_COLLECTION_SUCC);
            }
        });
    }

    private void initSimpleView() {
        titleTextView.setText(bookName);
    }

    private void loadData() {
        String url = AppConfig.ESSENCE_DETAIL_URL + "?essenceId=" + essenceId;
        LogUtil.d(TAG, "detail url: " + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                LogUtil.d(TAG, "response for essence detail ok: " + response);

                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    Map<String, Object> data = objectMapper.readValue(response, Map.class);
                    content = (String) data.get("formatText");
                    imageUrl = (String) data.get("imgUrl");

                    Map<String, Object> book = (Map<String, Object>) data.get("book");
                    bookId = (int) book.get("id");
                    bookAuthor = (String) book.get("author");
                    bookName = (String) book.get("name");
                    bookImg = (String) book.get("imageUrl");
                    bookPublisher = (String) book.get("puborg");
                    bookDesc = (String) book.get("desc");

                    handler.sendEmptyMessage(MSG_LOAD_ESSENCE_DETAIL_SUCC);
                } catch (IOException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(MSG_LOAD_ESSENCE_DETAIL_FAIL);
                }
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.d(TAG, " on error response for books.json: " + error.toString());
                handler.sendEmptyMessage(MSG_LOAD_ESSENCE_DETAIL_FAIL);
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        LogUtil.d(TAG, "add string request: " + AppConfig.DAILY_BOOKS_URL);
        requestQueue.add(stringRequest);

    }
}
