package com.xuewen.kidsbook.service.beans;

import com.xuewen.kidsbook.AppConfig;

/**
 * Created by lker_zy on 16-4-22.
 */
public class ConfigBean {
    private String SEARCH_URL = "http://180.76.176.227/web/search.action";
    private String UPDATE_URL = "http://180.76.176.227/web/checkUpdate";
    private String APK_DOWNLOAD_URL = "http://180.76.176.227/web/app-apk/app-debug.apk";
    private String LOGIN_URL = "http://180.76.176.227/user/login";
    private String USER_REGISTER_URL = "http://180.76.176.227/user/reg";

    public String getSearchUrl() {
        return SEARCH_URL;
    }

    public void setSearchUrl(String SEARCH_URL) {
        this.SEARCH_URL = SEARCH_URL;
        AppConfig.SEARCH_URL = SEARCH_URL;
    }

    public String getUpdateUrl() {
        return UPDATE_URL;
    }

    public void setUpdateUrl(String UPDATE_URL) {
        this.UPDATE_URL = UPDATE_URL;
        AppConfig.UPDATE_URL = UPDATE_URL;
    }

    public String getApkDownloadUrl() {
        return APK_DOWNLOAD_URL;
    }

    public void setApkDownloadUrl(String APK_DOWNLOAD_URL) {
        this.APK_DOWNLOAD_URL = APK_DOWNLOAD_URL;
        AppConfig.APK_DOWNLOAD_URL = APK_DOWNLOAD_URL;
    }

    public String getLoginUrl() {
        return LOGIN_URL;
    }

    public void setLoginUrl(String LOGIN_URL) {
        this.LOGIN_URL = LOGIN_URL;
        AppConfig.LOGIN_URL = LOGIN_URL;
    }

    public String getUserRegisterUrl() {
        return USER_REGISTER_URL;
    }

    public void setUserRegisterUrl(String USER_REGISTER_URL) {
        this.USER_REGISTER_URL = USER_REGISTER_URL;
        AppConfig.USER_REGISTER_URL = USER_REGISTER_URL;
    }
}
