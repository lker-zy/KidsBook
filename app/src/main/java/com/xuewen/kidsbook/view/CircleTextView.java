package com.xuewen.kidsbook.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lker_zy on 16-5-26.
 */
public class CircleTextView extends View {

    private Paint mPaintCircle;
    private Paint mPaintText;
    private String mTextStr;
    private Rect mBoundText;
    private int mTextWidth;
    private int mTextHeight;
    private int mWidth;
    private int mHeight;
    private float mRadius;


    public CircleTextView(Context context) {
        this(context, null);
        init(context, null);
    }

    public CircleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mPaintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintCircle.setColor(ContextCompat.getColor(context, android.R.color.holo_blue_light));

        mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintText.setColor(ContextCompat.getColor(context, android.R.color.black));
        mPaintText.setTextSize(20F);
        mTextStr = "字";
        mBoundText = new Rect();
        mPaintText.getTextBounds(mTextStr, 0, mTextStr.length(), mBoundText);
        mTextWidth = mBoundText.width();
        mTextHeight = mBoundText.height();
    }

    protected void onSizeChanged (int w, int h, int oldw, int oldh) {
        mWidth = w;  // x 的值就是宽度的一半
        mHeight = h;  // y 的值就是高度的一半
        mRadius = Math.min(mWidth / 2, mHeight / 2);  // 得到了radius的值
    }

    public void setText(String text) {
        mTextStr = text;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius, mPaintCircle);

        canvas.drawText(mTextStr, (mWidth-mTextWidth) / 2, (mHeight + mTextHeight) / 2, mPaintText);
    }
}
