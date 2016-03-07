package com.xuewen.kidsbook.utils;

import android.app.ProgressDialog;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lker_zy on 15-12-31.
 */
public class UpdateHandler {

    private static String updateUrl = "http://180.76.176.227/web/app-apk/app-debug.apk";
    private Handler mHandler;
    private ProgressDialog mProgressBar;

    public UpdateHandler(ProgressDialog pBar, Handler handler) {
        mProgressBar = pBar;
        mHandler = handler;
    }

    public void downFile(final String saveName) {
        mProgressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressBar.setTitle("正在下载");
        mProgressBar.setMessage("请稍候...");
        mProgressBar.setProgress(0);
        mProgressBar.show();

        new Thread() {
            public void run() {
                URL serverURL = null;
                try {
                    serverURL = new URL(updateUrl);
                    HttpURLConnection connect = (HttpURLConnection) serverURL.openConnection();
                    BufferedInputStream bis = new BufferedInputStream(connect.getInputStream());
                    File apkfile = new File(Environment.getExternalStorageDirectory(), saveName);
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(apkfile));

                    int fileLength = connect.getContentLength();
                    int downLength = 0;
                    int progress = 0;
                    int n;
                    byte[] buffer = new byte[40960];
                    while ((n = bis.read(buffer, 0, buffer.length)) != -1) {
                        bos.write(buffer, 0, n);
                        downLength += n;
                        progress = (int) (((float) downLength / fileLength) * 100);
                        Message msg = new Message();
                        msg.arg1 = progress;

                        mProgressBar.setProgress(progress);
                    }

                    Message msg = new Message();
                    msg.arg1 = 100;
                    mHandler.sendMessage(msg);
                    bis.close();
                    bos.close();
                    connect.disconnect();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }

        }.start();
    }

    public void down(Handler handler, final ProgressDialog pBar) {
        handler.post(new Runnable() {
            public void run() {
                pBar.cancel();
                //update();
            }
        });
    }



    public static boolean isNeedUpdate() {

        VersionUpdateJsonBean updateJsonBean = Version.checkNewVersion(); // 最新版本的版本号
        if (updateJsonBean ==  null) {
            return false;
        }

        LogUtil.i("need update : ", updateJsonBean.isNeedUpdate() ? "true" : "false");

        if (updateJsonBean.isNeedUpdate()) {
            LogUtil.i("need update. latest version : " , updateJsonBean.getVersion() +  " ("  + updateJsonBean.getVerDesc() + ")");
            return true;
        }

        return false;
    }

}
