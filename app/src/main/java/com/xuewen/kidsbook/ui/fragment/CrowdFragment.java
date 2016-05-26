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
import com.xuewen.kidsbook.AppConfig;
import com.xuewen.kidsbook.R;
import com.xuewen.kidsbook.ui.CrawdApplyActivity;
import com.xuewen.kidsbook.ui.CustomSearch;
import com.xuewen.kidsbook.ui.ReportDetailActivity;
import com.xuewen.kidsbook.ui.SearchActivity;
import com.xuewen.kidsbook.view.SwipeListView;
import com.xuewen.kidsbook.view.ViewHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by lker_zy on 16-4-9.
 */
public class CrowdFragment extends BaseFragment {
    private final static String TAG = SuggestFragment.class.getSimpleName();

    private TabLayout mSearchTabLayout;
    private ViewPager mSearchViewPager;

    private LayoutInflater mInflater;

    private View view1, view2, view3;
    private List<String> mTitleList = new ArrayList<String>();
    private List<View> mViewList = new ArrayList<View>();

    private SwipeListView applySwipeListView;
    private SwipeListView reportSwipeListView;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        // init network && image

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        mSearchViewPager = (ViewPager) view.findViewById(R.id.crowd_view_pager);
        mSearchTabLayout = (TabLayout) view.findViewById(R.id.crowd_tabs);

        mInflater = LayoutInflater.from(getActivity());
        view1 = mInflater.inflate(R.layout.crowd_application_frag, null);
        view2 = mInflater.inflate(R.layout.crowd_report_frag, null);
        view3 = mInflater.inflate(R.layout.crowd_application_frag, null);

        mViewList.clear();
        mViewList.add(view1);
        mViewList.add(view2);
        mViewList.add(view3);

        mTitleList.clear();
        mTitleList.add("试读申请");
        mTitleList.add("试读报告");
        mTitleList.add("我的试读");

        mSearchTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mSearchTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mSearchTabLayout.addTab(mSearchTabLayout.newTab().setText(mTitleList.get(0)));
        mSearchTabLayout.addTab(mSearchTabLayout.newTab().setText(mTitleList.get(1)));
        mSearchTabLayout.addTab(mSearchTabLayout.newTab().setText(mTitleList.get(2)));

        MyPagerAdapter adapter = new MyPagerAdapter(mViewList);
        mSearchViewPager.setAdapter(adapter);

        mSearchTabLayout.setupWithViewPager(mSearchViewPager);
        mSearchTabLayout.setTabsFromPagerAdapter(adapter);

        initView();
        initReportView();

        return view;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_main_crawd;
    }

    class CrowdReportViewHolder extends ViewHolder {
        @Bind(R.id.report_title)
        public TextView title;

        @Bind(R.id.report_num)
        TextView report_num;

        public CrowdReportViewHolder(View view) {
            super(view);
        }

        @Override
        public void render(Object data) {
            Map<String, Object> reportData = (Map<String, Object>) data;

            title.setText("《" + reportData.get("book_name") + "》 众读报告");
            report_num.setText("已有" + reportData.get("count") + "人提交报告");
        }
    }

    class CrowdApplyViewHolder extends ViewHolder {
        @Bind(R.id.book_image)
        public ImageView img;

        @Bind(R.id.book_publisher)
        public TextView publisher;

        @Bind(R.id.apply_num)
        TextView apply_num;

        public CrowdApplyViewHolder(View view) {
            super(view);
        }

        @Override
        public void render(Object data) {
            Map<String, Object> applyData = (Map<String, Object>) data;

            publisher.setText((String) applyData.get("publisher"));
            apply_num.setText("申请人数 " + applyData.get("apply_num"));
        }
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
        applySwipeListView = setCommonSwipeListView(view1, R.id.swipe_list_layout, R.layout.crowd_application_item);
    }

    private void initReportView() {
        reportSwipeListView = setCommonSwipeListView(view2, R.id.swipe_list_layout, R.layout.crowd_report_item);
    }

    private SwipeListView setCommonSwipeListView(final View parent, int layout, int item_layout) {
        final SwipeListView swipeListView = (SwipeListView) parent.findViewById(layout);
        swipeListView.setLayoutId(R.layout.swipe_listview_view);
        swipeListView.setItemLayout(item_layout);
        swipeListView.setVolley(this.requestQueue);
        if (parent == view2) {
            swipeListView.setRequestUrl(AppConfig.REPORTS_OVERVIEW_URL);
        } else {
            swipeListView.setRequestUrl(AppConfig.LIST_CROWD_APPLY_URL);
        }

        swipeListView.setViewListener(new SwipeListView.SwipeListViewListener() {
            @Override
            public ViewHolder onInstanceViewHolder(View view) {
                if (parent == view2) {
                    CrowdReportViewHolder holder = new CrowdReportViewHolder(view);
                    return  holder;

                } else {
                    CrowdApplyViewHolder holder = new CrowdApplyViewHolder(view);
                    return  holder;
                }
            }

            @Override
            public void onListItemClick(View view, int position, long id) {
                Map<String, Object> book = swipeListView.getDataSet().get(position);

                if (parent == view2) {
                    Intent intent = new Intent(getActivity(), ReportDetailActivity.class);
                    intent.putExtra("apply_id", (int) book.get("id"));
                    intent.putExtra("book_name", (String) book.get("book_name"));
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), CrawdApplyActivity.class);
                    intent.putExtra("name", (String) book.get("book_name"));
                    intent.putExtra("author", (String) book.get("book_author"));
                    intent.putExtra("desc", (String) book.get("book_desc"));
                    intent.putExtra("publisher", (String)book.get("publisher"));
                    intent.putExtra("image_url", (String) book.get("image_url"));
                    startActivity(intent);
                }

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

                return (List<Map<String, Object>>) data.get("list");
            }
        });

        swipeListView.initView();

        return swipeListView;
    }

    /*
    class FragmentPagerAdapter extends FragmentStatePagerAdapter {
        private int pageCount;

        public FragmentPagerAdapter(FragmentManager fm, int count) {
            super(fm);

            pageCount = count;
        }

        @Override
        public Fragment getItem(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return pageCount;
        }
    }
    */

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
