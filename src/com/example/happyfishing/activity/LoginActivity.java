package com.example.happyfishing.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.happyfishing.R;
import com.example.happyfishing.R.color;
import com.example.happyfishing.R.layout;
import com.example.happyfishing.R.menu;
import com.example.happyfishing.manager.SharedPreferencesManager;
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
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class LoginActivity extends Activity implements OnClickListener, OnTouchListener {

	private ActionBarView actionBar_login;
	public static int LOGIN_FORGET = 2;
	private EditText edt_login_phone;
	private EditText edt_login_password;
	private InputMethodManager inputManager;
	private CheckBox cb_rember;
	private Handler mHandler;
	private JSONObject jsonObject;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		initHandler();

		initView();
	}

	private void initHandler() {
		mHandler = new Handler(LoginActivity.this.getMainLooper()) {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					String text = (String) msg.obj;
					Toast.makeText(LoginActivity.this, text, Toast.LENGTH_SHORT).show();
					break;
				case 2:
					Toast.makeText(LoginActivity.this, "手机号或密码错误，请重新输入", Toast.LENGTH_SHORT).show();
					break;
				case 5:
					Log.d("result", "数据解析或网络连接错误");
//					Toast.makeText(LoginActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
				super.handleMessage(msg);
			}
		};

	}

	private void initSharePreference(String token , String phoneNumber) {
		SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString("token", token);
		editor.putString("phone", phoneNumber);

		editor.commit();
	}

	private void initView() {
		findViewById(R.id.btn_login_login).setOnClickListener(this);
		findViewById(R.id.ll_loginparent).setOnTouchListener(this);
		actionBar_login = (ActionBarView) findViewById(R.id.actionBar_login);
		actionBar_login.setActionBar(R.string.back, R.string.title_actionbar_register, R.string.title_actionbar_login, 1, this);
		findViewById(R.id.tv_login_forget).setOnClickListener(this);

		edt_login_phone = (EditText) findViewById(R.id.edt_login_phone);
		edt_login_phone.setInputType(InputType.TYPE_CLASS_PHONE);
		

		edt_login_password = (EditText) findViewById(R.id.edt_login_verification);

		cb_rember = (CheckBox) findViewById(R.id.cb_login_rember);
		cb_rember.setChecked(true);
		//暂时默认记住密码登录
		cb_rember.setEnabled(false);
		
		edt_login_password.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus){
					int length = edt_login_password.getText().toString().length();
					if(length < 6){
						Toast.makeText(LoginActivity.this, "密码的长度为6~18位", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_actionbar_left:
			LoginActivity.this.finish();
			break;
		case R.id.tv_actionbar_right:
			Intent intent1 = new Intent(LoginActivity.this, RegisterActivity.class);
			startActivity(intent1);
			break;
		case R.id.tv_login_forget:
			Intent intent2 = new Intent(LoginActivity.this, PasswordCreatActivity.class);
			intent2.putExtra("type", LOGIN_FORGET);
			startActivity(intent2);
			break;
		case R.id.btn_login_login:
			
			String phone = edt_login_phone.getText().toString();
			String pass = edt_login_password.getText().toString();
			
			if(null == phone || "".equals(phone)){
				Toast.makeText(getApplicationContext(), "请输入手机号", Toast.LENGTH_SHORT).show();
				return;
			}
			if(null == pass || "".equals(pass)){
				Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT).show();
				return;
			}
			
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("phoneNumber", phone);
			params.put("password", pass);
			String str = HttpAddress.ADDRESS + HttpAddress.PROJECT + HttpAddress.CLASS_APPUSER + HttpAddress.METHOD_LOGIN;
			Log.d("log", str);
			HttpUtil.getJSON(str, params, new HttpCallbackListener() {

				@Override
				public void onFinish(Object response) {
					Log.d("response", response.toString());
					
					jsonObject = (JSONObject) response;
					JSONObject jsonObject1 = null;
					try {
						jsonObject1 = jsonObject.getJSONObject("appUser");
						Log.d("successs", response.toString());
					} catch (JSONException e) {
						mHandler.sendEmptyMessage(5);
						e.printStackTrace();
					}
					try {
						Log.d("success", "会员到期时间" + jsonObject1.getString("outOfDate"));
						Log.d("success", "会员开通时间" + jsonObject1.getString("startOfDate"));
						Log.d("success", "昵称" + jsonObject1.getString("nickname"));
						Log.d("success", "用户头像地址" + jsonObject1.getString("headImageUrl"));
						Log.d("success", "用户的唯一标识" + jsonObject1.getString("token"));
						Log.d("success", "注册用的电话" + jsonObject1.getString("phoneNumber"));
						Log.d("success", "经验值" + jsonObject1.getString("userExp"));
						Log.d("success", "用户积分" + jsonObject1.getString("userPoint"));
						Log.d("success", "是否为会员" + jsonObject1.getString("isMember"));
						Log.d("success", "会员类别" + jsonObject1.getString("category"));
						Log.d("success", "用户等级" + jsonObject1.getString("userRank"));
						SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
						Editor editor = sp.edit();
						// 比较两次userpoint值 如果变化则修改message图标为带红点
//						String userPoint_previous = sp.getString("userPoint", "");
//						String userPoint_now = jsonObject1.getString("userPoint");
//						long current = System.currentTimeMillis();
//						String outOfDate = jsonObject1.getString("outOfDate");
//						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-hh");
//						try {
//							Date date = dateFormat.parse(outOfDate);
//							long outOfDatejiange = current - date.getTime();
//							Log.d("outOfDatejiange", outOfDatejiange + "");
//							if (outOfDatejiange < 259200000) {
//								Log.d("outOfDatejiange", outOfDatejiange + "");
//								UiUtil.setNewMessage(true);
//							}
//						} catch (ParseException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
						
//						if (userPoint_now.equals(userPoint_previous)) {
//						} else {
//							UiUtil.setNewMessage(true);
//						}
						
						editor.putString("outOfDate", jsonObject1.getString("outOfDate"));
						editor.putString("startOfDate", jsonObject1.getString("startOfDate"));
						editor.putString("nickname", jsonObject1.getString("nickname"));
						editor.putString("headImageUrl", jsonObject1.getString("headImageUrl"));
						editor.putString("token", jsonObject1.getString("token"));
						editor.putString("phoneNumber", jsonObject1.getString("phoneNumber"));
						editor.putString("userExp", jsonObject1.getString("userExp"));
						//获取的积分需要缩小100倍
						Long up = Long.parseLong(jsonObject1.getString("userPoint"));
						up = up/100;
						editor.putString("userPoint", new java.text.DecimalFormat("#0.00").format(up));
						editor.putString("isMember", jsonObject1.getString("isMember"));
						editor.putString("category", jsonObject1.getString("category"));
						editor.putString("userRank", jsonObject1.getString("userRank"));
						editor.putString("hasPayPass", jsonObject1.getString("hasPayPass"));
						editor.commit();
					
					Message message = new Message();
					message.what = 1;
					int code = 0 ;
					String statisString = null; 
					try {
						code = jsonObject.getInt("status");
					} catch (JSONException e1) {
						mHandler.sendEmptyMessage(5);
						e1.printStackTrace();
					}
					try {
						JSONObject jsonObject2 = jsonObject.getJSONObject("appUser");
						String token = jsonObject2.getString("token");
						String phoneNumber = jsonObject2.getString("phoneNumber");
						
						if (cb_rember.isChecked()) {
							initSharePreference(token,phoneNumber);
						}
					} catch (JSONException e) {
						mHandler.sendEmptyMessage(5);
						e.printStackTrace();
					}
					if (code == 2000) {
						LoginActivity.this.finish();
					}else {
						try {
							statisString = jsonObject1.getString("text");
							message.obj = statisString;
							mHandler.sendMessage(message);
						} catch (JSONException e) {
							mHandler.sendEmptyMessage(5);
							e.printStackTrace();
						}
					}
				} catch (JSONException e) {
					mHandler.sendEmptyMessage(5);
				}
				}
				@Override
				public void onError(Exception e) {
					mHandler.sendEmptyMessage(2);
				}
			});
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		v.setFocusable(true);
		v.setFocusableInTouchMode(true);
		v.requestFocus();
		return false;
	}

}
