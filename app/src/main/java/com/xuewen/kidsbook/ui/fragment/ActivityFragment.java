package com.xuewen.kidsbook.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xuewen.kidsbook.R;
import com.xuewen.kidsbook.ui.ActivityDetailActivity;
import com.xuewen.kidsbook.ui.CustomSearch;
import com.xuewen.kidsbook.ui.MainActivity;
import com.xuewen.kidsbook.ui.SearchActivity;
import com.xuewen.kidsbook.view.SwipeListView;
import com.xuewen.kidsbook.view.ViewHolder;
import com.xuewen.kidsbook.zxing.Intents;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by lker_zy on 16-4-20.
 */
public class ActivityFragment extends BaseFragment {
    @Bind(R.id.activity_tabs) TabLayout activity_tabs;
    @Bind(R.id.activity_view_pager) ViewPager activity_view_pager;

    private View view1, view2;
    private List<View> mViewList = new ArrayList<>();
    private List<String> mTitleList = new ArrayList<>();

    private LayoutInflater mInflater;

    private SwipeListView offline_tabview;
    private SwipeListView online_tabview;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        mInflater = LayoutInflater.from(getActivity());
        view1 = mInflater.inflate(R.layout.crowd_application_frag, null);
        view2 = mInflater.inflate(R.layout.crowd_report_frag, null);

        mViewList.clear();
        mViewList.add(view1);
        mViewList.add(view2);

        mTitleList.clear();
        mTitleList.add("线上活动");
        mTitleList.add("线下活动");

        activity_tabs.setTabMode(TabLayout.MODE_FIXED);
        activity_tabs.setTabGravity(TabLayout.GRAVITY_FILL);
        activity_tabs.addTab(activity_tabs.newTab().setText(mTitleList.get(0)));
        activity_tabs.addTab(activity_tabs.newTab().setText(mTitleList.get(1)));

        MyPagerAdapter adapter = new MyPagerAdapter(mViewList);
        activity_view_pager.setAdapter(adapter);

        activity_tabs.setupWithViewPager(activity_view_pager);
        activity_tabs.setTabsFromPagerAdapter(adapter);

        initOfflineTabView();
        initOnlineTabView();

        return view;
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

        //title_right_btn.setOnClickListener(this);

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
                    case R.id.menu_isbn_search:
                        intent = new Intent(Intents.Scan.ACTION);
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

    class CrowdApplyViewHolder extends ViewHolder {
        @Bind(R.id.activity_image)
        public ImageView img;

        public CrowdApplyViewHolder(View view) {
            super(view);
        }

        @Override
        public void render(Object data) {

        }
    }

    private void initOfflineTabView() {
        offline_tabview = setCommonSwipeListView(view2, R.id.swipe_list_layout, R.layout.activity_online_item);
    }

    private void initOnlineTabView() {
        online_tabview = setCommonSwipeListView(view1, R.id.swipe_list_layout, R.layout.activity_online_item);
    }

    private SwipeListView setCommonSwipeListView(final View parent, int layout, int item_layout) {
        final SwipeListView swipeListView = (SwipeListView) parent.findViewById(layout);
        swipeListView.setLayoutId(R.layout.swipe_listview_view);
        swipeListView.setItemLayout(item_layout);
        swipeListView.setVolley(this.requestQueue);
        swipeListView.setViewListener(new SwipeListView.SwipeListViewListener() {
            @Override
            public ViewHolder onInstanceViewHolder(View view) {
                CrowdApplyViewHolder holder = new CrowdApplyViewHolder(view);
                return holder;
            }

            @Override
            public void onListItemClick(View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ActivityDetailActivity.class);
                startActivity(intent);

            }

            @Override
            public List<Map<String, Object>> onDataLoading(int errno, String content) throws IOException {
                ObjectMapper objectMapper = new ObjectMapper();
                if (errno != 0) {
                    return new ArrayList<Map<String, Object>>();
                }

                Map<String, Object> data = objectMapper.readValue(content, Map.class);
                if (data == null) {
                    return new ArrayList<Map<String, Object>>();
                }

                return (List<Map<String, Object>>) data.get("books");
            }
        });

        swipeListView.initView();

        return swipeListView;
    }

    class MyPagerAdapter extends PagerAdapter {
        private List<View> mViewList;

        public MyPagerAdapter(List<View> mViewList) {
            this.mViewList = mViewList;
        }

        @Override
        public int getCount() {
            return mViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;//官方推荐写法
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewList.get(position));//添加页卡
            return mViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));//删除页卡
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);//页卡标题
        }

    }
}
