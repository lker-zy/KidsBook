package com.xuewen.kidsbook.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.xuewen.kidsbook.R;
import com.xuewen.kidsbook.view.CircleView;

/**
 * Created by lker_zy on 16-4-22.
 */
public class CircleActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.test_circle;
    }

    private CircleView mCircleView;

    private String[] mItemTexts = new String[] { "安全中心 ", "特色服务", "投资理财",
            "转账汇款", "我的账户", "信用卡" };
    private int[] mItemImgs = new int[] { R.drawable.search_circle_button,
            R.drawable.search_circle_button, R.drawable.search_circle_button,
            R.drawable.search_circle_button, R.drawable.search_circle_button,
            R.drawable.search_circle_button };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mCircleView = (CircleView) findViewById(R.id.id_menulayout);
        mCircleView.setMenuItemIconsAndTexts(mItemImgs, mItemTexts);



        mCircleView.setOnMenuItemClickListener(new CircleView.OnMenuItemClickListener()
        {

            @Override
            public void itemClick(View view, int pos)
            {
                Toast.makeText(CircleActivity.this, mItemTexts[pos],
                        Toast.LENGTH_SHORT).show();

            }

            @Override
            public void itemCenterClick(View view)
            {
                Toast.makeText(CircleActivity.this,
                        "you can do something just like ccb  ",
                        Toast.LENGTH_SHORT).show();

            }
        });

    }

}

