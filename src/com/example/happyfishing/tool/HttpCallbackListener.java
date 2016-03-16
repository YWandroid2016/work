package com.example.happyfishing.tool;

public interface HttpCallbackListener {
    /**
     * 请求完成时的回调函数
     * @param response 视具体情况而定，可能为JSONObject/String等
     */
    void onFinish(Object response);
    void onError(Exception e);
}
