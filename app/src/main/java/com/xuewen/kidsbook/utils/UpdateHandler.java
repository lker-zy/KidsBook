package com.xuewen.kidsbook.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;

import com.xuewen.kidsbook.AppConfig;
import com.xuewen.kidsbook.Const;
import com.xuewen.kidsbook.KidsBookApplication;
import com.xuewen.kidsbook.service.ConfigService;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by lker_zy on 15-12-31.
 */
public class UpdateHandler {

    private Handler mHandler;
    private ProgressDialog mProgressBar;

    public UpdateHandler(ProgressDialog pBar, Handler handler) {
        mProgressBar = pBar;
        mHandler = handler;
    }

    private void downToSdcard(final String saveName, HttpURLConnection connect) throws Exception {
        BufferedInputStream bis = new BufferedInputStream(connect.getInputStream());
        File apkfile = new File(Environment.getExternalStorageDirectory() + "/" + AppConfig.CACHE_BASE, saveName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(apkfile));

        int fileLength = connect.getContentLength();
        int downLength = 0;
        int n;

        byte[] buffer = new byte[40960];
        while ((n = bis.read(buffer, 0, buffer.length)) != -1) {
            bos.write(buffer, 0, n);
            downLength += n;
            int progress = (int) (((float) downLength / fileLength) * 100);
            mProgressBar.setProgress(progress);
        }

        bis.close();
        bos.close();
    }

    private void downToPackageFiles(final String saveName, HttpURLConnection connect) throws Exception {
        BufferedInputStream bis = new BufferedInputStream(connect.getInputStream());
        Context context = KidsBookApplication.getInstance().getContext();
        context.deleteFile(saveName);
        FileOutputStream outputStream = KidsBookApplication.getInstance().getContext().openFileOutput(saveName, Context.MODE_APPEND + Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);

        int fileLength = connect.getContentLength();
        int downLength = 0;
        int n;
        byte[] buffer = new byte[40960];
        while ((n = bis.read(buffer, 0, buffer.length)) != -1) {
            outputStream.write(buffer, 0, n);
            downLength += n;
            int progress = (int) (((float) downLength / fileLength) * 100);
            mProgressBar.setProgress(progress);
        }

        bis.close();
        outputStream.close();
    }

    public void downFile(final String saveName, final boolean sdcard, final boolean dev) {
        mProgressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressBar.setTitle("正在下载");
        mProgressBar.setMessage("请稍候...");
        mProgressBar.setProgress(0);
        mProgressBar.show();

        new Thread() {
            @Override
            public void run() {
                try {
                    if (dev) {
                        (new ConfigService()).changeToTestEnv();
                    }
                    URL serverURL = new URL(AppConfig.APK_DOWNLOAD_URL);
                    HttpURLConnection connect = (HttpURLConnection) serverURL.openConnection();
                    connect.setConnectTimeout(3000);
                    connect.setReadTimeout(3000);

                    /*
                    if (sdcard) {
                        downToSdcard(saveName, connect);
                    } else {
                        downToPackageFiles(saveName, connect);
                    }
                    */
                    downToPackageFiles(saveName, connect);

                    mHandler.sendEmptyMessage(Const.MSG_DOWN_UPDATE_DONE);
                    connect.disconnect();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    mProgressBar.setMessage(e.toString());
                    mHandler.sendEmptyMessage(Const.MSG_DOWN_UPDATE_DONE);
                } catch (IOException e) {
                    e.printStackTrace();
                    mProgressBar.setMessage(e.toString());
                    mHandler.sendEmptyMessage(Const.MSG_DOWN_UPDATE_DONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    mProgressBar.setMessage(e.toString());
                    mHandler.sendEmptyMessage(Const.MSG_DOWN_UPDATE_DONE);
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
