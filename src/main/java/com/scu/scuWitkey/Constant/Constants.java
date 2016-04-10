package com.scu.scuWitkey.Constant;

import java.io.Serializable;

public class Constants implements Serializable {

    private static final long serialVersionUID = 4300451160128501235L;

    public static int HTTP_SESSION_INVALID_TIME = 30 * 60;   //session有效期
    public static int HTTP_REQUEST_STATUS_CODE_FAILURE = -1;    //http请求失败
    public static int HTTP_REQUEST_STATUS_CODE_SUCCESS = 0;     //http请求成功
    public static int HTTP_REQUEST_STATUS_CODE_SUCCESS_SESSION_VALIDATE_FAILURE = 1;     //http请求成功，用户验证失效
    public static int HTTP_REQUEST_STATUS_CODE_SUCCESS_AUTHORIZE_VALIDATE_FAILURE = 2;     //http请求成功，用户权限不够
    public static int HTTP_REQUEST_STATUS_CODE_SUCCESS_REGISTER_USER_EXIST = 3;     //http请求成功，注册用户已存在
    public static String MANAGER_USER_EMAIL = "manager@scu.com";
    public static final String SCU_WITKEY_IMAGE_FOLDER = "scuWitkey/image";
    public static final String SCU_WITKEY_ZIP_FOLDER = "scuWitkey/zip";
    public final static String ACCESS_KEY = "nmyl2zno4z";
    public final static String SECRET_KEY = "1zi0jw3zzli3z0lmhjyiyklih3wx341m2k5jx3y5";
    public final static String SAE_STORAGE_DOMAIN = "scuwitkeydomain";
    public final static int MISSION_LIST_PAGE_COUNT = 4;//任务列表单页显示任务数
    public final static String USER_DEFAULT_AVATAR = "http://scuwitkey-scuwitkeydomain.stor.sinaapp.com/Koala.jpg";//默认用户头像url
    public static String MANAGER_SETTING_DEFAULT_RECOMMEND_PIC_URL = "http://scuwitkey-scuwitkeydomain.stor.sinaapp.com/Penguins.jpg";//默认推荐新闻图片url
    public static String MANAGER_SETTING_ADD_RECOMMEND_PIC_URL = "http://scuwitkey-scuwitkeydomain.stor.sinaapp.com/Penguins.jpg";//管理员配置界面添加新闻图片url
    public static final int DEFAULT_THUMBNAIL_WIDTH = 633;//默认图片压缩宽度
    public static final int DEFAULT_THUMBNAIL_HEIGHT = 310;//默认图片压缩高度
    public static final int AVATAR_THUMBNAIL_WIDTH = 140;//默认头像压缩宽度
    public static final int AVATAR_THUMBNAIL_HEIGHT = 140;//默认头像压缩高度
}
