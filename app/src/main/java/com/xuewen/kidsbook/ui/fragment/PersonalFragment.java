package com.xuewen.kidsbook.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xuewen.kidsbook.R;
import com.xuewen.kidsbook.ui.SettingActivity;
import com.xuewen.kidsbook.view.DynamicGridView;
import com.xuewen.kidsbook.view.DynamicGridViewItem;
import com.xuewen.kidsbook.view.StudyTitleItemView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lker_zy on 16-3-28.
 */
public class PersonalFragment extends BaseFragment implements View.OnClickListener {
    @Bind(R.id.user_photo) SimpleDraweeView userPhoto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Fresco.initialize(getActivity());

        View view = super.onCreateView(inflater, container, savedInstanceState);

        initViews();

        return view;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal;
    }

    private List<Map<String, Object>> appGridInfo = new ArrayList<>();

    @Bind(R.id.personal_app_grid_container) LinearLayout app_grid_container;
    @Bind(R.id.setting_btn) ImageView settingBtn;
    @Bind(R.id.book_study_title_outside) LinearLayout book_study_items_lay;

    private void initViews() {
        setDynamicGrid();

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });

        userPhoto.setImageResource(R.drawable.user_photo_default);

        initStudyTitle();
    }

    private void addItemView(String name) {

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.weight = 1;

        StudyTitleItemView studyTitleItemView = new StudyTitleItemView(getActivity());
        studyTitleItemView.setText(name);

        book_study_items_lay.addView(studyTitleItemView, lp);
    }

    private void initStudyTitle() {
        book_study_items_lay.removeAllViews();

        addItemView("书架");
        addItemView("书单");

    }


    private void prepareGridData() {
        appGridInfo.clear();

        Map<String, Object> share = new HashMap<>();
        share.put("text", "分享有礼");
        share.put("image", R.drawable.waimai_shoplist_item_bg_nomal);
        share.put("actName", "ReadingBooks");
        appGridInfo.add(share);

        Map<String, Object> myBox = new HashMap<>();
        myBox.put("text", "我的盒子");
        myBox.put("image", R.drawable.waimai_shoplist_item_bg_nomal);
        myBox.put("actName", "MyBookBox");
        appGridInfo.add(myBox);

        Map<String, Object> myBaby = new HashMap<>();
        myBaby.put("text", "我的宝贝");
        myBaby.put("image", R.drawable.waimai_shoplist_item_bg_nomal);
        myBaby.put("actName", "ReadingBooks");
        appGridInfo.add(myBaby);

        Map<String, Object> myStudy = new HashMap<>();
        myStudy.put("text", "书房");
        myStudy.put("image", R.drawable.waimai_shoplist_item_bg_nomal);
        myStudy.put("actName", "ReadingBooks");
        appGridInfo.add(myStudy);

        Map<String, Object> myComment = new HashMap<>();
        myComment.put("text", "书评");
        myComment.put("image", R.drawable.waimai_shoplist_item_bg_nomal);
        myComment.put("actName", "MyBookComment");
        appGridInfo.add(myComment);

        Map<String, Object> myProbation = new HashMap<>();
        myProbation.put("text", "试读");
        myProbation.put("image", R.drawable.waimai_shoplist_item_bg_nomal);
        myProbation.put("actName", "MyProbation");
        appGridInfo.add(myProbation);

        Map<String, Object> history = new HashMap<>();
        history.put("text", "阅读轨迹");
        history.put("image", R.drawable.waimai_shoplist_item_bg_nomal);
        history.put("actName", "ReadingBooks");
        appGridInfo.add(history);

        Map<String, Object> myMessage = new HashMap<>();
        myMessage.put("text", "消息");
        myMessage.put("image", R.drawable.waimai_shoplist_item_bg_nomal);
        myMessage.put("actName", "MyBuyBooks");
        appGridInfo.add(myMessage);

        Map<String, Object> moneyAccount = new HashMap<>();
        moneyAccount.put("text", "我的钱袋");
        moneyAccount.put("image", R.drawable.waimai_shoplist_item_bg_nomal);
        moneyAccount.put("actName", "MyBuyBooks");
        appGridInfo.add(moneyAccount);
    }

    private void setDynamicGrid() {
        Map<String, Object> itemData = null;
        prepareGridData();

        DynamicGridView dynamicGridView = new DynamicGridView(app_grid_container, getActivity());
        dynamicGridView.setRowItemsNum(4);

        int total = appGridInfo.size();
        int totalNum = dynamicGridView.getTotalNum(total);

        List<DynamicGridViewItem> items = new ArrayList<>();
        for (int i = 0; i < totalNum; ++i, itemData = null) {
            if (i < total) {
                itemData = appGridInfo.get(i);
            }
            PersonalDynamicGridItem item = new PersonalDynamicGridItem(getActivity(), itemData);
            item.setLayout(R.layout.personal_app_grid_item);
            item.init();

            if (i < total) {
                item.setBackgroundResource(R.drawable.waimai_shoplist_item_bg_color_selector);
            } else {
                item.setBackgroundResource(R.drawable.waimai_shoplist_item_bg_nomal);
            }

            items.add(item);
        }

        dynamicGridView.fill(items);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_btn:
                break;
        }

    }

    class PersonalDynamicGridItem extends DynamicGridViewItem implements View.OnClickListener {
        @Bind(R.id.at_me_dynamic_image) ImageView image;
        @Bind(R.id.at_me_dynamic_text) TextView text;
        @Bind(R.id.at_me_dynamic_complement_text) TextView complementText;

        private Map<String, Object> itemData;

        public PersonalDynamicGridItem(Context context, Map<String, Object> itemData) {
            super(context);

            this.itemData = itemData;
        }

        @Override
        public void init() {
            super.init();

            ButterKnife.bind(this, getRootView());

            if (itemData != null) {
                image.setImageResource(R.drawable.update_promote);
                image.setVisibility(VISIBLE);

                text.setText((String) itemData.get("text"));
                text.setVisibility(View.VISIBLE);

                complementText.setText((String) itemData.get("complementText"));
                complementText.setVisibility(View.VISIBLE);

                setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
        }
    }
}
