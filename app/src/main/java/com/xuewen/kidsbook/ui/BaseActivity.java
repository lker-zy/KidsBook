package com.xuewen.kidsbook.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.xuewen.kidsbook.KidsBookApplication;
import com.xuewen.kidsbook.utils.LogUtil;

import butterknife.ButterKnife;


/**
 * Created by lker_zy on 15-12-18.
 */
public abstract class BaseActivity extends Activity {
    /**
     * LOG打印标签
     */
    private static final String TAG = BaseActivity.class.getSimpleName();

    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogUtil.d(BaseActivity.TAG, "onCreate...");
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);


        int layoutId = getLayoutId();
        if (layoutId != 0) {
            setContentView(layoutId);
            // 删除窗口背景
            //getWindow().setBackgroundDrawable(null);
        }

        ButterKnife.bind(this);

        mContext = this.getApplicationContext();
        ((KidsBookApplication) this.getApplication()).addActivity(this);

        initTitleView();
    }

    protected abstract int getLayoutId();

    protected void initTitleView() {};
}
