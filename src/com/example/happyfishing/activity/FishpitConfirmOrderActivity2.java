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
import com.example.happyfishing.view.MBtn;

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
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FishpitConfirmOrderActivity2 extends Activity implements OnClickListener {

	private LinearLayout lin_date;
	private TextView tv_sum;
	private LinearLayout lin_limit;
	private GridView gv;
	private Button btn_yu;
	
	private String idString;	//商家id
	private String nameString;	//鱼坑名
	private int fishPositionTotal = 80;	//鱼坑总数
	private String token;	//用户token
	private String location;	//鱼坑位置
	private String dateString;	//今天
	private String[] orderedLocations;	//已被预约的坑位 
	private String orderDate;	//预约时间
	private String currentLocation;	//当前选中鱼坑
	
	private View currentV;
	private MBtn currentBtn;
	
	private FishPositionShowAdapter adapter3;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yuyue_fishpit);

//		Intent intent = getIntent();
//		Bundle bundle = intent.getExtras();
//		idString = bundle.getString("id");
//		nameString = bundle.getString("name");
//		
//		fishPositionTotal = bundle.getInt("fishPositionTotal");
		
		initView();

	}

	private void initView() {
		
		lin_date = (LinearLayout) findViewById(R.id.lin_yuyue_fishpit_date);
		tv_sum = (TextView) findViewById(R.id.tv_yuyue_fishfit_fitnum);
		lin_limit = (LinearLayout) findViewById(R.id.lin_yuyue_fishpit_limit);
		gv = (GridView) findViewById(R.id.gv_yuyue_fishfit_fitlocation);
		btn_yu = (Button) findViewById(R.id.btn_yuyue_fishpit);
		
		//可预约时间
		initDates();
		
		tv_sum.setHint("共"+fishPositionTotal+"个钓位");
		
		//分页
		initLimit();
		
	}
	
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
		
		View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_yuyue_fishpit_date, null);
		
		currentV = v;
		
		currentV.setBackgroundResource(R.color.hong);
		
		TextView tv_date = (TextView) v.findViewById(R.id.tv_item_yuyue_fishfit_date);
		
		tv_date.setText(dateString2+"\n"+"今天");
		
		v.setId(100);
		v.setTag(dateString);
		v.setOnClickListener(this);
		
		lin_date.addView(v);
		
		// 获取当前时间并向后获得4个将来时间
		for (int i = 0; i < 4; i++) {
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
	
	private int groupNum;
	private MBtn btn_limit;
	
	private void initLimit(){
		
		if ((fishPositionTotal % 49) == 0) {
			groupNum = fishPositionTotal / 49;
		} else {
			groupNum = fishPositionTotal / 49 + 1;
		}
		
		int s = 0;
		int e = 0;
		for (int i = 0; i < groupNum; i++) {
			btn_limit = new MBtn(getApplicationContext());
			if(i == 0){
				s = 1;
				e = 49;
				btn_limit.dispalyLine(true);
			} else if(i == groupNum-1){
				s = 49*i+1;
				e = fishPositionTotal;
			} else {
				s = 49*i+1;
				e = 49*(i+1)+1;
			}
			btn_limit.setTv(s+"-"+e);
			btn_limit.setId(300+i);
			lin_limit.addView(btn_limit);
		}
		
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
							adapter3 = new FishPositionShowAdapter(FishpitConfirmOrderActivity2.this, 50, 0, orderedLocations);
							adapter3.setCurrentOrder(-1);
							gv.setAdapter(adapter3);
							adapter3.notifyDataSetChanged();
						}
					});
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	
	
	
	private String totalFee;
	private int userPoint;
	String tokenString = null;
	String orderIdString = null;
	String merchantIdString = null;
	String reserveTime = null;
	String phoneNumber = null;
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_fishpitconfirmorder_confirm:
			
			UiUtil.setNewMessage(true);
			
			if(null == currentLocation || "".equals(currentLocation)){
				Toast.makeText(getApplicationContext(), "请选择坑位", Toast.LENGTH_SHORT).show();
				return;
			}
			
			SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("merchantId", idString);
			params.put("token", sp.getString("token", ""));
			params.put("reserveTime", orderDate);
			params.put("location", currentLocation);

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
							e1.printStackTrace();
						}
						
						Intent intent1 = new Intent(FishpitConfirmOrderActivity2.this, OrderInformationActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("dateCreate", dateCreate);
						bundle.putString("token", tokenString);
						bundle.putString("orderId", orderIdString);
						bundle.putString("merchantId", merchantIdString);
						bundle.putString("date", orderDate);
						bundle.putString("location", currentLocation);
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
							e.printStackTrace();
						}
						Message message = new Message();
						message.what = 1;
						message.obj = statusText;
						break;
					}
				}

				@Override
				public void onError(Exception e) {
					e.printStackTrace();
				}
			});

			break;
		case R.id.tv_actionbar_left:
			FishpitConfirmOrderActivity2.this.finish();
			break;
		case 100:
			currentV.setBackgroundResource(R.color.bai);
			currentV = v;
			currentV.setBackgroundResource(R.color.hong);
			loadData(v.getTag().toString());
			break;
		case 101:
			currentV.setBackgroundResource(R.color.bai);
			currentV = v;
			currentV.setBackgroundResource(R.color.hong);
			loadData(v.getTag().toString());
			break;
		case 102:
			currentV.setBackgroundResource(R.color.bai);
			currentV = v;
			currentV.setBackgroundResource(R.color.hong);
			loadData(v.getTag().toString());
			break;
		case 103:
			currentV.setBackgroundResource(R.color.bai);
			currentV = v;
			currentV.setBackgroundResource(R.color.hong);
			loadData(v.getTag().toString());
			break;
		case 104:
			currentV.setBackgroundResource(R.color.bai);
			currentV = v;
			currentV.setBackgroundResource(R.color.hong);
			loadData(v.getTag().toString());
			break;
		default:
			break;
		}
	}

}
