package com.example.happyfishing.activity;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.happyfishing.R;
import com.example.happyfishing.R.id;
import com.example.happyfishing.R.layout;
import com.example.happyfishing.R.menu;
import com.example.happyfishing.R.string;
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
import android.text.Editable;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class AlterPhoneConfirmActivity extends Activity implements OnClickListener,OnTouchListener {

	private ActionBarView actionBarView;
	private TextView tv_alterphone_confirm;
	private EditText edt_alterphone_confirm;
	private TextView tv_alterphone_confirm_value_validateCode;
	private EditText edt_alterphone_confirm_value_validateCode;
	private InputMethodManager inputManager;
	private String oldPhone;
	private Handler mainHandler ; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alter_phone_confirm);
		findViewById(R.id.ll_alterphone_confirm).setOnTouchListener(this);
		
		Intent intent = getIntent();
		oldPhone = intent.getStringExtra("oldPhone");
		
		mainHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					Toast.makeText(AlterPhoneConfirmActivity.this, "网络连接异常", Toast.LENGTH_SHORT).show();
					break;
				case 2:
					Toast.makeText(AlterPhoneConfirmActivity.this, "发生未知错误", Toast.LENGTH_SHORT).show();
					break;
				case 4:
					String text = (String) msg.obj;
					Toast.makeText(AlterPhoneConfirmActivity.this, text, Toast.LENGTH_SHORT).show();
					break;
				case 11:
					Toast.makeText(AlterPhoneConfirmActivity.this, "更换手机号成功", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(AlterPhoneConfirmActivity.this, OrderResultActivity.class);
					intent.putExtra("type", 7);
					startActivity(intent);
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
		actionBarView = (ActionBarView) findViewById(R.id.actionbar_userinfo_alterphone_confirm);
		actionBarView.setActionBar(R.string.cancl, -1, R.string.title_actionbar_alterphone, this);
		findViewById(R.id.tbtn_alterphone_confirm).setOnClickListener(this);
		findViewById(R.id.btn_userinfoalter_phone).setOnClickListener(this);
		
		tv_alterphone_confirm = (TextView) findViewById(R.id.tv_alterphone_confirm);
		edt_alterphone_confirm = (EditText) findViewById(R.id.edt_alterphone_confirm);
		tv_alterphone_confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tv_alterphone_confirm.setVisibility(View.INVISIBLE);
				edt_alterphone_confirm.setVisibility(View.VISIBLE);
				
				tv_alterphone_confirm_value_validateCode.setVisibility(View.VISIBLE);
				edt_alterphone_confirm_value_validateCode.setVisibility(View.INVISIBLE);
				
				edt_alterphone_confirm.requestFocus();
				inputManager = (InputMethodManager) edt_alterphone_confirm.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

				inputManager.showSoftInput(edt_alterphone_confirm, 0);
				edt_alterphone_confirm.setOnEditorActionListener(new OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_DONE) {
							// 点击搜索按钮隐藏键盘
							inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
							edt_alterphone_confirm.setVisibility(View.INVISIBLE);
							tv_alterphone_confirm.setVisibility(View.VISIBLE);
							return true;
						}
						return false;
					}
				});
			}
		});
		edt_alterphone_confirm.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				tv_alterphone_confirm.setText(edt_alterphone_confirm.getText().toString());
			}
		});
		

		tv_alterphone_confirm_value_validateCode = (TextView) findViewById(R.id.tv_alterphone_confirm_value_validateCode);
		edt_alterphone_confirm_value_validateCode = (EditText) findViewById(R.id.edt_alterphone_confirm_value_validateCode);
		tv_alterphone_confirm_value_validateCode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tv_alterphone_confirm_value_validateCode.setVisibility(View.INVISIBLE);
				edt_alterphone_confirm_value_validateCode.setVisibility(View.VISIBLE);
				
				tv_alterphone_confirm.setVisibility(View.VISIBLE);
				edt_alterphone_confirm.setVisibility(View.INVISIBLE);
				
				edt_alterphone_confirm_value_validateCode.requestFocus();
				
				
				inputManager = (InputMethodManager) edt_alterphone_confirm_value_validateCode.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

				inputManager.showSoftInput(edt_alterphone_confirm_value_validateCode, 0);
				edt_alterphone_confirm_value_validateCode.setOnEditorActionListener(new OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_DONE) {
							// 点击搜索按钮隐藏键盘
							inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
							edt_alterphone_confirm_value_validateCode.setVisibility(View.INVISIBLE);
							tv_alterphone_confirm_value_validateCode.setVisibility(View.VISIBLE);
							return true;
						}
						return false;
					}
				});
			}
		});
		edt_alterphone_confirm_value_validateCode.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				tv_alterphone_confirm_value_validateCode.setText(edt_alterphone_confirm_value_validateCode.getText().toString());
			}
		});
		
		
	}

	private void loadData() {

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.alter_phone_confirm, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_actionbar_left:
			AlterPhoneConfirmActivity.this.finish();
			break;
		case R.id.tbtn_alterphone_confirm:
			if (tv_alterphone_confirm.getText().toString() == null) {
				Toast.makeText(AlterPhoneConfirmActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
			}else {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("phoneNumber", tv_alterphone_confirm.getText().toString());
				HttpUtil.getJSON(HttpAddress.ADDRESS+HttpAddress.PROJECT+
						HttpAddress.CLASS_APPUSER+HttpAddress.METHOD_SENDVALIDATECONDE, 
						params, 
						new HttpCallbackListener() {
					
					@Override
					public void onFinish(Object response) {
						JSONObject jsonObject = (JSONObject) response;
						int code;
						String text;
						try {
							code = jsonObject.getInt("status");
							text = jsonObject.getString("text");
							Message message = new Message();
							message.what = 4;
							message.obj = text;
							mainHandler.sendMessage(message);
						} catch (JSONException e) {
							mainHandler.sendEmptyMessage(2);
						}
					}
					
					@Override
					public void onError(Exception e) {
						mainHandler.sendEmptyMessage(1);
					}
				});
			}
			break;
		case R.id.btn_userinfoalter_phone:
			if (tv_alterphone_confirm_value_validateCode.getText().toString() ==null) {
				Toast.makeText(AlterPhoneConfirmActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
			}else {
				
				SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
				String token = sp.getString("token", "");
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("oldNumber", oldPhone);
				params.put("newNumber", tv_alterphone_confirm.getText().toString());
				params.put("validateCode", tv_alterphone_confirm_value_validateCode.getText().toString());
				params.put("token", token);
				Log.d("aaa", oldPhone+"  "+tv_alterphone_confirm.getText().toString()+"  "+tv_alterphone_confirm_value_validateCode.getText().toString()+"  "+token);
				
				
				HttpUtil.getJSON(HttpAddress.ADDRESS+HttpAddress.PROJECT+
						HttpAddress.CLASS_USERINFO+HttpAddress.METHOD_CHANGEPHONENUMBER, 
						params, 
						new HttpCallbackListener() {
							
							@Override
							public void onFinish(Object response) {
								JSONObject jsonObject = (JSONObject) response;
								try {
									JSONObject jsonObject1 = jsonObject.getJSONObject("appUser");
									Log.d("success", "会员到期时间" +jsonObject1.getString("outOfDate"));
									Log.d("success", "会员开通时间"+jsonObject1.getString("startOfDate"));
									Log.d("success", "昵称"+jsonObject1.getString("nickname"));
									Log.d("success", "用户头像地址"+jsonObject1.getString("headImageUrl"));
									Log.d("success", "用户的唯一标识"+jsonObject1.getString("token"));
									Log.d("success", "注册用的电话"+jsonObject1.getString("phoneNumber"));
									Log.d("success", "经验值"+jsonObject1.getString("userExp"));
									Log.d("success", "用户积分"+jsonObject1.getString("userPoint"));
									Log.d("success", "是否为会员"+jsonObject1.getString("isMember"));
									Log.d("success", "会员类别"+jsonObject1.getString("category"));
									Log.d("success", "用户等级"+jsonObject1.getString("userRank"));
									SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
									Editor editor = sp.edit();
									editor.putString("outOfDate", jsonObject1.getString("outOfDate"));
									editor.putString("startOfDate", jsonObject1.getString("startOfDate"));
									editor.putString("nickname", jsonObject1.getString("nickname"));
									editor.putString("headImageUrl", jsonObject1.getString("headImageUrl"));
									editor.putString("token", jsonObject1.getString("token"));
									editor.putString("phoneNumber", jsonObject1.getString("phoneNumber"));
									editor.putString("userExp", jsonObject1.getString("userExp"));
									editor.putString("userPoint", jsonObject1.getString("userPoint"));
									editor.putString("isMember", jsonObject1.getString("isMember"));
									editor.putString("category", jsonObject1.getString("category"));
									editor.putString("userRank", jsonObject1.getString("userRank"));
									editor.commit();
									mainHandler.sendEmptyMessage(11);
								
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								
							}
							
							@Override
							public void onError(Exception e) {
								// TODO Auto-generated method stub
								
							}
						});
			}
			break;
		default:
			break;
		}
	}
	
	

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		tv_alterphone_confirm.setVisibility(View.VISIBLE);
		tv_alterphone_confirm_value_validateCode.setVisibility(View.INVISIBLE);
		edt_alterphone_confirm.setVisibility(View.VISIBLE);
		edt_alterphone_confirm_value_validateCode.setVisibility(View.INVISIBLE);
		
		return false;
	}
	

}
