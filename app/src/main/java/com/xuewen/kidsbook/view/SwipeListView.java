package com.xuewen.kidsbook.view;

import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xuewen.kidsbook.AppConfig;
import com.xuewen.kidsbook.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lker_zy on 16-4-15.
 */
public class SwipeListView extends LinearLayout {
    private int layoutId;
    private int itemLayoutId;

    private ListView listView;
    private SwipeRefreshLayout swipeLayout;
    private SwipeListAdapter adapter;
    private SwipeListViewListener viewListener;

    private RequestQueue requestQueue;

    public List<Map<String, Object>> getDataSet() {
        return dataSet;
    }

    public void setDataSet(List<Map<String, Object>> dataSet) {
        this.dataSet = dataSet;
        adapter.setDataSet(dataSet);
    }

    List<Map<String, Object>> dataSet;

    public SwipeListView(Context context) {
        super(context);
    }

    public SwipeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setLayoutId(int id) {
        layoutId = id;
    }
    public void setItemLayout(int id) {
        itemLayoutId = id;
    }
    public void setViewListener(SwipeListViewListener listener) {
        viewListener = listener;
    }
    public void setVolley(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public void initView() {
        inflate(getContext(), layoutId, this);

        listView = (ListView) findViewById(R.id.swipe_list_listview);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_list_swipe);

        List<Map<String, Object>> fakeData = new ArrayList<>();
        adapter = new SwipeListAdapter(fakeData);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                viewListener.onListItemClick(view, position, id);
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
                        getData();
                    }
                });
            }
        });

        swipeLayout.setProgressViewOffset(false, 0, 30);
        swipeLayout.setRefreshing(true);
        getData();
    }

    private void getData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.DAILY_BOOKS_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                swipeLayout.setRefreshing(false);

                try {
                    dataSet = viewListener.onDataLoading(0, response);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                adapter.setDataSet(dataSet);
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                swipeLayout.setRefreshing(false);

                try {
                    viewListener.onDataLoading(-1, error.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        this.requestQueue.add(stringRequest);
    }

    public class SwipeListAdapter extends BaseAdapter {
        List<Map<String, Object>> dataSet;

        public SwipeListAdapter(List<Map<String, Object>> data) {
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
            ViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(itemLayoutId, parent, false);
                holder = viewListener.onInstanceViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Map<String, Object> data = dataSet.get(position);

            holder.render(data);

            return convertView;
        }

        public void setDataSet(List<Map<String, Object>> dataSet) {
            this.dataSet = dataSet;
        }

    }

    public interface SwipeListViewListener {
        ViewHolder onInstanceViewHolder(View view);

        void onListItemClick(View view, int position, long id);

        List<Map<String, Object>> onDataLoading(int errno, String content) throws IOException;

    }
}
