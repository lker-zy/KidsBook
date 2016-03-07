package com.xuewen.kidsbook.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.format.Time;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.xuewen.kidsbook.R;
import com.xuewen.kidsbook.utils.LogUtil;
import com.xuewen.kidsbook.utils.UpdateHandler;
import com.xuewen.kidsbook.utils.Version;
import com.xuewen.kidsbook.zxing.Intents;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final int MSG_CHECK_UPDATE_TOAST = 1;
    private static final String TAG = MainActivity.class.getSimpleName();

    WebView mainWebView;

    @Bind(R.id.main_list_view)
    ListView listView;

    private RelativeLayout dailyLayout, studyLayout, meLayout, latestLayout, listLayout;
    private ProgressDialog pBar;

    private Handler mHandler = new receiveVersionHandler();
    private Handler cHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case MSG_CHECK_UPDATE_TOAST:
                    Toast.makeText(MainActivity.this, "检查版本更新", Toast.LENGTH_SHORT).show();
                    return;
                default:
                    break;
            }

            Bundle data = message.getData();
            String needUpdate = data.getString("needUpdate");
            if (needUpdate.equals("true")) {
                /*
                String version = data.getString("version");
                String verDesc = data.getString("verDesc");
                */
                showUpdateDialog(Version.getRemoteVersion(), Version.getVersionDesc());
            } else {
                Toast.makeText(MainActivity.this, "已经是最新版本了", Toast.LENGTH_SHORT).show();
            }
        }
    };

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
        pBar = new ProgressDialog(MainActivity.this);    //进度条，在下载的时候实时更新进度，提高用户友好度

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
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = MSG_CHECK_UPDATE_TOAST;
                cHandler.sendMessage(message);

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                message = new Message();
                Bundle data = new Bundle();
                if (UpdateHandler.isNeedUpdate()) {
                    data.putString("needUpdate", "true");
                    data.putString("version", "1.2.3");
                    data.putString("verDesc", "version(1.2.3)");
                } else {
                    data.putString("needUpdate", "false");
                }


                message.setData(data);
                cHandler.sendMessage(message);
            }
        };

        new Thread(runnable).start();
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

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

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

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, getData(), R.layout.book_item_listview,
                new String[]{"title", "author", "desc", "img"},
                new int[]{R.id.book_title, R.id.book_author, R.id.book_desc, R.id.book_img});
        listView.setAdapter(simpleAdapter);

        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeLayout.setRefreshing(false);
                    }
                }, 1500);

            }
        });
    }

    private void initWebView() {

        initTitleView();

        // Load page
        //mainWebView = (WebView) findViewById(R.id.main_web_view);
        mainWebView.requestFocus();

        final WebSettings webSettings = mainWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        if(Build.VERSION.SDK_INT >= 19) {
            webSettings.setLoadsImagesAutomatically(true);
        } else {
            webSettings.setLoadsImagesAutomatically(false);
        }

        mainWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (!webSettings.getLoadsImagesAutomatically()) {
                    webSettings.setLoadsImagesAutomatically(true);
                }
            }
        });

        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                //重新刷新页面
                mainWebView.loadUrl(mainWebView.getUrl());

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeLayout.setRefreshing(false);
                    }
                }, 1500);

            }
        });

        mainWebView.loadUrl("http://180.76.176.227/kidsbook/daily.action");
    }
    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", "book1");
        map.put("author", "google 1");
        map.put("desc", "google 1");
        map.put("img", R.drawable.ic_launcher);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "book2");
        map.put("author", "google 2");
        map.put("desc", "google 2");
        map.put("img", R.drawable.ic_launcher);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "book3");
        map.put("author", "google 3");
        map.put("desc", "google 3");
        map.put("img", R.drawable.ic_launcher);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "book3");
        map.put("author", "google 3");
        map.put("desc", "google 3");
        map.put("img", R.drawable.ic_launcher);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "book3");
        map.put("author", "google 3");
        map.put("desc", "google 3");
        map.put("img", R.drawable.ic_launcher);
        list.add(map);

        return list;
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
