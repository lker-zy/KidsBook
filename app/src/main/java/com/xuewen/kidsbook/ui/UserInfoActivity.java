package com.xuewen.kidsbook.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xuewen.kidsbook.R;

import java.io.File;

import butterknife.Bind;

import static com.xuewen.kidsbook.utils.Utils.isMediaOk;

/**
 * Created by lker_zy on 16-5-15.
 */
public class UserInfoActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.common_title_left_btn) LinearLayout back_btn;
    @Bind(R.id.common_title_left_btn_icon) ImageView back_icon;

    @Bind(R.id.user_head_photo) SimpleDraweeView user_head_photo_iv;
    @Bind(R.id.user_photo_parent_layout) RelativeLayout user_head_photo_select_lay;
    @Bind(R.id.btn_photo_camera) Button photo_camera_btn;
    @Bind(R.id.btn_photo_gallery) Button photo_gallery_btn;
    @Bind(R.id.cancle) Button user_head_photo_cancel;

    @Bind(R.id.usr_info_nick_member_text) TextView nick_name_tv;
    @Bind(R.id.usr_info_nick_member_et) EditText nick_name_et;
    @Bind(R.id.usr_info_nick_name_edit_icon) ImageView nick_name_edit_icon;
    @Bind(R.id.usr_info_nick_name_submit_btn) Button nick_name_submit_btn;

    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final String IMAGE_FILE_NAME = "temp_head_image.jpg";

    // 裁剪后图片的宽(X)和高(Y),480 X 480的正方形。
    private static int output_X = 480;
    private static int output_Y = 480;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user_head_photo_iv.setOnClickListener(this);
        user_head_photo_cancel.setOnClickListener(this);
        photo_camera_btn.setOnClickListener(this);
        photo_gallery_btn.setOnClickListener(this);

        nick_name_edit_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nick_name_tv.setVisibility(View.GONE);
                nick_name_et.setVisibility(View.VISIBLE);
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
        }
    }

    // 从本地相册选取图片作为头像
    private void choseHeadImageFromGallery() {
        Intent intentFromGallery = new Intent();
        // 设置文件类型
        intentFromGallery.setType("image/*");
        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
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
                cropRawPhoto(intent.getData());
                break;

            case CODE_CAMERA_REQUEST:
                if (isMediaOk()) {
                    File tempFile = new File(
                            Environment.getExternalStorageDirectory(),
                            IMAGE_FILE_NAME);
                    cropRawPhoto(Uri.fromFile(tempFile));
                } else {
                    Toast.makeText(getApplication(), "没有SDCard!", Toast.LENGTH_LONG)
                            .show();
                }

                break;

            case CODE_RESULT_REQUEST:
                if (intent != null) {
                    setImageToHeadView(intent);
                }

                break;
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }

    /**
     * 裁剪原始的图片
     */
    public void cropRawPhoto(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        // 设置裁剪
        intent.putExtra("crop", "true");

        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", output_X);
        intent.putExtra("outputY", output_Y);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }

    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            user_head_photo_iv.setImageBitmap(photo);
        }
    }

}
