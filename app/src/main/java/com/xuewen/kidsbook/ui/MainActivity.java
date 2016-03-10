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
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.xuewen.kidsbook.R;
import com.xuewen.kidsbook.utils.LogUtil;
import com.xuewen.kidsbook.utils.UpdateHandler;
import com.xuewen.kidsbook.utils.Version;
import com.xuewen.kidsbook.zxing.Intents;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final int MSG_CHECK_UPDATE_TOAST = 1;
    private static final int MSG_CHECK_UPDATE_DONE  = 2;
    private static final int MSG_LIST_REFRESH_ERROR = 3;
    private static final String TAG = MainActivity.class.getSimpleName();

    WebView mainWebView;

    @Bind(R.id.main_list_view)
    ListView listView;

    @Bind(R.id.swipe_container)
    SwipeRefreshLayout swipeLayout;

    private RelativeLayout dailyLayout, studyLayout, meLayout, latestLayout, listLayout;
    private ProgressDialog pBar;

    private ImageLoader imageLoader;
    private DisplayImageOptions displayImageOptions;
    private RequestQueue requestQueue;

    BookItemListAdapter bookItemListAdapter = null;

    public void initAdapter() {
        // 使用DisplayImageOptions.Builder()创建DisplayImageOptions
        displayImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_launcher) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.ic_launcher) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.ic_launcher) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 构建完成

        imageLoader = ImageLoader.getInstance();

    }
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
    private Handler mHandler = new receiveVersionHandler();

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

        List<Map<String, Object>> fakeData = new ArrayList<Map<String, Object>>();

        bookItemListAdapter = new BookItemListAdapter(fakeData);
        listView.setAdapter(bookItemListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Intents.Scan.ACTION);
                startActivity(intent);
            }
        });

        swipeLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                /*
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        swipeLayout.setRefreshing(false);
                        bookItemListAdapter.setDataSet(getData());
                        bookItemListAdapter.notifyDataSetChanged();
                    }
                }, 1500);
                */

                // 立即启动向server的数据请求
                // 同时， 应该用postDelayed或者Timer启动一个定时器， 防止超时
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        LogUtil.d(TAG, "onRefresh run xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                        getData();
                    }
                });
            }
        });
        //swipeLayout.setProgressViewOffset(false, 0, CommonUtil.dip2px(context, 24));
        swipeLayout.setProgressViewOffset(false, 0, 30);
        swipeLayout.setRefreshing(true);
        getData();
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

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.start();

        initAdapter();
        initView();

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
        requestQueue.start();
    }

    private void getData() {

        /*
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", "book1");
        map.put("author", "google 1");
        map.put("desc", "google 1");
        //map.put("img", R.drawable.ic_launcher);
        map.put("img", "http://img3x8.ddimg.cn/20/14/22489058-1_l_1.jpg");
        list.add(map);
        */

        String url = "http://180.76.176.227/web/assets/books.json";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                LogUtil.d(TAG, "response for books.json: " + response);
                swipeLayout.setRefreshing(false);

                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    Map<String, Object> data = objectMapper.readValue(response, Map.class);
                    bookItemListAdapter.setDataSet((List<Map<String, Object>>)data.get("books"));
                    bookItemListAdapter.notifyDataSetChanged();
                } catch (IOException e) {
                    e.printStackTrace();
                    cHandler.sendEmptyMessage(MSG_LIST_REFRESH_ERROR);
                }
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.d(TAG, " on error response for books.json: " + error.toString());
                swipeLayout.setRefreshing(false);
                cHandler.sendEmptyMessage(MSG_LIST_REFRESH_ERROR);
            }
        });

        LogUtil.d(TAG, "add string request: " + url);
        requestQueue.add(stringRequest);
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

    public class BookItemListAdapter extends BaseAdapter {
        List<Map<String, Object>> dataSet;

        public BookItemListAdapter(List<Map<String, Object>> data) {
            dataSet = data;
        }

        @Override
        public int getCount() {
            return dataSet.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if (convertView == null) {
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.book_item_listview, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Map<String, Object> data = dataSet.get(position);

            holder.title.setText((String)data.get("title"));
            holder.author.setText((String)data.get("author"));
            holder.desc.setText((String)data.get("desc"));

            String imgUrl = (String)data.get("img");

            imageLoader.displayImage(imgUrl, holder.img, displayImageOptions);

            return convertView;
        }

        public void setDataSet(List<Map<String, Object>> dataSet) {
            this.dataSet = dataSet;
        }

        class ViewHolder {
            @Bind(R.id.book_img)
            public ImageView img;

            @Bind(R.id.book_title)
            public TextView title;

            @Bind(R.id.book_author)
            public TextView author;

            @Bind(R.id.book_desc)
            public TextView desc;

            public ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
