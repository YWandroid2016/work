package com.example.happyfishing.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.amap.api.services.core.bu;
import com.example.happyfishing.R;
import com.example.happyfishing.R.layout;
import com.example.happyfishing.R.menu;
import com.example.happyfishing.adapter.OrderSumaryFinishAdapter;
import com.example.happyfishing.customlistview.XListView;
import com.example.happyfishing.customlistview.XListView.IXListViewListener;
import com.example.happyfishing.entity.OrderEntity;
import com.example.happyfishing.tool.HttpAddress;
import com.example.happyfishing.tool.HttpCallbackListener;
import com.example.happyfishing.tool.HttpUtil;
import com.example.happyfishing.view.ActionBarView;
import com.example.happyfishing.view.MBtn;

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
public class MyOrderActivity extends Activity implements OnClickListener,IXListViewListener{

	private ActionBarView actionBar_myorder;
	private Handler mainHandler;
	private int orderType;
	private XListView xliv_myorder;
	private OrderSumaryFinishAdapter finishAdapter;
	private ArrayList<OrderEntity> arrayList_finish;
	private ArrayList<OrderEntity> arrayList_waitpay;
	private MBtn btn_finish;
	private MBtn btn_waitpay;
	private MBtn btn_after;
	private MBtn btn_all;
	private MBtn btn_current;
	
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
					xliv_myorder.setAdapter(finishAdapter);
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
		xliv_myorder = (XListView) findViewById(R.id.liv_myorder_sumary);
		xliv_myorder.setXListViewListener(this);
		xliv_myorder.setPullLoadEnable(true);
		arrayList_finish = new ArrayList<OrderEntity>();
		arrayList_waitpay = new ArrayList<OrderEntity>();
		finishAdapter = new OrderSumaryFinishAdapter(MyOrderActivity.this);
		btn_finish = (MBtn) findViewById(R.id.btn_myorder_finish);
		btn_waitpay = (MBtn) findViewById(R.id.btn_myorder_waitpay);
		btn_after = (MBtn) findViewById(R.id.btn_myorder_after);
		btn_all = (MBtn) findViewById(R.id.btn_myorder_all);
		btn_finish.setTv("已完成");
		btn_waitpay.setTv("待支付");
		btn_after.setTv("已过期");
		btn_all.setTv("全部");
		btn_waitpay.dispalyLine(false);
		btn_after.dispalyLine(false);
		btn_all.dispalyLine(false);
		btn_waitpay.setOnClickListener(this);
		btn_finish.setOnClickListener(this);
		btn_after.setOnClickListener(this);
		btn_all.setOnClickListener(this);
		
		btn_current = btn_finish;
		btn_current.dispalyLine(true);
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
								String orderType;
								String category;
								for (int i = 0; i < jsonArray_finish.length(); i++) {
									jsonObject2 = jsonArray_finish.getJSONObject(i);
									id= jsonObject2.getString("id");
									orderType = jsonObject2.getString("type");
									category = jsonObject2.getString("category");
									merchantId = jsonObject2.getString("merchantId");
									reserveTime = jsonObject2.getString("reserveTime");
									location = jsonObject2.getString("location");
									orderId = jsonObject2.getString("orderId");
									name = jsonObject2.getString("name");
									dateCreated = jsonObject2.getString("dateCreated");
									totalFee = jsonObject2.getString("totalFee");
									picUrl = jsonObject2.getString("picUrl");
									merchantName = jsonObject2.getString("merchantName");
									arrayList_finish.add(new OrderEntity(id, orderType, category, merchantId, reserveTime, location, orderId, name, dateCreated, totalFee, picUrl, merchantName));
								}
								Message message = new Message();
								message.what = 3;
								message.obj = arrayList_finish;
								mainHandler.sendMessage(message);
								
								JSONArray jsonArray_waitpay = jsonObject.getJSONArray("waitPayOrders");
								for (int i = 0; i < jsonArray_waitpay.length(); i++) {
									jsonObject2 = jsonArray_waitpay.getJSONObject(i);
									id= jsonObject2.getString("id");
									orderType = jsonObject2.getString("type");
									category = jsonObject2.getString("category");
									merchantId = jsonObject2.getString("merchantId");
									reserveTime = jsonObject2.getString("reserveTime");
									location = jsonObject2.getString("location");
									orderId = jsonObject2.getString("orderId");
									name = jsonObject2.getString("name");
									dateCreated = jsonObject2.getString("dateCreated");
									totalFee = jsonObject2.getString("totalFee");
									picUrl = jsonObject2.getString("picUrl");
									merchantName = jsonObject2.getString("merchantName");
									arrayList_waitpay.add(new OrderEntity(id, orderType, category, merchantId, reserveTime, location, orderId, name, dateCreated, totalFee, picUrl, merchantName));
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
			btn_current.dispalyLine(false);
			btn_current = (MBtn) v;
			btn_current.dispalyLine(true);
			orderType = 0;
			finishAdapter.add2Adapter(arrayList_finish);
			xliv_myorder.setAdapter(finishAdapter);
			finishAdapter.notifyDataSetChanged();
			xliv_myorder.setOnItemClickListener(null);
			break;
			
		case R.id.btn_myorder_after:
			//TODO 获取已过期订单并加载
			break;
			
		case R.id.btn_myorder_all:
			
			//TODO 获取全部订单并加载
			
			break;
			
		case R.id.btn_myorder_waitpay:
			btn_current.dispalyLine(false);
			btn_current = (MBtn) v;
			btn_current.dispalyLine(true);
			orderType = 1;
			finishAdapter.add2Adapter(arrayList_waitpay);
			xliv_myorder.setAdapter(finishAdapter);
			finishAdapter.notifyDataSetChanged();
			xliv_myorder.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					int type = finishAdapter.getItemViewType((position-1));
					switch (type) {
					case 1:
						SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
						OrderEntity entity = arrayList_waitpay.get((position-1));
						String token = sp.getString("token", "");
						String orderId = entity.orderId;
						String merchantId = entity.merchantId;
						String userjifen = sp.getString("userPoint", "");
						Long userPoint = Long.parseLong(userjifen);
						String name = entity.merchantName;
						String date = entity.reserveTime;
						String dateCreated = entity.dateCreated;
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
						bundle.putString("dateCreate", dateCreated);
						intent1.putExtras(bundle);
						startActivity(intent1);
						break;
					case 0:
						Log.d("type", "会员开通");
						Intent intent2 = new Intent(MyOrderActivity.this, BecomeVipOrderActivity.class);
						OrderEntity entity2 = arrayList_waitpay.get((position-1));
						String category = entity2.category;
						String price = entity2.totalFee;	
						String price2 = price.substring(0, price.length()-2);
						Bundle bundle2 = new Bundle();
						bundle2.putString("dateCreate", entity2.dateCreated);
						int money = Integer.parseInt(price2);
						int memberType = 0;
						if (category.equals("month")) {
							memberType = 1;
						}else if (category.equals("season")) {
							memberType = 2;
						}else if (category.equals("year")) {
							memberType = 3;
						}
						intent2.putExtras(bundle2);
						intent2.putExtra("money", money);
						intent2.putExtra("type", memberType);
						startActivity(intent2);
						break;

					default:
						break;
					}
					
					
				}
			});
			break;
		default:
			break;
		}
	}

	@Override
	public void onRefresh() {
		
	}

	@Override
	public void onLoadMore() {
		switch (orderType) {
		case 0:
			//获取更多的已完成的订单
			break;
		case 1:
			//获取更多的待支付的订单
			break;
		default:
			break;
		}
	}

}
