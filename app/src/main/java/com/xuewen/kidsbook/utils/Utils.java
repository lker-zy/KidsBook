package com.xuewen.kidsbook.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.xuewen.kidsbook.KidsBookApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lker_zy on 16-3-26.
 */

public class Utils {

    public static boolean isNetWorkAvaliable(Context context) {
        ConnectivityManager mConnectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = mConnectivity.getActiveNetworkInfo();
        if (info == null) {
            return false;
        } else {
            int netType = info.getType();
            return netType == ConnectivityManager.TYPE_WIFI ? info.isConnected() : (netType == ConnectivityManager.TYPE_MOBILE ? info.isConnected() : false);
        }
    }


    public static boolean equalsIncludeNULL(String str1, String str2){
        if(TextUtils.isEmpty(str1) && TextUtils.isEmpty(str2)){
            return true;
        }
        else if(!TextUtils.isEmpty(str1) && !TextUtils.isEmpty(str2)){
            return str1.equals(str2);
        }
        else{
            return false;
        }
    }

    /**
     * 获取系统版本
     * @return
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    public static String getModel(Context context) {
        return android.os.Build.MODEL != null ? android.os.Build.MODEL.replace(" ", "") : "unknown";
    }

    /**
     * 获取屏幕像素
     * @param context
     * @return
     */
    public static String getScreen(Context context) {
        return getScreenWidth(context) + "*" + getScreenHeight(context);
    }

    /**
     * 获取屏幕宽度
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        try {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            return metrics.widthPixels;
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 获取屏幕高度
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        try {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            return metrics.heightPixels;
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * dip/dp转像素
     *
     * @param context  context
     * @param dipValue dip或 dp大小
     * @return 像素值
     */
    public static int dip2px(Context context, float dipValue) {
        try {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dipValue * scale + 0.5f);
        } catch (Exception e) {
        }
        return 0;
    }

    public static final String PIC_WIDTH_TAG = "<wm[width]wm>";
    public static final String PIC_HEIGHT_TAG = "<wm[height]wm>";
    private static final String THUMBNAILS_URL = "http://webmap1.map.bdimg.com/maps/services/thumbnails?";
    /**
     * 切图服务url拼接
     * @param url
     * @param width
     * @param height
     * @return
     */
    public static String convertURLNew(String url, int width, int height) {
        if(!TextUtils.isEmpty(url)){
            String imageUrl = url;
            if (imageUrl.contains(PIC_WIDTH_TAG) || imageUrl.contains(PIC_HEIGHT_TAG)) {
                imageUrl = imageUrl.replace(PIC_WIDTH_TAG, width + "");
                imageUrl = imageUrl.replace(PIC_HEIGHT_TAG, height + "");
            } else {
                imageUrl = THUMBNAILS_URL + "align=middle,middle&width=" + width + "&height=" + height +
                        "&quality=80&src=" + url;
            }
            return imageUrl;
        }
        else{
            return url;
        }
    }

    /**
     * 返回不带.0的字符串，如20.0转成20
     * @param s
     * @return
     */
    public static String getStringWithoutDot0(String s) {
        // return removeZeroAfterDot(s);
        try {
            if (TextUtils.isEmpty(s))
                return "";
            if (s.length() < 3)
                return s;
            int indexOfPoint = s.indexOf(".");
            String dot = s.substring(indexOfPoint + 1);
            if (Double.valueOf(dot) == 0) {
                return s.substring(0, indexOfPoint);
            }
            return s;
        } catch (Exception e) {
            return s;
        }
    }

    /**
     * md5 加密
     *
     * @param str
     * @return
     */
    public static String md5Encode(String str) {
        StringBuffer buf = new StringBuffer();
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(str.getBytes());
            byte bytes[] = md5.digest();
            for (int i = 0; i < bytes.length; i++) {
                String s = Integer.toHexString(bytes[i] & 0xff);
                if (s.length() == 1) {
                    buf.append("0");
                }
                buf.append(s);
            }

        } catch (Exception ex) {
        }
        return buf.toString();
    }

    /**
     * 是否为飞行模式
     * @param context
     * @return
     */
    public static boolean IsAirModeOn(Context context) {
        return (android.provider.Settings.System.getInt(context.getContentResolver(),
                android.provider.Settings.System.AIRPLANE_MODE_ON, 0) == 1 ? true : false);
    }

    /**
     * 移动连接
     */
    public static final int MOBILE_CONNECTED = 1;
    /**
     * wifi连接
     */
    public static final int WIFI_CONNECTED = 2;
    /**
     * 网络不可用
     */
    public static final int DISCONNECTED_NETWORK = 0;
    /**
     * 3G网络
     */
    public static final int MOBILE_CONNECTED_UNDER_3G = 3;

    /**
     * 返回网络状态
     * @param context
     * @return
     */
    public static int checkNetStatus(Context context) {
        if (context == null) {
            return MOBILE_CONNECTED;
        }
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // 如果3G、wifi、2G等网络状态是连接的，则退出，否则显示提示信息进入网络设置界面
        NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileInfo != null) {
            NetworkInfo.State mobile = mobileInfo.getState();
            if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING)
                return MOBILE_CONNECTED;
        }

        NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null) {
            NetworkInfo.State wifi = wifiInfo.getState();
            if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING)
                return WIFI_CONNECTED;
        }
        // showTips();
        return DISCONNECTED_NETWORK;
    }

    /**
     * 获取屏幕到view的距离
     * @param view
     * @return
     */
    public static int findViewTopOnScreen(View view) {
        int[] locOnSrc = new int[2];
        view.getLocationOnScreen(locOnSrc);
        return locOnSrc[1];
    }

    /**
     * 移除回车换行符
     * @param source
     * @return
     */
    public static String removeLineBreak(String source) {
        if (TextUtils.isEmpty(source)) {
            return "";
        } else {
            return source.trim().replaceAll("\n", "").replaceAll("\n", "\r");
        }
    }

    /**
     * 判断list有没有内容
     *
     * @param list
     * @return false 表示空 return true 表示不为空
     */
    public static boolean hasContent(List<?> list) {
        if (list == null || list.isEmpty() || list.size() <= 0) {
            return false;
        }
        return true;
    }

    /**
     * 返回一个带有透明度的的颜色
     * @param alpha 透明度百分比
     * @param baseColor
     * @return
     */
    public static int getColorWithAlpha(float alpha, int baseColor) {
        int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
        int rgb = 0x00ffffff & baseColor;
        return a + rgb;
    }

    /**
     * 停止list的滚动
     * @param view
     */
    public static void stopScroll(AbsListView view) {// http://stackoverflow.com/questions/6369491/stop-listview-scroll-animation
        // http://stackoverflow.com/questions/7819145/stop-scrolling-in-a-listview

        try {
            view.smoothScrollBy(0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 另一种实现
//		try {
//			Field field = android.widget.AbsListView.class
//					.getDeclaredField("mFlingRunnable");
//			field.setAccessible(true);
//			Object flingRunnable = field.get(view);
//			if (flingRunnable != null) {
//				Method method = Class.forName(
//						"android.widget.AbsListView$FlingRunnable")
//						.getDeclaredMethod("endFling");
//				method.setAccessible(true);
//				method.invoke(flingRunnable);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
    }

//    public static void sendStatistic(String actName, String actType) {
//
//        try {
//            HostBridge.sendStatistic(actName,actType);
//        } catch (Exception e) {
//
//        }
//
//    }
//
//    public static void sendNewStatistic(String reference,String lastReference,String action,String extString) {
//
//        try {
//            HostBridge.sendNewStatistic(reference,lastReference,action,extString);
//        } catch (Exception e) {
//
//        }
//
//    }

    /**
     * float精度问题修复
     *
     * @param
     * @return
     */
    public static String multiply(double v1, double v2) {
        String price = "";
        try {
            BigDecimal b1 = new BigDecimal(Double.toString(v1));
            BigDecimal b2 = new BigDecimal(Double.toString(v2));
            price = String.valueOf(b1.multiply(b2).doubleValue());
        } catch (Exception e) {
            price = String.valueOf((float)v1 * (float)v2);
        }
        return price;
    }

    /**
     * @param time 单位，秒
     * @return 分，秒  eg:06:12
     */
    public static String formatMMss(long time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
            return sdf.format(new Date(time * 1000L));
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * @param time 单位，毫秒
     * @return 分，秒  eg:06:12
     */
    public static String formathhMM(String time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");//kk:mm
            return sdf.format(new Date(Long.valueOf(time) * 1000L));
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 键盘弹出
     * @param activity
     */
    public static void showInputMethod(final Activity activity) {
        if (activity == null)
            return;
        InputMethodManager inputMethodManager =
                ((InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (activity.getCurrentFocus() != null) {
            inputMethodManager.showSoftInput(activity.getCurrentFocus(), 0);
        }
    }

    /**
     * 隐藏键盘
     * @param activity
     */
    public static void hideInputMethod(final Activity activity) {
        if (activity == null)
            return;
        InputMethodManager inputMethodManager =
                ((InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * 判断键盘是否弹出
     * @param activity
     * @return
     */
    public static boolean isShowInputMethod(final Activity activity) {
        if (activity == null)
            return false;
        InputMethodManager inputMethodManager =
                ((InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE));
        return inputMethodManager.isActive();
    }

    /**
     * 根据json对象里的key来获取value
     * @param object
     * @param key
     * @return
     */
    public static String getValue(JSONObject object, String key) {
        if (object == null || TextUtils.isEmpty(key)) {
            return "";
        } else {
            return object.optString(key);
        }
    }

    /**
     * 结束activity
     * @param act
     */
    public static void backWithAnim(Activity act) {
        act.finish();
//        act.overridePendingTransition(R.anim.in_left_right, R.anim.out_left_right);
    }

    public static void startActivityWithAnim(Activity act, Class<?> cla) {
        Intent intent = new Intent(act, cla);
        act.startActivity(intent);
//        act.overridePendingTransition(R.anim.in_right_left, R.anim.out_right_left);
    }

    public static void startActivityForResultWithAnim(Activity act, Class<?> cla, int requestCode) {
        Intent intent = new Intent(act, cla);
        act.startActivityForResult(intent, requestCode);
//        act.overridePendingTransition(R.anim.in_right_left, R.anim.out_right_left);
    }

    public static void startActivityForResultWithAnim(Activity act, Intent intent, int requestCode) {
        act.startActivityForResult(intent, requestCode);
//        act.overridePendingTransition(R.anim.in_right_left, R.anim.out_right_left);
    }

    /**
     * 移除末尾的0
     * @param s
     * @return
     */
    public static String removeZeroAfterDot(String s) {
        boolean exceptionHappened = false;
        try {
            if (TextUtils.isEmpty(s))
                return "";

            BigDecimal b = new BigDecimal(s);
            return b.stripTrailingZeros().toPlainString();


//            int indexOfPoint = s.indexOf(".");
//            if(-1 == indexOfPoint || 0 == indexOfPoint){
//                return s;
//            }
//            String digitAfterDot = "";
//            int i = 1;
//            for (i = 1; i < s.length(); i++) {
//                if (Character.isDigit(s.charAt(indexOfPoint + i))) {
//                    digitAfterDot = digitAfterDot + s.charAt(indexOfPoint + i);
//                } else {
//                    break;
//                }
//            }
//            if (Long.valueOf(digitAfterDot) == 0) {
//                return s.substring(0, indexOfPoint) + s.substring(indexOfPoint + i + 1, s.length());
//            }
//            return s;
        } catch (Exception e) {
//            e.printStackTrace();
            exceptionHappened = true;
            //return s;
        }

        if(exceptionHappened){
            try {
                s = new BigDecimal(s).stripTrailingZeros().toPlainString();
                return s;
            } catch (Exception e) {
                e.printStackTrace();
                return s;
            }
        }
        return s;
    }

    /**
     * 减法
     */
    public static double sub(double arg1, double arg2) {
        BigDecimal b1 = new BigDecimal(Double.toString(arg1));
        BigDecimal b2 = new BigDecimal(Double.toString(arg2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 乘法
     */
    public static double mul(double arg1, double arg2) {
        BigDecimal b1 = new BigDecimal(Double.toString(arg1));
        BigDecimal b2 = new BigDecimal(Double.toString(arg2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 加法
     */
    public static double add(double arg1, double arg2) {
        BigDecimal b1 = new BigDecimal(Double.toString(arg1));
        BigDecimal b2 = new BigDecimal(Double.toString(arg2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 加法
     */
    public static float add(float arg1, float arg2) {
        BigDecimal b1 = new BigDecimal(Float.toString(arg1));
        BigDecimal b2 = new BigDecimal(Float.toString(arg2));
        return b1.add(b2).floatValue();
    }

    /**
     * 判断list是否为空
     * @param list
     * @param <T>
     * @return
     */
    public static <T> boolean isListEmpty(List<T> list) {
        if (list == null || list.size() <= 0) {
            return true;
        }
        return false;
    }

    /**
     * 将stirng转换为map
     * @param payParam
     * @return
     */
    public static Map<String, String> getPolyPayParam(String payParam) {
        Map<String, String> payParamMaps = new HashMap<String, String>();

        JSONObject payObject = null;
        try {
            payObject = new JSONObject(payParam);
            Iterator<String> keys = payObject.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                payParamMaps.put(key, payObject.optString(key));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return payParamMaps;
    }

    /**
     * 如果为null或者""则返回true，否则返回false
     *
     * @param in
     * @return boolean
     */
    public static boolean isEmpty(String in) {
        return TextUtils.isEmpty(in);
    }

    public static boolean isEmpty(char[] array) {
        if (array == null || array.length <= 0) {
            return true;
        }
        return false;
    }

    /**
     * 根据路径删除图片
     *
     * @param path
     */
    public static void deleteTempFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }


    /**
     * 获取文件的类型
     *
     * @param filePath
     * @return
     */
    public static String getMimeType(String filePath) {
//	    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
//	    String mime = "";
//	    if (filePath != null) {
//	         try {
//	             mmr.setDataSource(filePath);
//	             mime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
//	         } catch (IllegalStateException e) {
//		    return mime;
//	         } catch (IllegalArgumentException e) {
//		    return mime;
//		} catch (RuntimeException e) {
//		    return mime;
//		}
//	    }
//	    return mime;
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(filePath);
        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }
        return type;
    }

    /**
     * 获取file的类型
     * @param file
     * @return
     */
    public static String getMimeType(final File file) {
        String extension = getExtension(file);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

    /**
     * 获取文件的后缀
     * @param file
     * @return
     */
    public static String getExtension(final File file) {
        String suffix = "";
        String name = file.getName();
        final int idx = name.lastIndexOf(".");
        if (idx > 0) {
            suffix = name.substring(idx + 1);
        }
        return suffix;
    }

    /**
     * 删除文件的操作
     *
     * @param path 文件的绝对路径
     * @return true if delete success,false otherwise.
     */
    public static boolean deleteFile(String path) {
        if (path == null) {
            return true;
        }
        File file = new File(path);
        return file.delete();
    }

    /**
     * 字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isVoid(String str) {
        return (str == null || str.trim().length() == 0);
    }

    /**
     * BASE64 解密
     *
     * @param str
     * @return
     */
    public static String decryptBASE64(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        try {
            byte[] encode = str.getBytes("UTF-8");
            // base64 解密
            return new String(Base64.decode(encode, 0, encode.length, Base64.DEFAULT), "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 设定view的BG
     *
     * @param view
     * @param drawable
     */
    public static void setBackground(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT > 15) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    /**
     * 安全删除view的listener事件
     * @param observer
     * @param listener
     */
    public static void safeRemoveOnGlobalLayoutListener(ViewTreeObserver observer,
                                                        ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            observer.removeGlobalOnLayoutListener(listener);
        } else {
            observer.removeOnGlobalLayoutListener(listener);
        }
    }

    /**
     * 通知系统更新新的img文件
     */
    public static void notifyGalleryNewPic(Context ctx, File file) {
//        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        //File f = new File(mCurrentPhotoPath);
//        Uri contentUri = Uri.fromFile(file);
//        mediaScanIntent.setData(contentUri);
//        ctx.sendBroadcast(mediaScanIntent);

        ctx.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getPath())));
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param arg1  需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double arg1, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(arg1));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 将要double转成string(精度问题)
     * @param value
     * @return
     */
    public static String doubleToString(double value){
        String result = "";
        try {
            BigDecimal b = new BigDecimal(Double.toString(value));
            result = b.stripTrailingZeros().toPlainString();
        } catch (Exception e) {

        }
        return result;
    }

    /**
     * float(精度问题)
     * @param value
     * @return
     */
    public static String floatToString(float value){
        String result = "";
        try {
            BigDecimal b = new BigDecimal(Float.toString(value));
            result = b.stripTrailingZeros().toPlainString();
        } catch (Exception e) {

        }
        return result;
    }

    /**
     * px转位dp
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 图片转为base64 string
     *
     * @param filePath
     * @return
     */
    public static String imgFile2Base64Str(String filePath) {
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);//参数100表示不压缩
            byte[] bytes = bos.toByteArray();
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        }
    }

    public static Bitmap image2Bitmap(String path) {
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);//参数100表示不压缩
            byte[] bytes = bos.toByteArray();

            if (bytes.length != 0) {
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }
        } catch (Exception e) {
            return null;
        }

        return null;
    }

    public static void saveBitmap(Bitmap mBitmap, String savedPic)  {
        File f = new File(savedPic);
        FileOutputStream fOut;

        try {
            fOut = new FileOutputStream(f);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // fOut有可能并未close， 比如flush的时候失败
    }

    public static void copyfile(File fromFile, File toFile, Boolean rewrite ) {
        if (!fromFile.exists()) {
            return;
        }
        if (!fromFile.isFile()) {
            return ;
        }
        if (!fromFile.canRead()) {
            return ;
        }
        if (!toFile.getParentFile().exists()) {
            toFile.getParentFile().mkdirs();
        }
        if (toFile.exists() && rewrite) {
            toFile.delete();
        }

        //当文件不存时，canWrite一直返回的都是false
        // if (!toFile.canWrite()) {
        // MessageDialog.openError(new Shell(),"错误信息","不能够写将要复制的目标文件" + toFile.getPath());
        // Toast.makeText(this,"不能够写将要复制的目标文件", Toast.LENGTH_SHORT);
        // return ;
        // }
        try {
            java.io.FileInputStream fosfrom = new java.io.FileInputStream(fromFile);
            java.io.FileOutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c); //将内容写到新文件当中
            }
            fosfrom.close();
            fosto.close();
        } catch (Exception ex) {
            LogUtil.e("readfile", ex.getMessage());
        }
    }

    private static long lastClickTime;

    /**
     * 检测是否小于一定毫秒数疯狂点击
     *
     * @return true表示小于，需要return处理 eg: if (Utils.isFastClick()){ return ; }
     * @times 希望的毫秒间隔，不传默认为600毫秒
     */
    public static synchronized boolean isFastClick(long... times) {
        long intervalTime = 600;
        if (null != times && times.length > 0) {
            intervalTime = times[0];
        }
        long time = System.currentTimeMillis();
        if (time - lastClickTime < intervalTime) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * sd卡是否可用
     * @return
     */
    public static boolean isMediaMounted() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }


    private static boolean hasExternalStoragePermission(Context context) {
        final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";

        int perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
        return perm == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isMediaOk() {
        if (isMediaMounted() && hasExternalStoragePermission(KidsBookApplication.getInstance().getContext())) {
            return true;
        } else {
            return false;
        }
    }
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    /**
     * Generate a value suitable for use in {@link #setId(int)}.
     * This value will not collide with ID values generated at build time by aapt for R.id.
     *
     * @return a generated ID value
     */
    public static int generateViewId() {
        for (;;) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    /**
     * 组装新统计json
     * @param action
     * @param wid
     * @return
     */
    /*
    public static String createStatisticJson(String action,String wid){
        try {
            long currentTime = Calendar.getInstance().getTimeInMillis()/1000;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("bduss", HostBridge.getBDUSS());
            jsonObject.put("cuid", CommonParam.getCUID(HostBridge.getApplicationContext()));
            jsonObject.put("time_stamp",currentTime);
            jsonObject.put("action",action);
            jsonObject.put("wid",wid);
            return jsonObject.toString();
        } catch (JSONException e) {
            return "";
        }
    }
    */

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
