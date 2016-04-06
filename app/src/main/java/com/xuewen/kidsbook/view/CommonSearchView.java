package com.xuewen.kidsbook.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xuewen.kidsbook.R;
import com.xuewen.kidsbook.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.xuewen.kidsbook.utils.Utils.setListViewHeightBasedOnChildren;

/**
 * Created by lker_zy on 16-3-17.
 */
public class CommonSearchView extends LinearLayout implements View.OnClickListener {

    /**
     * 输入框
     */
    private EditText etInput;

    /**
     * 删除键
     */
    private ImageView ivDelete;

    /**
     * 返回按钮
     */
    private ImageView btnBack;

    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 弹出列表
     */
    private ListView lvTips;

    /**
     * 热搜
     */
    private LinearLayout hotSearchLay;

    /**
     * 提示adapter （推荐adapter）
     */
    private ArrayAdapter<String> mHintAdapter;

    /**
     * 自动补全adapter 只显示名字
     */
    private ArrayAdapter<String> mAutoCompleteAdapter;

    /**
     * 搜索回调接口
     */
    private SearchViewListener mListener;

    /**
     * 设置搜索回调接口
     *
     * @param listener 监听者
     */
    public void setSearchViewListener(SearchViewListener listener) {
        mListener = listener;
    }

    public CommonSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.search_view_layout, this);
        initViews();
    }

    private TextView createAttrBtn() {
        TextView btn = new TextView(getContext());
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, Utils.dip2px(getContext(), 27), 1);
        btnParams.setMargins(0, 0, Utils.dip2px(getContext(), 15),
                Utils.dip2px(getContext(), 13));
        int padding = Utils.dip2px(getContext(), 5);
        btn.setPadding(padding, padding, padding, padding);
        btn.setLayoutParams(btnParams);
        btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        btn.setTextColor(getResources().getColor(R.color.hot_search_text));
        btn.setBackgroundResource(R.drawable.hot_search_bg_color_selector);
        btn.setGravity(Gravity.CENTER);
        return btn;
    }

    private void initViews() {
        etInput = (EditText) findViewById(R.id.search_et_input);
        ivDelete = (ImageView) findViewById(R.id.search_iv_delete);
        btnBack = (ImageView) findViewById(R.id.common_title_left_btn_icon);
        lvTips = (ListView) findViewById(R.id.search_lv_tips);
        hotSearchLay = (LinearLayout) findViewById(R.id.hot_search_lay);

        lvTips.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String text = lvTips.getAdapter().getItem(i).toString();
                etInput.setText(text);
                etInput.setSelection(text.length());
                lvTips.setVisibility(View.GONE);
                notifyStartSearching(text);
            }
        });

        ivDelete.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        Drawable drawable_left = getResources().getDrawable(R.drawable.common_search_icon);
        drawable_left.setBounds(0, 0, 48, 48);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        etInput.setCompoundDrawables(drawable_left, null, null, null);

        etInput.addTextChangedListener(new EditChangedListener());
        etInput.setOnClickListener(this);
        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    lvTips.setVisibility(GONE);
                    hotSearchLay.setVisibility(GONE);
                    notifyStartSearching(etInput.getText().toString());
                }
                return true;
            }
        });

        LineWrapLayoutHotDishSearch hotSearchDishLay = (LineWrapLayoutHotDishSearch)hotSearchLay.findViewById(R.id.hot_search_dish_lay);
        List<String> dishList = new ArrayList<>();
        dishList.add("test1");
        dishList.add("test2");
        dishList.add("testsssss");
        dishList.add("testsaaas");

        if (null != dishList) {
            for (int i = 0; i < dishList.size(); i++) {
                final String dish = dishList.get(i);
                TextView btn = createAttrBtn();
                btn.setText(dish);
                btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                btn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        /*
                        StatUtils.sendStatistic(
                                "hot.search.keywords.click",
                                "click");
                        StatUtils.sendStatistic(
                                "searchhistorypg.hotsearch.btn",
                                "click");

                        searchWithoutReqSug(dish);
                        StatReferManager.getInstance().setNestedRefer(StatReferManager.NestedStatistics.HOTSEARCH.value);
                        */

                        notifyStartSearching(dish);
                    }
                });
                hotSearchDishLay.addView(btn);
            }
        }
    }

    /**
     * 通知监听者 进行搜索操作
     * @param text
     */
    private void notifyStartSearching(String text){
        if (mListener != null) {
            mListener.onSearch(etInput.getText().toString());
        }
        //隐藏软键盘
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 设置热搜版提示 adapter
     */
    public void setTipsHintAdapter(ArrayAdapter<String> adapter) {
        this.mHintAdapter = adapter;
        if (lvTips.getAdapter() == null) {
            lvTips.setAdapter(mHintAdapter);
            setListViewHeightBasedOnChildren(lvTips);
        }
    }

    /**
     * 设置自动补全adapter
     */
    public void setAutoCompleteAdapter(ArrayAdapter<String> adapter) {
        this.mAutoCompleteAdapter = adapter;
    }

    private class EditChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (!"".equals(charSequence.toString())) {
                ivDelete.setVisibility(VISIBLE);
                lvTips.setVisibility(VISIBLE);
                if (mAutoCompleteAdapter != null && lvTips.getAdapter() != mAutoCompleteAdapter) {
                    lvTips.setAdapter(mAutoCompleteAdapter);
                    setListViewHeightBasedOnChildren(lvTips);
                }
                //更新autoComplete数据
                if (mListener != null) {
                    mListener.onRefreshAutoComplete(charSequence + "");
                }
            } else {
                ivDelete.setVisibility(GONE);
                if (mHintAdapter != null) {
                    lvTips.setAdapter(mHintAdapter);
                    setListViewHeightBasedOnChildren(lvTips);
                }
                lvTips.setVisibility(GONE);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_et_input:
                //lvTips.setVisibility(VISIBLE);
                hotSearchLay.setVisibility(VISIBLE);
                setListViewHeightBasedOnChildren(lvTips);
                break;
            case R.id.search_iv_delete:
                etInput.setText("");
                ivDelete.setVisibility(GONE);
                break;
            case R.id.common_title_left_btn_icon:
                ((Activity) mContext).finish();
                break;
        }
    }

    /**
     * search view回调方法
     */
    public interface SearchViewListener {

        /**
         * 更新自动补全内容
         *
         * @param text 传入补全后的文本
         */
        void onRefreshAutoComplete(String text);

        /**
         * 开始搜索
         *
         * @param text 传入输入框的文本
         */
        void onSearch(String text);

        /**
         * 提示列表项点击时回调方法 (提示/自动补全)
         */
        //void onTipsItemClick(String text);
    }
}
