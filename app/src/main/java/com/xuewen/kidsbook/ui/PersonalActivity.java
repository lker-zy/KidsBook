package com.xuewen.kidsbook.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuewen.kidsbook.R;
import com.xuewen.kidsbook.view.DynamicGridView;
import com.xuewen.kidsbook.view.DynamicGridViewItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PersonalActivity extends BaseActivity implements View.OnClickListener {
    private static String TAG = PersonalActivity.class.getSimpleName();

    private List<Map<String, Object>> appGridInfo = new ArrayList<>();

    @Bind(R.id.personal_app_grid_container) LinearLayout app_grid_container;
    //@Bind(R.id.setting_btn) ImageView settingBtn;

    private void initViews() {
        /*
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
        */
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setDynamicGrid();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_main_personal;
    }

    private void prepareGridData() {
        Map<String, Object> readingBooks = new HashMap<>();
        readingBooks.put("text", "正在读");
        readingBooks.put("image", R.drawable.waimai_shoplist_item_bg_nomal);
        readingBooks.put("actName", "ReadingBooks");
        appGridInfo.add(readingBooks);

        Map<String, Object> readedBooks = new HashMap<>();
        readedBooks.put("text", "我读过");
        readedBooks.put("image", R.drawable.waimai_shoplist_item_bg_nomal);
        readedBooks.put("actName", "ReadedBooks");
        appGridInfo.add(readedBooks);

        Map<String, Object> myBuyBooks = new HashMap<>();
        myBuyBooks.put("text", "已购买");
        myBuyBooks.put("image", R.drawable.waimai_shoplist_item_bg_nomal);
        myBuyBooks.put("actName", "MyBuyBooks");
        appGridInfo.add(myBuyBooks);

        Map<String, Object> myComment = new HashMap<>();
        myComment.put("text", "我的书评");
        myComment.put("image", R.drawable.waimai_shoplist_item_bg_nomal);
        myComment.put("actName", "MyBookComment");
        appGridInfo.add(myComment);

        Map<String, Object> myBox = new HashMap<>();
        myBox.put("text", "我的盒子");
        myBox.put("image", R.drawable.waimai_shoplist_item_bg_nomal);
        myBox.put("actName", "MyBookBox");
        appGridInfo.add(myBox);

        Map<String, Object> myProbation = new HashMap<>();
        myProbation.put("text", "试读");
        myProbation.put("image", R.drawable.waimai_shoplist_item_bg_nomal);
        myProbation.put("actName", "MyProbation");
        appGridInfo.add(myProbation);
    }

    private void setDynamicGrid() {
        Map<String, Object> itemData = null;
        prepareGridData();

        DynamicGridView dynamicGridView = new DynamicGridView(app_grid_container, this);
        dynamicGridView.setRowItemsNum(4);

        int total = appGridInfo.size();
        int totalNum = dynamicGridView.getTotalNum(total);

        List<DynamicGridViewItem> items = new ArrayList<>();
        for (int i = 0; i < totalNum; ++i, itemData = null) {
            if (i < total) {
                itemData = appGridInfo.get(i);
            }
            PersonalDynamicGridItem item = new PersonalDynamicGridItem(this, itemData);
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
