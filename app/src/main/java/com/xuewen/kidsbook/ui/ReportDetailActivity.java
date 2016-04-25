package com.xuewen.kidsbook.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuewen.kidsbook.R;
import com.xuewen.kidsbook.view.ScrollListView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lker_zy on 16-4-16.
 */
public class ReportDetailActivity extends BaseActivity {
    @Bind(R.id.content_scroll_listview) ScrollListView reportsListView;
    @Bind(R.id.top_image) ImageView topImage;
    @Bind(R.id.common_title_left_btn) LinearLayout left_btn;
    @Bind(R.id.common_title_right_btn) LinearLayout right_btn;

    class ReportsListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Object getItem(int position) {
            return null;
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

            holder.report_title.setText("xx 妈: 爱探险的孩子");
            holder.report_content.setText("  这是一本讲述探险故事的小人书\n");
            holder.report_content.append("  作者是一个探险家，也是一个父亲");
            holder.report_content.append("   这是第 " + position + "篇试读报告");

            return convertView;
        }

        class ViewHolder {
            @Bind(R.id.report_title) TextView report_title;
            @Bind(R.id.report_content) TextView report_content;

            public ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

    private void initReportsList() {
        reportsListView.setFocusable(false);
        ReportsListAdapter adapter = new ReportsListAdapter();
        reportsListView.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initReportsList();
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
}
