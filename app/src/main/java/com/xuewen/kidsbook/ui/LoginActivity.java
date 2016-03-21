package com.xuewen.kidsbook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuewen.kidsbook.R;
import com.xuewen.kidsbook.service.LoginService;

import butterknife.Bind;


public class LoginActivity extends BaseActivity
        implements View.OnClickListener, LoginService.LoginListener {
    @Bind(R.id.common_title_text)
    TextView title_text;

    @Bind(R.id.common_title_left_btn)
    LinearLayout back_btn;

    @Bind(R.id.common_title_left_btn_icon)
    ImageView back_btn_icon;

    @Bind(R.id.act_login_registration)
    Button reg_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
        title_text.setText("登录");
        title_text.setVisibility(View.VISIBLE);

        back_btn.setVisibility(View.VISIBLE);

        back_btn_icon.setBackgroundResource(R.drawable.commont_title_back);
        back_btn_icon.setVisibility(View.VISIBLE);

        reg_btn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.act_login_ok:
                LoginService loginService = new LoginService();
                loginService.setLoginListener(this);
                break;
        }
    }

    @Override
    public void onLoginSucc(Object data) {

    }

    @Override
    public void onLoginFail(Object data) {

    }
}
