package com.xuewen.kidsbook.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xuewen.kidsbook.R;
import com.xuewen.kidsbook.view.CircleView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lker_zy on 16-4-22.
 */
public class CustomSearch extends BaseActivity {
    private static String TAG = CustomSearch.class.getSimpleName();

    private CircleView mCircleView;

    @Bind(R.id.kw_et) EditText keyword_add_et;
    @Bind(R.id.kw_add_btn) Button keyword_add_btn;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_custom_search;
    }

    private String[] mItemTexts = new String[] { "励志", "搞笑", "鸡汤",
            "益智", "亲情", "游戏" };
    private int[] mItemImgs = new int[] { R.drawable.search_circle_button,
            R.drawable.search_circle_button, R.drawable.search_circle_button,
            R.drawable.search_circle_button, R.drawable.search_circle_button,
            R.drawable.search_circle_button };

    private void createKeywordItem(String kwText) {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        keyword_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = keyword_add_et.getText().toString();
                if ("".equals(keyword)) {
                    return;
                }

                if (mCircleView.currentItems() >= mCircleView.maxItems()) {
                    Toast.makeText(CustomSearch.this, "关键词个数达到最大限制",
                            Toast.LENGTH_SHORT).show();
                }

                mCircleView.addMenuItem(0, keyword);

                keyword_add_et.setText("");
            }
        });

        mCircleView = (CircleView) findViewById(R.id.id_menulayout);
        //mCircleView.setMenuItemIconsAndTexts(mItemImgs, mItemTexts);
        /*
        for (int i = 0; i < mItemImgs.length; ++i) {
            mCircleView.addMenuItem(mItemImgs[i], mItemTexts[i]);
        }
        */
        mCircleView.addMenuItem(0, "");

        mCircleView.setOnMenuItemClickListener(new CircleView.OnMenuItemClickListener() {

            @Override
            public void itemClick(View view, int pos) {
                Toast.makeText(CustomSearch.this, mItemTexts[pos],
                        Toast.LENGTH_SHORT).show();

            }

            @Override
            public void itemCenterClick(View view) {
                Toast.makeText(CustomSearch.this,
                        "you can do something just like ccb  ",
                        Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void initTitleView() {
        TextView title_text = (TextView) findViewById(R.id.common_title_text);
        title_text.setText("私人定制");
        title_text.setVisibility(View.VISIBLE);

        LinearLayout title_left_btn = (LinearLayout) findViewById(R.id.common_title_left_btn);
        title_left_btn.setVisibility(View.VISIBLE);
        title_left_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView left_img = (ImageView) findViewById(R.id.common_title_left_btn_icon);
        left_img.setBackgroundResource(R.drawable.commont_title_back);
        left_img.setVisibility(View.VISIBLE);
    }
}
