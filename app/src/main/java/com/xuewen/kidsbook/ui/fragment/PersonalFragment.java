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
import com.xuewen.kidsbook.service.BookCollectionService;
import com.xuewen.kidsbook.service.EssenceCollectionService;
import com.xuewen.kidsbook.service.beans.EssenceCollection;
import com.xuewen.kidsbook.ui.SettingActivity;
import com.xuewen.kidsbook.view.CirclePercentView;
import com.xuewen.kidsbook.view.DynamicGridView;
import com.xuewen.kidsbook.view.DynamicGridViewItem;
import com.xuewen.kidsbook.view.StudyTitleItemView;

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
    private LinearLayout book_study_items_lay;
    private LinearLayout grow_plan_items_lay;
    private LinearLayout collection_items_lay;

    @Bind(R.id.personal_app_grid_container) LinearLayout app_grid_container;
    @Bind(R.id.setting_btn) ImageView settingBtn;
    @Bind(R.id.personal_navi) LinearLayout navi_layout;

    private void initViews() {
        //setDynamicGrid();

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });

        //userPhoto.setImageResource(R.drawable.user_photo_default);
        userPhoto.setImageResource(R.drawable.act_registration_user_icon);

        initStudyTitle();
        initGrowPlanTitle();
        initCollectionTitle();
    }

    private void addItemView(LinearLayout container, String name) {

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.weight = 1;

        StudyTitleItemView studyTitleItemView = new StudyTitleItemView(getActivity());
        studyTitleItemView.setText(name);

        container.addView(studyTitleItemView, lp);
    }

    private void addBlankView(LinearLayout container, int height) {

        LinearLayout blank_layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.blank_view, null);
        blank_layout.setMinimumHeight(height);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.topMargin = 10;
        navi_layout.addView(blank_layout);
    }

    private void addPercentView(LinearLayout container, String name, int percent) {

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.weight = 1;

        LayoutInflater mInflater = LayoutInflater.from(getActivity());
        LinearLayout studyPercentView = (LinearLayout) mInflater.inflate(R.layout.study_percent_item, null);

        CirclePercentView percentCircle = (CirclePercentView) studyPercentView.findViewById(R.id.percent_circle);
        percentCircle.setPercent(percent);

        container.addView(studyPercentView, lp);
    }

    private void initStudyTitle() {
        LinearLayout study_navi_layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.personal_study_navi, null);
        book_study_items_lay = (LinearLayout) study_navi_layout.findViewById(R.id.book_study_title_outside);
        book_study_items_lay.removeAllViews();

        addPercentView(book_study_items_lay, "", 50);
        addPercentView(book_study_items_lay, "", 40);
        addPercentView(book_study_items_lay, "", 10);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.topMargin = 10;
        navi_layout.addView(study_navi_layout);
    }

    private void addPlanItem(LinearLayout container) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.weight = 1;

        LayoutInflater mInflater = LayoutInflater.from(getActivity());
        LinearLayout planItemView = (LinearLayout) mInflater.inflate(R.layout.grow_plan_item, null);

        BookCollectionService bookCollectionService = new BookCollectionService(getActivity());
        int count = bookCollectionService.count();
        String numViewText = "已添加 " + count + " 本收藏";

        TextView numView = (TextView) planItemView.findViewById(R.id.book_collection_num);
        numView.setText(numViewText);

        container.addView(planItemView, lp);
    }

    private void initGrowPlanTitle() {
        addBlankView(navi_layout, 16);

        LinearLayout study_navi_layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.personal_study_navi, null);
        TextView title_name = (TextView) study_navi_layout.findViewById(R.id.atme_navi_title_name);
        title_name.setText("成长计划");
        grow_plan_items_lay = (LinearLayout) study_navi_layout.findViewById(R.id.book_study_title_outside);
        grow_plan_items_lay.removeAllViews();

        addPlanItem(grow_plan_items_lay);

        navi_layout.addView(study_navi_layout);

    }

    private void addCollectionItem(LinearLayout container, EssenceCollection collection) {

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.weight = 1;

        LayoutInflater mInflater = LayoutInflater.from(getActivity());
        LinearLayout collectionView = (LinearLayout) mInflater.inflate(R.layout.my_collection_title_item, null);

        TextView title = (TextView) collectionView.findViewById(R.id.collection_title);
        title.setText(collection.getTitle());

        container.addView(collectionView, lp);
    }

    private void initCollectionTitle() {
        addBlankView(navi_layout, 16);

        LinearLayout study_navi_layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.personal_study_navi, null);
        TextView title_name = (TextView) study_navi_layout.findViewById(R.id.atme_navi_title_name);
        title_name.setText("收藏");

        collection_items_lay = (LinearLayout) study_navi_layout.findViewById(R.id.book_study_title_outside);
        collection_items_lay.removeAllViews();

        collection_items_lay.setOrientation(LinearLayout.VERTICAL);

        EssenceCollectionService essenceCollectionService = new EssenceCollectionService(getActivity());
        List<EssenceCollection> collections = essenceCollectionService.list();
        for (int i = 0; i < collections.size(); ++i) {
            addCollectionItem(collection_items_lay, collections.get(i));
        }

        navi_layout.addView(study_navi_layout);
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
