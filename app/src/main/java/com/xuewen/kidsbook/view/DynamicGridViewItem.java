package com.xuewen.kidsbook.view;

/**
 * Created by lker_zy on 16-3-26.
 */
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class DynamicGridViewItem extends LinearLayout {
    private int layoutId;
    protected View root;
    protected Context context;

    private ImageView atMeDynamicImageDot;

    public View getRootView() {
        return root;
    }

    public DynamicGridViewItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public DynamicGridViewItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public DynamicGridViewItem(Context context) {
        super(context);
        this.context = context;
    }

    public void setLayout(int layoutId) {
        this.layoutId = layoutId;
    }

    protected void init() {
        root = inflate(context, this.layoutId, this);

        // TODO: 空白item 不能设置ClickAble
        this.setClickable(true);
    }

    public ImageView getDotImageView() {
        return atMeDynamicImageDot;
    }

    protected void setData(Object data) {
    }


    public void showDot(boolean show) {
        if (show) {
            atMeDynamicImageDot.setVisibility(View.VISIBLE);
        } else {
            atMeDynamicImageDot.setVisibility(View.INVISIBLE);
        }
    }
}
