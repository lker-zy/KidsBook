package com.xuewen.kidsbook.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.xuewen.kidsbook.R;
import com.xuewen.kidsbook.ui.BookDetailActivity;
import com.xuewen.kidsbook.utils.LogUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lker_zy on 16-3-28.
 */
public class MainDefaultFrag extends Fragment {
    private final static String TAG = MainDefaultFrag.class.getSimpleName();
    private static final int MSG_LIST_REFRESH_ERROR = 3;

    @Bind(R.id.main_list_view) ListView listView;
    @Bind(R.id.swipe_container) SwipeRefreshLayout swipeLayout;

    BookItemListAdapter bookItemListAdapter = null;
    private Handler handler;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private DisplayImageOptions displayImageOptions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.act_main_default_frag, container, false);
        ButterKnife.bind(this, view);

        initImageLoader();
        initView();

        return view;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void setVolley(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public void initImageLoader() {
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

    private void initView() {
        List<Map<String, Object>> fakeData = new ArrayList<Map<String, Object>>();
        bookItemListAdapter = new BookItemListAdapter(fakeData);
        listView.setAdapter(bookItemListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> book = (Map<String, Object>) bookItemListAdapter.getItem(position);

                Intent intent = new Intent(getActivity(), BookDetailActivity.class);
                intent.putExtra("name", (String) book.get("name"));
                intent.putExtra("author", (String) book.get("author"));
                intent.putExtra("desc", (String) book.get("desc"));
                intent.putExtra("img", (String) book.get("img"));

                startActivity(intent);
            }
        });

        swipeLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
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

    private void getData() {
        String url = "http://180.76.176.227/web/dailyBooks";
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
                    handler.sendEmptyMessage(MSG_LIST_REFRESH_ERROR);
                }
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.d(TAG, " on error response for books.json: " + error.toString());
                swipeLayout.setRefreshing(false);
                handler.sendEmptyMessage(MSG_LIST_REFRESH_ERROR);
            }
        });

        LogUtil.d(TAG, "add string request: " + url);
        requestQueue.add(stringRequest);
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
            return dataSet.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.book_item_listview, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Map<String, Object> data = dataSet.get(position);

            if (data.get("title") == null) {
                holder.title.setText((String)data.get("name"));
            } else {
                holder.title.setText((String) data.get("title"));
            }
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
