package com.xuewen.kidsbook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xuewen.kidsbook.R;
import com.xuewen.kidsbook.utils.LogUtil;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.active_main;
    }

    @Override
    public void onClick(View v) {
        LogUtil.d(TAG, "view is clicked");
        Intent intent =null;

        switch (v.getId()) {
            case R.id.iv_me:
                LogUtil.d(TAG, "iv_me is clicked");
                intent = new Intent(MainActivity.this, PersonalActivity.class);
                break;
            case R.id.iv_know:
                LogUtil.d(TAG, "iv_know is clicked");
                break;
            case R.id.iv_i_want_know:
                LogUtil.d(TAG, "iv_i_want_know is clicked");
                break;
            default:
                break;
        }

        if (intent != null) {
            startActivity(intent);
        }

    }
}
