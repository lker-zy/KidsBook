package com.xuewen.kidsbook.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xuewen.kidsbook.AppConfig;
import com.xuewen.kidsbook.KidsBookApplication;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lker_zy on 15-12-31.
 */
public class Version {
    private static final String TAG = Version.class.getSimpleName();
    private static String localVersion = "unknown";

    private static String versionDesc;
    private static String remoteVersion;

    public static String getRemoteVersion() {
        return remoteVersion;
    }

    public static String getVersionDesc() {
        return versionDesc;
    }

    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public static String getLocalVersion() {
        try {
            Context context = KidsBookApplication.getInstance().getApplicationContext();
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            localVersion = info.versionName;
            return localVersion;
        } catch (Exception e) {
            e.printStackTrace();
            return "unknown";
        }
    }

    public static VersionUpdateJsonBean checkNewVersion() {
        String versionQueryUrl  = AppConfig.UPDATE_URL + "?version=" + localVersion;

        String content = Network.simpleDownload(versionQueryUrl);
        LogUtil.d(TAG, content);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        VersionUpdateJsonBean updateJsonBean = null;
        try {
            updateJsonBean = objectMapper.readValue(content, VersionUpdateJsonBean.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        Version.remoteVersion = updateJsonBean.getVersion();
        Version.versionDesc = updateJsonBean.getVerDesc();
        Version.versionDesc = "1. 增加活动模块\n2. 更新众读申请详细页面";
        return updateJsonBean;
    }
}
