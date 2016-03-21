package com.example.happyfishing.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

import com.example.happyfishing.R;
import com.example.happyfishing.activity.FishingShopActivity;
import com.example.happyfishing.activity.FishpitActivity;
import com.example.happyfishing.activity.FishpitDetailActivity;
import com.example.happyfishing.activity.FishshopDetailActivity;
import com.example.happyfishing.adapter.ACTVAdapter;
import com.example.happyfishing.adapter.FishpitFishpitAdapter;
import com.example.happyfishing.adapter.FishshopAdapter;
import com.example.happyfishing.entity.FishpitSumaryEntity;
import com.example.happyfishing.entity.FishshopEntity;
import com.example.happyfishing.tool.HttpAddress;
import com.example.happyfishing.tool.HttpCallbackListener;
import com.example.happyfishing.tool.HttpUtil;

public class SeachActivity extends Activity implements OnClickListener {
	private TextView tv_seach;
	private ListView lv_seach;
	private AutoCompleteTextView atv_fishpit_search;
	private InputMethodManager inputManager;
	private Handler mHandler;
	private ACTVAdapter<String> adapter;
	private ArrayList<FishpitSumaryEntity> fishpitSumaries;
	private ArrayList<FishshopEntity> fishshopEntities;
	private FishpitFishpitAdapter fishpitAdapter;
	private int from;
	private FishshopAdapter fishShopAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seach);
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 5:
					Toast.makeText(SeachActivity.this, "网络连接错误",
							Toast.LENGTH_SHORT).show();
					break;
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
					adapter = new ACTVAdapter<String>(SeachActivity.this,
							android.R.layout.simple_list_item_1, fishpitName);
					Log.d("aaaaa", fishpitName.size() + "");
					adapter.add2Adapter(fishpitName);
					atv_fishpit_search.setAdapter(adapter);
					break;
				case 11:
					ArrayList<String> nameStrings = new ArrayList<String>();
					ArrayList<FishshopEntity> arrayList = new ArrayList<FishshopEntity>();
					arrayList = (ArrayList<FishshopEntity>) msg.obj;
//					fishShopAdapter.add2Adapter(fishshopEntities);
//					fishShopAdapter.notifyDataSetChanged();
					lv_seach.setAdapter(fishShopAdapter);
					for (int i = 0; i < arrayList.size(); i++) {
						nameStrings.add(arrayList.get(i).fishshopName);
						Log.d("aaa", arrayList.get(i).fishshopName);
					}
					adapter = new ACTVAdapter<String>(SeachActivity.this,
							android.R.layout.simple_list_item_1, nameStrings);
					adapter.add2Adapter(nameStrings);
					atv_fishpit_search.setAdapter(adapter);
					break;
				case 2:
					ArrayList<FishpitSumaryEntity> arrayList2 = (ArrayList<FishpitSumaryEntity>) msg.obj;
					fishpitAdapter.addToAdapter(arrayList2, true);
					lv_seach.setAdapter(fishpitAdapter);
					lv_seach.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							Intent intent = new Intent(SeachActivity.this,
									FishpitDetailActivity.class);
							Bundle bundle = new Bundle();
							bundle.putString("title", fishpitAdapter
									.getArrayList().get(position).name);
							bundle.putString("id", fishpitAdapter
									.getArrayList().get(position).id);
							intent.putExtras(bundle);
							startActivity(intent);
						}
					});
					fishpitAdapter.notifyDataSetChanged();
					break;

				
				case 22:
					ArrayList<FishshopEntity> arrayList22 = (ArrayList<FishshopEntity>) msg.obj;
					fishShopAdapter.add2Adapter(arrayList22);
					lv_seach.setAdapter(fishShopAdapter);
					lv_seach.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							Intent intent = new Intent(SeachActivity.this,
									FishshopDetailActivity.class);

							intent.putExtra("title", fishshopEntities
									.get(position).fishshopName.toString());
							intent.putExtra("id",
									fishshopEntities.get(position).idString);
							startActivity(intent);
						}
					});
					fishShopAdapter.notifyDataSetChanged();
					break;
				default:
					break;
				}
				super.handleMessage(msg);
			}
		};

		Intent intent = getIntent();
		from = intent.getIntExtra("from", -1);
		Log.d("from", from + "");

		initView();

		loadData();
	}

	private void initView() {
		// from = 0 来自鱼坑
		if (from == 0) {
			lv_seach = (ListView) findViewById(R.id.lv_seach);
			fishpitAdapter = new FishpitFishpitAdapter(SeachActivity.this);
			// lv_seach.setAdapter(fishpitAdapter);

			findViewById(R.id.img_seach_back).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							SeachActivity.this.finish();
						}
					});

			tv_seach = (TextView) findViewById(R.id.tv_seach_seach);
			tv_seach.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {

				}
			});

			atv_fishpit_search = (AutoCompleteTextView) findViewById(R.id.atv_fishshop_search);
			atv_fishpit_search.setThreshold(1);
			// atv_fishpit_search.setFocusable(false);
			atv_fishpit_search.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					atv_fishpit_search.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.tbtn_background));
					atv_fishpit_search
							.setBackgroundResource(R.drawable.tbtn_background);
					atv_fishpit_search.setFocusable(true);
					atv_fishpit_search.requestFocus();
					inputManager = (InputMethodManager) atv_fishpit_search
							.getContext().getSystemService(
									Context.INPUT_METHOD_SERVICE);
					inputManager.showSoftInput(atv_fishpit_search, 0);
					atv_fishpit_search
							.setOnEditorActionListener(new OnEditorActionListener() {

								@Override
								public boolean onEditorAction(TextView v,
										int actionId, KeyEvent event) {
									// TODO Auto-generated method stub
									if (actionId == EditorInfo.IME_ACTION_SEARCH) {
										// 点击按钮隐藏键盘
										inputManager.hideSoftInputFromWindow(
												v.getWindowToken(), 0);
										return true;
									}
									return false;
								}
							});
				}
			});
			atv_fishpit_search
					.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {

							HashMap<String, String> params = new HashMap<String, String>();
							params.put("fishpit", adapter.getItem(position));
							HttpUtil.getJSON(HttpAddress.ADDRESS
									+ HttpAddress.PROJECT
									+ HttpAddress.CLASS_MERCHANT
									+ HttpAddress.METHOD_FISHPITLIST, params,
									new HttpCallbackListener() {

										@Override
										public void onFinish(Object response) {
											Log.d("select_result",
													response.toString());
											JSONObject jsonObject = (JSONObject) response;
											ArrayList<FishpitSumaryEntity> arrayList = new ArrayList<FishpitSumaryEntity>();
											try {
												int total;
												String totalString = jsonObject
														.getString("total");
												total = Integer
														.parseInt(totalString);
												JSONArray jsonArray = jsonObject
														.getJSONArray("data");
												for (int i = 0; i < jsonArray
														.length(); i++) {
													JSONObject jsonObject2 = jsonArray
															.getJSONObject(i);
													Log.d("aaa", jsonObject2
															.toString());
													String name = (String) jsonObject2
															.get("name");
													String headImageURL = (String) jsonObject2
															.get("headImage");
													String detail = (String) jsonObject2
															.get("introduction");
													int id = jsonObject2
															.getInt("id");
													arrayList
															.add(new FishpitSumaryEntity(
																	name,
																	headImageURL,
																	detail,
																	id + ""));
												}
												Message message = new Message();
												message.what = 2;
												message.obj = arrayList;
												mHandler.sendMessage(message);
												fishpitAdapter.addToAdapter(
														arrayList, true);
												Log.d("total", total + " "
														+ jsonArray);

											} catch (JSONException e) {
												mHandler.sendEmptyMessage(5);
												e.printStackTrace();
											}

										}

										@Override
										public void onError(Exception e) {
											mHandler.sendEmptyMessage(5);
										}
									});
						}
					});
		} else if (from == 1) {
			// from = 1 来自渔具店
			lv_seach = (ListView) findViewById(R.id.lv_seach);
			fishshopEntities = new ArrayList<FishshopEntity>();
			fishShopAdapter = new FishshopAdapter(SeachActivity.this);
			atv_fishpit_search = (AutoCompleteTextView) findViewById(R.id.atv_fishshop_search);
			atv_fishpit_search.setThreshold(1);
			// atv_fishshop_search.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// liv_fishingshop.setBackgroundColor(Color.parseColor("#dddddd"));
			// }
			// });
			atv_fishpit_search.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							HashMap<String, String> params = new HashMap<String, String>();
							params.put("fishpit", adapter.getItem(position));
							HttpUtil.getJSON(HttpAddress.ADDRESS
									+ HttpAddress.PROJECT
									+ HttpAddress.CLASS_MERCHANT
									+ HttpAddress.METHOD_FISHSHOPLIST, params,
									new HttpCallbackListener() {

										@Override
										public void onFinish(Object response) {
											ArrayList<FishshopEntity> arrayList = new ArrayList<FishshopEntity>();
											Log.d("response",
													response.toString());
											JSONObject jsonObject = (JSONObject) response;
											JSONObject jsonObject2;
											String idString;
											String nameString;
											String imgUrl;
											String fishshopDetail;
											try {
												JSONArray jsonArray = jsonObject.getJSONArray("data");
												for (int i = 0; i < jsonArray
														.length(); i++) {
													jsonObject2 = (JSONObject) jsonArray.get(i);
													Log.d("jsonObject",
															jsonObject2.toString());
													idString = jsonObject2.getString("id");
													nameString = jsonObject2.getString("name");
													imgUrl = jsonObject2.getString("headImage");
													fishshopDetail = jsonObject2.getString("introduction");
													arrayList.add(new FishshopEntity(
																	nameString,
																	imgUrl,
																	fishshopDetail,
																	idString));
													// adapter.add2Adapter(fishshopEntities,false);
													// liv_fishingshop.setAdapter(adapter);
												}
												Message message = new Message();
												message.what = 22;
												message.obj = arrayList;
												mHandler.sendMessage(message);
											} catch (JSONException e) {
												mHandler.sendEmptyMessage(5);
												e.printStackTrace();
											}
										}

										@Override
										public void onError(Exception e) {
											mHandler.sendEmptyMessage(5);
										}
									});
						}
					});
		}
	}

	private void loadData() {
		if (from == 0) {
			HttpUtil.getJSON(HttpAddress.ADDRESS + HttpAddress.PROJECT
					+ HttpAddress.CLASS_MERCHANT
					+ HttpAddress.METHOD_FISHPITLIST, null,
					new HttpCallbackListener() {
						@Override
						public void onFinish(Object response) {
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
									fishpitSumaries.add(new FishpitSumaryEntity(
													jsonObject.get("name").toString(),
													jsonObject.get("headImage").toString(),
													jsonObject.get("introduction").toString(),
													jsonObject.get("id").toString()));
									// add(new FishpitSumary(jsonObject.get("name").toString(),
									// jsonObject.get("headImage").toString(),
									// jsonObject.get("introduction").toString(),),
									// jsonObject.get("id").toString());
								}

								Message message = new Message();
								message.what = 1;
								message.arg1 = fishpit_total;
								message.obj = fishpitSumaries;
								mHandler.sendMessage(message);

							} catch (JSONException e) {
								mHandler.sendEmptyMessage(5);
							}
						}

						@Override
						public void onError(Exception e) {
							mHandler.sendEmptyMessage(5);
						}
					});
		} else if (from == 1) {
			fishshopEntities = new ArrayList<FishshopEntity>();
			HttpUtil.getJSON(HttpAddress.ADDRESS+HttpAddress.PROJECT+
					HttpAddress.CLASS_MERCHANT+HttpAddress.METHOD_FISHSHOPLIST, 
					null, 
					new HttpCallbackListener() {
						
						@Override
						public void onFinish(Object response) {
							Log.d("response", response.toString());
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
//									adapter.add2Adapter(fishshopEntities,false);
//									liv_fishingshop.setAdapter(adapter);
								}
								Message message = new Message();
								message.what=11;
								message.obj = fishshopEntities;
								mHandler.sendMessage(message);
							} catch (JSONException e) {
								mHandler.sendEmptyMessage(5);
								e.printStackTrace();
							}
						}
						
						@Override
						public void onError(Exception e) {
							mHandler.sendEmptyMessage(5);
						}
					});
		}
	}

	@Override
	public void onClick(View v) {
		
	}


}
