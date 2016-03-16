package com.example.happyfishing.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.happyfishing.R;
import com.example.happyfishing.R.layout;
import com.example.happyfishing.R.menu;
import com.example.happyfishing.adapter.OrderSumaryFinishAdapter;
import com.example.happyfishing.entity.OrderEntity;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
public class MyOrderActivity extends Activity implements OnClickListener{

	private ActionBarView actionBar_myorder;
	private Handler mainHandler;
	private ListView liv_myorder;
	private OrderSumaryFinishAdapter finishAdapter;
	private ArrayList<OrderEntity> arrayList_finish;
	private ArrayList<OrderEntity> arrayList_waitpay;
	private Button btn_finish;
	private Button btn_waitpay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_order);
		mainHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					String errorText = (String) msg.obj;
					Toast.makeText(MyOrderActivity.this, errorText, Toast.LENGTH_SHORT).show();
					break;
				case 3:
					finishAdapter.add2Adapter(arrayList_finish);
					liv_myorder.setAdapter(finishAdapter);
					break;
				case 5:
					Toast.makeText(MyOrderActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
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
		actionBar_myorder = (ActionBarView) findViewById(R.id.actionBar_myorder);
		actionBar_myorder.setActionBar(R.string.back, -1, R.string.title_actionbar_myorder, this);
		liv_myorder = (ListView) findViewById(R.id.liv_myorder_sumary);
		arrayList_finish = new ArrayList<OrderEntity>();
		arrayList_waitpay = new ArrayList<OrderEntity>();
		finishAdapter = new OrderSumaryFinishAdapter(MyOrderActivity.this);
		btn_finish = (Button) findViewById(R.id.btn_myorder_finish);
		btn_finish.setOnClickListener(this);
		btn_waitpay = (Button) findViewById(R.id.btn_myorder_waitpay);
		btn_waitpay.setOnClickListener(this);
	}

	private void loadData() {
		SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
		String token = sp.getString("token", "");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("token", token);
		HttpUtil.getJSON(HttpAddress.ADDRESS+HttpAddress.PROJECT+
				HttpAddress.CLASS_APPORDER+HttpAddress.METHOD_ORDERLIST, 
				params, 
				new HttpCallbackListener() {
					@Override
					public void onFinish(Object response) {
						Log.d("response", response.toString());
						JSONObject jsonObject = (JSONObject) response;
						int code = 0;
						String errorText = "";
						try {
							code = jsonObject.getInt("status");
							switch (code) {
							case 2000:
								JSONArray jsonArray_finish = jsonObject.getJSONArray("finishOrders");
								JSONObject jsonObject2;
								String id;
								String merchantId;
								String reserveTime;
								String location;
								String orderId;
								String name;
								String dateCreated;
								String totalFee;
								String picUrl;
								String merchantName;
								for (int i = 0; i < jsonArray_finish.length(); i++) {
									jsonObject2 = jsonArray_finish.getJSONObject(i);
									id= jsonObject2.getString("id");
									merchantId = jsonObject2.getString("merchantId");
									reserveTime = jsonObject2.getString("reserveTime");
									location = jsonObject2.getString("location");
									orderId = jsonObject2.getString("orderId");
									name = jsonObject2.getString("name");
									dateCreated = jsonObject2.getString("dateCreated");
									totalFee = jsonObject2.getString("totalFee");
									picUrl = jsonObject2.getString("picUrl");
									merchantName = jsonObject2.getString("merchantName");
									arrayList_finish.add(new OrderEntity(id, merchantId, reserveTime, location, orderId, name, dateCreated, totalFee, picUrl, merchantName));
								}
								Message message = new Message();
								message.what = 3;
								message.obj = arrayList_finish;
								mainHandler.sendMessage(message);
								
								JSONArray jsonArray_waitpay = jsonObject.getJSONArray("waitPayOrders");
								for (int i = 0; i < jsonArray_waitpay.length(); i++) {
									jsonObject2 = jsonArray_waitpay.getJSONObject(i);
									id= jsonObject2.getString("id");
									merchantId = jsonObject2.getString("merchantId");
									reserveTime = jsonObject2.getString("reserveTime");
									location = jsonObject2.getString("location");
									orderId = jsonObject2.getString("orderId");
									name = jsonObject2.getString("name");
									dateCreated = jsonObject2.getString("dateCreated");
									totalFee = jsonObject2.getString("totalFee");
									picUrl = jsonObject2.getString("picUrl");
									merchantName = jsonObject2.getString("merchantName");
									arrayList_waitpay.add(new OrderEntity(id, merchantId, reserveTime, location, orderId, name, dateCreated, totalFee, picUrl, merchantName));
									Log.d("reserveTime", reserveTime);
									Log.d("dateCreated", dateCreated);
								}
								break;
							default:
								errorText = jsonObject.getString("text");
								Message message1 = new Message();
								message1.what=1;
								message1.obj = errorText;
								mainHandler.sendMessage(message1);
								break;
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_order, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_actionbar_left:
			MyOrderActivity.this.finish();
			break;
		case R.id.btn_myorder_finish:
			finishAdapter.add2Adapter(arrayList_finish);
			liv_myorder.setAdapter(finishAdapter);
			finishAdapter.notifyDataSetChanged();
			liv_myorder.setOnItemClickListener(null);
			break;
		case R.id.btn_myorder_waitpay:
			finishAdapter.add2Adapter(arrayList_waitpay);
			liv_myorder.setAdapter(finishAdapter);
			finishAdapter.notifyDataSetChanged();
			liv_myorder.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
					
					OrderEntity entity = arrayList_waitpay.get(position);
					String token = sp.getString("token", "");
					String orderId = entity.orderId;
					String merchantId = entity.merchantId;
					String userjifen = sp.getString("userPoint", "");
					Long userPoint = Long.parseLong(userjifen);
					String name = entity.merchantName;
					String date = entity.reserveTime;
					String location = entity.location;
					int orderPosition = Integer.parseInt(location);
					String phoneNumber = sp.getString("phoneNumber", "");
					Intent intent1 = new Intent(MyOrderActivity.this, OrderInformationActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("token", token);
					bundle.putString("orderId", orderId);
					bundle.putString("merchantId", merchantId);
					bundle.putString("date", date);
					bundle.putInt("location", orderPosition);
					bundle.putString("name", name);
					bundle.putString("phone", phoneNumber);
					bundle.putLong("userPoint", userPoint);
					intent1.putExtras(bundle);
					startActivity(intent1);
				}
			});
			break;
		default:
			break;
		}
	}

}
