package com.xuewen.kidsbook.view;

/**
 * Created by lker_zy on 16-4-4.
 */
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.xuewen.kidsbook.R;


public class LineWrapLayoutHotDishSearch extends ViewGroup {

    private  int MARGIN_HORIZNAL ; // 水平间距
    private  int MARGIN_VERTICAL ; // 垂直间距
    private Context mContext;

    public LineWrapLayoutHotDishSearch(Context context) {
        super(context);
        mContext=context;
    }

    public LineWrapLayoutHotDishSearch(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext=context;
    }

    public LineWrapLayoutHotDishSearch(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        MARGIN_HORIZNAL=mContext.getResources().getDimensionPixelSize(R.dimen.hot_search_dip_15);
        MARGIN_VERTICAL=mContext.getResources().getDimensionPixelSize(R.dimen.hot_search_dip_15);
        int mWidth = MeasureSpec.getSize(widthMeasureSpec);
        int totalWidth = 0;
        int mHeight = 0;
        int row = 1;

        for (int index = 0; index < getChildCount(); index++) {
            if(row>=3){
                break;
            }
            View child = getChildAt(index);
            child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

            mHeight = child.getMeasuredHeight() + MARGIN_VERTICAL;
            totalWidth += child.getMeasuredWidth() + MARGIN_HORIZNAL;
            if (totalWidth > mWidth) {
                row++;

                totalWidth = 0;
                index = index -1;
            }
        }

        mHeight = row* mHeight;
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int count = getChildCount();
        int row = 0;
        int posX = 0;
        int posY = 0;

        for (int i = 0; i < count; i++) {
            View child = this.getChildAt(i);
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();

            posX += width + MARGIN_HORIZNAL;
            posY = row * (height + MARGIN_VERTICAL) + height + t;

            if (posX > r - l) {
                row++;

                if(row>=3){
                    break;
                }
                posX = width + MARGIN_HORIZNAL;

                posY = row * (height + MARGIN_VERTICAL) + height + t;
            }

            child.layout(posX - width, posY - height, posX, posY);
        }
    }

}
