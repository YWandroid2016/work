package com.example.happyfishing.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.happyfishing.R;
import com.example.happyfishing.activity.SeachActivity;
import com.example.happyfishing.adapter.ACTVAdapter;
import com.example.happyfishing.adapter.FishshopAdapter;
import com.example.happyfishing.entity.FishshopEntity;
import com.example.happyfishing.tool.HttpAddress;
import com.example.happyfishing.tool.HttpCallbackListener;
import com.example.happyfishing.tool.HttpUtil;
import com.example.happyfishing.tool.UiUtil;
import com.example.happyfishing.view.ActionBarView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
public class FishingShopActivity extends Activity implements OnClickListener {
	// private Spinner sp_distence;
	// private Spinner sp_renqi;
	private AutoCompleteTextView atv_fishshop_search;
	private ACTVAdapter<String> adapter_ACTV ;
	private InputMethodManager inputManager;
	private ArrayList<FishshopEntity> fishshopEntities;
	private FishshopAdapter adapter;
	private ActionBarView actionBar_fishshop;

	private TextView tv_homeactivity_heikeng;
	private TextView tv_actionbar_right;
	private TextView tv_homeactivity_fishingshop;
	private TextView tv_homeactivity_mywallet;
	private TextView tv_homeactivity_jump2home;
	private Handler mainHandler;
	// private String distance[] = {"500米","1000米","2000米","5000米"};
	// private String renqi[] = {"智能排序","人气最高","离我最近"};
	// private ArrayAdapter<String> adapter_distance;
	// private ArrayAdapter<String> adapter_renqi;
	private ListView liv_fishingshop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fishing_shop);
		
		findViewById(R.id.img_fishshop_seach).setOnClickListener(this);

		mainHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					Log.d("aaa", "111");
					ArrayList<String> nameStrings = new ArrayList<String>();
					ArrayList<FishshopEntity> arrayList = new ArrayList<FishshopEntity>();
					arrayList = (ArrayList<FishshopEntity>) msg.obj;
					adapter.add2Adapter(fishshopEntities);
					adapter.notifyDataSetChanged();
					liv_fishingshop.setAdapter(adapter);
					for (int i = 0; i < arrayList.size(); i++) {
						nameStrings.add(arrayList.get(i).fishshopName);
						Log.d("aaa", arrayList.get(i).fishshopName);
					}
					Log.d("aaa", "1112");
					adapter_ACTV = new  ACTVAdapter<String>(FishingShopActivity.this,
							android.R.layout.simple_list_item_1, nameStrings);
					adapter_ACTV.add2Adapter(nameStrings);
//					atv_fishshop_search.setAdapter(adapter_ACTV);
					Log.d("aaa", "1113");
					break;

				case 2:
					ArrayList<FishshopEntity> arrayList2 = (ArrayList<FishshopEntity>) msg.obj;
					adapter.add2Adapter(arrayList2);
					adapter.notifyDataSetChanged();
					break;
				case 5:
					Toast.makeText(FishingShopActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
				super.handleMessage(msg);
			}
		};
		
		initView();

		initData();

		loadData();
	}
	
	@Override
	protected void onStart() {
		Log.d("boolean", "newMessage"+UiUtil.getNewMessage()+"");
		tv_actionbar_right = (TextView) findViewById(R.id.tv_actionbar_right);
		Drawable drawable = null;
		if (UiUtil.getNewMessage()) {
			drawable = getResources().getDrawable(R.drawable.ic_message_hasnew);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		}else {
			drawable = getResources().getDrawable(R.drawable.ic_message_nonew);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		}
		tv_actionbar_right.setCompoundDrawables(drawable, null, null, null);
		super.onStart();
	}

	private void initView() {
		
		
		Drawable drawable1 = getResources().getDrawable(R.drawable.ic_menu_home_default);
		drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
		tv_homeactivity_jump2home = (TextView) findViewById(R.id.tv_homeactivity_jump2home);
		tv_homeactivity_jump2home.setOnClickListener(this);
		tv_homeactivity_jump2home.setCompoundDrawables(null, drawable1, null, null);
		
		Drawable drawable2 = getResources().getDrawable(R.drawable.ic_menu_fishshop_selected);
		drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
		tv_homeactivity_fishingshop = (TextView) findViewById(R.id.tv_homeactivity_fishingshop);
		tv_homeactivity_fishingshop.setOnClickListener(this);
		tv_homeactivity_fishingshop.setCompoundDrawables(null, drawable2, null, null);
		
		Drawable drawable3 = getResources().getDrawable(R.drawable.ic_menu_fishpit_default);
		drawable3.setBounds(0, 0, drawable3.getMinimumWidth(), drawable3.getMinimumHeight());
		tv_homeactivity_heikeng = (TextView) findViewById(R.id.tv_homeactivity_heikeng);
		tv_homeactivity_fishingshop.setTextColor(Color.parseColor("#3086f2"));
		tv_homeactivity_heikeng.setOnClickListener(this);
		tv_homeactivity_heikeng.setCompoundDrawables(null, drawable3, null, null);
		
		Drawable drawable4 = getResources().getDrawable(R.drawable.ic_menu_mywallet_default);
		drawable4.setBounds(0, 0, drawable4.getMinimumWidth(), drawable4.getMinimumHeight());
		tv_homeactivity_mywallet = (TextView) findViewById(R.id.tv_homeactivity_mywallet);
		tv_homeactivity_mywallet.setOnClickListener(this);
		tv_homeactivity_mywallet.setCompoundDrawables(null, drawable4, null, null);
		
		
		actionBar_fishshop = (ActionBarView) findViewById(R.id.actionBar_fishshop);
		actionBar_fishshop.setActionBar(-1, -1, R.string.title_actionbar_fishshop, null);
		
		findViewById(R.id.tv_homeactivity_jump2home).setOnClickListener(this);
		findViewById(R.id.tv_homeactivity_fishingshop).setOnClickListener(this);
		findViewById(R.id.tv_homeactivity_heikeng).setOnClickListener(this);
		findViewById(R.id.tv_actionbar_right).setOnClickListener(this);
		findViewById(R.id.tv_homeactivity_mywallet).setOnClickListener(this);
//		atv_fishshop_search = (AutoCompleteTextView) findViewById(R.id.atv_fishshop_search);
//		atv_fishshop_search.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				liv_fishingshop.setBackgroundColor(Color.parseColor("#dddddd"));
//			}
//		});
//		atv_fishshop_search.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				HashMap<String, String> params = new HashMap<String, String>();
//				params.put("fishpit", adapter_ACTV.getItem(position));
//				Log.d("paras", adapter_ACTV.getItem(position));
//				HttpUtil.getJSON(HttpAddress.ADDRESS+HttpAddress.PROJECT+
//						HttpAddress.CLASS_MERCHANT+HttpAddress.METHOD_FISHSHOPLIST, 
//						params, new HttpCallbackListener() {
//							
//							@Override
//							public void onFinish(Object response) {
//								Log.d("select_result", response.toString());
//								JSONObject jsonObject = (JSONObject) response;
//								ArrayList<FishshopEntity> arrayList = new ArrayList<FishshopEntity>();
//								try {
//									int total ;
//									String totalString= jsonObject.getString("total");
//									total = Integer.parseInt(totalString);
//									JSONArray jsonArray = jsonObject.getJSONArray("data");
//									for (int i = 0; i < jsonArray.length(); i++) {
//										JSONObject jsonObject2 = jsonArray.getJSONObject(i);
//										String name = (String) jsonObject2.get("name");
//										String headImageURL = (String) jsonObject2.get("headImage");
//										String detail = (String) jsonObject2.get("introduction");
//										int id = jsonObject2.getInt("id");
//										arrayList.add(new FishshopEntity(name,  headImageURL, detail,id+""));
//									}
//									Message message = new Message();
//									message.what=2;
//									message.obj = arrayList;
//									mainHandler.sendMessage(message);
////									fishpitAdapter.addToAdapter(arrayList, true);
//									Log.d("total", total+" "+jsonArray);
//									
//								} catch (JSONException e) {
//									mainHandler.sendEmptyMessage(5);
//									e.printStackTrace();
//								}
//								
//							}
//							
//							@Override
//							public void onError(Exception e) {
//								mainHandler.sendEmptyMessage(5);
//							}
//						});
//			}
//		});
//		// sp_distence=(Spinner) findViewById(R.id.sp_fishpit_distance);
		// sp_distence.setPrompt("距离");
		// sp_renqi=(Spinner) findViewById(R.id.sp_fishpit_renqi);
		liv_fishingshop = (ListView) findViewById(R.id.liv_fishingshop);

		// adapter_distance = new ArrayAdapter<String>(FishpitActivity.this,
		// R.layout.fishpit_spinner_distance, distance);
		//
		// adapter_renqi = new ArrayAdapter<String>(FishpitActivity.this,
		// R.layout.fishpit_spinner_distance, renqi);
	}

	private void initData() {

	}

	private void loadData() {
		fishshopEntities = new ArrayList<FishshopEntity>();
		HttpUtil.getJSON(HttpAddress.ADDRESS+HttpAddress.PROJECT+
				HttpAddress.CLASS_MERCHANT+HttpAddress.METHOD_FISHSHOPLIST, 
				null, 
				new HttpCallbackListener() {
					
					@Override
					public void onFinish(Object response) {
						JSONObject jsonObject = (JSONObject) response;
						JSONObject jsonObject2;
						String idString;
						String nameString;
						String imgUrl;
						String fishshopDetail;
						try {
							JSONArray jsonArray = jsonObject.getJSONArray("data");
							for (int i = 0; i < jsonArray.length(); i++) {
								jsonObject2 = (JSONObject) jsonArray.get(i);
								Log.d("jsonObject", jsonObject2.toString());
								idString = jsonObject2.getString("id");
								nameString = jsonObject2.getString("name");
								imgUrl = jsonObject2.getString("headImage");
								fishshopDetail = jsonObject2.getString("introduction");
								fishshopEntities.add(new FishshopEntity(nameString, imgUrl, fishshopDetail, idString));
//								adapter.add2Adapter(fishshopEntities,false);
//								liv_fishingshop.setAdapter(adapter);
							}
							Message message = new Message();
							message.what=1;
							message.obj = fishshopEntities;
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
	
		adapter = new FishshopAdapter(FishingShopActivity.this);
		
		liv_fishingshop.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FishingShopActivity.this, FishshopDetailActivity.class);

				intent.putExtra("title", fishshopEntities.get(position).fishshopName.toString());
				intent.putExtra("id", fishshopEntities.get(position).idString);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fishing_shop, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_homeactivity_heikeng:
			Intent intent2 = new Intent(FishingShopActivity.this, FishpitActivity.class);
			startActivity(intent2);
			break;
		case R.id.tv_homeactivity_mywallet:
			Intent intent4 = new Intent(FishingShopActivity.this, MyWalletActivity.class);
			startActivity(intent4);
			break;
		case R.id.tv_homeactivity_jump2home:
			Intent intent3 = new Intent(FishingShopActivity.this, HomeActivity.class);
			startActivity(intent3);
			break;
		case R.id.img_fishshop_seach:
			Intent intent5 = new Intent(FishingShopActivity.this, SeachActivity.class);
			startActivity(intent5);
			break;
		case R.id.tv_actionbar_right:
			SharedPreferences sp = getSharedPreferences("token", Context.MODE_PRIVATE);
			String token = sp.getString("token", "");
			if (token.equals("")) {
				
				Toast.makeText(FishingShopActivity.this, "尚未登录，请登录", Toast.LENGTH_SHORT).show();
			}else {
				Intent intent11 = new Intent(FishingShopActivity.this, MessageActivity.class);
				startActivity(intent11);
			}
			break;
		default:
			break;
		}

	}
}
