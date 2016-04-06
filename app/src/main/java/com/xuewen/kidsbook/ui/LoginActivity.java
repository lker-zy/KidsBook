package com.xuewen.kidsbook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xuewen.kidsbook.R;
import com.xuewen.kidsbook.service.LoginService;
import com.xuewen.kidsbook.service.beans.User;

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

    @Bind(R.id.act_login_ok)
    Button login_btn;

    private final static int MSG_LOGIN_FAILED = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case MSG_LOGIN_FAILED:
                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

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
        back_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        reg_btn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        login_btn.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.act_login_ok:
                login_btn.setText("登录中...");
                LoginService loginService = LoginService.getInstance();
                loginService.setLoginListener(this);

                User user = new User();
                user.setPhonenum("15810535238");
                user.setPassword("love");
                loginService.login(user);
                break;
        }
    }

    @Override
    public void onLoginSucc(Object data) {
        Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginActivity.this, PersonalActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLoginFail(Object data) {
        login_btn.setText("登录");
        Toast.makeText(this, "登录失败", Toast.LENGTH_SHORT).show();
        handler.sendEmptyMessage(MSG_LOGIN_FAILED);
    }
}
