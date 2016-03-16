package com.example.happyfishing.activity;

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
	private TextView tv_login_phone;
	private EditText edt_login_phone;
	private TextView tv_login_password;
	private EditText edt_login_password;
	private InputMethodManager inputManager;
	private CheckBox cb_rember;
	private Handler mHandler;
	private SharedPreferences sharedPreferences;

	//111
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		initHandler();

		initView();

		loadData();
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
					Toast.makeText(LoginActivity.this, "登陆失败,网络连接错误", Toast.LENGTH_SHORT).show();
					break;
				case 5:
					Toast.makeText(LoginActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
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
//		findViewById(R.id.btn_login_login).setBackgroundColor(getResources().getColor(R.color.button_background));
		findViewById(R.id.btn_login_login).setOnClickListener(this);
		findViewById(R.id.ll_loginparent).setOnTouchListener(this);
		actionBar_login = (ActionBarView) findViewById(R.id.actionBar_login);
//		actionBar_login.setActionBar(R.string.back, R.string.title_actionbar_register, R.string.title_actionbar_login, this);
		actionBar_login.setActionBar(R.string.back, R.string.title_actionbar_register, R.string.title_actionbar_login, 1, this);
		actionBar_login.setBackgroundColor(Color.parseColor("#ff8d07"));
		findViewById(R.id.tv_login_forget).setOnClickListener(this);

//		tv_login_phone = (TextView) findViewById(R.id.tv_login_phone);
//
//		// 对电话号码输入框进行监听
//		tv_login_phone.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				tv_login_phone.setVisibility(View.INVISIBLE);
//				edt_login_phone.setVisibility(View.VISIBLE);
//				edt_login_phone.requestFocus();
//				inputManager = (InputMethodManager) edt_login_phone.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//
//				// 点击号码框时，默认验证码输入操作已完成
//				edt_login_password.setVisibility(View.INVISIBLE);
//				tv_login_password.setVisibility(View.VISIBLE);
//
//				inputManager.showSoftInput(edt_login_phone, 0);
//				edt_login_phone.setOnEditorActionListener(new OnEditorActionListener() {
//
//					@Override
//					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//						if (actionId == EditorInfo.IME_ACTION_DONE) {
//							// 点击按钮隐藏键盘
//							inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
//							edt_login_phone.setVisibility(View.INVISIBLE);
//							tv_login_phone.setVisibility(View.VISIBLE);
//							return true;
//						}
//						return false;
//					}
//				});
//
//			}
//		});
		edt_login_phone = (EditText) findViewById(R.id.edt_login_phone);
		edt_login_phone.setInputType(InputType.TYPE_CLASS_PHONE);
		
		edt_login_phone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				inputManager = (InputMethodManager) edt_login_phone.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(edt_login_phone, 0);
				edt_login_phone.setOnEditorActionListener(new OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_DONE) {
							// 点击按钮隐藏键盘
							inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
							edt_login_phone.setVisibility(View.INVISIBLE);
							tv_login_phone.setVisibility(View.VISIBLE);
							return true;
						}
						return false;
					}
				});
			}
		});
//		
//		edt_login_phone.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before, int count) {
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//				tv_login_phone.setText(edt_login_phone.getText().toString());
//			}
//		});

		// 对验证码输入框进行监听
//		tv_login_password = (TextView) findViewById(R.id.tv_login_verification);
//		tv_login_password.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				tv_login_password.setVisibility(View.INVISIBLE);
//				edt_login_password.setVisibility(View.VISIBLE);
//				edt_login_password.requestFocus();
//				inputManager = (InputMethodManager) edt_login_password.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//
//				inputManager.showSoftInput(edt_login_password, 0);
//				// 点击验证码输入框时 认定好吗框输入操作已完成
//				edt_login_phone.setVisibility(View.INVISIBLE);
//				tv_login_phone.setVisibility(View.VISIBLE);
//
//				edt_login_password.setOnEditorActionListener(new OnEditorActionListener() {
//
//					@Override
//					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//						if (actionId == EditorInfo.IME_ACTION_DONE) {
//							// 点击按钮隐藏键盘
//							inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
//							edt_login_password.setVisibility(View.INVISIBLE);
//							tv_login_password.setVisibility(View.VISIBLE);
//							return true;
//						}
//						return false;
//					}
//				});
//			}
//		});
		edt_login_password = (EditText) findViewById(R.id.edt_login_verification);
		edt_login_password.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				inputManager = (InputMethodManager) edt_login_password.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(edt_login_password, 0);
				// 点击验证码输入框时 认定好吗框输入操作已完成
				edt_login_password.setOnEditorActionListener(new OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_DONE) {
							// 点击按钮隐藏键盘
							inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
							edt_login_password.setVisibility(View.INVISIBLE);
							tv_login_password.setVisibility(View.VISIBLE);
							return true;
						}
						return false;
					}
				});
			}
		});

		cb_rember = (CheckBox) findViewById(R.id.cb_login_rember);
		cb_rember.setChecked(true);
		//暂时默认记住密码登录
		cb_rember.setEnabled(false);
		cb_rember.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				edt_login_phone.setVisibility(View.INVISIBLE);
				tv_login_phone.setVisibility(View.VISIBLE);
				edt_login_password.setVisibility(View.INVISIBLE);
				tv_login_password.setVisibility(View.VISIBLE);
			}
		});
	}

	private void loadData() {

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
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
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("phoneNumber", edt_login_phone.getText().toString());
			params.put("password", edt_login_password.getText().toString());
			HttpUtil.getJSON(HttpAddress.ADDRESS + HttpAddress.PROJECT + HttpAddress.CLASS_APPUSER + HttpAddress.METHOD_LOGIN, params, new HttpCallbackListener() {

				@Override
				public void onFinish(Object response) {
					Log.d("response", response.toString());
					Message message = new Message();
					message.what = 1;
					JSONObject jsonObject1 = (JSONObject) response;
					int code = 0 ;
					String statisString = null; 
					try {
						code = jsonObject1.getInt("status");
					} catch (JSONException e1) {
						mHandler.sendEmptyMessage(5);
						e1.printStackTrace();
					}
					try {
						JSONObject jsonObject2 = jsonObject1.getJSONObject("appUser");
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
						Intent intent3 = new Intent(LoginActivity.this, HomeActivity.class);
						startActivity(intent3);
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
//		edt_login_phone.setVisibility(View.INVISIBLE);
//		tv_login_phone.setVisibility(View.VISIBLE);
//		edt_login_password.setVisibility(View.INVISIBLE);
//		tv_login_password.setVisibility(View.VISIBLE);
		return false;
	}

}
