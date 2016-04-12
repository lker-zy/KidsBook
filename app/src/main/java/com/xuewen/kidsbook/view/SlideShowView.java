package com.xuewen.kidsbook.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuewen.kidsbook.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by lker_zy on 16-4-9.
 */
public class SlideShowView extends FrameLayout {
    private int currentItem = 0;
    private static boolean isAutoPlay = true;

    private List<View> dotViewsList = new ArrayList<>();
    private List<View> pageViewsList = new ArrayList<>();
    private TextView pageDescTextView;

    private ScheduledExecutorService scheduledExecutorService;

    private ViewPager viewPager;
    private LinearLayout dotLayout;

    public SlideShowView(Context context) {
        //super(context);
        this(context, null);
    }

    public SlideShowView(Context context, AttributeSet attrs) {
        //super(context, attrs);
        this(context, attrs, 0);
    }

    public SlideShowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);

            viewPager.setCurrentItem(currentItem);
        }

    };

    private PagerAdapter pagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return pageViewsList.size();
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager)container).removeView(pageViewsList.get(position));
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(View container, int position) {
            // set View
            ((ViewPager)container).addView(pageViewsList.get(position));
            return pageViewsList.get(position);
        }
    };

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {

        boolean isAutoPlay = false;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currentItem = position;

            for(int i = 0; i < dotViewsList.size(); i++){
                if(i == position) {
                    dotViewsList.get(i).setBackgroundResource(R.drawable.dot_focused);
                }else {
                    dotViewsList.get(i).setBackgroundResource(R.drawable.dot_normal);
                }
            }

            pageDescTextView.setText("测试:  text - " + position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

            switch (state) {
                case 1:// 手势滑动，空闲中
                    isAutoPlay = false;
                    break;
                case 2:// 界面切换中
                    isAutoPlay = true;
                    break;
                case 0:// 滑动结束，即切换完毕或者加载完毕
                    // 当前为最后一张，此时从右向左滑，则切换到第一张
                    if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !isAutoPlay) {
                        viewPager.setCurrentItem(0);
                    }
                    // 当前为第一张，此时从左向右滑，则切换到最后一张
                    else if (viewPager.getCurrentItem() == 0 && !isAutoPlay) {
                        viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 1);
                    }
                    break;
            }
        }
    };

    public void init() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        LayoutInflater.from(getContext()).inflate(R.layout.common_slide_show_view, this, true);
        dotLayout = (LinearLayout) findViewById(R.id.dotLayout);
        dotLayout.removeAllViews();
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        pageDescTextView = (TextView) findViewById(R.id.view_desc_text);

        TextView view = new TextView(getContext());
        view.setGravity(Gravity.CENTER);
        view.setText("view 1");
        pageViewsList.add(view);

        TextView view2 = new TextView(getContext());
        view2.setGravity(Gravity.CENTER);
        view2.setText("view 2");
        pageViewsList.add(view2);

        TextView view3 = new TextView(getContext());
        view3.setGravity(Gravity.CENTER);
        view3.setText("view 3");
        pageViewsList.add(view3);
    }

    public void load() {
        for (int i = 0; i < pageViewsList.size(); ++i) {
            LinearLayout dotViewLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.view_pager_indicator, null);
            View dotView = dotViewLayout.findViewById(R.id.dot);

            if (i == 0) {
                dotView.setBackgroundResource(R.drawable.dot_focused);
            } else {
                dotView.setBackgroundResource(R.drawable.dot_normal);
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 4;
            params.rightMargin = 4;
            dotLayout.addView(dotViewLayout, params);
            dotViewsList.add(dotView);
        }

        viewPager.setFocusable(true);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(pageChangeListener);

        if (isAutoPlay) {
            startPlay();
        }
    }

    private void startPlay() {
        scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, 4, TimeUnit.SECONDS);
    }

    private void stopPlay() {
        scheduledExecutorService.shutdown();
    }

    // cron task to play
    private class SlideShowTask implements Runnable {

        @Override
        public void run() {
            synchronized (viewPager) {
                currentItem = (currentItem + 1) % pageViewsList.size();

                handler.obtainMessage().sendToTarget();
            }
        }
    }
}
