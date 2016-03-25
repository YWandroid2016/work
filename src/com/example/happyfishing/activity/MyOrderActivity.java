package com.example.happyfishing.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import com.example.happyfishing.entity.DingEntity;
import com.example.happyfishing.entity.FishEntity;
import com.example.happyfishing.entity.OrderEntity;
import com.example.happyfishing.tool.HttpAddress;
import com.example.happyfishing.tool.HttpCallbackListener;
import com.example.happyfishing.tool.HttpUtil;
import com.example.happyfishing.view.ActionBarView;
import com.example.happyfishing.view.MBtn;
import com.google.gson.Gson;

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
	private int orderType;
	private ListView xliv_myorder;
	private OrderSumaryFinishAdapter finishAdapter;
	private List<DingEntity> arrayList_finish;
	private List<DingEntity> arrayList_kong;
	private MBtn btn_finish;	//可使用
	private MBtn btn_waitpay;	//待支付
	private MBtn btn_after;		//已过期
	private MBtn btn_all;		//全部
	private MBtn btn_current;
	private Gson gson;
	
	private static final int FISH = 1;
	private static final int WAIT = 2;
	private static final int AFTER = 3;
	private static final int ALL = 4;
	private static final int ERROR = 5;
	
	private FishEntity fishentity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_order);
		gson = new Gson();
		arrayList_kong = new ArrayList<DingEntity>();
		arrayList_finish = new ArrayList<DingEntity>();
		mainHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case ERROR:
					Toast.makeText(MyOrderActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
					break;
				case FISH:
					M_sw(FISH);
					break;
				case WAIT:
					M_sw(WAIT);
					break;
				case AFTER:
					M_sw(AFTER);
					break;
				case ALL:
					M_sw(ALL);
					break;

				default:
					break;
				}
				super.handleMessage(msg);
			}
		};
		
		initView();
		
		loadData(FISH,HttpAddress.METHOD_GETLIST_FINISH);
		
	}

	//判断数据状态
	public void M_sw(int i){
		int status = fishentity.getStatus();
		if(2000 == status){
			//加载数据
			arrayList_finish =  fishentity.getOrders();
			finishAdapter.add2Adapter(arrayList_finish, i);
			xliv_myorder.setAdapter(finishAdapter);
			finishAdapter.notifyDataSetChanged();
			
		} else {
			//清空数据
			String str = fishentity.getText();
			
			Toast.makeText(MyOrderActivity.this, str, Toast.LENGTH_SHORT).show();
			//加载空集合
			finishAdapter.add2Adapter(arrayList_kong, i);
			xliv_myorder.setAdapter(finishAdapter);
			finishAdapter.notifyDataSetChanged();
			
		}
		
		
	}
	
	private void initView() {
		actionBar_myorder = (ActionBarView) findViewById(R.id.actionBar_myorder);
		actionBar_myorder.setActionBar(R.string.back, -1, R.string.title_actionbar_myorder, this);
		xliv_myorder = (ListView) findViewById(R.id.liv_myorder_sumary);
		finishAdapter = new OrderSumaryFinishAdapter(MyOrderActivity.this);
		btn_finish = (MBtn) findViewById(R.id.btn_myorder_finish);
		btn_waitpay = (MBtn) findViewById(R.id.btn_myorder_waitpay);
		btn_after = (MBtn) findViewById(R.id.btn_myorder_after);
		btn_all = (MBtn) findViewById(R.id.btn_myorder_all);
		btn_finish.setTv("可使用");
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

	private void loadData(final int i,String str) {
		SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
		String token = sp.getString("token", "");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("token", token);
		params.put("start", "0");
		params.put("length", "100");
		HttpUtil.getJSON(HttpAddress.ADDRESS+HttpAddress.PROJECT+
				HttpAddress.CLASS_APPORDER+str, 
				params, 
				new HttpCallbackListener() {
					@Override
					public void onFinish(Object response) {
						Log.d("response", response.toString());
						String json = response.toString();
						fishentity = gson.fromJson(json, FishEntity.class);
						Message msg = mainHandler.obtainMessage();
						msg.what = i;
						msg.obj = fishentity;
						mainHandler.sendMessage(msg);
					}
					
					@Override
					public void onError(Exception e) {
						mainHandler.sendEmptyMessage(ERROR);
					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_actionbar_left:
			MyOrderActivity.this.finish();
			break;
		case R.id.btn_myorder_finish:
			//TODO 获取可使用订单并加载
			btn_current.dispalyLine(false);
			btn_current = (MBtn) v;
			btn_current.dispalyLine(true);
			loadData(FISH,HttpAddress.METHOD_GETLIST_FINISH);
			break;
			
		case R.id.btn_myorder_after:
			//TODO 获取已过期订单并加载
			btn_current.dispalyLine(false);
			btn_current = (MBtn) v;
			btn_current.dispalyLine(true);
			loadData(AFTER,HttpAddress.METHOD_GETLIST_AFTER);
			break;
			
		case R.id.btn_myorder_all:
			//TODO 获取全部订单并加载
			btn_current.dispalyLine(false);
			btn_current = (MBtn) v;
			btn_current.dispalyLine(true);
			loadData(ALL,HttpAddress.METHOD_GETLIST_ALL);
			break;
			
		case R.id.btn_myorder_waitpay:
			btn_current.dispalyLine(false);
			btn_current = (MBtn) v;
			btn_current.dispalyLine(true);
			loadData(WAIT,HttpAddress.METHOD_GETLIST_WAIT);
			break;
		}
			
	}

}
