package com.example.happyfishing.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.happyfishing.R;
import com.example.happyfishing.R.layout;
import com.example.happyfishing.R.menu;
import com.example.happyfishing.adapter.FishpitFishpitAdapter;
import com.example.happyfishing.adapter.FishshopAdapter;
import com.example.happyfishing.entity.FishpitSumaryEntity;
import com.example.happyfishing.entity.FishshopEntity;
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

public class MyCollectionActivity extends Activity implements OnClickListener{

	private ActionBarView actionBar_mycollection;
	private ListView liv_mycollection;
	private ArrayList<FishpitSumaryEntity> fishpitSumaries;
	private ArrayList<FishshopEntity> fishshopEntities;
	private FishpitFishpitAdapter fishpitAdapter;
	private FishshopAdapter fishshopAdapter;
	private Handler mainHandler;
	private MBtn btn_hei;
	private MBtn btn_yu;
	private MBtn btn_current;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_collection);
		
		mainHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					ArrayList<FishpitSumaryEntity> fishpitSumaries = (ArrayList<FishpitSumaryEntity>) msg.obj;
					fishpitAdapter.addToAdapter(fishpitSumaries, true);
					liv_mycollection.setAdapter(fishpitAdapter);
					break;
				case 5:
					Toast.makeText(MyCollectionActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
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
		fishpitSumaries = new ArrayList<FishpitSumaryEntity>();
		fishshopEntities = new ArrayList<FishshopEntity>();
		actionBar_mycollection = (ActionBarView) findViewById(R.id.actionBar_mycollection);
		actionBar_mycollection.setActionBar(R.string.back, -1, R.string.title_actionbar_mycollection, this);
		btn_hei = (MBtn) findViewById(R.id.btn_mycollection_fishpit);
		btn_yu = (MBtn) findViewById(R.id.btn_mycollection_fishshop);
		btn_hei.setTv("黑坑");
		btn_yu.setTv("渔具店");
		btn_hei.setOnClickListener(this);
		btn_yu.setOnClickListener(this);
		btn_current = btn_hei;
		btn_current.dispalyLine(true);
		btn_yu.dispalyLine(false);
		liv_mycollection = (ListView) findViewById(R.id.liv_mycollection_show);
		
		fishpitAdapter = new FishpitFishpitAdapter(MyCollectionActivity.this);
		fishshopAdapter = new FishshopAdapter(MyCollectionActivity.this);
		
		liv_mycollection.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(MyCollectionActivity.this, FishpitDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("title", fishpitAdapter.getArrayList().get(position).name);
				bundle.putString("id", fishpitAdapter.getArrayList().get(position).id);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		
	}

	private void loadData() {
		SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
		String token = sp.getString("token", "");
		HashMap<String, String> params1 = new HashMap<String, String>();
		params1.put("token", token);
		HttpUtil.getJSON(HttpAddress.ADDRESS+HttpAddress.PROJECT+
				HttpAddress.CLASS_USERSHOW+HttpAddress.METHOD_COLLECTFISHPONDS,
				params1 , 
				new HttpCallbackListener() {
					@Override
					public void onFinish(Object response) {
						Log.d("response", response.toString());
						JSONObject jsonObject = (JSONObject) response;
						JSONObject jsonObject2;
						
						try {
							JSONArray jsonArray = jsonObject.getJSONArray("data");
							for (int i = 0; i < jsonArray.length(); i++) {
								jsonObject2=jsonArray.getJSONObject(i);
								Log.d("name", jsonObject2.getString("merchantName"));
								Log.d("name", jsonObject2.getString("merchantId"));
								Log.d("name", jsonObject2.getString("merchantImage"));
								Log.d("name", jsonObject2.getString("merchantInfo"));
								fishpitSumaries.add(new FishpitSumaryEntity(
										jsonObject2.getString("merchantName"), jsonObject2.getString("merchantImage"), 
										jsonObject2.getString("merchantInfo"), jsonObject2.getString("merchantId")));
								Message message = new Message();
								message.what = 1;
								message.obj = fishpitSumaries;
								mainHandler.sendMessage(message);
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
		HttpUtil.getJSON(HttpAddress.ADDRESS+HttpAddress.PROJECT+
				HttpAddress.CLASS_USERSHOW+HttpAddress.METHOD_COLLECTFISHSHOPS, 
				params1, 
				new HttpCallbackListener() {
					
					@Override
					public void onFinish(Object response) {
						Log.d("response", response.toString());
						JSONObject jsonObject = (JSONObject) response;
						JSONObject jsonObject2;
						try {
							JSONArray jsonArray = jsonObject.getJSONArray("data");
							for (int i = 0; i < jsonArray.length(); i++) {
								jsonObject2=jsonArray.getJSONObject(i);
								Log.d("name", jsonObject2.getString("merchantName"));
								Log.d("name", jsonObject2.getString("merchantId"));
								Log.d("name", jsonObject2.getString("merchantImage"));
								Log.d("name", jsonObject2.getString("merchantInfo"));
								fishshopEntities.add(new FishshopEntity(
										jsonObject2.getString("merchantName"), jsonObject2.getString("merchantImage"), 
										jsonObject2.getString("merchantInfo"), jsonObject2.getString("merchantId")));
								fishshopAdapter.add2Adapter(fishshopEntities);
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
		getMenuInflater().inflate(R.menu.my_collection, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_actionbar_left:
			MyCollectionActivity.this.finish();
			break;
		case R.id.btn_mycollection_fishpit:
			btn_current.dispalyLine(false);
			btn_current = (MBtn) v;
			btn_current.dispalyLine(true);
			liv_mycollection.setAdapter(fishpitAdapter);
			liv_mycollection.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent intent = new Intent(MyCollectionActivity.this, FishpitDetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("title", fishpitAdapter.getArrayList().get(position).name);
					bundle.putString("id", fishpitAdapter.getArrayList().get(position).id);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			});
			fishpitAdapter.notifyDataSetChanged();
			break;
		case R.id.btn_mycollection_fishshop:
			btn_current.dispalyLine(false);
			btn_current = (MBtn) v;
			btn_current.dispalyLine(true);
			liv_mycollection.setAdapter(fishshopAdapter);
			liv_mycollection.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent intent = new Intent(MyCollectionActivity.this, FishshopDetailActivity.class);

					intent.putExtra("title", fishshopEntities.get(position).fishshopName.toString());
					intent.putExtra("id", fishshopEntities.get(position).idString);
					startActivity(intent);
				}
			});
			fishshopAdapter.notifyDataSetChanged();
			break;
		default:
			break;
		}
	}

}
