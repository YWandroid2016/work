package com.example.happyfishing.tool;

import org.json.JSONObject;

import android.util.Log;

import com.example.happyfishing.manager.SharedPreferencesManager;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class HttpUtil {

	public static final int TYPE_STRING = 0;
	public static final int TYPE_JSON = 1;
	public static final int TYPE_XML = 2;

	public static void postJSON(final String address, HashMap<String, String> params, final HttpCallbackListener listener) {
		sendHttpRequest(address, "POST", params, TYPE_JSON, listener);
	}

	public static void getJSON(final String address, HashMap<String, String> params, final HttpCallbackListener listener) {
		sendHttpRequest(address, "GET", params, TYPE_JSON, listener);
	}

	public static void sendHttpRequestGet(final String address, HashMap<String, String> params, final HttpCallbackListener listener) {
		sendHttpRequest(address, "GET", params, TYPE_STRING, listener);
	}

	public static void sendHttpRequestPost(final String address, HashMap<String, String> params, final HttpCallbackListener listener) {
		sendHttpRequest(address, "POST", params, TYPE_STRING, listener);
	}

	/**
	 * 发送http请求方法
	 * 
	 * @param address
	 * @param method
	 * @param params
	 * @param listener
	 */
	public static void sendHttpRequest(final String address, final String method, final HashMap<String, String> params, final int type, final HttpCallbackListener listener) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				HttpURLConnection connection = null;
				try {
					URL url = new URL("http://" + address);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod(method);
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					connection.setDoInput(true);
					connection.setDoOutput(true);
					// 传递参数
					if (null != params) {
						OutputStream os = connection.getOutputStream();
						BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
						writer.write(getPostDataString(params));
						writer.flush();
						writer.close();
						os.close();
					}

					// 获取返回值
					int responseCode = connection.getResponseCode();
					StringBuffer response = new StringBuffer();
					if (responseCode == HttpURLConnection.HTTP_OK) {
						InputStream in = connection.getInputStream();
						BufferedReader reader = new BufferedReader(new InputStreamReader(in));

						String line;
						while ((line = reader.readLine()) != null) {
							response.append(line);
						}

						if (listener != null) {
							switch (type) {
							case TYPE_STRING:
								listener.onFinish(response.toString());
								break;
							case TYPE_JSON:
								JSONObject obj = new JSONObject(response.toString());
								listener.onFinish(obj);
								break;
							case TYPE_XML:
								listener.onFinish(response.toString());
								break;
							default:
								listener.onFinish(response.toString());
							}
						}
					} else {
					}

				} catch (Exception e) {
					LogUtil.e("mycashier", "http request exception:" + e.getMessage());
					if (listener != null) {
						listener.onError(e);
					}
				} finally {
					if (connection != null) {
						connection.disconnect();
					}
				}
			}
		}).start();
	}

	private static String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();

		boolean isFirst = true;
		for (Map.Entry<String, String> entry : params.entrySet()) {
			if (isFirst) {
				isFirst = false;
			} else {
				result.append("&");
			}
			result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
		}
		return result.toString();
	}
}
