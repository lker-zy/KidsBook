package com.xuewen.kidsbook;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.xuewen.kidsbook.utils.LogUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by lker_zy on 15-12-18.
 */


public class KidsBookApplication extends Application {
    private static final String TAG = "KidsBookApplication";
    public static Context context;
    public static Map<String, Activity> activitys = new HashMap<String, Activity>();
    /**
     * 当前系统保存的所有用户
     */
    public static List<String> USER_NAME_LIST;
    /**
     * 当前应用状态
     */
    public static AppStatus APP_STATUS = AppStatus.OFFLINE;
    /**
     * 程序句柄 YQH 20120706
     */
    private static KidsBookApplication instance = null;
    /**
     * 主线程刷新界面handler YQH 20120706
     */
    private Handler handler = null;

    /**
     * 设置信息
     **/
    //public static Settings settings;
    public KidsBookApplication() {
        super();
        instance = this;
    }

    // 程序句柄获取函数 YQH 20120706
    public static KidsBookApplication getInstance() {
        return instance;
    }

    /**
     * 是否在线
     *
     * @return
     */
    public static boolean isOnline() {
        boolean isOnLine = false;

        return isOnLine;
    }

    public Handler getHandler() {
        if (handler == null) {
            handler = new Handler();
        }
        return handler;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.i(TAG, "onCreate当前的程序:" + android.os.Process.myPid());
        context = this;
        // MessageWait.getInstance().start();
        // 禁用 IPV6
        System.setProperty("java.net.preferIPv6Addresses", "false");
        //USER_NAME_LIST = PreferenceUtil.getSavedUserList();
        // 初始化声音
        //SoundManager.getInstance();
        // 创建图片文件夹
        // 初始化 主线程刷新界面handler YQH 20120706
        handler = new Handler();
        initFolder();
        // recordLogInfo();
    }

    private void initFolder() {

    }

    /**
     * 非主线程刷新界面调用函数 YQH 20120706
     *
     * @param r
     */
    public void runOnUIThread(Runnable r) {
        handler.post(r);
    }

    /**
     * 客户端崩溃添加txt文本日志到本地
     */
    private void recordLogInfo() {

    }

    public void addActivity(Activity activity) {
        String name = activity.getClass().getName();

        Iterator<Map.Entry<String, Activity>> it = activitys.entrySet().iterator();

        for (; it.hasNext(); ) {
            Map.Entry<String, Activity> entry = it.next();
            if (name.equals(entry.getKey())) {
                it.remove();
                break;
            }
        }

        activitys.put(name, activity);
    }

    public enum AppStatus {
        /**
         * 没有网络
         */
        NO_NETWORK,

        /**
         * 连接中
         **/
        CONNECTING,
        /**
         * 有网络，但无法访问服务器，进行重试
         **/
        RETRY,
        /**
         * 重试超时，停止链接
         **/
        NOT_CONNECTION,
        /** 链接出错 **/
        // CONNECTION_ERROE,
        /** 链接关闭 **/
        // CONNECTION_CLOSE,
        /**
         * 握手中
         **/
        HANDSHACK,
        /**
         * 链接完成，握手完成
         **/
        CONNECTION_OK,
        /** 登录中 **/
        // LOGINING,
        /**
         * 登录失败
         **/
        LOGINFAILD,
        /**
         * 已经登录成功
         **/
        LOGIN_READLY,
        /**
         * 已经登出
         **/
        OFFLINE;
    }

}
