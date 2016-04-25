package com.xuewen.kidsbook.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.xuewen.kidsbook.R;
import com.xuewen.kidsbook.service.SearchService;
import com.xuewen.kidsbook.service.beans.SearchBookItem;
import com.xuewen.kidsbook.service.beans.SearchResult;
import com.xuewen.kidsbook.ui.monindicator.MonIndicator;
import com.xuewen.kidsbook.utils.LogUtil;
import com.xuewen.kidsbook.view.CommonSearchView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lker_zy on 16-3-17.
 */

public class SearchActivity extends BaseActivity implements CommonSearchView.SearchViewListener {
    private static String TAG = SearchActivity.class.getSimpleName();
    /**
     * 热搜框列表adapter
     */
    private ArrayAdapter<String> hintAdapter;

    /**
     * 自动补全列表adapter
     */
    private ArrayAdapter<String> autoCompleteAdapter;

    /**
     * 搜索结果列表adapter
     */
    private SearchAdapter resultAdapter;

    /**
     * 热搜版数据
     */
    private List<String> hintData;

    /**
     * 搜索过程中自动补全数据
     */
    private List<String> autoCompleteData;

    /**
     * 搜索结果的数据
     */
    private List<SearchBookItem> resultData;

    /**
     * 默认提示框显示项的个数
     */
    private static int DEFAULT_HINT_SIZE = 4;

    /**
     * 提示框显示项的个数
     */
    private static int hintSize = DEFAULT_HINT_SIZE;

    /**
     * 设置提示框显示项的个数
     *
     * @param hintSize 提示框显示个数
     */
    public static void setHintSize(int hintSize) {
        SearchActivity.hintSize = hintSize;
    }

    @Bind(R.id.main_search_view_layout)
    CommonSearchView searchView;

    @Bind(R.id.main_lv_search_results)
    ListView lvResults;

    @Bind(R.id.loading_monindicator)
    MonIndicator loading_indicator;

    @Bind(R.id.loading_layout)
    LinearLayout loading_lay;

    private ImageLoader imageLoader;
    private DisplayImageOptions displayImageOptions;

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

    private void initViews() {
        searchView.setSearchViewListener(this);
        searchView.setTipsHintAdapter(hintAdapter);
        searchView.setAutoCompleteAdapter(autoCompleteAdapter);

        lvResults.setVisibility(View.VISIBLE);
        lvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                SearchBookItem book = (SearchBookItem) resultAdapter.getItem(position);

                Intent intent = new Intent(SearchActivity.this, BookDetailActivity.class);
                intent.putExtra("name", book.getName());
                intent.putExtra("author", book.getAuthor());
                intent.putExtra("desc", book.getDesc());
                intent.putExtra("img", book.getImageUrl());

                startActivity(intent);
            }
        });

        resultData = new ArrayList<>();
        resultData.clear();
        resultAdapter = new SearchAdapter(this, resultData, R.layout.book_item_listview);
        lvResults.setAdapter(resultAdapter);

        setHintSize(5);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initImageLoader();

        initData();
        initViews();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.main_search_layout;
    }


    /**
     * 初始化数据
     */
    private void initData() {
        //初始化热搜版数据
        getHintData();
        getAutoCompleteData(null);
    }

    private void search() {
        SearchService searchService =  new SearchService();
        searchService.setSearchServiceListener(new SearchService.SearchServiceListener() {
            @Override
            public void onSuccess(SearchResult searchResult) {
                loading_lay.setVisibility(View.GONE);
                lvResults.setVisibility(View.VISIBLE);

                List<SearchBookItem> books = searchResult.getBooks();
                if (resultData == null) {
                    resultData = new ArrayList<>();
                } else {
                    resultData.clear();
                }

                LogUtil.d(TAG, "books num: " + books.size());
                /*
                for (int i = 0; i < books.size(); i++) {
                    SearchBookItem book = books.get(i);
                    LogUtil.d(TAG, "set ImageUrl: " + book.getImageUrl());
                    resultData.add(book);
                }
                */
                resultData = books;

                resultAdapter.updateData(resultData);
                resultAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int errno, String errmsg) {
                Toast.makeText(getApplicationContext(), "search 失败", Toast.LENGTH_SHORT).show();

                loading_lay.setVisibility(View.GONE);
                lvResults.setVisibility(View.VISIBLE);
            }
        });

        searchService.search();
    }

    /**
     * 获取热搜版data 和adapter
     */
    private void getHintData() {
        hintData = new ArrayList<>(hintSize);
        for (int i = 1; i <= hintSize; i++) {
            hintData.add("热搜版" + i + "：Android自定义View");
        }
        hintAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, hintData);
    }

    /**
     * 获取自动补全data 和adapter
     */
    private void getAutoCompleteData(String text) {
        if (autoCompleteData == null) {
            //初始化
            autoCompleteData = new ArrayList<>(hintSize);
        } else {
            // 根据text 获取auto data
            autoCompleteData.clear();
            /*
            for (int i = 0, count = 0; i < dbData.size()
                    && count < hintSize; i++) {
                if (dbData.get(i).getTitle().contains(text.trim())) {
                    autoCompleteData.add(dbData.get(i).getTitle());
                    count++;
                }
            }
            */
        }
        if (autoCompleteAdapter == null) {
            autoCompleteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, autoCompleteData);
        } else {
            autoCompleteAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 当搜索框 文本改变时 触发的回调 ,更新自动补全数据
     * @param text
     */
    @Override
    public void onRefreshAutoComplete(String text) {
        //更新数据
        getAutoCompleteData(text);
    }

    /**
     * 点击搜索键时edit text触发的回调
     *
     * @param text
     */
    @Override
    public void onSearch(String text) {
        lvResults.setVisibility(View.GONE);

        loading_lay.setVisibility(View.VISIBLE);
        loading_indicator.setColors(new int[] {0xFF942909, 0xFF577B8C, 0xFF201289, 0xFF000000, 0xFF38B549});

        search();
    }

    public class SearchAdapter extends BaseAdapter {
        private Context context;
        private List<SearchBookItem> data;
        private int view_item_layout_id;

        public SearchAdapter(Context context, List<SearchBookItem> data, int layout_id) {
            this.context = context;
            this.data = data;
            this.view_item_layout_id = layout_id;
        }

        public void updateData(List<SearchBookItem> data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(this.context).inflate(this.view_item_layout_id, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            SearchBookItem itemData = this.data.get(position);

            holder.title.setText(itemData.getName());
            holder.author.setText(itemData.getAuthor());
            //holder.desc.setText(itemData.getDesc());

            imageLoader.displayImage(itemData.getImageUrl(), holder.image, displayImageOptions);

            return convertView;
        }
    }

    public class ViewHolder {
        @Bind(R.id.book_img)
        public ImageView image;

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
