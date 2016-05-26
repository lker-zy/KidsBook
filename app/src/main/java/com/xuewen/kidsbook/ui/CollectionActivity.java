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

import com.xuewen.kidsbook.R;
import com.xuewen.kidsbook.service.EssenceCollectionService;
import com.xuewen.kidsbook.service.beans.EssenceCollection;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lker_zy on 16-5-15.
 */
public class CollectionActivity extends BaseActivity {
    private static final String TAG = CollectionActivity.class.getSimpleName();

    @Bind(R.id.common_title_left_btn) LinearLayout left_btn;

    private List<EssenceCollection> collections;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_collection;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadData();
    }

    @Override
    protected void initTitleView() {
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
    }

    private void loadData() {
        EssenceCollectionService essenceCollectionService = new EssenceCollectionService(this);
        collections = essenceCollectionService.list();

        CollectionItemAdapter adapter = new CollectionItemAdapter();
        ListView listView = (ListView) findViewById(R.id.main_list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EssenceCollection collection = collections.get(position);

                Intent intent = new Intent(CollectionActivity.this, SuggestDetailActivity.class);
                intent.putExtra("title", collection.getTitle());
                intent.putExtra("author", collection.getAuthor());
                intent.putExtra("essence_id", collection.getId());

                startActivity(intent);
            }
        });
        listView.setAdapter(adapter);
    }

    public class CollectionItemAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return collections.size();
        }

        @Override
        public Object getItem(int position) {
            return collections.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if (convertView == null) {
                convertView = LayoutInflater.from(CollectionActivity.this).inflate(R.layout.collection_list_item, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            EssenceCollection collection = collections.get(position);
            holder.title_tv.setText(collection.getTitle());

            return convertView;
        }

        class ViewHolder {
            @Bind(R.id.title) TextView title_tv;
            @Bind(R.id.book_publisher) TextView publisher_tv;
            @Bind(R.id.collection_time) TextView time_tv;

            public ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
