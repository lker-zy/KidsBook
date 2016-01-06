package com.xuewen.kidsbook.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Message;

import com.xuewen.kidsbook.KidsBookApplication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lker_zy on 15-12-31.
 */
public class Version {
    private static String versionUrl = "";

    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public static String getLocalVersion() {
        try {
            Context context = KidsBookApplication.getInstance().getApplicationContext();
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "unknow";
        }
    }

    public static String getRemoteVersion() {
        URL serverURL = null;
        try {
            serverURL = new URL(versionUrl);
            HttpURLConnection connect = (HttpURLConnection) serverURL.openConnection();
            BufferedInputStream bis = new BufferedInputStream(connect.getInputStream());

            int fileLength = connect.getContentLength();
            int downLength = 0;
            String version = "";

            int n;
            byte[] buffer = new byte[1024];
            while ((n = bis.read(buffer, 0, buffer.length)) != -1) {
                downLength += n;
                version += new String(buffer);
            }

            if (downLength != fileLength) {
                return "0.0";
            }

            bis.close();
            connect.disconnect();

            return version;
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return "1.1.2";
    }

    public boolean checkNewVersion() {
        return true;
    }
}
