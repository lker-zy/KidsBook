package com.xuewen.kidsbook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xuewen.kidsbook.Const;
import com.xuewen.kidsbook.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lker_zy on 16-5-15.
 */
public class StudyActivity extends BaseActivity {
    @Bind(R.id.common_title_left_btn) LinearLayout back_btn;
    @Bind(R.id.common_title_left_btn_icon) ImageView back_icon;

    private int [] category_type_arr = new int[] {Const.CATEGORY_WENXUE, Const.CATEGORy_HUIBEN, Const.CATEGORY_BAIKE, Const.CATEGORY_TUHUASHU};
    private String [] category_name_arr = new String[] {"文学", "绘本", "百科", "图画书"};
    private int [] image_resid_arr = new int[] {R.drawable.study_ic_wenxue, R.drawable.study_ic_huiben,
            R.drawable.study_ic_baike, R.drawable.study_ic_tuhuashu};

    @Override
    protected int getLayoutId() {
        return R.layout.activity_study;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadData();
    }

    private void loadData() {
        CategoryItemAdapter adapter = new CategoryItemAdapter();
        ListView listView = (ListView) findViewById(R.id.main_list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*
                Intent intent = new Intent(StudyActivity.this, StudyDetailActivity.class);
                intent.putExtra("category", category_type_arr[position]);

                startActivity(intent);
                */
            }
        });
        listView.setAdapter(adapter);
    }

    @Override
    protected void initTitleView() {
        TextView title_text = (TextView) findViewById(R.id.common_title_text);
        title_text.setText("书房");
        title_text.setVisibility(View.VISIBLE);

        back_btn.setVisibility(View.VISIBLE);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        back_icon.setBackgroundResource(R.drawable.commont_title_back);
        back_icon.setVisibility(View.VISIBLE);
    }

    public class CategoryItemAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return category_name_arr.length;
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
            ViewHolder holder = null;

            if (convertView == null) {
                convertView = LayoutInflater.from(StudyActivity.this).inflate(R.layout.study_category_list_item, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.category_name_tv.setText(category_name_arr[position]);
            holder.image_tv.setImageResource(image_resid_arr[position]);
            holder.book_num_tv.setText("藏书" + String.valueOf(position) + "本");

            return convertView;
        }

        class ViewHolder {
            @Bind(R.id.image_iv) ImageView image_tv;
            @Bind(R.id.category_name_tv) TextView category_name_tv;
            @Bind(R.id.book_num_tv) TextView book_num_tv;

            public ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
