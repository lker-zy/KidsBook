package com.xuewen.kidsbook.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.PopupMenu;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.xuewen.kidsbook.AppConfig;
import com.xuewen.kidsbook.Const;
import com.xuewen.kidsbook.R;
import com.xuewen.kidsbook.ui.BookDetailActivity;
import com.xuewen.kidsbook.ui.CustomSearch;
import com.xuewen.kidsbook.ui.SearchActivity;
import com.xuewen.kidsbook.ui.SuggestDetailActivity;
import com.xuewen.kidsbook.utils.LogUtil;
import com.xuewen.kidsbook.zxing.Intents;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.BindDimen;
import butterknife.ButterKnife;

/**
 * Created by lker_zy on 16-3-28.
 */
public class SuggestFragment extends BaseFragment {
    private final static String TAG = SuggestFragment.class.getSimpleName();

    @Bind(R.id.main_list_view) ListView listView;
    @Bind(R.id.swipe_container) SwipeRefreshLayout swipeLayout;

    BookItemListAdapter bookItemListAdapter = null;
    private ImageLoader imageLoader;
    private DisplayImageOptions displayImageOptions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        initImageLoader();
        initView();

        return view;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_main_essence;
    }

    public void initImageLoader() {
        // 使用DisplayImageOptions.Builder()创建DisplayImageOptions
        displayImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.essence_item_image_bg) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.essence_item_image_bg) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.essence_item_image_bg) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 构建完成

        imageLoader = ImageLoader.getInstance();
    }

    @Override
    protected void initTitleView() {
        TextView title_text = (TextView) getActivity().findViewById(R.id.common_title_text);
        title_text.setText(R.string.app_name);
        title_text.setVisibility(View.VISIBLE);

        LinearLayout title_right_btn = (LinearLayout) getActivity().findViewById(R.id.common_title_right_btn);
        title_right_btn.setVisibility(View.VISIBLE);

        ImageView right_img = (ImageView) getActivity().findViewById(R.id.common_title_right_btn_image_1);
        right_img.setBackgroundResource(R.drawable.title_right_more_func);
        right_img.setVisibility(View.VISIBLE);

        final PopupMenu right_menu = new PopupMenu(getActivity(), title_right_btn);
        Menu menu = right_menu.getMenu();

        title_right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                right_menu.show();
            }
        });

        getActivity().getMenuInflater().inflate(R.menu.title_right_more, menu);
        right_menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = null;

                switch (item.getItemId()) {
                    case R.id.menu_common_search:
                        intent = new Intent(getActivity(), SearchActivity.class);
                        break;
                    case R.id.menu_dingzhi_search:
                        intent = new Intent(getActivity(), CustomSearch.class);
                        break;
                    case R.id.menu_shouqi_search:
                        intent = new Intent(getActivity(), SearchActivity.class);
                        break;
                    default:
                        break;
                }

                if (intent != null) {
                    startActivity(intent);
                }
                return false;
            }
        });
    }

    private void initView() {
        List<Map<String, Object>> fakeData = new ArrayList<Map<String, Object>>();
        bookItemListAdapter = new BookItemListAdapter(fakeData);
        listView.setAdapter(bookItemListAdapter);

        /*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> book = (Map<String, Object>) bookItemListAdapter.getItem(position);

                Intent intent = new Intent(getActivity(), SuggestDetailActivity.class);
                intent.putExtra("title", (String) book.get("title"));
                intent.putExtra("author", (String) book.get("author"));
                intent.putExtra("essence_id", (int) book.get("id"));

                startActivity(intent);
            }
        });
        */

        swipeLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                // 立即启动向server的数据请求
                // 同时， 应该用postDelayed或者Timer启动一个定时器， 防止超时
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        LogUtil.d(TAG, "onRefresh run ");
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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.ESSENCE_LIST_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                LogUtil.d(TAG, "response for books.json ok. ");
                swipeLayout.setRefreshing(false);

                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    Map<String, Object> data = objectMapper.readValue(response, Map.class);
                    bookItemListAdapter.setDataSet((List<Map<String, Object>>)data.get("list"));
                    bookItemListAdapter.notifyDataSetChanged();
                } catch (IOException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(Const.MSG_LIST_REFRESH_ERROR);
                }
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.d(TAG, " on error response for books.json: " + error.toString());
                swipeLayout.setRefreshing(false);
                handler.sendEmptyMessage(Const.MSG_LIST_REFRESH_ERROR);
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        LogUtil.d(TAG, "add string request: " + AppConfig.DAILY_BOOKS_URL);
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
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.essence_listview_item, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final Map<String, Object> data = dataSet.get(position);

            if (data.get("title") == null) {
                holder.title.setText((String)data.get("name"));
            } else {
                holder.title.setText((String) data.get("title"));
            }

            String imgUrl = (String)data.get("img");

            imageLoader.displayImage(imgUrl, holder.img, displayImageOptions);

            String rawDesc = (String)data.get("desc");
            holder.desc.setText(Html.fromHtml(rawDesc));

            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), SuggestDetailActivity.class);
                    intent.putExtra("title", (String) data.get("title"));
                    intent.putExtra("author", (String) data.get("author"));
                    intent.putExtra("essence_id", (int) data.get("id"));

                    startActivity(intent);

                }
            });

            return convertView;
        }

        public void setDataSet(List<Map<String, Object>> dataSet) {
            this.dataSet = dataSet;
        }

        class ViewHolder {

            @Bind(R.id.essence_item_image)
            public ImageView img;

            @Bind(R.id.essence_item_title)
            public TextView title;

            @Bind(R.id.essence_item_desc)
            public TextView desc;

            public ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
