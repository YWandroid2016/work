package com.example.happyfishing.activity;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.happyfishing.R;
import com.example.happyfishing.R.id;
import com.example.happyfishing.R.layout;
import com.example.happyfishing.R.menu;
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

public class UserInfoAlterActivity extends Activity implements OnClickListener,OnTouchListener{

	private ActionBarView actionbar_userinfo_alter;
	private TextView tv_userinfo_alter_item;
	private TextView tv_userinfoalter_value;
	private EditText edt_userinfoalter_value;
	private TextView tv_userinfoalter_value_phone;
	private EditText edt_userinfoalter_value_phone;
	private InputMethodManager inputManager;
	private Handler mainHandler;
	private TextView tv_userinfoalter_value_validateCode;
	private EditText edt_userinfoalter_value_validateCode;
	private boolean isFirst;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info_alter);
		
		
		
		mainHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					String statusString = (String) msg.obj;
					Toast.makeText(UserInfoAlterActivity.this, statusString, Toast.LENGTH_SHORT).show();
					break;
				case 2:
					Toast.makeText(UserInfoAlterActivity.this, "网络出现未知错误", Toast.LENGTH_SHORT).show();
					break;
				case 5:
					Toast.makeText(UserInfoAlterActivity.this, "发送验证码成功", Toast.LENGTH_SHORT).show();
					break;
				case 12:
					Intent intent1 = new Intent(UserInfoAlterActivity.this, AlterPhoneConfirmActivity.class);
					intent1.putExtra("oldPhone", tv_userinfoalter_value.getText().toString());
					startActivity(intent1);
					break;
				default:
					break;
				}
				super.handleMessage(msg);
			}
		};
		
		findViewById(R.id.ll_userinfo_alter).setOnTouchListener(this);
		findViewById(R.id.tbtn_userinfoalter_alterphone).setOnClickListener(this);
		Intent intent = getIntent();
		boolean phone = intent.getBooleanExtra("phone", true);
		
		
		//对上边的textview和edittext进行点击效果的设置
		tv_userinfoalter_value = (TextView) findViewById(R.id.tv_userinfoalter_value);
		edt_userinfoalter_value =(EditText) findViewById(R.id.edt_userinfoalter_value);
		tv_userinfoalter_value.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tv_userinfoalter_value.setVisibility(View.INVISIBLE);
				edt_userinfoalter_value.setVisibility(View.VISIBLE);
				edt_userinfoalter_value.requestFocus();
				inputManager = (InputMethodManager) edt_userinfoalter_value.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

				inputManager.showSoftInput(edt_userinfoalter_value, 0);
				edt_userinfoalter_value.setOnEditorActionListener(new OnEditorActionListener() {
					
					@Override
					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_DONE) {
							// 点击搜索按钮隐藏键盘
							inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
							edt_userinfoalter_value.setVisibility(View.INVISIBLE);
							tv_userinfoalter_value.setVisibility(View.VISIBLE);
							return true;
						}
						return false;
					}
				});
			}
		});
		edt_userinfoalter_value.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				tv_userinfoalter_value.setText(edt_userinfoalter_value.getText().toString());
			}
		});
		//对下边的textview和edittext进行点击效果的设置
		
		//如果进入修改用户nickname界面
		actionbar_userinfo_alter = (ActionBarView) findViewById(R.id.actionbar_userinfo_alter);
		if (phone == false) {
			findViewById(R.id.ll_userinfoalter_phone).setVisibility(View.INVISIBLE);
			findViewById(R.id.rl_userinfoalter_phone).setVisibility(View.INVISIBLE);
			findViewById(R.id.btn_userinfoalter_nickname).setVisibility(View.VISIBLE);
			findViewById(R.id.tbtn_userinfoalter_alterphone).setVisibility(View.INVISIBLE);
			findViewById(R.id.btn_userinfoalter_nickname).setOnClickListener(this);
			actionbar_userinfo_alter.setActionBar(R.string.cancl, -1, R.string.title_actionbar_alternick, this);
			tv_userinfo_alter_item=(TextView) findViewById(R.id.tv_passwordalter_phone);
			tv_userinfo_alter_item.setHint("昵称");
			SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
			String nickname = sp.getString("nickname", "获取昵称失败");
			tv_userinfoalter_value.setHint(nickname);
			
		}else {
			//如果进入修改用户手机界面
			findViewById(R.id.btn_userinfoalter_phone).setOnClickListener(this);
			findViewById(R.id.btn_userinfoalter_nickname).setVisibility(View.INVISIBLE);
			findViewById(R.id.tbtn_userinfoalter_alterphone).setVisibility(View.VISIBLE);
			actionbar_userinfo_alter.setActionBar(R.string.cancl, -1, R.string.title_actionbar_alterphone, this);
			tv_userinfo_alter_item=(TextView) findViewById(R.id.tv_passwordalter_phone);
			tv_userinfo_alter_item.setHint("手机号");
			tv_userinfoalter_value_phone=(TextView) findViewById(R.id.tv_userinfoalter_value_validateCode);
			edt_userinfoalter_value_phone = (EditText) findViewById(R.id.edt_userinfoalter_value_validateCode);
			tv_userinfoalter_value_phone.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					tv_userinfoalter_value_phone.setVisibility(View.INVISIBLE);
					edt_userinfoalter_value_phone.setVisibility(View.VISIBLE);
					tv_userinfoalter_value.setVisibility(View.VISIBLE);
					edt_userinfoalter_value.setVisibility(View.INVISIBLE);
					
					edt_userinfoalter_value_phone.requestFocus();
					inputManager = (InputMethodManager) edt_userinfoalter_value_phone.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

					inputManager.showSoftInput(edt_userinfoalter_value_phone, 0);
				}
			});
			edt_userinfoalter_value_phone.setOnEditorActionListener(new OnEditorActionListener() {
				
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					if (actionId == EditorInfo.IME_ACTION_DONE) {
						// 点击搜索按钮隐藏键盘
						inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
						edt_userinfoalter_value_phone.setVisibility(View.INVISIBLE);
						tv_userinfoalter_value_phone.setVisibility(View.VISIBLE);
						return true;
					}
					return false;
				}
			});
			
			edt_userinfoalter_value_phone.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				}

				@Override
				public void afterTextChanged(Editable s) {
					tv_userinfoalter_value_phone.setText(edt_userinfoalter_value_phone.getText().toString());
				}
			});
			SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
			String phoneNumber = sp.getString("phoneNumber", "获取手机号失败");
			tv_userinfoalter_value.setText(phoneNumber);
			
			
		}
		
		
		
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_info_alter, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tbtn_userinfoalter_alterphone:
				HashMap<String , String> params3 = new HashMap<String, String>();
				Log.d("phoneNumber", tv_userinfoalter_value.getText().toString()+"   ");
				params3.put("phoneNumber", tv_userinfoalter_value.getText().toString());
				HttpUtil.getJSON(HttpAddress.ADDRESS+HttpAddress.PROJECT+
						HttpAddress.CLASS_USERINFO+HttpAddress.METHOD_SENDVALIDATECONDE, 
						params3, new HttpCallbackListener() {
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
						}if (code == 2000) {
							mainHandler.sendEmptyMessage(5);
						}else {
							try {
								statisString = jsonObject1.getString("text");
								message.obj = statisString;
								message.what = 1;
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
			break;
		case R.id.btn_userinfoalter_phone:
			SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
			HashMap<String, String> params1 = new HashMap<String, String>();
			params1.put("oldNumber", tv_userinfoalter_value.getText().toString());
			params1.put("validateCode", edt_userinfoalter_value_phone.getText().toString());
			params1.put("token", sp.getString("token", ""));
			HttpUtil.getJSON(HttpAddress.ADDRESS+HttpAddress.PROJECT+
					HttpAddress.CLASS_USERINFO+HttpAddress.METHOD_CHANGEPHONENUMBER, 
					params1, 
					new HttpCallbackListener() {
						
						@Override
						public void onFinish(Object response) {
							JSONObject jsonObject = (JSONObject) response;
							int code ;
							String text;
							try {
								code = jsonObject.getInt("status");
								text = jsonObject.getString("text");
								if (code == 2000) {
									mainHandler.sendEmptyMessage(12);
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
						
						@Override
						public void onError(Exception e) {
							// TODO Auto-generated method stub
							
						}
					});
			
			
			
		
			break;
		case R.id.btn_userinfoalter_nickname:
			HashMap<String, String> params = new HashMap<String, String>();
			SharedPreferences sp1 = getSharedPreferences("user", Context.MODE_PRIVATE);
			String token = sp1.getString("token", "");
			String nickName = tv_userinfoalter_value.getText().toString();
			params.put("token", token);
			params.put("nickname", nickName);
			HttpUtil.getJSON(HttpAddress.ADDRESS+HttpAddress.PROJECT+
					HttpAddress.CLASS_USERINFO+HttpAddress.METHOD_EDITNICKNAME,
					params,
					new HttpCallbackListener() {
						
						@Override
						public void onFinish(Object response) {
							JSONObject jsonObject = (JSONObject) response;
							int code = 0;
							String statusString = null;
							try {
								code =jsonObject.getInt("status");
								statusString = jsonObject.getString("text");
							} catch (JSONException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							switch (code) {
							case 2000:
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
									Intent intent1 = new Intent(UserInfoAlterActivity.this, OrderResultActivity.class);
									intent1.putExtra("type", 5);
									startActivity(intent1);
									
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								break;
							case 2001:
								Message message1 = new Message();
								message1.what = 1;
								message1.obj = statusString;
								mainHandler.sendMessage(message1);
								break;
							case 2008:
								Message message2 = new Message();
								message2.what = 1;
								message2.obj = statusString;
								mainHandler.sendMessage(message2);
								break;
							default:
								break;
							}
							
						}
						
						@Override
						public void onError(Exception e) {
							Log.e("error", e.toString());
						}
					});
			break;
		case R.id.tv_actionbar_left:
			UserInfoAlterActivity.this.finish();
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		Log.d("aaaaaaaaaa", "点击了别的地方");
		tv_userinfoalter_value.setVisibility(View.VISIBLE);
		edt_userinfoalter_value.setVisibility(View.INVISIBLE);
		tv_userinfoalter_value_phone.setVisibility(View.VISIBLE);
		edt_userinfoalter_value_phone.setVisibility(View.INVISIBLE);
		return false;
	}

}
