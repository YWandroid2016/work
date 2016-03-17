package com.example.happyfishing.activity;

import java.util.ArrayList;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.happyfishing.R;
import com.example.happyfishing.SeachActivity;
import com.example.happyfishing.adapter.ACTVAdapter;
import com.example.happyfishing.adapter.FishpitFishpitAdapter;
import com.example.happyfishing.entity.FishpitSumaryEntity;
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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class FishpitActivity extends Activity implements OnClickListener,OnTouchListener{

	// private Spinner sp_distence;
	// private Spinner sp_renqi;
	private InputMethodManager inputManager;
	private AutoCompleteTextView atv_fishpit_search;
	private ACTVAdapter<String> adapter ;
	private TextView tv_actionbar_right;
	
	private TextView tv_homeactivity_heikeng;
	private TextView tv_homeactivity_fishingshop;
	private TextView tv_homeactivity_mywallet;
	private TextView tv_homeactivity_jump2home;
	private ImageView img_fishpit_seach;
	
	
	private ActionBarView actionBarView_fishpit;

	// private String distance[] = {"500米","1000米","2000米","5000米"};
	// private String renqi[] = {"智能排序","人气最高","离我最近"};
	// private ArrayAdapter<String> adapter_distance;
	// private ArrayAdapter<String> adapter_renqi;
	private ArrayList<FishpitSumaryEntity> fishpitEntities;
	private ListView liv_fishpit;
	private FishpitFishpitAdapter fishpitAdapter;
	private ArrayList<FishpitSumaryEntity> fishpitSumaries;
	private Handler mainHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fishpit);
		
		findViewById(R.id.ll_fishpit).setOnTouchListener(this);
		findViewById(R.id.img_fishpit_seach).setOnClickListener(this);
		
		mainHandler = new Handler(FishpitActivity.this.getMainLooper()){
			
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					int fishpit_total = msg.arg1;
					ArrayList<String> fishpitName = new ArrayList<String>();
					for (int i = 0; i < fishpit_total; i++) {
						fishpitName.add(fishpitSumaries.get(i).name);
					}
					ArrayList<FishpitSumaryEntity> arrayList1 = new ArrayList<FishpitSumaryEntity>();
					arrayList1 = (ArrayList<FishpitSumaryEntity>) msg.obj;
					fishpitAdapter.addToAdapter(arrayList1, true);
					fishpitAdapter.notifyDataSetChanged();
					adapter = new ACTVAdapter<String>(FishpitActivity.this,
							android.R.layout.simple_list_item_1, fishpitName);
					Log.d("aaaaa", fishpitName.size()+"");
					adapter.add2Adapter(fishpitName);
//					atv_fishpit_search.setAdapter(adapter);
					break;
				case 2:
					ArrayList<FishpitSumaryEntity> arrayList2 = (ArrayList<FishpitSumaryEntity>) msg.obj;
					fishpitAdapter.addToAdapter(arrayList2, true);
					fishpitAdapter.notifyDataSetChanged();
					break;
				case 5:
					Toast.makeText(FishpitActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
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
		
		Drawable drawable2 = getResources().getDrawable(R.drawable.ic_menu_fishshop_default);
		drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
		tv_homeactivity_fishingshop = (TextView) findViewById(R.id.tv_homeactivity_fishingshop);
		tv_homeactivity_fishingshop.setOnClickListener(this);
		tv_homeactivity_fishingshop.setCompoundDrawables(null, drawable2, null, null);
		
		Drawable drawable3 = getResources().getDrawable(R.drawable.ic_menu_fishpit_selected);
		drawable3.setBounds(0, 0, drawable3.getMinimumWidth(), drawable3.getMinimumHeight());
		tv_homeactivity_heikeng = (TextView) findViewById(R.id.tv_homeactivity_heikeng);
		tv_homeactivity_heikeng.setTextColor(Color.parseColor("#3086f2"));
		tv_homeactivity_heikeng.setOnClickListener(this);
		tv_homeactivity_heikeng.setCompoundDrawables(null, drawable3, null, null);
		
		Drawable drawable4 = getResources().getDrawable(R.drawable.ic_menu_mywallet_default);
		drawable4.setBounds(0, 0, drawable4.getMinimumWidth(), drawable4.getMinimumHeight());
		tv_homeactivity_mywallet = (TextView) findViewById(R.id.tv_homeactivity_mywallet);
		tv_homeactivity_mywallet.setOnClickListener(this);
		tv_homeactivity_mywallet.setCompoundDrawables(null, drawable4, null, null);
		// sp_distence=(Spinner) findViewById(R.id.sp_fishpit_distance);
		// sp_distence.setPrompt("距离");
		// sp_renqi=(Spinner) findViewById(R.id.sp_fishpit_renqi);
		
		actionBarView_fishpit=(ActionBarView) findViewById(R.id.actionbar_fishpit);
		actionBarView_fishpit.setActionBar(-1, -1, R.string.fishipit, this);
		
//		atv_fishpit_search = (AutoCompleteTextView) findViewById(R.id.atv_fishpit_search);
//		atv_fishpit_search.setFocusable(false);
//		atv_fishpit_search.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
////				atv_fishpit_search.setBackgroundDrawable(getResources().getDrawable(R.drawable.tbtn_background));
//				atv_fishpit_search.setBackgroundResource(R.drawable.tbtn_background);
//				liv_fishpit.setAlpha((float) 0.1);
////				atv_fishpit_search.setFocusable(true);
////				atv_fishpit_search.requestFocus();
//				inputManager = (InputMethodManager) atv_fishpit_search.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//				inputManager.showSoftInput(atv_fishpit_search, 0);
//				atv_fishpit_search.setOnEditorActionListener(new OnEditorActionListener() {
//					
//					@Override
//					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//						// TODO Auto-generated method stub
//						if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//							// 点击按钮隐藏键盘
//							inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
//							liv_fishpit.setAlpha(1);
//							return true;
//						}
//						return false;
//					}
//				});
//			}
//		});
//		atv_fishpit_search.setOnItemClickListener(new OnItemClickListener() {

//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				
////				192.168.1.135:8092/appConsole/merchant/fishpitList?
//				liv_fishpit.setAlpha(1);
//				HashMap<String, String> params = new HashMap<String, String>();
//				params.put("fishpit", adapter.getItem(position));
//				Log.d("paras", adapter.getItem(position));
//				HttpUtil.getJSON(HttpAddress.ADDRESS+HttpAddress.PROJECT+
//						HttpAddress.CLASS_MERCHANT+HttpAddress.METHOD_FISHPITLIST, 
//						params, new HttpCallbackListener() {
//							
//							@Override
//							public void onFinish(Object response) {
//								Log.d("select_result", response.toString());
//								JSONObject jsonObject = (JSONObject) response;
//								ArrayList<FishpitSumaryEntity> arrayList = new ArrayList<FishpitSumaryEntity>();
//								try {
//									int total ;
//									String totalString= jsonObject.getString("total");
//									total = Integer.parseInt(totalString);
//									JSONArray jsonArray = jsonObject.getJSONArray("data");
//									for (int i = 0; i < jsonArray.length(); i++) {
//										JSONObject jsonObject2 = jsonArray.getJSONObject(i);
//										Log.d("aaa", jsonObject2.toString());
//										String name = (String) jsonObject2.get("name");
//										String headImageURL = (String) jsonObject2.get("headImage");
//										String detail = (String) jsonObject2.get("introduction");
//										int id =  jsonObject2.getInt("id");
//										arrayList.add(new FishpitSumaryEntity(name,  headImageURL, detail,id+""));
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
//				liv_fishpit.setVisibility(View.VISIBLE);
//			}
//		});
		
		
		
//		edt_fishpit = (EditText) findViewById(R.id.edt_fishpit);
//		edt_fishpit.setOnEditorActionListener(new OnEditorActionListener() {
//
//			@Override
//			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//					Toast.makeText(FishpitActivity.this, "执行搜索功能", Toast.LENGTH_SHORT).show();
//					liv_fishpit.setVisibility(View.VISIBLE);
////					点击搜索按钮隐藏键盘
//					inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
//					edt_fishpit.setVisibility(View.INVISIBLE);
//					tv_edt_fishpit.setVisibility(View.VISIBLE);
//					return true;
//				}
//				return false;
//			}
//		});
//
//		tv_edt_fishpit = (TextView) findViewById(R.id.tv_edt_fishpit);
//		tv_edt_fishpit.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				tv_edt_fishpit.setVisibility(View.INVISIBLE);
//				edt_fishpit.setVisibility(View.VISIBLE);
//				edt_fishpit.requestFocus();
//				inputManager = (InputMethodManager) edt_fishpit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//
//				inputManager.showSoftInput(edt_fishpit, 0);
//
//				liv_fishpit.setVisibility(View.INVISIBLE);
//			}
//		});

		// adapter_distance = new ArrayAdapter<String>(FishpitActivity.this,
		// R.layout.fishpit_spinner_distance, distance);
		//
		// adapter_renqi = new ArrayAdapter<String>(FishpitActivity.this,
		// R.layout.fishpit_spinner_distance, renqi);

		fishpitAdapter = new FishpitFishpitAdapter(FishpitActivity.this);

		liv_fishpit = (ListView) findViewById(R.id.liv_heikeng);
	}

	private void initData() {
		// sp_distence.setAdapter(adapter_distance);
		// sp_renqi.setAdapter(adapter_renqi);

		fishpitEntities = new ArrayList<FishpitSumaryEntity>();
	}

	private void loadData() {
		
		HttpUtil.getJSON(HttpAddress.ADDRESS+HttpAddress.PROJECT+HttpAddress.CLASS_MERCHANT+HttpAddress.METHOD_FISHPITLIST, null, new HttpCallbackListener() {
			
			@Override
			public void onFinish(Object response) {
				
				Log.d("result", response.toString());
				JSONObject object1 = (JSONObject) response;
				try {
					JSONArray object2 = object1.getJSONArray("data");
					
					int fishpit_total = object1.getInt("total");
					fishpitSumaries = new ArrayList<FishpitSumaryEntity>();
					
					for (int i = 0; i < fishpit_total; i++) {
						JSONObject jsonObject = (JSONObject) object2.get(i);
						jsonObject.get("name");
						jsonObject.get("headImage");
						jsonObject.get("id");
						fishpitSumaries.add(new FishpitSumaryEntity(jsonObject.get("name").toString(), 
								jsonObject.get("headImage").toString(), 
								jsonObject.get("introduction").toString(), 
								jsonObject.get("id").toString()));
						
//						add(new FishpitSumary(jsonObject.get("name").toString(), 
//								jsonObject.get("headImage").toString(), 
//								jsonObject.get("introduction").toString(),),
//								jsonObject.get("id").toString());
					}
					
					Message message = new Message();
					message.what=1;
					message.arg1 = fishpit_total;
					message.obj = fishpitSumaries;
					mainHandler.sendMessage(message);
					
				} catch (JSONException e) {
					mainHandler.sendEmptyMessage(5);
				}
			}
			
			@Override
			public void onError(Exception e) {
				mainHandler.sendEmptyMessage(5);
			}
		});

		fishpitAdapter.addToAdapter(fishpitEntities, true);
		liv_fishpit.setAdapter(fishpitAdapter);
		liv_fishpit.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(FishpitActivity.this, FishpitDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("title", fishpitAdapter.getArrayList().get(position).name);
				bundle.putString("id", fishpitAdapter.getArrayList().get(position).id);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.fishpit, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_homeactivity_jump2home:
			Intent intent4 = new Intent(FishpitActivity.this, HomeActivity.class);
			startActivity(intent4);
			break;
		case R.id.tv_homeactivity_fishingshop:
			Intent intent3 = new Intent(FishpitActivity.this, FishingShopActivity.class);
			startActivity(intent3);
			break;
		case R.id.tv_homeactivity_mywallet:
			Intent intent1 = new Intent(FishpitActivity.this, MyWalletActivity.class);
			startActivity(intent1);
			break;
		case R.id.img_fishpit_seach:
			Intent intent5 = new Intent(FishpitActivity.this, SeachActivity.class);
			startActivity(intent5);
			break;
		case R.id.tv_actionbar_right:
			SharedPreferences sp = getSharedPreferences("token", Context.MODE_PRIVATE);
			String token = sp.getString("token", "");
			if (token.equals("")) {
				Toast.makeText(FishpitActivity.this, "尚未登录，请登录", Toast.LENGTH_SHORT).show();
			}else {
				Intent intent2 = new Intent(FishpitActivity.this, MessageActivity.class);
				startActivity(intent2);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		liv_fishpit.setAlpha(1);
		return false;
	}

}
