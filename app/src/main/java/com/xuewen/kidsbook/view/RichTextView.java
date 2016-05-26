package com.xuewen.kidsbook.view;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.xuewen.kidsbook.R;
import com.xuewen.kidsbook.utils.MyURLSpan;

/**
 * Created by lker_zy on 16-5-8.
 */
public class RichTextView extends FrameLayout {
    //对应的view
    private LinearLayout mContentView = null;

    //对应的数据
    private CharSequence mData = null;
    private String[] mImageUrl = null;
    private ImageView[] mImage = null;
    private int mImageBaseIndex = 1; //从[图 1]开始

    //是否支持超链接点击
    private Boolean supportMovementMethod = false;
    //是否显示图索引
    private Boolean showImageIndex = false;

    private ImageLoader imageLoader;
    private DisplayImageOptions displayImageOptions;

    public RichTextView(Context context) {
        this(context, null);
    }

    public RichTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RichTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init();
    }

    private void init() {
        setDrawingCacheEnabled(false);
        setClipChildren(false);
        initImageLoader();

        mContentView = (LinearLayout) LayoutInflater.from(getContext()).inflate(
                R.layout.rich_textview_container, null);
        addView(mContentView);
    }

    /**
     * 设置待显示内容
     * @param content
     */
    public void setText(CharSequence content) {
        try {
            if(TextUtils.isEmpty(content)) { return; }
            if(content.equals(mData)) { return; }
            mData = content;
            mContentView.removeAllViews(); // 首先清理之前加入的子视图

            int viewIndex = 0;
            int len = content.length();
            SpannableStringBuilder style = new SpannableStringBuilder(content);
            ImageSpan[] imgAry = style.getSpans(0, len, ImageSpan.class);
            if(imgAry == null || imgAry.length <= 0) {
                addTextView(content, viewIndex);
                return;
            }

            int pos = 0;
            int start = 0;
            int end = 0;
            ImageSpan img = null;
            mImageUrl = new String[imgAry.length];
            mImage = new ImageView[imgAry.length];
            for(int i = 0; i < imgAry.length; i++) {
                img = imgAry[i];
                mImageUrl[i] = img.getSource();
                start = style.getSpanStart(img);
                if(pos < start) {
                    addTextView(style.subSequence(pos, start), viewIndex++);
                }
                end = style.getSpanEnd(img);
                addImageView(i, viewIndex++);

                pos = end + 1;
            }

            if(pos > 0 && pos < len) {
                addTextView(style.subSequence(pos, len), viewIndex);
            }

            requestLayout();
            invalidate(); //on a UI thread
        } catch(Exception ex) {

        }
    }

    private void addTextView(CharSequence text, int viewIndex) {
        TextView tv = (TextView) LayoutInflater.from(getContext()).inflate(
                R.layout.rich_textview_content_text, null);
        mContentView.addView(tv, viewIndex);
        tv.setText(text);

        if(supportMovementMethod) {
            changeLink(tv);
        }
    }

    private void addImageView(int index, int viewIndex) {
        View parent = LayoutInflater.from(getContext()).inflate(
                R.layout.rich_textview_content_image, null);
        mImage[index] = (ImageView) parent.findViewById(R.id.content_imageview_image);
        TextView tvTitle = (TextView)parent.findViewById(R.id.content_imageview_title);
        if(showImageIndex) {
            //这里的图片标题，也可以通过<img>标签的title/alt等属性分析出来
            tvTitle.setText("[图 " + Integer.toString(mImageBaseIndex + index) + "]");
            tvTitle.setVisibility(View.VISIBLE);
        } else {
            tvTitle.setVisibility(View.GONE);
        }
        mContentView.addView(parent, viewIndex);

        setImage(parent, mImage[index], mImageUrl[index]);
    }

    private void initImageLoader() {
        // 使用DisplayImageOptions.Builder()创建DisplayImageOptions
        displayImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ig_holder_image) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.ig_holder_image) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.ig_holder_image) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 构建完成

        imageLoader = ImageLoader.getInstance();
    }

    private void setImage(View parent, ImageView iv, String picUrl){
        if(picUrl != null && picUrl.trim().length() > 0) {
            parent.setVisibility(View.VISIBLE);

            iv.setImageResource(R.drawable.ig_holder_image);
            setPicNew(iv, picUrl);
            parent.requestLayout();
        }
        else{
            parent.setVisibility(View.GONE);
        }
    }

    private void setPicNew(ImageView imgView, String imgUrl) { //异步加载图片代码略
        imageLoader.displayImage(imgUrl, imgView, displayImageOptions);
    }

    /*
    private Size setPic(ImageView imgView, String imgUrl) { //异步加载图片代码略
        return XXXFileManager.getInstance().setImageBitmapWithMemoryCache(
                getContext(), logoView, logoUrl, XXXFileManager.getImagetLrucache(),
                getContext().getClass().getName(), false);

    }
    */

    /**
     * 供图片下载完毕时调用
     * @param fileURL
     */
    public void setPic(String fileURL) {
        if(mImage != null && mImageUrl != null && !TextUtils.isEmpty(fileURL)) {
            String source = null;
            for(int i = 0; i < mImageUrl.length && i < mImage.length; i++) {
                source = mImageUrl[i];
                if(!TextUtils.isEmpty(source)) {
                    if(fileURL.equals(source)) {
                        setPicNew(mImage[i], source);
                        mImage[i].getParent().requestLayout();
                        break;
                    }
                }
            }
        }
    }

    /**
     * 设置是否支持超链接点击
     */
    public void setSupportMovementMethod(Boolean supportMovementMethod) {
        this.supportMovementMethod = supportMovementMethod;
    }

    /**
     * 设置是否显示图索引
     * @param showImageIndex
     */
    public void setShowImageIndex(Boolean showImageIndex) {
        this.showImageIndex = showImageIndex;
    }

    /**
     * 设置TextView超链接跳转
     * @param tv
     */
    private void changeLink(TextView tv){
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        CharSequence text = tv.getText();
        if (text instanceof Spannable) {
            int end = text.length();
            Spannable sp = (Spannable) tv.getText();
            URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
            if(urls == null || urls.length <= 0) { return; }
            SpannableStringBuilder style = new SpannableStringBuilder(text);
            URLSpan[] urlsn = style.getSpans(0, end, URLSpan.class);
            if(urlsn == null || urls.length != urlsn.length) { return; }

            //循环把链接发过去
            URLSpan url = null;
            for(int i = 0; i < urls.length && i < urlsn.length; i++) {
                url = urls[i];
                MyURLSpan myURLSpan = new MyURLSpan(getContext(), url.getURL());
                style.removeSpan(urlsn[i]);
                style.setSpan(myURLSpan, sp.getSpanStart(url),
                        sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            }
            tv.setText(style);
        }
    }

    public int getImageCount() {
        int cnt = mImageBaseIndex;
        if(mImage != null && mImageUrl != null) {
            cnt += mImage.length;
        }
        return cnt;
    }

    public void setmImageBaseIndex(int baseIndex) {
        this.mImageBaseIndex = baseIndex;
    }

    public CharSequence getmData() {
        return mData;
    }
}
