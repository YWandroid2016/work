package com.example.happyfishing.activity;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.amap.api.mapcore2d.el;
import com.example.happyfishing.R;
import com.example.happyfishing.adapter.FishPositionGroupNumAdapter;
import com.example.happyfishing.adapter.FishPositionShowAdapter;
import com.example.happyfishing.adapter.FishpitOrderDateAdapter;
import com.example.happyfishing.entity.OrderDateEntity;
import com.example.happyfishing.tool.HttpAddress;
import com.example.happyfishing.tool.HttpCallbackListener;
import com.example.happyfishing.tool.HttpUtil;
import com.example.happyfishing.tool.UiUtil;
import com.example.happyfishing.view.ActionBarView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ResourceAsColor")
public class FishpitConfirmOrderActivity extends Activity implements OnClickListener {

//	private GridView grv_fishipitoder_date;
	private LinearLayout lin_date;
	private FishpitOrderDateAdapter adapter;
	private GridView grv_fishipitoder_position_group;
	private FishPositionGroupNumAdapter adapter2;
	private ArrayList<OrderDateEntity> arrayList;
	private FishPositionShowAdapter adapter3;

	private ActionBarView actionBar_fishipit_confirm;
	private String idString;
	private String nameString;
	private int fishPositionTotal;
	private int groupNum;
	private TextView tv_fishpit_confirm_fishPositionTotal;
	private GridView grv_fishpitorder_position;
	private String[] orderedLocations;
	private View currentView;
	private View previousView;
	private int currentOrder = -1;
	private TextView tv_date;
	private String orderDate;
	private String phoneNumber;
	private Handler mainHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fishpit_confirm_order);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		idString = bundle.getString("id");
		nameString = bundle.getString("name");
		
		fishPositionTotal = bundle.getInt("fishPositionTotal");

		mainHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					String statusText = (String) msg.obj;
					Toast.makeText(FishpitConfirmOrderActivity.this, statusText, Toast.LENGTH_SHORT).show();
					break;
				case 5:
					Toast.makeText(FishpitConfirmOrderActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
				super.handleMessage(msg);
			}
		};

		initView();

		initDataView();

	}

	private void loadData(String dateString) {
		orderDate = dateString;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("merchantId", idString);
		params.put("curDate", dateString);
		HttpUtil.getJSON(HttpAddress.ADDRESS + HttpAddress.PROJECT + HttpAddress.CLASS_MERCHANT + HttpAddress.METHOD_RESERVED, params, new HttpCallbackListener() {

			@Override
			public void onFinish(Object response) {
				JSONObject jsonObject = (JSONObject) response;
				try {
					String aaaString = jsonObject.getString("locations");
					orderedLocations = aaaString.split(",");
					for (int i = 0; i < orderedLocations.length; i++) {
						Log.d("split", orderedLocations[i]);
					}
					runOnUiThread(new Runnable() {
						public void run() {
							adapter3 = new FishPositionShowAdapter(FishpitConfirmOrderActivity.this, 28, 0, orderedLocations);
							adapter3.setCurrentOrder(-1);
							grv_fishpitorder_position.setAdapter(adapter3);
							adapter3.notifyDataSetChanged();
						}
					});
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

	private void initView() {
		findViewById(R.id.tv_fishpitconfirmorder_confirm).setEnabled(false);
		findViewById(R.id.tv_fishpitconfirmorder_confirm).setBackgroundColor(Color.parseColor("#bbbbbb"));
		
		if ((fishPositionTotal % 28) == 0) {
			groupNum = fishPositionTotal / 28;
		} else {
			groupNum = fishPositionTotal / 28 + 1;
		}

		adapter2 = new FishPositionGroupNumAdapter(FishpitConfirmOrderActivity.this, groupNum, fishPositionTotal);

//		grv_fishipitoder_date = (GridView) findViewById(R.id.grv_fishipitorder_date);
		lin_date = (LinearLayout) findViewById(R.id.lin_yuyue_fishpit_date);
		adapter = new FishpitOrderDateAdapter(FishpitConfirmOrderActivity.this);
		arrayList = new ArrayList<OrderDateEntity>();

		grv_fishipitoder_position_group = (GridView) findViewById(R.id.grv_fishipitorder_position_group);
		actionBar_fishipit_confirm = (ActionBarView) findViewById(R.id.actionBar_fishipit_confirm);
		actionBar_fishipit_confirm.setActionBar(R.string.back, -1, R.string.title_actionbar_fishpit_detail, this);
		findViewById(R.id.tv_fishpitconfirmorder_confirm).setOnClickListener(this);
		tv_fishpit_confirm_fishPositionTotal = (TextView) findViewById(R.id.tv_fishipit_confirm_fishPositionTotal);
		tv_fishpit_confirm_fishPositionTotal.setText("共 " + fishPositionTotal + " 个钓位");

		grv_fishpitorder_position = (GridView) findViewById(R.id.grv_fishpitorder_position);
		grv_fishpitorder_position.setSelector(new ColorDrawable(Color.TRANSPARENT));
		grv_fishpitorder_position.setOnItemClickListener(new OnItemClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if ((Boolean) view.getTag()) {
					findViewById(R.id.tv_fishpitconfirmorder_confirm).setEnabled(true);
					findViewById(R.id.tv_fishpitconfirmorder_confirm).setBackgroundColor(getResources().getColor(R.color.actionbar_background));
					if (adapter3.getCurrentOrder() != -1) {
						adapter3.notifyDataSetChanged();
					}
					if (currentView == null) {
						currentView = view;
					} else {
						previousView = currentView;
						currentView = view;
					}
					if (previousView != null) {
						previousView.findViewById(R.id.tv_layoutinflater_fishposition_show).setBackgroundResource(R.drawable.bgd_diaowei_default);
					}
					if (currentView != null) {
						currentView.findViewById(R.id.tv_layoutinflater_fishposition_show).setBackgroundResource(R.drawable.bgd_diaowei_selected);
						TextView tView = (TextView) currentView.findViewById(R.id.tv_layoutinflater_fishposition_show);
						currentOrder = Integer.parseInt(tView.getText().toString());
						adapter3.setCurrentOrder(currentOrder);
					}
				} else {

				}
			}
		});
	}

	private void initDataView() {

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		float density = dm.density;
		// 设置positionGroupGridView在界面显示时的属性
		int allWidth2 = (int) (80 * groupNum * density);
		int itemSize2 = (int) (80 * density);
		LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(allWidth2, LinearLayout.LayoutParams.MATCH_PARENT);
		grv_fishipitoder_position_group.setLayoutParams(params2);
		grv_fishipitoder_position_group.setColumnWidth(itemSize2);
//		grv_fishipitoder_position_group.setHorizontalSpacing(10);
		grv_fishipitoder_position_group.setStretchMode(GridView.NO_STRETCH);
		grv_fishipitoder_position_group.setNumColumns(groupNum);
		grv_fishipitoder_position_group.setAdapter(adapter2);
		grv_fishipitoder_position_group.setOnItemClickListener(new OnItemClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				for (int i = 0; i < groupNum; i++) {
					parent.getChildAt(i).setBackground(null);
				}
				parent.getChildAt(position).setBackgroundResource(R.color.appcolor);
				if ((fishPositionTotal - 28 * position) >= 28) {
					adapter3 = new FishPositionShowAdapter(FishpitConfirmOrderActivity.this, 28, position, orderedLocations);
					adapter3.setCurrentOrder(currentOrder);
					grv_fishpitorder_position.setAdapter(adapter3);
					adapter3.notifyDataSetChanged();
				} else {
					adapter3 = new FishPositionShowAdapter(FishpitConfirmOrderActivity.this, (fishPositionTotal - 28 * position), position, orderedLocations);
					adapter3.setCurrentOrder(currentOrder);
					grv_fishpitorder_position.setAdapter(adapter3);
					adapter3.notifyDataSetChanged();
				}
			}
		});

		initDates();
	}
	
	private String dateString;	//当前选中鱼坑
	
	private View currentV;
	
	private void initDates(){
		long currentTime = System.currentTimeMillis();
		SimpleDateFormat format_week = new SimpleDateFormat("EEEE");
		SimpleDateFormat format_date = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat format_date2 = new SimpleDateFormat("MM-dd");
		Date date = new Date(currentTime);
		String weekString;
		String dateString2 = null;
		
		weekString = format_week.format(date);
		dateString = format_date.format(date);
		dateString2 = format_date2.format(date);
		
		loadData(dateString);
		
		View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_yuyue_fishpit_date, null);
		
		
		
		tv_date = (TextView) v.findViewById(R.id.tv_item_yuyue_fishfit_date);
		tv_date.setTextColor(getResources().getColor(R.color.bai));
		tv_date.setText(dateString2+"\n"+"今天");
		
		v.setId(100);
		v.setTag(dateString);
		v.setOnClickListener(this);
		
		lin_date.addView(v);
		
		currentV = v;
		
		currentV.setBackgroundResource(R.color.hong);
		
		// 获取当前时间并向后获得4个将来时间
		for (int i = 1; i < 5; i++) {
			date = new Date(currentTime + 86400000 * i);
			weekString = format_week.format(date);
			dateString = format_date.format(date);
			dateString2 = format_date2.format(date);
			
			View v2 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_yuyue_fishpit_date, null);
			TextView tv_date2 = (TextView) v2.findViewById(R.id.tv_item_yuyue_fishfit_date);
			
			tv_date2.setText(dateString2+"\n("+weekString+")");
			
			v2.setId(i+101);
			v2.setTag(dateString);
			v2.setOnClickListener(this);
			
			lin_date.addView(v2);
		}
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fishpit_confirm_order, menu);
		return true;
	}

	private String totalFee;
	private int userPoint;
	String tokenString = null;
	String orderIdString = null;
	String merchantIdString = null;
	String reserveTime = null;
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_fishpitconfirmorder_confirm:
			
			UiUtil.setNewMessage(true);
			
			SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("merchantId", idString);
			params.put("token", sp.getString("token", ""));
			params.put("reserveTime", orderDate);
			params.put("location", currentOrder + "");

			HttpUtil.getJSON(HttpAddress.ADDRESS + HttpAddress.PROJECT + 
					HttpAddress.CLASS_APPORDER + HttpAddress.METHOD_MAKEORDER, 
					params, 
					new HttpCallbackListener() {

				@Override
				public void onFinish(Object response) {
					Log.d("my", response.toString());
					JSONObject jsonObject = (JSONObject) response;
					
					int code = 0;
					String statusText = null;
					try {
						code = jsonObject.getInt("status");

					} catch (JSONException e) {
						mainHandler.sendEmptyMessage(5);
						e.printStackTrace();
					}
					switch (code) {
					case 2000:
						String dateCreate = null;
						try {
							JSONObject jsonObject2 = jsonObject.getJSONObject("order");
							userPoint = jsonObject.getInt("userPoint");
							totalFee = jsonObject2.getString("totalFee");
							SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
							phoneNumber = sp.getString("phoneNumber", "");
							tokenString = jsonObject2.getString("token");
							orderIdString = jsonObject2.getString("orderId");
							merchantIdString = jsonObject2.getString("merchantId");
							reserveTime = jsonObject2.getString("reserveTime");
							
							
							SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
							long currentTime = System.currentTimeMillis();
							Date date = new Date(currentTime);
							dateCreate = dateFormat.format(date);
							
						} catch (JSONException e1) {
							mainHandler.sendEmptyMessage(5);
							e1.printStackTrace();
						}
						
						Intent intent1 = new Intent(FishpitConfirmOrderActivity.this, OrderInformationActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("dateCreate", dateCreate);
						bundle.putString("token", tokenString);
						bundle.putString("orderId", orderIdString);
						bundle.putString("merchantId", merchantIdString);
						bundle.putString("date", orderDate);
						bundle.putInt("location", currentOrder);
						bundle.putString("name", nameString);
						bundle.putString("phone", phoneNumber);
						bundle.putString("totalFee", totalFee);
						bundle.putLong("userPoint", userPoint);
						intent1.putExtras(bundle);
						startActivity(intent1);
						break;

					default:
						try {
							statusText = jsonObject.getString("text");
						} catch (JSONException e) {
							mainHandler.sendEmptyMessage(5);
							e.printStackTrace();
						}
						Message message = new Message();
						message.what = 1;
						message.obj = statusText;
						mainHandler.sendMessage(message);
						break;
					}
				}

				@Override
				public void onError(Exception e) {
					mainHandler.sendEmptyMessage(5);
				}
			});

			break;
		case R.id.tv_actionbar_left:
			FishpitConfirmOrderActivity.this.finish();
			break;
			
		case 100:
			iniView(v);
			break;
		case 101:
			iniView(v);
			break;
		case 102:
			iniView(v);
			break;
		case 103:
			iniView(v);
			break;
		case 104:
			iniView(v);
			break;
			
		default:
			break;
		}
	}

	public void iniView(View v){
		tv_date.setTextColor(getResources().getColor(R.color.hei));
		currentV.setBackgroundResource(R.color.bai);
		currentV = v;
		currentV.setBackgroundResource(R.color.hong);
		tv_date = (TextView) currentV.findViewById(R.id.tv_item_yuyue_fishfit_date);
		tv_date.setTextColor(getResources().getColor(R.color.bai));
		loadData(v.getTag().toString());
	}
	
}
