package com.xuewen.kidsbook.net;

import android.graphics.Bitmap;
import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.xuewen.kidsbook.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lker_zy on 16-5-28.
 */
public class ImageUpload  {
    /**
     * 上传图片接口
     * @param bitmap 需要上传的图片
     * @param listener 请求回调
     */
    public static void uploadImg(Bitmap bitmap, RequestQueue requestQueue, String url,
                                 Response.Listener listener, Response.ErrorListener errorListener){
        List<FormImage> imageList = new ArrayList<FormImage>() ;
        imageList.add(new FormImage(bitmap)) ;
        Request request = new PostUploadRequest(url,imageList,listener, errorListener) ;

        requestQueue.add(request);
    }

    public static void uploadImg(Uri uri, RequestQueue requestQueue, String url,
                                 Response.Listener listener, Response.ErrorListener errorListener){

        uploadImg(Utils.image2Bitmap(uri.getPath()), requestQueue, url, listener, errorListener);
    }

    public static void uploadImg(String path, RequestQueue requestQueue, String url,
                                 Response.Listener listener, Response.ErrorListener errorListener){

        uploadImg(Utils.image2Bitmap(path), requestQueue, url, listener, errorListener);
    }
}
