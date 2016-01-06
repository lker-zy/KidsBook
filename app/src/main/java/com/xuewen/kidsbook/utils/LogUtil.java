package com.xuewen.kidsbook.utils;

import android.os.Environment;
import android.util.Log;

import com.xuewen.kidsbook.KidsBookApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Date;


/**
 * Created by lker_zy on 15-12-18.
 */


public class LogUtil {
    final static String TAG = LogUtil.class.getSimpleName();
    // public final static boolean LOG_ENABLED = true;

    final static int LOGCAT_LEVEL = 16;// logcat level
    // final static int LOGCAT_LEVEL = 2;// logcat level
    final static int FILE_LOG_LEVEL = 2;// log file level, must >= LOGCAT_LEVEL

    final static int LOG_LEVEL_ERROR = 16;
    public final static boolean ERROR = (LOGCAT_LEVEL <= LOG_LEVEL_ERROR);
    final static int LOG_LEVEL_WARN = 8;
    public final static boolean WARN = (LOGCAT_LEVEL <= LOG_LEVEL_WARN);
    final static int LOG_LEVEL_INFO = 4;
    public final static boolean INFO = (LOGCAT_LEVEL <= LOG_LEVEL_INFO);
    final static int LOG_LEVEL_DEBUG = 2;
    public final static boolean DEBUG = (LOGCAT_LEVEL <= LOG_LEVEL_DEBUG);
    final static String LOG_FILE_NAME = "kids_read.log";
    final static String LOG_ENTRY_FORMAT = "[%tF %tT][%s][%s]%s"; // [2010-01-22
    private final static String LOG_TAG_STRING = "KidsRead";
    // 13:39:1][D][com.a.c]error
    // occured
    static PrintStream logStream;

    static boolean initialized = false;

    public static void d(String tag, String msg) {
        if (DEBUG) {
            tag = Thread.currentThread().getName() + ":" + tag;
            Log.d(LOG_TAG_STRING, tag + " [" + KidsBookApplication.APP_STATUS + "]: " + msg);
            if (FILE_LOG_LEVEL <= LOG_LEVEL_DEBUG)
                write("D", tag, msg, null);
        }
    }

    public static void d(String tag, String msg, Throwable error) {
        if (DEBUG) {
            tag = Thread.currentThread().getName() + ":" + tag;
            Log.d(LOG_TAG_STRING, tag + " [" + KidsBookApplication.APP_STATUS + "]: " + msg, error);
            if (FILE_LOG_LEVEL <= LOG_LEVEL_DEBUG)
                write("D", tag, msg, error);
        }
    }

    public static void v(String tag, String msg) {
        if (DEBUG) {
            tag = Thread.currentThread().getName() + ":" + tag;
            Log.v(LOG_TAG_STRING, tag + " [" + KidsBookApplication.APP_STATUS + "]: " + msg);
            if (FILE_LOG_LEVEL <= LOG_LEVEL_DEBUG)
                write("V", tag, msg, null);
        }
    }

    public static void v(String tag, String msg, Throwable error) {
        if (DEBUG) {
            tag = Thread.currentThread().getName() + ":" + tag;
            Log.v(LOG_TAG_STRING, tag + " [" + KidsBookApplication.APP_STATUS + "]: " + msg, error);
            if (FILE_LOG_LEVEL <= LOG_LEVEL_DEBUG)
                write("V", tag, msg, error);
        }
    }

    public static void i(String tag, String msg) {
        if (INFO) {
            tag = Thread.currentThread().getName() + ":" + tag;
            Log.i(LOG_TAG_STRING, tag + " [" + KidsBookApplication.APP_STATUS + "]: " + msg);
            if (FILE_LOG_LEVEL <= LOG_LEVEL_INFO)
                write("I", tag, msg, null);
        }
    }

    public static void I(String tag, String msg) {
        if (INFO) {
            tag = Thread.currentThread().getName() + ":" + tag;
            Log.i(tag, msg);
            if (FILE_LOG_LEVEL <= LOG_LEVEL_INFO)
                write("I", tag, msg, null);
        }
    }

    public static void i(String rz, String tag, String msg) {
        if (INFO) {
            tag = Thread.currentThread().getName() + ":" + tag;
            Log.i(rz, tag + " [" + KidsBookApplication.APP_STATUS + "]: " + msg);
            if (FILE_LOG_LEVEL <= LOG_LEVEL_INFO)
                write("I", tag, msg, null);
        }
    }

    public static void i(String tag, String msg, Throwable error) {
        if (INFO) {
            tag = Thread.currentThread().getName() + ":" + tag;
            Log.i(LOG_TAG_STRING, tag + " [" + KidsBookApplication.APP_STATUS + "]: " + msg, error);
            if (FILE_LOG_LEVEL <= LOG_LEVEL_INFO)
                write("I", tag, msg, error);
        }
    }

    public static void w(String tag, String msg) {
        if (WARN) {
            tag = Thread.currentThread().getName() + ":" + tag;
            Log.w(LOG_TAG_STRING, tag + " [" + KidsBookApplication.APP_STATUS + "]: " + msg);
            if (FILE_LOG_LEVEL <= LOG_LEVEL_WARN)
                write("W", tag, msg, null);
        }
    }

    public static void w(String tag, String msg, Throwable error) {
        if (WARN) {
            tag = Thread.currentThread().getName() + ":" + tag;
            Log.w(LOG_TAG_STRING, tag + " [" + KidsBookApplication.APP_STATUS + "]: " + msg, error);
            if (FILE_LOG_LEVEL <= LOG_LEVEL_WARN)
                write("W", tag, msg, error);
        }
    }

    public static void e(String tag, String msg) {
        if (ERROR) {
            tag = Thread.currentThread().getName() + ":" + tag;
            Log.e(LOG_TAG_STRING, tag + " [" + KidsBookApplication.APP_STATUS + "]: " + msg);

            if (FILE_LOG_LEVEL <= LOG_LEVEL_ERROR)
                write("E", tag, msg, null);
        }
    }

    public static void e(String tag, String msg, Throwable error) {
        if (ERROR) {
            tag = Thread.currentThread().getName() + ":" + tag;
            Log.e(LOG_TAG_STRING, tag + " [" + KidsBookApplication.APP_STATUS + "]: " + msg, error);

            if (FILE_LOG_LEVEL <= LOG_LEVEL_ERROR)
                write("E", tag, msg, error);
        }
    }

    public static void wtf(String tag, String msg) {
        if (ERROR) {
            tag = Thread.currentThread().getName() + ":" + tag;
            Log.wtf(LOG_TAG_STRING, tag + " [" + KidsBookApplication.APP_STATUS + "]: " + msg);

            if (FILE_LOG_LEVEL <= LOG_LEVEL_ERROR)
                write("E", tag, msg, null);
        }
    }

    public static void wtf(String tag, String msg, Throwable error) {
        if (ERROR) {
            tag = Thread.currentThread().getName() + ":" + tag;
            Log.wtf(LOG_TAG_STRING, tag + " [" + KidsBookApplication.APP_STATUS + "]: " + msg, error);

            if (FILE_LOG_LEVEL <= LOG_LEVEL_ERROR)
                write("E", tag, msg, error);
        }
    }

    private static void write(String level, String tag, String msg, Throwable error) {
        if (!initialized)
            init();
        if (logStream == null || logStream.checkError()) {
            initialized = false;
            return;
        }
        Date now = new Date();
        logStream.printf(LOG_ENTRY_FORMAT, now, now, level, tag, " [" + KidsBookApplication.APP_STATUS + "]: " + msg);
        logStream.println();

        if (error != null) {
            error.printStackTrace(logStream);
            logStream.println();
        }
    }

    public static synchronized void init() {
        if (initialized)
            return;
        try {
            File sdRoot = getSDRootFile();
            if (sdRoot != null) {
                File logFile = new File(sdRoot, LOG_FILE_NAME);
                logFile.createNewFile();

                Log.d(LOG_TAG_STRING, TAG + " : Log to file : " + logFile);
                if (logStream != null) {
                    logStream.close();
                }
                logStream = new PrintStream(new FileOutputStream(logFile, true), true);
                initialized = true;
            }
        } catch (Exception e) {
            Log.e(LOG_TAG_STRING, "init log stream failed", e);
        }
    }

    public static boolean isSdCardAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static File getSDRootFile() {
        if (isSdCardAvailable()) {
            return Environment.getExternalStorageDirectory();
        } else {
            return null;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (logStream != null)
            logStream.close();
    }

}

