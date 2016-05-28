package com.xuewen.kidsbook.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xuewen.kidsbook.AppConfig;
import com.xuewen.kidsbook.R;
import com.xuewen.kidsbook.net.GlobalVolley;
import com.xuewen.kidsbook.net.ImageUpload;
import com.xuewen.kidsbook.service.UserService;
import com.xuewen.kidsbook.service.beans.User;
import com.xuewen.kidsbook.ui.fragment.DatePickerFragment;
import com.xuewen.kidsbook.utils.LogUtil;
import com.xuewen.kidsbook.utils.Utils;

import java.io.File;

import butterknife.Bind;

import static com.xuewen.kidsbook.utils.Utils.copyfile;
import static com.xuewen.kidsbook.utils.Utils.isMediaOk;

/**
 * Created by lker_zy on 16-5-15.
 */
public class UserInfoActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = UserInfoActivity.class.getSimpleName();

    @Bind(R.id.common_title_left_btn) LinearLayout back_btn;
    @Bind(R.id.common_title_left_btn_icon) ImageView back_icon;

    @Bind(R.id.user_head_photo) SimpleDraweeView user_head_photo_iv;
    @Bind(R.id.user_photo_parent_layout) RelativeLayout user_head_photo_select_lay;
    @Bind(R.id.usr_info_baby_birthday_layout) RelativeLayout baby_birthday_layout;
    @Bind(R.id.btn_photo_camera) Button photo_camera_btn;
    @Bind(R.id.btn_photo_gallery) Button photo_gallery_btn;
    @Bind(R.id.cancle) Button user_head_photo_cancel;

    @Bind(R.id.usr_info_nick_member_text) TextView nick_name_tv;
    @Bind(R.id.usr_info_nick_member_et) EditText nick_name_et;
    @Bind(R.id.usr_info_nick_name_edit_icon) ImageView nick_name_edit_icon;
    @Bind(R.id.usr_info_nick_name_submit_btn) Button nick_name_submit_btn;
    @Bind(R.id.usr_info_baby_birthday_member_text) TextView birthday_tv;

    private UserService userService;
    private User user;

    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final String IMAGE_FILE_NAME = "temp_head_image.jpg";

    private Uri raw_head_photo_uri = null;
    private Uri croped_head_photo_uri = null;

    // 裁剪后图片的宽(X)和高(Y),480 X 480的正方形。
    private static int output_X = 480;
    private static int output_Y = 480;

    private static final int MSG_START_CROP_RAW_IMAGE = 0;
    private static final int MSG_START_UPLOAD_IMAGE = 1;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case MSG_START_CROP_RAW_IMAGE:
                    Toast.makeText(UserInfoActivity.this, "开始裁剪图片", Toast.LENGTH_SHORT).show();
                    cropRawPhoto(raw_head_photo_uri);
                    break;

                case MSG_START_UPLOAD_IMAGE:
                    uploadUserHeadPhoto();
                    break;
            }
        }
    };

    ProgressDialog dialog;

    private void uploadUserHeadPhoto() {
        dialog = new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("用户头像上传中...");
        dialog.show();

        ImageUpload.uploadImg(croped_head_photo_uri, GlobalVolley.requestQueue(), "http://www.baidu.com", new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                dialog.dismiss();
                Toast.makeText(UserInfoActivity.this, "上传头像成功", Toast.LENGTH_SHORT);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(UserInfoActivity.this, "上传头像失败", Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void beforeSetContentView() {
        Fresco.initialize(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user_head_photo_iv.setImageURI(Uri.fromFile(new File(AppConfig.USER_HEAD_PHOTO_DIR, AppConfig.USER_HEAD_PHOTO_FILE)));

        userService = new UserService();
        userService.load();
        user = userService.user();

        user_head_photo_iv.setOnClickListener(this);
        user_head_photo_cancel.setOnClickListener(this);
        photo_camera_btn.setOnClickListener(this);
        photo_gallery_btn.setOnClickListener(this);
        baby_birthday_layout.setOnClickListener(this);

        nick_name_tv.setText(user.getNickname());
        String birtyday = user.getBirYear() + " 年 " + user.getBirMonth() + " 月 " + user.getBirDay() + " 日";
        birthday_tv.setText(birtyday);

        nick_name_edit_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nick_name_tv.setVisibility(View.GONE);
                nick_name_et.setVisibility(View.VISIBLE);
                nick_name_et.requestFocus();
                nick_name_submit_btn.setVisibility(View.VISIBLE);
                nick_name_edit_icon.setVisibility(View.GONE);
            }
        });

        nick_name_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nick_name = nick_name_et.getText().toString();
                nick_name_et.setVisibility(View.GONE);
                nick_name_tv.setVisibility(View.VISIBLE);
                nick_name_tv.setText(nick_name);
                nick_name_edit_icon.setVisibility(View.VISIBLE);
                nick_name_submit_btn.setVisibility(View.INVISIBLE);

                user.setNickname(nick_name);
                userService.update(user);
            }
        });
    }

    @Override
    protected void initTitleView() {
        TextView title_text = (TextView) findViewById(R.id.common_title_text);
        title_text.setText("个人信息");
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

    private void showDatePickerDialog(View view) {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setOnDateSetListener(new DatePickerFragment.OnDateSetListener() {
            @Override
            public void onDateSet(int year, int month, int day) {
                birthday_tv.setText(year + " 年 " + (month + 1) + " 月 " + day + " 日");

                user.setBirthday(year + "-" + (month + 1) + "-" + day);
                userService.update(user);
            }
        });
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_head_photo:
                user_head_photo_select_lay.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_photo_camera:
                choseHeadImageFromCameraCapture();
                break;
            case R.id.btn_photo_gallery:
                choseHeadImageFromGallery();
                break;
            case R.id.cancle:
                user_head_photo_select_lay.setVisibility(View.GONE);
                break;
            case R.id.usr_info_baby_birthday_layout:
                showDatePickerDialog(v);
                break;
        }
    }

    // 从本地相册选取图片作为头像
    private void choseHeadImageFromGallery() {
        Intent intentFromGallery = new Intent();
        // 设置文件类型
        intentFromGallery.setType("image/*");
        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);

        /*
        cropRawPhoto(raw_head_photo_uri);
        setImageToHeadView(croped_head_photo_uri);
        */
    }

    // 启动手机相机拍摄照片作为头像
    private void choseHeadImageFromCameraCapture() {
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 判断存储卡是否可用，存储照片文件
        if (isMediaOk()) {
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                    .fromFile(new File(Environment
                            .getExternalStorageDirectory(), IMAGE_FILE_NAME)));
        }

        startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);

        /*
        cropRawPhoto(raw_head_photo_uri);
        setImageToHeadView(croped_head_photo_uri);
        */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {

        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplication(), "取消", Toast.LENGTH_LONG).show();
            return;
        }

        switch (requestCode) {
            case CODE_GALLERY_REQUEST:
                raw_head_photo_uri = intent.getData();
                //cropRawPhoto(intent.getData());
                handler.sendEmptyMessage(MSG_START_CROP_RAW_IMAGE);
                break;

            case CODE_CAMERA_REQUEST:
                if (isMediaOk()) {
                    File tempFile = new File(
                            Environment.getExternalStorageDirectory(),
                            IMAGE_FILE_NAME);
                    raw_head_photo_uri = Uri.fromFile(tempFile);
                    //cropRawPhoto(Uri.fromFile(tempFile));
                    handler.sendEmptyMessage(MSG_START_CROP_RAW_IMAGE);
                } else {
                    Toast.makeText(getApplication(), "没有SDCard!", Toast.LENGTH_LONG)
                            .show();
                }

                break;

            case CODE_RESULT_REQUEST:
                setImageToHeadView(croped_head_photo_uri);
                /*
                if (intent != null) {
                    setImageToHeadView(intent);
                } else {
                    Toast.makeText(getApplication(), "没有Bitmap!", Toast.LENGTH_LONG)
                            .show();
                }
                */

                break;
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }

    /**
     * 裁剪原始的图片
     */
    public void cropRawPhoto(Uri uri) {

        File bakFile = new File(AppConfig.USER_HEAD_PHOTO_DIR, "user_head_photo_bak.png");
        File newFile = new File(AppConfig.USER_HEAD_PHOTO_DIR, AppConfig.USER_HEAD_PHOTO_FILE);
        copyfile(newFile, bakFile, true);

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        // 设置裁剪
        intent.putExtra("crop", "true");

        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX , outputY : 裁剪图片宽高
        /*
        intent.putExtra("outputX", output_X);
        intent.putExtra("outputY", output_Y);
        intent.putExtra("return-data", true);
        */
        croped_head_photo_uri = Uri.fromFile(new File(AppConfig.USER_HEAD_PHOTO_DIR,
                AppConfig.USER_HEAD_PHOTO_FILE));
        intent.putExtra("output", croped_head_photo_uri);
        intent.putExtra("outputFormat", "PNG");

        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }

    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        LogUtil.e(TAG, "setImageToHeadView --- 1");
        if (extras != null) {
            LogUtil.e(TAG, "setImageToHeadView --- 2");
            Bitmap photo = extras.getParcelable("data");

            Uri uri = Uri.fromFile(new File(AppConfig.USER_HEAD_PHOTO_DIR,
                    AppConfig.USER_HEAD_PHOTO_FILE));

            Utils.saveBitmap(photo, uri.getPath());
            LogUtil.e(TAG, "setImageToHeadView --- 3");

            setImageToHeadView(uri);
            croped_head_photo_uri = uri;
        }
    }

    private void setImageToHeadView(Uri uri) {
        /*
        File bakFile = new File(AppConfig.USER_HEAD_PHOTO_DIR, "user_head_photo_bak.png");
        Uri bakUri = Uri.fromFile(bakFile);

        user_head_photo_iv.setImageURI(bakUri);
        */
        handler.sendEmptyMessage(MSG_START_UPLOAD_IMAGE);
        user_head_photo_iv.setImageURI(uri);
        user_head_photo_select_lay.setVisibility(View.GONE);

    }

}
