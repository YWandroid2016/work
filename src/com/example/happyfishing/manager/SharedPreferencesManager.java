package com.example.happyfishing.manager;



import com.example.happyfishing.tool.MyApplication;

import android.content.Context;

import android.content.SharedPreferences;

public class SharedPreferencesManager {
    public static final String APP_DOMAIN = "chancecreate";
    public static final String SERVER_PRE = "http://";

    public static final String SERVER_ADDRESS_KEY = "server_address";
    public static final String AUTH_TOKEN_KEY = "auth_token";

    public static void saveServerConfig(String address){
        saveStringData(SERVER_ADDRESS_KEY,address);
    }

    public static String getServerConfig(){
        return getStringData(SERVER_ADDRESS_KEY,SERVER_PRE);

    }

    public static void saveAuthToken(String token){
        saveStringData(AUTH_TOKEN_KEY,token);
    }

    public static String getAuthToken(){
        return getStringData(AUTH_TOKEN_KEY,null);
    }

    /**
     * 保存String类型的数据到SharedPreferences中
     * @param key
     * @param str
     */
    public static void saveStringData(String key, String str){
        SharedPreferences.Editor editor = MyApplication.getContext()
                .getSharedPreferences(APP_DOMAIN, Context.MODE_PRIVATE).edit();
        if(null != str){
            editor.putString(key,str);
        }
        editor.commit();
    }

    /**
     * 根据key值获取String类型的数据，如果SharedPreferences中不存在，则适用defValue
     * @param key
     * @param defValue
     * @return
     */
    public static String getStringData(String key, String defValue){
        SharedPreferences pref = MyApplication.getContext()
                .getSharedPreferences(APP_DOMAIN,Context.MODE_PRIVATE);
        return pref.getString(key,defValue);
    }
}
