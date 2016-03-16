package com.example.happyfishing.activity;

import com.example.happyfishing.tool.MyApplication;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.UmengRegistrar;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
import com.umeng.message.entity.UMessage;

public class MyApp extends Application{
	
	private static final String TAG = MyApplication.class.getName();

	private PushAgent mPushAgent;

	@Override
	public void onCreate() {
		mPushAgent = PushAgent.getInstance(getApplicationContext());
		mPushAgent.setDebugMode(true);
		mPushAgent.enable();
		String device_token = UmengRegistrar.getRegistrationId(this);
		SharedPreferences sp = getSharedPreferences("phone_info", MODE_PRIVATE);
		if(null == device_token){
			sp.edit().putString("device_token", "").commit();
			Log.d("my", "-----------------------------------------------------------------------------");
		} else {
			sp.edit().putString("device_token", device_token).commit();
			Log.d("my", device_token);
		}
		
		
		/**
		 * 该Handler是在BroadcastReceiver中被调用，故
		 * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
		 * */
		UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler(){
			@Override
			public void dealWithCustomAction(Context context, UMessage msg) {
				Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
			}
			public void openActivity(Context context, UMessage msg){
				//自定义行为信息
				//Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
				
				//自定义参数信息
				Toast.makeText(context, msg.extra.get("m"), Toast.LENGTH_LONG).show();
				
				Intent it=new Intent();  
				it.setClass(MyApp.this,MessageActivity.class);//跳转到信息页
				it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//加上Flag  
				startActivity(it); 
			};
			public void launchApp(Context context, UMessage msg){
				Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
				Toast.makeText(context, msg.extra.get("m"), Toast.LENGTH_LONG).show();
			};
		};
		mPushAgent.setNotificationClickHandler(notificationClickHandler);
	}
}
