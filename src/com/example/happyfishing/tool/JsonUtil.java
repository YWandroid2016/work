package com.example.happyfishing.tool;

import org.json.JSONObject;

public class JsonUtil {

    public static JSONObject parseJSONObject(String str){
        try {
            JSONObject obj = new JSONObject(str);
            return obj;
        }catch (Exception e){
            LogUtil.e("mycashier","json parse error!");
            e.printStackTrace();
            return null;
        }
    }
}
