package com.xuewen.kidsbook.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xuewen.kidsbook.AppConfig;
import com.xuewen.kidsbook.R;
import com.xuewen.kidsbook.utils.LogUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private static String TAG = RegisterActivity.class.getSimpleName();

    @Bind(R.id.common_title_left_btn) LinearLayout back_btn;
    @Bind(R.id.common_title_text) TextView title_text;
    @Bind(R.id.common_title_left_btn_icon) ImageView back_btn_icon;

    @Bind(R.id.act_registration_get_code) Button verify_code_btn;

    @Bind(R.id.act_registration_nick) EditText nickname_et;
    @Bind(R.id.act_registration_phone) EditText phone_et;
    @Bind(R.id.act_registration_code) EditText verify_code_et;
    @Bind(R.id.act_registration_pwd) EditText password_et;

    @Bind(R.id.act_registration_ok) Button reg_submit_btn;

    private static final int MSG_REG_SUCCESS    = 1;
    private static final int MSG_REG_FAILED     = 2;

    private static final int REG_FAIL_SYS_ERR   = -1;
    private static final int REG_FAIL_DUP_USER  = -2;
    private static final int REG_FAIL_VCODE_ERR = -3;

    private RequestQueue requestQueue;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);

            switch (message.what) {
                case MSG_REG_SUCCESS:
                    break;
                case MSG_REG_FAILED:
                    break;
            }
        }

    };

    private RequestQueue getRequestQueue() {
        return requestQueue;
    }

    private void initView() {
        verify_code_btn.setOnClickListener(this);
        reg_submit_btn.setOnClickListener(this);
    }

    @Override
    protected void initTitleView() {
        title_text.setText("注册");
        title_text.setVisibility(View.VISIBLE);

        back_btn.setVisibility(View.VISIBLE);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        back_btn_icon.setBackgroundResource(R.drawable.commont_title_back);
        back_btn_icon.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.start();
    }

    @Override
    protected int getLayoutId() { return R.layout.activity_register;}

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.act_registration_get_code:
                break;
            case R.id.act_registration_ok:
                doRegisterRequest();
                break;
            default:
                break;
        }
    }

    private void doRegisterRequest() {
        final String nickname = nickname_et.getText().toString();
        final String phonenum = phone_et.getText().toString();
        final String verifycode = verify_code_et.getText().toString();
        final String password = password_et.getText().toString();

        StringRequest request = new StringRequest(Request.Method.POST, AppConfig.USER_REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        LogUtil.d(TAG, "receive registration response");
                        //Login();
                        handler.sendEmptyMessage(MSG_REG_SUCCESS);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Message message = new Message();
                        message.what = MSG_REG_FAILED;

                        Bundle data = new Bundle();
                        data.putInt("ERROR_CODE", REG_FAIL_SYS_ERR);

                        message.setData(data);
                        handler.sendMessage(message);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String>  map = new HashMap<>();
                map.put("nickname", nickname);
                map.put("phonenum", phonenum);
                map.put("verifycode", verifycode);
                map.put("password", password);

                return map;
            }
        };

        LogUtil.d(TAG, "add reg request to request queue");
        getRequestQueue().add(request);
    }
}
