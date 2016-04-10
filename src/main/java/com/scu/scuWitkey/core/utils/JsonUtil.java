package com.scu.scuWitkey.core.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.LoggerFactory;

public class JsonUtil {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

    public static String toJson(Object obj) {
        Gson gson = new GsonBuilder().setDateFormat(DEFAULT_DATE_PATTERN).create();
        String json = gson.toJson(obj);
        logger.info("toJson---json = " + json);
        return json;
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        Gson gson = new GsonBuilder().setDateFormat(DEFAULT_DATE_PATTERN).create();
        return gson.fromJson(json, clazz);
    }
}
