package com.xuewen.kidsbook.service;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xuewen.kidsbook.AppConfig;
import com.xuewen.kidsbook.net.GlobalVolley;
import com.xuewen.kidsbook.service.beans.SearchResult;
import com.xuewen.kidsbook.utils.LogUtil;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by lker_zy on 16-3-22.
 */
public class SearchService {
    private static String TAG = SearchService.class.getSimpleName();

    private RequestQueue requestQueue;
    private List<String> searchKeys;

    private SearchServiceListener listener;

    private SearchResult parseResult(String resultText) {
        SearchResult result;

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        try {
            result = objectMapper.readValue(resultText, SearchResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return result;
    }

    public SearchService() {
        this.requestQueue = GlobalVolley.requestQueue();
    }

    public SearchService(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    private String doSyncHttpRequest() {
        RequestFuture future = RequestFuture.newFuture();
        StringRequest request = new StringRequest(AppConfig.DAILY_BOOKS_URL, future, future);
        requestQueue.add(request);

        String result;
        try {
            result = (String) future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }

        return result;
    }

    private void doAsyncHttpRequest() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.DAILY_BOOKS_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //LogUtil.d(TAG, "response for books.json ok: " + response);
                SearchResult result = parseResult(response);

                if (result.getErrno() == 0) {
                    listener.onSuccess(result);
                } else {
                    listener.onError(-1, "server response error");
                }
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.d(TAG, " on error response for books.json: " + error.toString());

                listener.onError(-2, error.getMessage());
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        LogUtil.d(TAG, "add string request: " + AppConfig.DAILY_BOOKS_URL);
        requestQueue.add(stringRequest);
    }

    public void setSearchKeys(List<String> keys) {
        searchKeys = keys;
    }

    public void setSearchServiceListener(SearchServiceListener listener) {
        this.listener = listener;
    }

    public void search() {
        doAsyncHttpRequest();
        /*
        SearchResult result = parseResult(content);

        LogUtil.d(TAG, "search result content: " + result);

        if (result.getErrno() == 0) {
            listener.onSuccess(result);
        } else {
            listener.onError(-1, "server response error");
        }
        */
    }

    public interface SearchServiceListener {
        void onSuccess(SearchResult searchResult);

        void onError(int errno, String errmsg);
    }
}
