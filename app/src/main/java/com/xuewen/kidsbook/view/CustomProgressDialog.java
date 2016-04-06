package com.xuewen.kidsbook.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;

import com.xuewen.kidsbook.R;

/**
 * Created by lker_zy on 16-4-4.
 */

public class CustomProgressDialog extends Dialog {
    private Context context = null;
    //private static AnimationDrawable animationDrawable;

    public CustomProgressDialog(Context context) {
        super(context);
        this.context = context;
    }

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    public static CustomProgressDialog createDialog(Context context) {
        CustomProgressDialog customProgressDialog = null;
        customProgressDialog = new CustomProgressDialog(context,
                R.style.CustomProgressDialog);
        customProgressDialog.setCancelable(true);// 璁剧疆鐐瑰嚮绌虹櫧鍖哄煙鍜岃繑鍥為敭涓嶆秷澶�
        customProgressDialog.setContentView(R.layout.loading_dialog_layout);
        customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        customProgressDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

//		ImageView imageView = (ImageView) customProgressDialog
//				.findViewById(R.id.loading_img);
        //animationDrawable = (AnimationDrawable) imageView.getBackground();
        return customProgressDialog;
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub
        //animationDrawable.start();
        super.show();
    }

    @Override
    public void dismiss() {
        // TODO Auto-generated method stub
        //animationDrawable.stop();
        super.dismiss();
    }
}
