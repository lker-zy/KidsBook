package com.xuewen.kidsbook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuewen.kidsbook.R;
import com.xuewen.kidsbook.service.LoginService;

import butterknife.Bind;

public class SettingActivity extends BaseActivity {
    @Bind(R.id.common_title_left_btn) LinearLayout back_btn;
    @Bind(R.id.common_title_left_btn_icon) ImageView back_icon;

    @Bind(R.id.act_setting_logout) LinearLayout logout_btn;

    @Bind(R.id.act_setting_person_msg) LinearLayout user_info_lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViews();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initTitleView() {
        TextView title_text = (TextView) findViewById(R.id.common_title_text);
        title_text.setText("设置");
        title_text.setVisibility(View.VISIBLE);

        back_btn.setVisibility(View.VISIBLE);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        back_icon.setBackgroundResource(R.drawable.commont_title_back);
        back_icon.setVisibility(View.VISIBLE);
    }

    private void initViews() {
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginService.getInstance().logout();
            }
        });

        user_info_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, UserInfoActivity.class);
                startActivity(intent);
            }
        });
    }
}
