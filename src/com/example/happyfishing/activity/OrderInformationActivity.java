package com.example.happyfishing.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


import org.json.JSONException;
import org.json.JSONObject;

import com.example.happyfishing.R;
import com.example.happyfishing.tool.HttpAddress;
import com.example.happyfishing.tool.HttpCallbackListener;
import com.example.happyfishing.tool.HttpUtil;
import com.example.happyfishing.tool.UiUtil;
import com.example.happyfishing.view.ActionBarView;
import com.example.happyfishing.view.RushBuyCountDownTimerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class OrderInformationActivity extends Activity implements OnClickListener{
	
	public static int TYPE_PAY_FISHPIT = 4;
	private ActionBarView actionBar_orderinformation;
	private RadioButton rdb_wechar;
//	private RadioButton rdb_daokeng;
	private RadioButton rdb_jifen;
	
	private String nameString;
	private String dateString;
	private String phoneNumber;
	private int location;
	private String merchantId;
	private String token;
	private String orderId;
	private long userPoint;
	private Handler mainHandler;   
	
	private TextView tv_orderdetail_content1;
	private TextView tv_orderdetail_content2;
	private TextView tv_orderdetail_phone;
	private RushBuyCountDownTimerView tv_timeUp;
	private String dateCreate;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_information);
		
		mainHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 5:
					Toast.makeText(OrderInformationActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
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
		tv_timeUp = (RushBuyCountDownTimerView) findViewById(R.id.timeup_ordershow);
		Intent intent = getIntent();
		boolean hide = intent.getBooleanExtra("hide", false);
		Bundle bundle = intent.getExtras();
		Log.d("bundle", bundle.getString("token")+bundle.getString("f")+bundle.getString("merchantId"));
		token = bundle.getString("token");
		orderId = bundle.getString("orderId");
		merchantId = bundle.getString("merchantId");
		userPoint = bundle.getLong("userPoint");
		Log.d("point", userPoint+" 积分");
		dateCreate = bundle.getString("dateCreate");
		nameString = bundle.getString("name");
		dateString = bundle.getString("date");
		Log.d("date", dateString);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			Log.d("dateCreate", dateCreate+"   ");
			Date daojishi = dateFormat.parse(dateCreate);
			String creatString = dateFormat.format(daojishi);
			String currentString = dateFormat.format(new Date(System.currentTimeMillis()));
			SimpleDateFormat dateFormat_minute= new SimpleDateFormat("mm");
			SimpleDateFormat dateFormat_second = new SimpleDateFormat("ss");
			Date daojishiCurrent = dateFormat.parse(currentString);
			long daojishiTime = 15*60*1000 - (daojishiCurrent.getTime() - daojishi.getTime());
			String minute = dateFormat_minute.format(new Date(daojishiTime));
			String second = dateFormat_second.format(new Date(daojishiTime));
			tv_timeUp.setTime(0, Integer.parseInt(minute), Integer.parseInt(second));
			tv_timeUp.start();
			Log.d("daojishi", minute+"  "+second);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		location = bundle.getInt("location");
		phoneNumber = bundle.getString("phone");
//		if (hide) {
//			LinearLayout ll_payment_daokeng = (LinearLayout) findViewById(R.id.ll_payment_daokeng);
//			ll_payment_daokeng.setVisibility(View.GONE);
//		}
//		rdb_daokeng = (RadioButton) findViewById(R.id.rb_paymentmethod_daokeng);
//		rdb_daokeng.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				if (isChecked) {
//					rdb_wechar.setChecked(false);
//					rdb_jifen.setChecked(false);
//				}
//			}
//		});
		rdb_wechar = (RadioButton) findViewById(R.id.rb_paymentmethod_wechart);
		rdb_wechar.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					
//					rdb_daokeng.setChecked(false);
					rdb_jifen.setChecked(false);
				}
			}
		});
		rdb_jifen = (RadioButton) findViewById(R.id.rb_paymentmethod_jifen);
		rdb_jifen.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
//					rdb_daokeng.setChecked(false);
					rdb_wechar.setChecked(false);
				}
			}
		});
		
		TextView textView = (TextView) findViewById(R.id.tv_orderinformation_jifenzhifu);
		if (userPoint < 30) {
			rdb_jifen.setEnabled(false);
			textView.setHint("积分余额："+userPoint);
			ImageView imageView = (ImageView) findViewById(R.id.img_orderinfomation_pay_jifen);
			imageView.setImageResource(R.drawable.ic_payment_jifen_unusable);
			
		}else {
			textView.setText("积分余额："+userPoint);
		}
		
		actionBar_orderinformation = (ActionBarView) findViewById(R.id.actionBar_orderinfomation);
		actionBar_orderinformation.setActionBar(R.string.cancl, -1, R.string.title_actionbar_order_detail, this);
		findViewById(R.id.btn_orderinformation_pay).setOnClickListener(this);
		
		tv_orderdetail_content1 = (TextView) findViewById(R.id.tv_orderdetail_content1);
		tv_orderdetail_content1.setText(dateString);
		tv_orderdetail_content2 = (TextView) findViewById(R.id.tv_orderdetail_content2);
		tv_orderdetail_content2.setText(nameString+"  "+location+" 号位");
		tv_orderdetail_phone = (TextView) findViewById(R.id.tv_orderinformation_phone);
		tv_orderdetail_phone.setHint(phoneNumber);
	}

	private void loadData() {
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.order_information, menu);
		return true;
	}
	
	public void payMethod() {
//		if (rdb_daokeng.isChecked()) {
//			HashMap<String, String> params = new HashMap<String, String>();
//			params.put("token", token);
//			params.put("merchantId", merchantId);
//			params.put("orderId", orderId);
//			HttpUtil.getJSON(HttpAddress.ADDRESS+HttpAddress.PROJECT+
//					HttpAddress.CLASS_ORDERPAY+HttpAddress.METHOD_CASHPAY, 
//					params, 
//					new HttpCallbackListener() {
//						@Override
//						public void onFinish(Object response) {
//							int code = 0 ;
//							String money = null;
//							JSONObject jsonObject = (JSONObject) response;
//							JSONObject jsonObject2;
//							try {
//								jsonObject2 = jsonObject.getJSONObject("order");
//								money = jsonObject2.getString("totalFee");
//							} catch (JSONException e1) {
//								// TODO Auto-generated catch block
//								e1.printStackTrace();
//							}
//							try {
//								code = jsonObject.getInt("status");
//							} catch (JSONException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//							if (code == 2000) {
//								Intent intent1 = new Intent(OrderInformationActivity.this, OrderResultActivity.class);
//								intent1.putExtra("type", TYPE_PAY_FISHPIT);
//								Bundle bundle = new Bundle();
//								bundle.putString("money", money);
//								bundle.putString("from", "order");
//								intent1.putExtras(bundle );
//								startActivity(intent1);
//							}
//							
//						}
//						
//						@Override
//						public void onError(Exception e) {
//							Log.d("error", e.toString());
//						}
//					});
//			
//		}else 
		if (rdb_wechar.isChecked()) {
			SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
			String token = sp.getString("token", "");
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", token);
			params.put("orderId", orderId);
			HttpUtil.getJSON(HttpAddress.ADDRESS+HttpAddress.PROJECT+
					HttpAddress.CLASS_ORDERPAY+HttpAddress.METHOD_WEIXINPAY, 
					params, 
					new HttpCallbackListener() {
						
						@Override
						public void onFinish(Object response) {
							int code = 0;
							String text = "";
							JSONObject jsonObject = (JSONObject) response;
							
						}
						
						@Override
						public void onError(Exception e) {
							mainHandler.sendEmptyMessage(5);
						}
					});
		}
		else if (rdb_jifen.isChecked()) {
			
			Log.d("jifenzhifu", "11111");
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("pointValue", "30");
			params.put("token", token);
			params.put("merchantId", merchantId);
			params.put("orderId", orderId);
			HttpUtil.getJSON(HttpAddress.ADDRESS+HttpAddress.PROJECT+
					HttpAddress.CLASS_ORDERPAY+HttpAddress.METHOD_POINTPAY, 
					params, 
					new HttpCallbackListener() {
						
						@Override
						public void onFinish(Object response) {
							Log.d("jifenzhifu", response.toString());
							int code = 0 ;
							String money = null;
							JSONObject jsonObject = (JSONObject) response;
							JSONObject jsonObject2;
							try {
								long userPoint = jsonObject.getLong("userPoint");
								SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
								Editor editor = sp.edit();
								editor.putLong("userPoint", userPoint);
								Log.d("jifenzhifu", userPoint+"");
								editor.commit();
								jsonObject2 = jsonObject.getJSONObject("order");
								money = jsonObject2.getString("totalFee");
							} catch (JSONException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							try {
								code = jsonObject.getInt("status");
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if (code == 2000) {
								Intent intent1 = new Intent(OrderInformationActivity.this, OrderResultActivity.class);
								intent1.putExtra("type", TYPE_PAY_FISHPIT);
								Bundle bundle = new Bundle();
								bundle.putString("money", money);
								bundle.putString("from", "order");
								intent1.putExtras(bundle );
								startActivity(intent1);
							}
							
						}
						
						@Override
						public void onError(Exception e) {
							Log.d("error", e.toString());
						}
					});
		}else {
			Toast.makeText(OrderInformationActivity.this, "请选择支付方式", Toast.LENGTH_SHORT).show();
		}
	}

	private TextView tv_pop_one;
	private TextView btn_pop_cancel;
	private TextView btn_pop_ok;
	private PopupWindow popupWindow;
	
	public void popupwindow(String str){
		View v = LayoutInflater.from(this).inflate(R.layout.popup_pay, null);
		popupWindow = new PopupWindow(v, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
		tv_pop_one = (TextView) v.findViewById(R.id.tv_pop_one);
		tv_pop_one.setText(str);
		btn_pop_cancel = (TextView) v.findViewById(R.id.tv_pop_cancel);
		btn_pop_ok = (TextView) v.findViewById(R.id.tv_pop_ok);
		btn_pop_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				popupWindow.dismiss();
				
				UiUtil.setNewMessage(true);
				payMethod();
			}
		});
		btn_pop_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				popupWindow.dismiss();
			}
		});
		
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_orderinformation_pay:	
			
			popupwindow("预约钓位\n30积分");
			
			break;
		case R.id.tv_actionbar_left:
			OrderInformationActivity.this.finish();
			break;
		default:
			break;
		}
	}

}
