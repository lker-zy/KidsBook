package com.xuewen.kidsbook.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xuewen.kidsbook.AppConfig;
import com.xuewen.kidsbook.R;
import com.xuewen.kidsbook.utils.LogUtil;
import com.xuewen.kidsbook.view.ScrollListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lker_zy on 16-4-16.
 */
public class ReportDetailActivity extends BaseActivity {
    private static String TAG = ReportDetailActivity.class.getSimpleName();
    private static final int MSG_LOAD_REPORTS_SUCC = 0;
    private static final int MSG_LOAD_REPORTS_FAIL = 1;

    @Bind(R.id.content_scroll_listview) ScrollListView reportsListView;
    @Bind(R.id.top_image) ImageView topImage;
    @Bind(R.id.common_title_left_btn) LinearLayout left_btn;
    @Bind(R.id.common_title_right_btn) LinearLayout right_btn;
    @Bind(R.id.reports_title) TextView reports_title;

    private RequestQueue requestQueue;
    private int applyId;
    private String bookName;

    private ReportsListAdapter adapter;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case MSG_LOAD_REPORTS_SUCC:
                    break;
                case MSG_LOAD_REPORTS_FAIL:
                    break;
                default:
                    break;
            }

        }

    };

    class ReportsListAdapter extends BaseAdapter {
        List<Map<String, Object>> dataSet;

        public ReportsListAdapter(List<Map<String, Object>> dataSet) {
            this.dataSet = dataSet;
        }

        public void setDataSet(List<Map<String, Object>> dataSet) {
            this.dataSet = dataSet;
        }

        @Override
        public int getCount() {
            return dataSet.size();
        }

        @Override
        public Object getItem(int position) {
            return dataSet.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(ReportDetailActivity.this).inflate(R.layout.crowd_report_detail_item, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Map<String, Object> data = dataSet.get(position);

            Spanned spannedContent = Html.fromHtml((String) data.get("content"));

            holder.report_title.setText(data.get("author") + ": " + data.get("name"));
            holder.report_content.setText(spannedContent);
            holder.report_zan_num.setText("" + data.get("zan_num"));

            return convertView;
        }

        class ViewHolder {
            @Bind(R.id.report_title) TextView report_title;
            @Bind(R.id.report_content) TextView report_content;
            @Bind(R.id.zan_num) TextView report_zan_num;

            public ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

    private void initReportsList() {
        reportsListView.setFocusable(false);
        List<Map<String, Object>> fakeData = new ArrayList<Map<String, Object>>();
        adapter = new ReportsListAdapter(fakeData);
        reportsListView.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getIntent().getExtras();
        applyId = bundle.getInt("apply_id");
        bookName = bundle.getString("book_name");
        reports_title.setText("《" + bookName + "》 众读报告");

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.start();

        initReportsList();
        loadData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_crowd_report;
    }

    @Override
    protected void initTitleView() {
        TextView title_text = (TextView) findViewById(R.id.common_title_text);
        title_text.setText("众读报告");
        title_text.setVisibility(View.VISIBLE);

        left_btn.setVisibility(View.VISIBLE);

        ImageView left_img = (ImageView) findViewById(R.id.common_title_left_btn_icon);
        left_img.setBackgroundResource(R.drawable.commont_title_back);
        left_img.setVisibility(View.VISIBLE);

        left_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        right_btn.setVisibility(View.VISIBLE);

        ImageView right_img1 = (ImageView) findViewById(R.id.common_title_right_btn_image_1);
        right_img1.setBackgroundResource(R.drawable.ic_menu_favorite);
        right_img1.setVisibility(View.VISIBLE);

        ImageView right_img2 = (ImageView) findViewById(R.id.common_title_right_btn_image_2);
        right_img2.setBackgroundResource(R.drawable.ic_menu_share);
        right_img2.setVisibility(View.VISIBLE);
    }

    private void loadData() {
        String url = AppConfig.LIST_CROWD_REPORTS_URL + "?applyId=" + applyId;
        LogUtil.d(TAG, "detail url: " + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                LogUtil.d(TAG, "response for essence detail ok: " + response);

                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    Map<String, Object> data = objectMapper.readValue(response, Map.class);
                    adapter.setDataSet((List<Map<String, Object>>) data.get("list"));

                    adapter.notifyDataSetChanged();
                } catch (IOException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(MSG_LOAD_REPORTS_FAIL);
                }
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.d(TAG, " on error response for books.json: " + error.toString());
                handler.sendEmptyMessage(MSG_LOAD_REPORTS_FAIL);
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        LogUtil.d(TAG, "add string request: " + AppConfig.LIST_CROWD_REPORTS_URL);
        requestQueue.add(stringRequest);

    }
}
