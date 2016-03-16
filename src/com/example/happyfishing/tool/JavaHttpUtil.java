package com.example.happyfishing.tool;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class JavaHttpUtil {

	/** HTTP GET请求 */
	public static InputStream httpGet(String host, HashMap<String, String> param) {
		// 拼装出完整URL
		host = generateURL(host, param);
		LogUtil.d("JavaHttpUtil ", "host = " + host);
		try {
			// 打开网络连接(统一资源定位符)
			URL url = new URL(host);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 设置常用相关请求头
			conn.setUseCaches(false);
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(5000);
			conn.setDoInput(true);
			conn.setDoOutput(false);
			// 网络连接
			conn.connect();
			// 判断处理响应信息
			int code = conn.getResponseCode();
			if (code == 200) {
				return conn.getInputStream();
			}
		} catch (Exception e) {
			LogUtil.e("JavaHttpUtil", " httpGet连接 ");
			return null;
		}
		return null;
	}

	/** URL拼装 */
	public static String generateURL(String host, HashMap<String, String> param) {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(host);
		if (param == null) {
			return strBuilder.toString();
		}
		strBuilder.append("?");
		Set<String> keys = param.keySet();
		Iterator<String> iterator = keys.iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			strBuilder.append(key);
			strBuilder.append("=");
			String val = param.get(key);
			strBuilder.append(val);
			strBuilder.append("&");
		}
		strBuilder.delete(strBuilder.length() - 1, strBuilder.length());
		return strBuilder.toString();
	}
}
