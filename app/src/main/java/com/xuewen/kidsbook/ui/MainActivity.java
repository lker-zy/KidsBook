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
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.xuewen.kidsbook.AppConfig;
import com.xuewen.kidsbook.Const;
import com.xuewen.kidsbook.R;
import com.xuewen.kidsbook.service.LoginService;
import com.xuewen.kidsbook.ui.fragment.BaseFragment;
import com.xuewen.kidsbook.ui.fragment.MainDefaultFrag;
import com.xuewen.kidsbook.ui.fragment.PersonalFragment;
import com.xuewen.kidsbook.ui.fragment.RankingFragment;
import com.xuewen.kidsbook.utils.UpdateHandler;
import com.xuewen.kidsbook.utils.Version;
import com.xuewen.kidsbook.zxing.Intents;

import java.io.File;

import butterknife.Bind;

import static com.xuewen.kidsbook.utils.Utils.isMediaOk;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private RelativeLayout dailyLayout, meLayout, latestLayout, listLayout;
    @Bind(R.id.title_bar) LinearLayout titleLayout;

    private long exitTime = 0;
    private ProgressDialog pBar;

    private RequestQueue requestQueue;

    private static final int CONTENT_FRAG_ID_MAIN       = 0;
    private static final int CONTENT_FRAG_ID_SHELF      = 1;
    private static final int CONTENT_FRAG_ID_RANK       = 2;
    private static final int CONTENT_FRAG_ID_PERSONAL   = 3;
    private BaseFragment[] contentFragment = {null, null, null, null};

    private Handler cHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case Const.MSG_CHECK_UPDATE_TOAST:
                    Toast.makeText(MainActivity.this, "检查版本更新", Toast.LENGTH_SHORT).show();
                    break;
                case Const.MSG_CHECK_UPDATE_DONE:
                    Bundle data = message.getData();
                    String needUpdate = data.getString("needUpdate");
                    if (needUpdate.equals("true")) {
                        showUpdateDialog(Version.getRemoteVersion(), Version.getVersionDesc());
                    } else {
                        Toast.makeText(MainActivity.this, "已经是最新版本了", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Const.MSG_DOWN_UPDATE_DONE:
                    pBar.cancel();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    String path = Environment.getExternalStorageDirectory()+ "/" + AppConfig.CACHE_BASE + "/KidsBook.apk";
                    intent.setDataAndType(Uri.fromFile(new File(path)),
                            "application/vnd.android.package-archive");
                    startActivity(intent);
                    break;
                case Const.MSG_LIST_REFRESH_ERROR:
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
                message.what = Const.MSG_CHECK_UPDATE_TOAST;
                cHandler.sendMessage(message);

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                message = new Message();
                message.what = Const.MSG_CHECK_UPDATE_DONE;
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
        pBar = new ProgressDialog(MainActivity.this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle("请升级APP至版本" + newVer);
        builder.setMessage(verDesc);
        builder.setCancelable(false);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isMediaOk()) {
                    UpdateHandler uHandler = new UpdateHandler(pBar, cHandler);
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

    private void changeContentFragment(int frag_id) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        BaseFragment fragment = contentFragment[frag_id];
        if (fragment == null) {

            switch (frag_id) {
                case CONTENT_FRAG_ID_MAIN:
                    fragment = new MainDefaultFrag();
                    break;
                case CONTENT_FRAG_ID_SHELF:
                    fragment = new RankingFragment();
                    break;
                case CONTENT_FRAG_ID_RANK:
                    fragment = new RankingFragment();
                    break;
                case CONTENT_FRAG_ID_PERSONAL:
                    fragment = new PersonalFragment();
                    break;
                default:
                    // TODO: Oops
                    break;
            }

            fragment.setHandler(cHandler);
            fragment.setVolley(requestQueue);

            contentFragment[frag_id] = fragment;
        }

        if (frag_id != CONTENT_FRAG_ID_PERSONAL) {
            titleLayout.setVisibility(View.VISIBLE);
        } else {
            titleLayout.setVisibility(View.GONE);
        }

        transaction.replace(R.id.main_content_frag, fragment);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dailyLayout = (RelativeLayout) findViewById(R.id.bottom_tab_daily);
        latestLayout = (RelativeLayout) findViewById(R.id.bottom_tab_latest);
        listLayout   = (RelativeLayout) findViewById(R.id.bottom_tab_list);
        meLayout = (RelativeLayout) findViewById(R.id.bottom_tab_me);

        dailyLayout.setOnClickListener(this);
        latestLayout.setOnClickListener(this);
        listLayout.setOnClickListener(this);
        meLayout.setOnClickListener(this);

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.start();

        changeContentFragment(CONTENT_FRAG_ID_MAIN);

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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
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
            case R.id.bottom_tab_daily:
                changeContentFragment(CONTENT_FRAG_ID_MAIN);
                break;
            case R.id.bottom_tab_latest:
                changeContentFragment(CONTENT_FRAG_ID_SHELF);
                break;
            case R.id.bottom_tab_list:
                changeContentFragment(CONTENT_FRAG_ID_RANK);
                break;
            case R.id.bottom_tab_me:
                if (LoginService.isLogin()) {
                    changeContentFragment(CONTENT_FRAG_ID_PERSONAL);
                } else {
                    intent = new Intent(MainActivity.this, LoginActivity.class);
                }
                break;
            case R.id.common_title_left_btn_icon:
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
