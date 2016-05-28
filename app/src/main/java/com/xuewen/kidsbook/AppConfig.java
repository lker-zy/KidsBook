package com.xuewen.kidsbook;

import android.os.Environment;

/**
 * Created by lker_zy on 16-3-29.
 */
public class AppConfig {
    public static String SEARCH_URL = "http://180.76.176.227/web/search.action";
    public static String DAILY_BOOKS_URL = "http://180.76.176.227/web/dailyBooks";
    public static String ESSENCE_LIST_URL = "http://180.76.176.227/web/listEssence";
    public static String ESSENCE_DETAIL_URL = "http://180.76.176.227/web/detailEssence";
    public static String WEB_ESSENCE_DETAIL_URL = "http://180.76.176.227/web/detailEssenceWeb";
    public static String UPDATE_URL = "http://180.76.176.227/web/checkUpdate";
    public static String APK_DOWNLOAD_URL = "http://180.76.176.227/web/app-apk/app-debug.apk";
    public static String LOGIN_URL = "http://180.76.176.227/user/login";
    //public static String LOGIN_URL = "http://192.168.1.103:8080/user/login";
    public static String USER_REGISTER_URL = "http://180.76.176.227/user/reg";
    public static String LIST_ACTIVITY_URL = "http://180.76.176.227/web/listActivity";
    public static String LIST_CROWD_APPLY_URL = "http://180.76.176.227/web/listCrowdApply";
    public static String LIST_CROWD_REPORTS_URL = "http://180.76.176.227/web/listReports";
    public static String REPORTS_OVERVIEW_URL = "http://180.76.176.227/web/reportsOverview";


    public static String STORAGE_BASE = "XuewenOnline/KidsBook";
    public static String CACHE_BASE = STORAGE_BASE + "/cache";

    public static int DB_VERSION_4_BOOK_COLLECTION = 4;
    public static int DB_VERSION_4_ESSENCE_COLLECTION = 1;

    public static String USER_HEAD_PHOTO_DIR = Environment.getExternalStorageDirectory() + "/" + STORAGE_BASE;
    public static String USER_HEAD_PHOTO_FILE = "user_head_photo.png";
    public static String USER_HEAD_PHOTO_PATH = USER_HEAD_PHOTO_DIR + "/" + USER_HEAD_PHOTO_FILE;
}
