package com.example.happyfishing.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.happyfishing.R;
import com.example.happyfishing.R.id;
import com.example.happyfishing.R.layout;
import com.example.happyfishing.R.menu;
import com.example.happyfishing.R.string;
import com.example.happyfishing.adapter.CommodityAdapter;
import com.example.happyfishing.entity.CommodityEntity;
import com.example.happyfishing.tool.HttpAddress;
import com.example.happyfishing.tool.HttpCallbackListener;
import com.example.happyfishing.tool.HttpUtil;
import com.example.happyfishing.view.ActionBarView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class CommodityDisplayActivity extends Activity implements OnClickListener{

	private ActionBarView actionBar_commodity;
	private GridView grv_commodity;
	private ArrayList<CommodityEntity> arrayList;
	private CommodityAdapter adapter;
	private String idString;
	private Handler mainHandler;
	private String idString_product;
	private double target_lat;
	private double target_lon;
	private double my_lat;
	private double my_lon;
	private String cityString;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commodity_display);
		
		mainHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.add2Adapter(arrayList);
					grv_commodity.setAdapter(adapter);
					break;
				case 5:
					Toast.makeText(CommodityDisplayActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
				super.handleMessage(msg);
			}
		};
		
		Intent intent = getIntent();
		idString = intent.getStringExtra("id");
		Bundle bundle = intent.getExtras();
		target_lat = bundle.getDouble("target_lat");
		target_lon = bundle.getDouble("target_lon");
		my_lat = bundle.getDouble("my_lat");
		my_lon = bundle.getDouble("my_lon");
		cityString = bundle.getString("city");
		initView();
		
		loadData();
		
	}

	private void initView() {
		actionBar_commodity = (ActionBarView) findViewById(R.id.actionbar_commodity_display);
		actionBar_commodity.setActionBar(R.string.back, -1, R.string.title_actionbar_commodity, this);
		
		grv_commodity=(GridView) findViewById(R.id.grv_commodity);
		arrayList= new ArrayList<CommodityEntity>();
		adapter = new CommodityAdapter(CommodityDisplayActivity.this);
		
	}

	private void loadData() {
//		arrayList.add(new CommodityEntity(null, "aaa", "￥4.5", "4444人已购买"));
//		arrayList.add(new CommodityEntity(null, "aaa", "￥4.5", "4444人已购买"));
//		arrayList.add(new CommodityEntity(null, "aaa", "￥4.5", "4444人已购买"));
//		arrayList.add(new CommodityEntity(null, "aaa", "￥4.5", "4444人已购买"));
//		arrayList.add(new CommodityEntity(null, "aaa", "￥4.5", "4444人已购买"));
//		arrayList.add(new CommodityEntity(null, "aaa", "￥4.5", "4444人已购买"));
//		arrayList.add(new CommodityEntity(null, "aaa", "￥4.5", "4444人已购买"));
//		arrayList.add(new CommodityEntity(null, "aaa", "￥4.5", "4444人已购买"));
//		arrayList.add(new CommodityEntity(null, "aaa", "￥4.5", "4444人已购买"));
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("merchantId", idString);
		Log.d("id", idString);
		HttpUtil.getJSON(HttpAddress.ADDRESS+HttpAddress.PROJECT+
				HttpAddress.CLASS_FISHPRODUCT+HttpAddress.METHOD_PRODUCTLIST, 
				params, 
				new HttpCallbackListener() {
					
					@Override
					public void onFinish(Object response) {
						Log.d("response", response.toString());
						JSONObject jsonObject = (JSONObject) response;
						JSONObject jsonObject2;
						String idString;
						String imageURL;
						String commoditySumary;
						String price;
						String sellNum;
						try {
							JSONArray jsonArray =jsonObject.getJSONArray("data");
							for (int i = 0; i < jsonArray.length(); i++) {
								jsonObject2 = (JSONObject) jsonArray.get(i);
								idString_product = jsonObject2.getString("id");
								idString = jsonObject2.getString("id");
								imageURL = jsonObject2.getString("listBannerUrl");
								commoditySumary = jsonObject2.getString("name");
								price = jsonObject2.getString("curPrice");
								sellNum = jsonObject2.getString("saleCount");
								arrayList.add(new CommodityEntity(idString, imageURL, commoditySumary, price, sellNum));
							}
							Message message = new Message();
							message.obj=arrayList;
							message.what=1;
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
		grv_commodity.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(CommodityDisplayActivity.this, CommodityDetailShowActivity.class);
				intent.putExtra("id", idString_product);
				Bundle bundle1 = new Bundle();
				bundle1.putDouble("target_lat", target_lat);
				bundle1.putDouble("target_lon", target_lon);
				bundle1.putDouble("my_lat", my_lat);
				bundle1.putDouble("my_lon", my_lon);
				bundle1.putString("city", cityString);
				intent.putExtras(bundle1);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.commodity_display, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_actionbar_left:
			CommodityDisplayActivity.this.finish();
			break;

		default:
			break;
		}
	}

}
