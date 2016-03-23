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
