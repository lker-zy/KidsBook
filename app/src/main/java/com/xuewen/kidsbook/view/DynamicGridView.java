package com.xuewen.kidsbook.view;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

/**
 * Created by lker_zy on 16-3-26.
 */
public class DynamicGridView {
    private Context context;
    private int rowItemsNums;
    private LinearLayout gridContainer;
    private int totalNum;

    public DynamicGridView(View container, Context context) {
        this.gridContainer = (LinearLayout) container;
        this.context = context;
    }

    public void setRowItemsNum(int num) {
        rowItemsNums = num;
    }

    public int getTotalNum(int total) {
        totalNum = total % rowItemsNums == 0 ? total : ((total / rowItemsNums) + 1) * rowItemsNums;
        return totalNum;
    }

    public void fill(List<DynamicGridViewItem> itemList) {
        /**
         * first、每次都先remove，否则会累加
         */
        gridContainer.removeAllViews();

        LinearLayout ll = null;
        LinearLayout.LayoutParams llParam = null;

        for (int i = 0; i < itemList.size(); i++) {
            if (i % rowItemsNums == 0) {
                ll = new LinearLayout(context);
                ll.setOrientation(LinearLayout.HORIZONTAL);
                llParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                ll.setLayoutParams(llParam);

            }

            DynamicGridViewItem item = itemList.get(i);
            LinearLayout.LayoutParams itemParam = new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            itemParam.weight = 1;

            // 如果是每行最后一个，不添加右间距

            if ((i + 1) % rowItemsNums != 0) {
                itemParam.rightMargin = 1;
            }
            // 每行底部的间距都要有
            itemParam.bottomMargin = 1;

            if (i < rowItemsNums) {
                // 第一行添加上间距
                itemParam.topMargin = 1;
            }

            ll.addView(item, itemParam);
            if ((i + 1) % rowItemsNums == 0) {
                gridContainer.addView(ll);
            }

        }
    }
}
