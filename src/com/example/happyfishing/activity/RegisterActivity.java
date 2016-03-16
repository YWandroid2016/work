package com.example.happyfishing.activity;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.happyfishing.R;

import com.example.happyfishing.R.color;
import com.example.happyfishing.tool.HttpAddress;
import com.example.happyfishing.tool.HttpCallbackListener;
import com.example.happyfishing.tool.HttpUtil;
import com.example.happyfishing.tool.StringFilter;
import com.example.happyfishing.view.ActionBarView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class RegisterActivity extends Activity implements OnClickListener, OnTouchListener {

	private InputMethodManager inputManager;
	private TextView tv_register_phone;
	private EditText edt_register_phone;
	private ActionBarView actionBar_register;
	private TextView tv_register_verification;
	private EditText edt_register_verification;
	private TextView tv_register_password;
	private EditText edt_register_password;
	private TextView tv_register_confirm;
	private EditText edt_register_confirm;
	public static int TYPE_REGISTER = 1;
	private CheckBox cb_register;
	private Button btn_register_registe;
	private Handler mainHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		mainHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					String text = (String) msg.obj;
					Toast.makeText(RegisterActivity.this, text, Toast.LENGTH_SHORT).show();
					break;
				case 5:
					Toast.makeText(RegisterActivity.this, "发送验证码成功", Toast.LENGTH_SHORT).show();
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
		findViewById(R.id.ll_registerparent).setOnTouchListener(this);

		actionBar_register = (ActionBarView) findViewById(R.id.actionBar_register);
		actionBar_register.setActionBar(R.string.back, -1, R.string.title_actionbar_register, this);
//		findViewById(R.id.rl_actionbar).setBackgroundColor(getResources().getColor(R.color.actionbar_background));
		actionBar_register.setBackgroundColor(getResources().getColor(R.color.actionbar_background));
		btn_register_registe = (Button)findViewById(R.id.btn_register_regist);
		btn_register_registe.setOnClickListener(this);
//		btn_register_registe.setBackgroundColor(getResources().getColor(R.color.actionbar_background));
		findViewById(R.id.btn_register_verification).setOnClickListener(this);
		// 电话号码
//		tv_register_phone = (TextView) findViewById(R.id.tv_register_phone);
//		tv_register_phone.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				tv_register_phone.setVisibility(View.INVISIBLE);
//				edt_register_phone.setVisibility(View.VISIBLE);
//				edt_register_phone.requestFocus();
//				inputManager = (InputMethodManager) edt_register_phone.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//				inputManager.showSoftInput(edt_register_phone, 0);
//				edt_register_verification.setVisibility(View.INVISIBLE);
//				tv_register_verification.setVisibility(View.VISIBLE);
//				edt_register_password.setVisibility(View.INVISIBLE);
//				tv_register_password.setVisibility(View.VISIBLE);
//				edt_register_confirm.setVisibility(View.INVISIBLE);
//				tv_register_confirm.setVisibility(View.VISIBLE);
//				edt_register_phone.setOnEditorActionListener(new OnEditorActionListener() {
//
//					
//					@Override
//					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//						if (actionId == EditorInfo.IME_ACTION_NEXT) {
//							// 点击搜索按钮隐藏键盘
//							inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
//							// edt_register_phone.setVisibility(View.INVISIBLE);
//							// tv_register_phone.setVisibility(View.VISIBLE);
//							edt_register_phone.setVisibility(View.INVISIBLE);
//							tv_register_phone.setVisibility(View.VISIBLE);
//
//							return true;
//						}
//						return false;
//					}
//				});
//			}
//		});
		edt_register_phone = (EditText) findViewById(R.id.edt_register_phone);
		edt_register_phone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				inputManager = (InputMethodManager) edt_register_phone.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(edt_register_phone, 0);
				edt_register_phone.setOnEditorActionListener(new OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_NEXT) {
							// 点击搜索按钮隐藏键盘
							inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);

							return true;
						}
						return false;
					}
				});
			}
		});
//		// 验证码
//		tv_register_verification = (TextView) findViewById(R.id.tv_register_verification);
//		tv_register_verification.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				tv_register_verification.setVisibility(View.INVISIBLE);
//				edt_register_verification.setVisibility(View.VISIBLE);
//				edt_register_verification.requestFocus();
//				inputManager = (InputMethodManager) edt_register_verification.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//				inputManager.showSoftInput(edt_register_verification, 0);
//				edt_register_phone.setVisibility(View.INVISIBLE);
//				tv_register_phone.setVisibility(View.VISIBLE);
//				// edt_register_verification.setVisibility(View.INVISIBLE);
//				// tv_register_verification.setVisibility(View.VISIBLE);
//				edt_register_password.setVisibility(View.INVISIBLE);
//				tv_register_password.setVisibility(View.VISIBLE);
//				edt_register_confirm.setVisibility(View.INVISIBLE);
//				tv_register_confirm.setVisibility(View.VISIBLE);
//				edt_register_verification.setOnEditorActionListener(new OnEditorActionListener() {
//
//					@Override
//					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//						if (actionId == EditorInfo.IME_ACTION_NEXT) {
//							// 点击搜索按钮隐藏键盘
//							inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
//							edt_register_verification.setVisibility(View.INVISIBLE);
//							tv_register_verification.setVisibility(View.VISIBLE);
//
//							return true;
//						}
//						return false;
//					}
//				});
//			}
//		});
		edt_register_verification = (EditText) findViewById(R.id.edt_register_verification);
		edt_register_verification.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				inputManager = (InputMethodManager) edt_register_verification.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(edt_register_verification, 0);
				edt_register_verification.setOnEditorActionListener(new OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_NEXT) {
							// 点击搜索按钮隐藏键盘
							inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);

							return true;
						}
						return false;
					}
				});
			}
		});
//		// 第一次输入密码
//		tv_register_password = (TextView) findViewById(R.id.tv_register_password);
//		tv_register_password.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				tv_register_password.setVisibility(View.INVISIBLE);
//				edt_register_password.setVisibility(View.VISIBLE);
//				edt_register_password.requestFocus();
//				inputManager = (InputMethodManager) edt_register_password.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//				inputManager.showSoftInput(edt_register_password, 0);
//				edt_register_phone.setVisibility(View.INVISIBLE);
//				tv_register_phone.setVisibility(View.VISIBLE);
//				edt_register_verification.setVisibility(View.INVISIBLE);
//				tv_register_verification.setVisibility(View.VISIBLE);
//				// edt_register_password.setVisibility(View.INVISIBLE);
//				// tv_register_password.setVisibility(View.VISIBLE);
//				edt_register_confirm.setVisibility(View.INVISIBLE);
//				tv_register_confirm.setVisibility(View.VISIBLE);
//				edt_register_password.setOnEditorActionListener(new OnEditorActionListener() {
//
//					@Override
//					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//						if (actionId == EditorInfo.IME_ACTION_NEXT) {
//							// 点击搜索按钮隐藏键盘
//							inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
//							edt_register_password.setVisibility(View.INVISIBLE);
//							tv_register_password.setVisibility(View.VISIBLE);
//
//							return true;
//						}
//						return false;
//					}
//				});
//			}
//		});
//		
		
		
		edt_register_password = (EditText) findViewById(R.id.edt_register_password);
		edt_register_password.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				inputManager = (InputMethodManager) edt_register_password.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(edt_register_password, 0);
				edt_register_password.setOnEditorActionListener(new OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_NEXT) {
							// 点击搜索按钮隐藏键盘
							inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);

							return true;
						}
						return false;
					}
				});
			}
		});
		
		edt_register_password.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					
				}else {
					int len = edt_register_password.getText().length();
					if (len<6) {
						edt_register_password.setText("");
						Toast.makeText(RegisterActivity.this, "请输入6至16位密码(数字或字母)", Toast.LENGTH_SHORT).show();
					}else {
						tv_register_password.setText(edt_register_password.getText().toString());
					}
				}
			}
		});
//		// 再次输入验证密码
//		tv_register_confirm = (TextView) findViewById(R.id.tv_register_confirm);
//		tv_register_confirm.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				tv_register_confirm.setVisibility(View.INVISIBLE);
//				edt_register_confirm.setVisibility(View.VISIBLE);
//				edt_register_confirm.requestFocus();
//				inputManager = (InputMethodManager) edt_register_confirm.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//				inputManager.showSoftInput(edt_register_confirm, 0);
//				edt_register_phone.setVisibility(View.INVISIBLE);
//				tv_register_phone.setVisibility(View.VISIBLE);
//				edt_register_verification.setVisibility(View.INVISIBLE);
//				tv_register_verification.setVisibility(View.VISIBLE);
//				edt_register_password.setVisibility(View.INVISIBLE);
//				tv_register_password.setVisibility(View.VISIBLE);
//				// edt_register_confirm.setVisibility(View.INVISIBLE);
//				// tv_register_confirm.setVisibility(View.VISIBLE);
//
//				edt_register_confirm.setOnEditorActionListener(new OnEditorActionListener() {
//
//					@Override
//					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//						if (actionId == EditorInfo.IME_ACTION_DONE) {
//							// 点击搜索按钮隐藏键盘
//							inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
//							edt_register_confirm.setVisibility(View.INVISIBLE);
//							tv_register_confirm.setVisibility(View.VISIBLE);
//
//							return true;
//						}
//						return false;
//					}
//				});
//			}
//		});
//		edt_register_confirm = (EditText) findViewById(R.id.edt_register_confirm);
//		edt_register_confirm.addTextChangedListener(new TextWatcher() {
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
//				tv_register_confirm.setText(edt_register_confirm.getText().toString());
//			}
//		});
		
		cb_register = (CheckBox) findViewById(R.id.cb_register);
		cb_register.setChecked(true);
	}

	private void loadData() {
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == event.KEYCODE_BACK){
			if(popupWindow.isShowing()){
				popupWindow.dismiss();
			}
		}
//		if (edt_register_password.isFocusable()) {
//			edt_register_phone.setVisibility(View.INVISIBLE);
//			tv_register_phone.setVisibility(View.VISIBLE);
//			edt_register_verification.setVisibility(View.INVISIBLE);
//			tv_register_verification.setVisibility(View.VISIBLE);
//			edt_register_password.setVisibility(View.INVISIBLE);
//			tv_register_password.setVisibility(View.VISIBLE);
//			edt_register_confirm.setVisibility(View.INVISIBLE);
//			tv_register_confirm.setVisibility(View.VISIBLE);
//		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_register_regist:
			if (cb_register.isChecked()) {
				if (edt_register_password.getText().toString().equals("")) {
					Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
					break;
				}
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("phoneNumber", edt_register_phone.getText().toString());
				params.put("nickname", "user_"+edt_register_phone.getText().toString());
				params.put("validateCode", edt_register_verification.getText().toString());
				params.put("password", edt_register_password.getText().toString());
				HttpUtil.getJSON(HttpAddress.ADDRESS+HttpAddress.PROJECT+HttpAddress.CLASS_APPUSER+HttpAddress.METHOD_REGISTER, params, new HttpCallbackListener() {
					
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
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						if (code == 2000) {
							
							dialog();
							
						}else {
							try {
								statisString = jsonObject1.getString("text");
								message.obj = statisString;
								mainHandler.sendMessage(message);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					
					@Override
					public void onError(Exception e) {
						mainHandler.sendEmptyMessage(2);
					}
				});
				
			}else {
				Toast.makeText(RegisterActivity.this, "请先阅读并同意《自渔自乐用户协议》", Toast.LENGTH_SHORT).show();
			}
			
			break;
		case R.id.btn_register_verification:
			
			HashMap<String , String> params = new HashMap<String, String>();
			params.put("phoneNumber", edt_register_phone.getText().toString());
			HttpUtil.getJSON(HttpAddress.ADDRESS+HttpAddress.PROJECT+
					HttpAddress.CLASS_APPUSER+HttpAddress.METHOD_SENDVALIDATECONDE, 
					params, new HttpCallbackListener() {
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
						statisString = jsonObject1.getString("text");
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
//					try {
//						JSONObject jsonObject2 = jsonObject1.getJSONObject("appUser");
//						String token = jsonObject2.getString("token");
//						String phoneNumber = jsonObject2.getString("phoneNumber");
//						
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					if (code == 2000) {
						mainHandler.sendEmptyMessage(5);
					}else {
						try {
							statisString = jsonObject1.getString("text");
							message.obj = statisString;
							mainHandler.sendMessage(message);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

				@Override
				public void onError(Exception e) {
					mainHandler.sendEmptyMessage(2);
				}
			});
//				@Override
//				public void onFinish(Object response) {
//					JSONObject jsonObject = (JSONObject) response;
//					try {
//						final String textString = jsonObject.getString("text");
//						int code = jsonObject.getInt("status");
//						if (code!=2000) {
//							runOnUiThread(new Runnable() {
//								public void run() {
//									Toast.makeText(RegisterActivity.this, textString, Toast.LENGTH_SHORT).show();
//								}
//							});
//						}
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					
//				}
//				
//				@Override
//				public void onError(Exception e) {
//					Log.d("fail", e.toString());
//				}
//			});
			break;
		case R.id.tv_actionbar_left:
			RegisterActivity.this.finish();
		default:
			break;
		}
	}
	
	
	
	public void dialog(){

		//对话框
		Builder builder = new Builder(this);
        builder.setMessage("注册成功");
        builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				arg0.dismiss();
				Intent intent3 = new Intent(RegisterActivity.this, LoginActivity.class);
				startActivity(intent3);
			}
		});
        builder.show();
        
	}
	
	private TextView tv_pop;
	private TextView btn_pop;
	private PopupWindow popupWindow;
	
	public void popupwindow(){
		View v = LayoutInflater.from(this).inflate(R.layout.popup, null);
		popupWindow = new PopupWindow(v, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
		tv_pop = (TextView) v.findViewById(R.id.tv_pop);
		btn_pop = (TextView) v.findViewById(R.id.btn_pop);
		btn_pop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				popupWindow.dismiss();
				Intent intent3 = new Intent(RegisterActivity.this, LoginActivity.class);
				startActivity(intent3);
			}
		});
		
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

//		edt_register_phone.setVisibility(View.INVISIBLE);
//		tv_register_phone.setVisibility(View.VISIBLE);
//		edt_register_verification.setVisibility(View.INVISIBLE);
//		tv_register_verification.setVisibility(View.VISIBLE);
//		edt_register_password.setVisibility(View.INVISIBLE);
//		tv_register_password.setVisibility(View.VISIBLE);
//		edt_register_confirm.setVisibility(View.INVISIBLE);
//		tv_register_confirm.setVisibility(View.VISIBLE);
		return false;
	}
	

}
