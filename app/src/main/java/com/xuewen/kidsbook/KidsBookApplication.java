package com.xuewen.kidsbook;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.xuewen.kidsbook.net.GlobalVolley;
import com.xuewen.kidsbook.service.ConfigService;
import com.xuewen.kidsbook.service.LoginService;
import com.xuewen.kidsbook.utils.LogUtil;
import com.xuewen.kidsbook.utils.StorageUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by lker_zy on 15-12-18.
 */


public class KidsBookApplication extends Application {
    private static final String TAG = "KidsBookApplication";
    public static Map<String, Activity> activitys = new HashMap<String, Activity>();
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

    public Context getContext() {
        return getApplicationContext();
    }

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
        System.setProperty("java.net.preferIPv6Addresses", "false");
        handler = new Handler();

        initImageLoader(getApplicationContext());
        initGlobalVolley();

        new LoginService();

        //File appStorageLoc = StorageUtils.getDirectory(getApplicationContext(), AppConfig.STORAGE_BASE);
        File appCacheLoc = StorageUtils.getDirectory(getApplicationContext(), AppConfig.CACHE_BASE);
    }

    private void initGlobalVolley() {
        GlobalVolley.initialize();
    }

    public static void initImageLoader(Context context) {
        //缓存文件的目录
        //File cacheDir = StorageUtils.getOwnCacheDirectory(context, "universalimageloader/Cache");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(3) //线程池内线程的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()) //将保存的时候的URI名称用MD5 加密
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024) // 内存缓存的最大值
                .diskCacheSize(50 * 1024 * 1024)  // SD卡缓存的最大值
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                        // 由原先的discCache -> diskCache
                //.diskCache(new UnlimitedDiscCache(cacheDir))//自定义缓存路径
                .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs() // Remove for release app
                .build();
        //全局初始化此配置
        ImageLoader.getInstance().init(config);
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
        NO_NETWORK,
        LOGINING,
        LOGINFAILD,
        LOGIN_READLY,
        OFFLINE;
    }

}
