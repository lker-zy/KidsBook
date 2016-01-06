package com.xuewen.kidsbook.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xuewen.kidsbook.R;
import com.xuewen.kidsbook.utils.LogUtil;
import com.xuewen.kidsbook.utils.UpdateHandler;
import com.xuewen.kidsbook.utils.Version;
import com.xuewen.kidsbook.zxing.CaptureActivity;
import com.xuewen.kidsbook.zxing.Intents;

import java.io.File;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private RelativeLayout dailyLayout, studyLayout, meLayout, latestLayout, listLayout;
    private ProgressDialog pBar;

    private Handler mHandler = new receiveVersionHandler();

    public class receiveVersionHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {

            if(msg.arg1 == 100){
                pBar.cancel();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                String path = Environment.getExternalStorageDirectory()+"/KidsBook.apk";
                intent.setDataAndType(Uri.fromFile(new File(path)),
                        "application/vnd.android.package-archive");
                startActivity(intent);
            }
        }

    }

    private void showUpdateDialog(String newVer, String verDesc) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle("请升级APP至版本" + newVer);
        builder.setMessage(verDesc);
        builder.setCancelable(false);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    UpdateHandler uHandler = new UpdateHandler(pBar, mHandler);
                    uHandler.downFile("KidsBook.apk");
                } else {
                    Toast.makeText(MainActivity.this, "SD卡不可用，请插入SD卡",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }

        });
        builder.create().show();
    }


    private void updateApp() {
        pBar = new ProgressDialog(MainActivity.this);    //进度条，在下载的时候实时更新进度，提高用户友好度

        Toast.makeText(MainActivity.this, "检查版本更新", Toast.LENGTH_SHORT).show();
        if (UpdateHandler.isNeedUpdate()) {
            showUpdateDialog(Version.getRemoteVersion(), "VersionDesc");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dailyLayout = (RelativeLayout) findViewById(R.id.bottom_tab_daily);
        studyLayout = (RelativeLayout) findViewById(R.id.bottom_tab_study);
        latestLayout = (RelativeLayout) findViewById(R.id.bottom_tab_latest);
        listLayout   = (RelativeLayout) findViewById(R.id.bottom_tab_list);
        meLayout = (RelativeLayout) findViewById(R.id.bottom_tab_me);

        dailyLayout.setOnClickListener(this);
        studyLayout.setOnClickListener(this);
        latestLayout.setOnClickListener(this);
        listLayout.setOnClickListener(this);
        meLayout.setOnClickListener(this);

        initView();

        updateApp();
    }

    @Override
    protected void initTitleView() {
        TextView title_text = (TextView) findViewById(R.id.common_title_text);
        title_text.setText("最美童书馆");
        title_text.setVisibility(View.VISIBLE);

        ((LinearLayout) findViewById(R.id.common_title_left_btn)).setVisibility(View.VISIBLE);

        ImageView left_img = (ImageView) findViewById(R.id.common_title_left_btn_icon);
        left_img.setBackgroundResource(R.drawable.common_title_qr);
        left_img.setVisibility(View.VISIBLE);

        ((LinearLayout) findViewById(R.id.common_title_right_btn)).setVisibility(View.VISIBLE);

        ImageView right_img = (ImageView) findViewById(R.id.common_title_right_btn_image_1);
        right_img.setBackgroundResource(R.drawable.commont_search_icon);
        right_img.setVisibility(View.VISIBLE);

        left_img.setOnClickListener(this);
        right_img.setOnClickListener(this);
    }

    private void initView() {

        initTitleView();

        // Load page
        WebView mainWebView = (WebView) findViewById(R.id.main_web_view);

        WebSettings webSettings = mainWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mainWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        //mainWebView.loadUrl("http://www.baidu.com");
        mainWebView.loadUrl("file:///android_asset/daily.html");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onClick(View v) {
        LogUtil.d(TAG, "view is clicked");
        Intent intent = null;

        boolean isLogin = false;

        switch (v.getId()) {
            case R.id.bottom_tab_daily:
                LogUtil.d(TAG, "iv_me is clicked");
                intent = new Intent(MainActivity.this, PersonalActivity.class);
                break;
            case R.id.bottom_tab_study:
                LogUtil.d(TAG, "iv_know is clicked");
                intent = new Intent(MainActivity.this, MainSearchActivity.class);
                break;
            case R.id.bottom_tab_me:
                LogUtil.d(TAG, "iv_i_want_know is clicked");
                if (isLogin) {
                    intent = new Intent(MainActivity.this, SettingActivity.class);
                } else {
                    intent = new Intent(MainActivity.this, LoginActivity.class);
                }
                break;
            case R.id.bottom_tab_latest:
            case R.id.bottom_tab_list:
                intent = new Intent(MainActivity.this, ListAllActivity.class);
                break;
            case R.id.common_title_left_btn_icon:
                //intent = new Intent(MainActivity.this, CaptureActivity.class);
                intent = new Intent(Intents.Scan.ACTION);
                break;
            case R.id.common_title_right_btn_image_1:
                LogUtil.d(TAG, "iv_i_want_know is clicked");
                intent = new Intent(MainActivity.this, MainSearchActivity.class);
                break;
            default:
                break;
        }

        if (intent != null) {
            startActivity(intent);
        }
    }
}
