package com.xuewen.kidsbook.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lker_zy on 15-12-22.
 */
public class BookItemView extends ViewGroup {
    public BookItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int ln = 0;
        int tn = 0;
        int rn = 0;
        int bn = 0;

        int width = 0;
        int height = 0;

        MarginLayoutParams params = null;
        // ImageView
        View imageView = getChildAt(0);
        width = imageView.getMeasuredWidth();
        height = imageView.getMeasuredHeight();
        params = (MarginLayoutParams) imageView.getLayoutParams();

        ln = params.leftMargin;
        tn = params.topMargin;
        rn = ln + width;
        bn = tn + height;
        imageView.layout(ln, tn, rn, bn);

        imageView = getChildAt(1);
        width = imageView.getMeasuredWidth();
        height = imageView.getMeasuredHeight();
        params = (MarginLayoutParams) imageView.getLayoutParams();

        width = 500;

        ln = rn + params.leftMargin;
        rn = ln + width;
        bn = tn + height;

        imageView.layout(ln, tn, rn, bn);
        height = imageView.getMeasuredHeight();

        imageView = getChildAt(2);
        imageView.layout(ln, bn, rn, bn + height);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(1000, 300);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
