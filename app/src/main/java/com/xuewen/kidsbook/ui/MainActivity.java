package com.xuewen.kidsbook.ui;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.xuewen.kidsbook.R;
import com.xuewen.kidsbook.service.LoginService;
import com.xuewen.kidsbook.ui.fragment.MainDefaultFrag;
import com.xuewen.kidsbook.utils.UpdateHandler;
import com.xuewen.kidsbook.utils.Version;
import com.xuewen.kidsbook.zxing.Intents;

import java.io.File;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final int MSG_CHECK_UPDATE_TOAST = 1;
    private static final int MSG_CHECK_UPDATE_DONE  = 2;
    private static final int MSG_LIST_REFRESH_ERROR = 3;
    private static final String TAG = MainActivity.class.getSimpleName();

    private RelativeLayout dailyLayout, studyLayout, meLayout, latestLayout, listLayout;
    private ProgressDialog pBar;

    private RequestQueue requestQueue;

    private Handler mHandler = new Handler() {
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
    };

    private Handler cHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case MSG_CHECK_UPDATE_TOAST:
                    Toast.makeText(MainActivity.this, "检查版本更新", Toast.LENGTH_SHORT).show();
                    break;
                case MSG_CHECK_UPDATE_DONE:
                    Bundle data = message.getData();
                    String needUpdate = data.getString("needUpdate");
                    if (needUpdate.equals("true")) {
                        showUpdateDialog(Version.getRemoteVersion(), Version.getVersionDesc());
                    } else {
                        Toast.makeText(MainActivity.this, "已经是最新版本了", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case MSG_LIST_REFRESH_ERROR:
                    Toast.makeText(MainActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }

        }
    };

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
                message.what = MSG_CHECK_UPDATE_DONE;
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

    @Override
    protected void initTitleView() {
        TextView title_text = (TextView) findViewById(R.id.common_title_text);
        title_text.setText(R.string.app_name);
        title_text.setVisibility(View.VISIBLE);

        findViewById(R.id.common_title_left_btn).setVisibility(View.VISIBLE);

        ImageView left_img = (ImageView) findViewById(R.id.common_title_left_btn_icon);
        left_img.setBackgroundResource(R.drawable.common_title_qr);
        left_img.setVisibility(View.VISIBLE);

        findViewById(R.id.common_title_right_btn).setVisibility(View.VISIBLE);

        ImageView right_img = (ImageView) findViewById(R.id.common_title_right_btn_image_1);
        right_img.setBackgroundResource(R.drawable.commont_search_icon);
        right_img.setVisibility(View.VISIBLE);

        left_img.setOnClickListener(this);
        right_img.setOnClickListener(this);
    }

    private void setDefaultFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        MainDefaultFrag main_frag = new MainDefaultFrag();
        main_frag.setHandler(cHandler);
        main_frag.setVolley(requestQueue);
        transaction.replace(R.id.main_content_frag, main_frag);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dailyLayout = (RelativeLayout) findViewById(R.id.bottom_tab_daily);
        //studyLayout = (RelativeLayout) findViewById(R.id.bottom_tab_study);
        latestLayout = (RelativeLayout) findViewById(R.id.bottom_tab_latest);
        listLayout   = (RelativeLayout) findViewById(R.id.bottom_tab_list);
        meLayout = (RelativeLayout) findViewById(R.id.bottom_tab_me);

        dailyLayout.setOnClickListener(this);
        //studyLayout.setOnClickListener(this);
        latestLayout.setOnClickListener(this);
        listLayout.setOnClickListener(this);
        meLayout.setOnClickListener(this);

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.start();

        setDefaultFragment();

        /***** 为了允许在主线程中进行网络操作 ******/
        /*
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        */

        updateApp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        requestQueue.cancelAll(null);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()) {
            /*
            case R.id.bottom_tab_daily:
                intent = new Intent(MainActivity.this, PersonalActivity.class);
                break;
            case R.id.bottom_tab_study:
                intent = new Intent(MainActivity.this, MainSearchActivity.class);
                break;
            */
            case R.id.bottom_tab_latest:
            case R.id.bottom_tab_list:
                intent = new Intent(MainActivity.this, RankingActivity.class);
                break;
            case R.id.bottom_tab_me:
                if (LoginService.isLogin()) {
                    intent = new Intent(MainActivity.this, PersonalActivity.class);
                } else {
                    //intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent = new Intent(MainActivity.this, PersonalActivity.class);
                }
                break;
            case R.id.common_title_left_btn_icon:
                //intent = new Intent(MainActivity.this, CaptureActivity.class);
                intent = new Intent(Intents.Scan.ACTION);
                break;
            case R.id.common_title_right_btn_image_1:
                intent = new Intent(MainActivity.this, SearchActivity.class);
                break;
            default:
                break;
        }

        if (intent != null) {
            startActivity(intent);
        }
    }
}
