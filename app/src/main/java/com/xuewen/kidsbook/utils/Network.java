package com.xuewen.kidsbook.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by lker_zy on 16-4-22.
 */
public class Network {
    public static String simpleDownload(String url) {
        String content = "";

        try {
            URL serverURL = new URL(url);
            HttpURLConnection connect = (HttpURLConnection) serverURL.openConnection();
            BufferedInputStream bis = new BufferedInputStream(connect.getInputStream());

            int fileLength = connect.getContentLength();
            int downLength = 0;

            int n;
            byte[] buffer = new byte[1024];
            while ((n = bis.read(buffer, 0, buffer.length)) != -1) {
                downLength += n;
                content += new String(buffer, 0, n);
            }

            if (downLength != fileLength) {
                return null;
            }

            bis.close();
            connect.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return content;
    }
}
