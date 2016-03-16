package com.example.happyfishing.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import com.example.happyfishing.R;
import com.example.happyfishing.adapter.MyBillAdapter;
import com.example.happyfishing.customlistview.XListView;
import com.example.happyfishing.customlistview.XListView.IXListViewListener;
import com.example.happyfishing.entity.BillEntity;
import com.example.happyfishing.tool.HttpAddress;
import com.example.happyfishing.tool.HttpCallbackListener;
import com.example.happyfishing.tool.HttpUtil;
import com.example.happyfishing.view.ActionBarView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MyBillActivity extends Activity implements OnClickListener,IXListViewListener{
	private ArrayList<BillEntity> arrayList_shouru;
	private ArrayList<BillEntity> arrayList_zhichu;
	private int bill_type;
	private XListView xliv_bill;
	private MyBillAdapter adapter;
	private Button btn_shouru;
	private Button btn_zhichu;
	private int shouru_start=0;
	private int zhichu_start=0;
	private ActionBarView actionBar_mybill;
	private Handler mainHandler;
	private TextView tv_mybill_show;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_bill);
		mainHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.add2Adapter(arrayList_shouru);
					xliv_bill.setAdapter(adapter);
					onLoad();
					boolean isLoad_shouru = (Boolean) msg.obj;
					xliv_bill.setPullLoadEnable(isLoad_shouru);
					break;
				case 2:
					adapter.add2Adapter(arrayList_zhichu);
					xliv_bill.setAdapter(adapter);
					boolean isLoad_zhichu = (Boolean) msg.obj;
					onLoad();
					xliv_bill.setPullLoadEnable(isLoad_zhichu);
					break;
				case 5:
					Toast.makeText(MyBillActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
					break;

				default:
					break;
				}
				super.handleMessage(msg);
			}
		};
		
		initView();
		
		loadData();
	}

	private void initView() {
		xliv_bill = (XListView) findViewById(R.id.xliv_mybill_bill);
		xliv_bill.setXListViewListener(this);
		xliv_bill.setPullLoadEnable(true);
		btn_shouru = (Button) findViewById(R.id.btn_mybill_shouru);
		btn_shouru.setOnClickListener(this);
		btn_zhichu = (Button) findViewById(R.id.btn_mybill_zhichu);
		btn_zhichu.setOnClickListener(this);
		actionBar_mybill = (ActionBarView) findViewById(R.id.actionBar_mybill);
		actionBar_mybill.setActionBar(R.string.back, -1, R.string.title_actionbar_mybill, this);
	
	}
	
	@Override
	public void onRefresh() {
		//暂时用不到
	}

	private void onLoad() {
		xliv_bill.stopRefresh();
		xliv_bill.stopLoadMore();
		xliv_bill.setRefreshTime("刚刚");
	}
	@Override
	public void onLoadMore() {
		
		Log.d("debug", "loadMore");
		switch (bill_type) {
		case 0:
			//获得获取积分的数据
			HashMap<String, String> params = new HashMap<String, String>();
			SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
			String token = sp.getString("token", "");
	 		params.put("token", token);
	 		params.put("start", shouru_start+"");
	 		params.put("length", "10");
			HttpUtil.getJSON(HttpAddress.ADDRESS+HttpAddress.PROJECT+
					HttpAddress.CLASS_USERPOINT+HttpAddress.METHOD_GETLIST, 
					params, 
					new HttpCallbackListener() {
						
						@Override
						public void onFinish(Object response) {
							Log.d("debug", response.toString());
							JSONObject jsonObject = (JSONObject) response;
							try {
								JSONArray jsonArray = jsonObject.getJSONArray("data");
								JSONObject jsonObject2;
								String pointUse ;
								String type ;
								String pointFrom;
								String date;
								String name;
								shouru_start = jsonArray.length()+shouru_start;
								for (int i = 0; i < jsonArray.length(); i++) {
									jsonObject2 = jsonArray.getJSONObject(i);
									Log.d("debug", jsonObject2.toString());
									pointUse = jsonObject2.getString("pointGet");
									type = jsonObject2.getString("type");
									pointFrom = jsonObject2.getString("pointFrom");
									date = jsonObject2.getString("date");
									name = jsonObject2.getString("name");
									arrayList_shouru.add(new BillEntity(0,pointUse, type, pointFrom, date, name));
								}
								Message message = new Message();
								message.what =1;
								message.obj = (boolean) (jsonArray.length()>10);
								mainHandler.sendMessage(message);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
						
						@Override
						public void onError(Exception e) {
							
						}
					});
			break;
		case 1:
			HashMap<String, String> params1 = new HashMap<String, String>();
			SharedPreferences sp1 = getSharedPreferences("user", Context.MODE_PRIVATE);
			String token1 = sp1.getString("token", "");
	 		params1.put("token", token1);
	 		params1.put("start", zhichu_start+"");
	 		params1.put("length", "10");
			HttpUtil.getJSON(HttpAddress.ADDRESS+HttpAddress.PROJECT+
					HttpAddress.CLASS_USERPOINT+HttpAddress.METHOD_OUTLIST, 
					params1, 
					new HttpCallbackListener() {
						
						@Override
						public void onFinish(Object response) {
							Log.d("debug", response.toString());
							JSONObject jsonObject = (JSONObject) response;
							try {
								JSONArray jsonArray = jsonObject.getJSONArray("data");
								JSONObject jsonObject2;
								String pointUse ;
								String type ;
								String pointFrom;
								String date;
								String name;
								zhichu_start = jsonArray.length()+zhichu_start;
								for (int i = 0; i < jsonArray.length(); i++) {
									jsonObject2 = jsonArray.getJSONObject(i);
									Log.d("debug", jsonObject2.toString());
									pointUse = jsonObject2.getString("pointUse");
									type = jsonObject2.getString("type");
									pointFrom = jsonObject2.getString("pointFrom");
									date = jsonObject2.getString("date");
									name = jsonObject2.getString("name");
									arrayList_zhichu.add(new BillEntity(1,pointUse, type, pointFrom, date, name));
								}
								Message message = new Message();
								message.what=2;
								message.obj = (boolean) (jsonArray.length()>10);
								mainHandler.sendMessage(message);
							} catch (JSONException e) {
								mainHandler.sendEmptyMessage(5);
								e.printStackTrace();
							}
						}
						
						@Override
						public void onError(Exception e) {
							mainHandler.sendEmptyMessage(5);
						}
					});
			break;
		default:
			break;
		}
	}

	private void loadData() {
		bill_type = 0;
		// TODO Auto-generated method stub
		arrayList_shouru = new ArrayList<BillEntity>();
		arrayList_zhichu = new ArrayList<BillEntity>();
		adapter = new MyBillAdapter(MyBillActivity.this);
		//获得获取积分的数据
		HashMap<String, String> params = new HashMap<String, String>();
		SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
		String token = sp.getString("token", "");
 		params.put("token", token);
 		params.put("start", shouru_start+"");
 		params.put("length", "10");
		HttpUtil.getJSON(HttpAddress.ADDRESS+HttpAddress.PROJECT+
				HttpAddress.CLASS_USERPOINT+HttpAddress.METHOD_GETLIST, 
				params, 
				new HttpCallbackListener() {
					
					@Override
					public void onFinish(Object response) {
						Log.d("debug", response.toString());
						JSONObject jsonObject = (JSONObject) response;
						try {
							JSONArray jsonArray = jsonObject.getJSONArray("data");
							JSONObject jsonObject2;
							String pointUse ;
							String type ;
							String pointFrom;
							String date;
							String name;
							shouru_start = shouru_start + jsonArray.length();
							for (int i = 0; i < jsonArray.length(); i++) {
								jsonObject2 = jsonArray.getJSONObject(i);
								Log.d("debug", jsonObject2.toString());
								pointUse = jsonObject2.getString("pointGet");
								type = jsonObject2.getString("type");
								pointFrom = jsonObject2.getString("pointFrom");
								date = jsonObject2.getString("date");
								name = jsonObject2.getString("name");
								arrayList_shouru.add(new BillEntity(0,pointUse, type, pointFrom, date, name));
							}
							Message message = new Message();
							message.what = 1;
							message.obj = (boolean)(jsonArray.length()>10);
							mainHandler.sendMessage(message);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					
					@Override
					public void onError(Exception e) {
						
					}
				});
		//获取支出积分的数据
		HashMap<String, String> params1 = new HashMap<String, String>();
		SharedPreferences sp1 = getSharedPreferences("user", Context.MODE_PRIVATE);
		String token1 = sp1.getString("token", "");
 		params1.put("token", token1);
 		params1.put("start", zhichu_start+"");
 		params1.put("length", "10");
		HttpUtil.getJSON(HttpAddress.ADDRESS+HttpAddress.PROJECT+
				HttpAddress.CLASS_USERPOINT+HttpAddress.METHOD_OUTLIST, 
				params1, 
				new HttpCallbackListener() {
					
					@Override
					public void onFinish(Object response) {
						Log.d("debug", response.toString());
						JSONObject jsonObject = (JSONObject) response;
						try {
							JSONArray jsonArray = jsonObject.getJSONArray("data");
							JSONObject jsonObject2;
							String pointUse ;
							String type ;
							String pointFrom;
							String date;
							String name;
							zhichu_start = zhichu_start+jsonArray.length();
							for (int i = 0; i < jsonArray.length(); i++) {
								jsonObject2 = jsonArray.getJSONObject(i);
								Log.d("debug", jsonObject2.toString());
								pointUse = jsonObject2.getString("pointUse");
								type = jsonObject2.getString("type");
								pointFrom = jsonObject2.getString("pointFrom");
								date = jsonObject2.getString("date");
								name = jsonObject2.getString("name");
								arrayList_zhichu.add(new BillEntity(1,pointUse, type, pointFrom, date, name));
							}
						} catch (JSONException e) {
							mainHandler.sendEmptyMessage(5);
							e.printStackTrace();
						}
					}
					
					@Override
					public void onError(Exception e) {
						mainHandler.sendEmptyMessage(5);
					}
				});
//		arrayList_all.add(new BillEntity(0, "哈哈哈哈", "2016-1-26", "300"));
//		arrayList_all.add(new BillEntity(1, "给 梨 的聘礼", "2016-1-26", "300"));
//		adapter = new MyBillAdapter(MyBillActivity.this);
//		adapter.add2Adapter(arrayList_all);
//		xliv_bill.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_bill, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_mybill_shouru:
			bill_type = 0;
//			getData(bill_type);
			Message message = new Message();
			message.what = 1;
			message.obj = true;
			mainHandler.sendMessage(message);
			break;
		case R.id.btn_mybill_zhichu:
			bill_type = 1;
//			getData(bill_type);
			Message message2 = new Message();
			message2.what = 2;
			message2.obj = true;
			mainHandler.sendMessage(message2);
			break;
		case R.id.tv_actionbar_left:
			MyBillActivity.this.finish();
			break;
		default:
			break;
		}
	}

	

}
