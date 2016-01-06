package com.xuewen.kidsbook.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuewen.kidsbook.R;

import java.util.ArrayList;
import java.util.List;

public class MainSearchActivity extends BaseActivity {
    private TabLayout mSearchTabLayout;
    private ViewPager mSearchViewPager;

    private LayoutInflater mInflater;

    private List<String> mTitleList = new ArrayList<String>();
    private View view1, view2, view3, view4, view5;
    private List<View> mViewList = new ArrayList<View>();

    @Override
    protected void initTitleView() {
        TextView title_text = (TextView) findViewById(R.id.common_title_text);
        title_text.setText("搜索");
        title_text.setVisibility(View.VISIBLE);

        ((LinearLayout) findViewById(R.id.common_title_left_btn)).setVisibility(View.VISIBLE);

        ImageView left_img = (ImageView) findViewById(R.id.common_title_left_btn_icon);
        left_img.setBackgroundResource(R.drawable.commont_title_back);
        left_img.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitleView();

        mSearchViewPager = (ViewPager) findViewById(R.id.search_view_pager);
        mSearchTabLayout = (TabLayout) findViewById(R.id.search_tabs);

        mInflater = LayoutInflater.from(this);
        view2 = mInflater.inflate(R.layout.activity_login, null);
        view1 = mInflater.inflate(R.layout.common_title, null);

        mViewList.add(view1);
        mViewList.add(view2);

        mTitleList.add("Tab1");
        mTitleList.add("Tab2");

        mSearchTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mSearchTabLayout.addTab(mSearchTabLayout.newTab().setText(mTitleList.get(0)));
        mSearchTabLayout.addTab(mSearchTabLayout.newTab().setText(mTitleList.get(1)));

        SearchPagerAdapter adapter = new SearchPagerAdapter(mViewList);
        mSearchViewPager.setAdapter(adapter);

        mSearchTabLayout.setupWithViewPager(mSearchViewPager);
        mSearchTabLayout.setTabsFromPagerAdapter(adapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_search;
    }

    class SearchPagerAdapter extends PagerAdapter {
        private List<View> mViewList;

        public SearchPagerAdapter(List<View> mViewList) {
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


