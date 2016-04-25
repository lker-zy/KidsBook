package com.xuewen.kidsbook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.xuewen.kidsbook.Const;
import com.xuewen.kidsbook.R;

/**
 * Created by lker_zy on 16-3-26.
 */
public class SplashActivity extends BaseActivity {
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case Const.MSG_START_MAIN_ACTIVITY:
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getWindow().setFlags(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);  //设置全屏

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                handler.sendEmptyMessage(Const.MSG_START_MAIN_ACTIVITY);
            }
        };

        new Thread(runnable).start();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash ;
    }

}
