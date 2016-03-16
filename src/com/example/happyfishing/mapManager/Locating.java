package com.example.happyfishing.mapManager;

import java.text.SimpleDateFormat;


import java.util.Date;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;

public class Locating {

	private  Context context;
	private static Locating locating;
	public double longitude;//经度
	public double latitude; //纬度
	public String cityName;
	private Locating(Context context){
		this.context=context;
	}
	
	public static Locating getInstance(Context context) {
		if (locating==null) {
			locating=new Locating(context);
		}
		return locating;
	}
	
	//声明AMapLocationClient类对象
	public AMapLocationClient mLocationClient = null;
	////声明mLocationOption对象
	public AMapLocationClientOption mLocationOption = null;
	//声明定位回调监听器
	public AMapLocationListener mLocationListener = null;
			
	public void locate() {
		//初始化定位
		if (mLocationClient == null) {
			
			mLocationClient = new AMapLocationClient(context);
		}
		//设置定位回调监听
		if (mLocationListener == null) {
			mLocationListener = new AMapLocationListener(){

				@Override
				public void onLocationChanged(AMapLocation amapLocation) {
					
					Log.d("locationChanger", "111111");
					if (amapLocation != null) {
				        if (amapLocation.getErrorCode() == 0) {
				        //定位成功回调信息，设置相关消息
				        amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
				        latitude=amapLocation.getLatitude();//获取纬度
				        longitude=amapLocation.getLongitude();//获取经度
				        cityName = amapLocation.getCity();
				        amapLocation.getAccuracy();//获取精度信息
				        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				        Date date = new Date(amapLocation.getTime());
				        df.format(date);//定位时间
				        amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果
				        amapLocation.getCountry();//国家信息
				        amapLocation.getProvince();//省信息
				        amapLocation.getCity();//城市信息
				        amapLocation.getDistrict();//城区信息
				        amapLocation.getRoad();//街道信息
				        amapLocation.getCityCode();//城市编码
				        amapLocation.getAdCode();//地区编码
				        Log.d("locate", amapLocation.toStr());
				        mLocationClient.stopLocation();
				    } else {
				              //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
				        Log.e("AmapError","location Error, ErrCode:"
				            + amapLocation.getErrorCode() + ", errInfo:"
				            + amapLocation.getErrorInfo());
				        }
				        mLocationClient.stopLocation();
				    }
				}};
		}
		
		mLocationClient.setLocationListener(mLocationListener);
		
		
		//初始化定位参数
		if (mLocationOption == null) {
			mLocationOption = new AMapLocationClientOption();
		}
		//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
		mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
		//设置是否返回地址信息（默认返回地址信息）
		mLocationOption.setNeedAddress(true);
		//设置是否只定位一次,默认为false
		mLocationOption.setOnceLocation(false);
		//设置是否强制刷新WIFI，默认为强制刷新
		mLocationOption.setWifiActiveScan(true);
		//设置是否允许模拟位置,默认为false，不允许模拟位置
		mLocationOption.setMockEnable(false);
		//设置定位间隔,单位毫秒,默认为2000ms
		mLocationOption.setInterval(2000);
		//给定位客户端对象设置定位参数
		mLocationClient.setLocationOption(mLocationOption);
		//启动定位
		mLocationClient.startLocation();
	}
	
	public void stop() {
		mLocationClient.stopLocation();
	}
	
	public void destory() {
		mLocationClient.onDestroy();
	}
}
