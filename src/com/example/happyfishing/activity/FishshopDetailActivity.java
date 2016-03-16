package com.example.happyfishing.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.happyfishing.R;
import com.example.happyfishing.R.color;
import com.example.happyfishing.R.layout;
import com.example.happyfishing.R.menu;
import com.example.happyfishing.adapter.ImagePagerAdapter;
import com.example.happyfishing.bannerview.CircleFlowIndicator;
import com.example.happyfishing.bannerview.ViewFlow;
import com.example.happyfishing.mapManager.Locating;
import com.example.happyfishing.tool.HttpAddress;
import com.example.happyfishing.tool.HttpCallbackListener;
import com.example.happyfishing.tool.HttpUtil;
import com.example.happyfishing.view.ActionBarView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class FishshopDetailActivity extends Activity implements OnClickListener{
	private ActionBarView actionBar_fishshopdetail;
	private TextView tv_actionbar_title;

	private ViewFlow mViewFlow;
	private CircleFlowIndicator mFlowIndicator;
	private ArrayList<String> imageUrlList;
	private ArrayList<String> linkUrlArray;
	private TextView tv_fishshop_detail_order;
	private Handler mainHandler;
	private String idString;
	private String nameString;
	private double longitude2;
	private double latitude2;
	private TextView tv_environScore_fishshop;
	private TextView tv_address_fishshop;
	private TextView tv_detail_fishshop;
	private Locating locating;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fishshop_detail);
		
		mainHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					String statusString = (String) msg.obj;
					Toast.makeText(FishshopDetailActivity.this, statusString, Toast.LENGTH_SHORT).show();
					break;
				case 5:
					Toast.makeText(FishshopDetailActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
				super.handleMessage(msg);
			}
		};

		initView();

		initBannerList();

		initBanner(imageUrlList);

		loadData();
	}

	private void initBanner(ArrayList<String> imageUrlList2) {

		mViewFlow.setAdapter(new ImagePagerAdapter(this, imageUrlList, linkUrlArray).setInfiniteLoop(true));
		mViewFlow.setmSideBuffer(imageUrlList.size()); // 实际图片张数，

		mViewFlow.setFlowIndicator(mFlowIndicator);
		mViewFlow.setTimeSpan(4500);
		mViewFlow.setSelection(imageUrlList.size() * 1000); // 设置初始位置
		mViewFlow.startAutoFlowTimer(); // 启动自动播放
	}

	private void initBannerList() {
		imageUrlList = new ArrayList<String>();
		linkUrlArray = new ArrayList<String>();
		imageUrlList.add("http://b.hiphotos.baidu.com/image/pic/item/d01373f082025aaf95bdf7e4f8edab64034f1a15.jpg");
		imageUrlList.add("http://g.hiphotos.baidu.com/image/pic/item/6159252dd42a2834da6660c459b5c9ea14cebf39.jpg");
		imageUrlList.add("http://d.hiphotos.baidu.com/image/pic/item/adaf2edda3cc7cd976427f6c3901213fb80e911c.jpg");
		imageUrlList.add("http://g.hiphotos.baidu.com/image/pic/item/b3119313b07eca80131de3e6932397dda1448393.jpg");

		linkUrlArray.add("http://blog.csdn.net/finddreams/article/details/44301359");
		linkUrlArray.add("http://blog.csdn.net/finddreams/article/details/43486527");
		linkUrlArray.add("http://blog.csdn.net/finddreams/article/details/44648121");
		linkUrlArray.add("http://blog.csdn.net/finddreams/article/details/44619589");
	}

	private void initView() {
		findViewById(R.id.img_actionbar_collection).setOnClickListener(this);
		findViewById(R.id.ll_fishshop_address).setOnClickListener(this);
		
		
//		tv_environScore_fishshop = (TextView) findViewById(R.id.tv_environScore_fishshop);
		tv_address_fishshop = (TextView) findViewById(R.id.tv_address_fishshop);
		tv_detail_fishshop = (TextView) findViewById(R.id.tv_detail_fishshop);
		
		mViewFlow = (ViewFlow) findViewById(R.id.viewflow);
		mFlowIndicator = (CircleFlowIndicator) findViewById(R.id.viewflowindic);
		
		actionBar_fishshopdetail = (ActionBarView) findViewById(R.id.actionbar_fishshopdetail);
		actionBar_fishshopdetail.setActionBar(R.string.back, -1, R.string.action_settings, this);
		actionBar_fishshopdetail.setBackgroundColor(getResources().getColor(R.color.actionbar_background));
		tv_actionbar_title = (TextView) findViewById(R.id.tv_actionbar_title);

		tv_fishshop_detail_order = (TextView) findViewById(R.id.tv_fishshopdetail_order);
		tv_fishshop_detail_order.setText("进入店铺");
		tv_fishshop_detail_order.setOnClickListener(this);
	}

	private void loadData() {
		Intent intent = getIntent();
		String title = intent.getStringExtra("title");
		idString = intent.getStringExtra("id");
		tv_actionbar_title.setText(title);
		locating = Locating.getInstance(FishshopDetailActivity.this);
		locating.locate();
		initData(idString);
	}

	private void initData(String idString) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("id", idString);
		HttpUtil.getJSON(HttpAddress.ADDRESS+HttpAddress.PROJECT+HttpAddress.CLASS_MERCHANT+HttpAddress.METHOD_DETAIL, 
				params, 
				new HttpCallbackListener() {
					
					@Override
					public void onFinish(Object response) {
						Log.d("detail", response.toString());
						JSONObject jsonObject = (JSONObject) response;
						try {
							//缺经纬度  imageurls haveWIFI haveParking
							JSONObject jsonObject2 = jsonObject.getJSONObject("merchant");
							final String location = jsonObject2.getString("address");//鱼坑位置
							nameString = jsonObject2.getString("name");//鱼坑名字
							final String envirScore = jsonObject2.getString("envirScore");//环境指数
							final String fishpitDetail = jsonObject2.getString("introduction");//鱼坑介绍
							String ADBannerURLS = jsonObject2.getString("imageUrls");//鱼坑的banner图片
							String longitude = jsonObject2.getString("longitude");//鱼坑所在的经度
							longitude2 = Double.parseDouble(longitude);
							String latitude = jsonObject2.getString("latitude");//鱼坑所在的纬度
							latitude2 = Double.parseDouble(latitude);
							boolean hasWifi =jsonObject2.getBoolean("hasWifi");//鱼坑是否有wifi
							boolean hasPark = jsonObject2.getBoolean("hasPark");//鱼坑是否有停车场
							String idString = jsonObject2.getString("id");
							
							runOnUiThread(new Runnable() {
								public void run() {
//									tv_environScore_fishshop.setText("环境："+envirScore);
									tv_address_fishshop.setText(location);
									tv_detail_fishshop.setText(fishpitDetail);
								}
							});
							Log.d("merchant:  ", "location:  "+
							location+"name:  "+nameString+"envirScore:  "+envirScore+"fishpitDetail:  "+
									fishpitDetail+"ADBannerURLS:  "+ADBannerURLS
									+"latitude:  "+latitude2 +"longitude:  "+longitude2
									+"hasWifi:  "+ hasWifi +"hasPark:  ");
						} catch (JSONException e) {
							mainHandler.sendEmptyMessage(5);
						}
						
					}
					
					@Override
					public void onError(Exception e) {
						mainHandler.sendEmptyMessage(5);
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fishshop_detail, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_actionbar_left:
			FishshopDetailActivity.this.finish();
			break;
		case R.id.tv_fishshopdetail_order:
			Intent intent1 = new Intent(FishshopDetailActivity.this, CommodityDisplayActivity.class);
			Bundle bundle1 = new Bundle();
			bundle1.putDouble("target_lat", latitude2);
			bundle1.putDouble("target_lon", longitude2);
			bundle1.putDouble("my_lat", locating.latitude);
			bundle1.putDouble("my_lon", locating.longitude);
			bundle1.putString("city", locating.cityName);
			intent1.putExtra("id", idString);
			intent1.putExtras(bundle1);
			startActivity(intent1);
			break;
		case R.id.ll_fishshop_address:
			Intent intent2 = new Intent(FishshopDetailActivity.this, LocationShowActivity.class);
			Bundle bundle = new Bundle();
			bundle.putDouble("target_lat", latitude2);
			bundle.putDouble("target_lon", longitude2);
			bundle.putDouble("my_lat", locating.latitude);
			bundle.putDouble("my_lon", locating.longitude);
			bundle.putString("city", locating.cityName);
			intent2.putExtras(bundle);
			startActivity(intent2);
			break;
		case R.id.img_actionbar_collection:
			SharedPreferences sp =getSharedPreferences("user", Context.MODE_PRIVATE);
			String token = sp.getString("token", "");
			if (token.equals("")) {
				Toast.makeText(FishshopDetailActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
			}else {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("token", token);
				params.put("merchantId", idString);
				HttpUtil.getJSON(HttpAddress.ADDRESS+HttpAddress.PROJECT+
						HttpAddress.CLASS_USERINFO+HttpAddress.METHOD_ADDCOLLET, 
						params, 
						new HttpCallbackListener() {
					
					@Override
					public void onFinish(Object response) {
						JSONObject jsonObject = (JSONObject) response;
						Log.d("response", response.toString());
						String statusString = "收藏失败";
						try {
							statusString = jsonObject.getString("text");
						} catch (JSONException e) {
							mainHandler.sendEmptyMessage(5);
							e.printStackTrace();
						}
						Message message = new Message();
						message.what = 1;
						message.obj = statusString;
						mainHandler.sendMessage(message);
					}
					
					@Override
					public void onError(Exception e) {
						mainHandler.sendEmptyMessage(5);
					}
				});
			}
			break;
		default:
			break;
		}
	}

}
