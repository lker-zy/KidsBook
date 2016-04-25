package com.xuewen.kidsbook.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xuewen.kidsbook.service.beans.ConfigBean;
import com.xuewen.kidsbook.utils.LogUtil;
import com.xuewen.kidsbook.utils.Network;

import java.io.IOException;

/**
 * Created by lker_zy on 16-3-29.
 */
public class ConfigService {
    private String TAG = ConfigService.class.getSimpleName();

    private static final String configUrl = "http://180.76.176.227/web/config.json";

    public void changeToTestEnv() {
        String content = Network.simpleDownload(configUrl);
        LogUtil.d(TAG, content);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        try {
            ConfigBean config = objectMapper.readValue(content, ConfigBean.class);
            LogUtil.d(TAG, "update config ok");
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.d(TAG, e.getMessage());
        }
    }
}
