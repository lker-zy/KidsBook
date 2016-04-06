package com.xuewen.kidsbook.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xuewen.kidsbook.R;

/**
 * Created by lker_zy on 16-4-6.
 */
public class StudyTitleItemView extends LinearLayout implements View.OnClickListener{
    public StudyTitleItemView(Context context) {
        super(context);

        init(context);
    }

    public StudyTitleItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    @Override
    public void onClick(View v) {

    }

    private void init(Context context) {
        inflate(context, R.layout.book_study_title_item, this);

        this.setOnClickListener(this);
    }

    public void setText(String text) {
        TextView textView = (TextView) findViewById(R.id.book_study_title_item_name);
        textView.setText(text);
    }

    public void setImage(int resId) {
        SimpleDraweeView imageView = (SimpleDraweeView) findViewById(R.id.book_study_title_item_icon);
        imageView.setImageResource(resId);
    }
}
