package com.xuewen.kidsbook.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xuewen.kidsbook.R;
import com.xuewen.kidsbook.ui.BookDetailActivity;
import com.xuewen.kidsbook.ui.CrawdApplyActivity;
import com.xuewen.kidsbook.ui.ReportDetailActivity;
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
        return R.layout.crawd_fragment;
    }

    class CrowdApplyViewHolder extends ViewHolder {
        @Bind(R.id.book_image)
        public ImageView img;

        public CrowdApplyViewHolder(View view) {
            super(view);
        }

        @Override
        public void render(Object data) {

        }
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
        swipeListView.setViewListener(new SwipeListView.SwipeListViewListener() {
            @Override
            public ViewHolder onInstanceViewHolder(View view) {
                CrowdApplyViewHolder holder = new CrowdApplyViewHolder(view);
                return holder;
            }

            @Override
            public void onListItemClick(View view, int position, long id) {
                Map<String, Object> book = swipeListView.getDataSet().get(position);

                if (parent == view2) {
                    Intent intent = new Intent(getActivity(), ReportDetailActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), CrawdApplyActivity.class);
                    intent.putExtra("name", (String) book.get("name"));
                    intent.putExtra("author", (String) book.get("author"));
                    intent.putExtra("desc", (String) book.get("desc"));
                    intent.putExtra("publisher", (String)book.get("puborg"));
                    intent.putExtra("image_url", (String) book.get("imageUrl"));
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

                return (List<Map<String, Object>>) data.get("books");
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
