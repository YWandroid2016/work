package com.example.happyfishing.activity;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.happyfishing.PayForgetActivity;
import com.example.happyfishing.R;
import com.example.happyfishing.tool.HttpAddress;
import com.example.happyfishing.tool.HttpCallbackListener;
import com.example.happyfishing.tool.HttpUtil;
import com.example.happyfishing.view.ActionBarView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UserInfoAlterActivity extends Activity implements OnClickListener {

	private EditText ed_userinfo_yan;
	private EditText ed_userinfo_yan_new;
	private EditText ed_userinfo_newphone;
	private Button btn_userinfo_next;
	private Button btn_userinfo_over;
	private TextView tv_phone;
	private SharedPreferences sp;
	private String phone;
	private JSONObject jsonObject;
	private Handler handler;
	
	private ActionBarView actionBar_userinfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("user", Context.MODE_PRIVATE);
		initView();
		handler = new Handler(UserInfoAlterActivity.this.getMainLooper()){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:
					switch (msg.arg1) {
					case 2000:
						Toast.makeText(getApplicationContext(), "验证码发送成功", Toast.LENGTH_SHORT).show();
						break;
					case 2007:
						Toast.makeText(getApplicationContext(), "验证码发送失败", Toast.LENGTH_SHORT).show();
						break;
					default:
						break;
					}
					
					break;
				case 2:
					switch (msg.arg1) {
					case 2000:
						Toast.makeText(getApplicationContext(), "验证成功", Toast.LENGTH_SHORT).show();
						initView2();
						break;
					case 2001:
						Toast.makeText(getApplicationContext(), "未找到该用户", Toast.LENGTH_SHORT).show();
						break;
					case 2006:
						Toast.makeText(getApplicationContext(), "验证码不正确或已过期", Toast.LENGTH_SHORT).show();
						break;
					default:
						break;
					}
					break;
					
				case 3:
					switch (msg.arg1) {
					case 2000:
						Toast.makeText(getApplicationContext(), "发送新手机验证码成功", Toast.LENGTH_SHORT).show();
						break;
					case 2007:
						Toast.makeText(getApplicationContext(), "发送新手机验证码失败", Toast.LENGTH_SHORT).show();
						break;
					case 8001:
						Toast.makeText(getApplicationContext(), "手机号已存在请直接登录", Toast.LENGTH_SHORT).show();
						break;
					default:
						break;
					}
					break;
					
				case 4:
					switch (msg.arg1) {
					case 2000:
						Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
						sp.edit().putString("phoneNumber", ed_userinfo_newphone.getText().toString()).commit();
						UserInfoAlterActivity.this.finish();
						break;
					case 2001:
						Toast.makeText(getApplicationContext(), "未找到该用户", Toast.LENGTH_SHORT).show();
						break;
					case 2006:
						Toast.makeText(getApplicationContext(), "验证码不正确或已过期", Toast.LENGTH_SHORT).show();
						break;
					case 2011:
						Toast.makeText(getApplicationContext(), "修改手机号失败", Toast.LENGTH_SHORT).show();
						break;
					default:
						break;
					}
					break;
				
				default:
					Toast.makeText(getApplicationContext(), "未知错误", Toast.LENGTH_SHORT).show();
					break;
				}
			}
		};
	}

	private void initView() {

		setContentView(R.layout.activity_user_info_alter_phone);
		
		actionBar_userinfo = (ActionBarView) findViewById(R.id.actionbar_userinfo_alter);
		actionBar_userinfo.setActionBar(R.string.back, -1, R.string.title_actionbar_alterphone, this);

		ed_userinfo_yan = (EditText) findViewById(R.id.ed_userinfo_yan);
		btn_userinfo_next = (Button) findViewById(R.id.btn_userinfo_next);
		tv_phone = (TextView) findViewById(R.id.tv_userinfo_current_phone);

		// TODO 获取用户手机号并加载到tv_phone
		phone = sp.getString("phoneNumber", "");
		tv_phone.setHint(phone);

		btn_userinfo_next.setOnClickListener(this);

		findViewById(R.id.btn_userinfo_verification).setOnClickListener(this);

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_userinfo_next:
			// TODO 下一步
			if (null == phone || "".equals(phone)) {
				Toast.makeText(getApplicationContext(), "手机号不能为空",
						Toast.LENGTH_SHORT).show();
				return;
			}
			String yan = ed_userinfo_yan.getText().toString();
			if (null == yan || "".equals(yan)) {
				Toast.makeText(getApplicationContext(), "请输入验证码",
						Toast.LENGTH_SHORT).show();
				return;
			}
			
			String token = sp.getString("token", "");
			if (token.equals("")) {
				Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
				return;
			}
			
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("oldNumber", phone);
			params.put("validateCode", yan);
			params.put("token", token);
			String str = HttpAddress.ADDRESS + HttpAddress.PROJECT + HttpAddress.CLASS_USERINFO + HttpAddress.METHOD_CHANGEPHONENUMBER;
			HttpUtil.getJSON(str, params, new HttpCallbackListener() {
				
				
				@Override
				public void onFinish(Object response) {
					jsonObject = (JSONObject) response;
					try {
						int status = jsonObject.getInt("status");
						Log.d("my", "pass:"+jsonObject.toString());
						Message msg = handler.obtainMessage();
						msg.what = 2;
						msg.arg1 = status;
						handler.sendMessage(msg);
					} catch (JSONException e) {
						Toast.makeText(getApplicationContext(), "发送验证码返回数据结构出错", Toast.LENGTH_SHORT).show();
						e.printStackTrace();
					}
				}
				
				@Override
				public void onError(Exception e) {
					
				}
			});
			break;
		case R.id.btn_userinfo_verification:
			// TODO 验证倒计时，获取验证码
			if (null == phone || "".equals(phone)) {
				Toast.makeText(getApplicationContext(), "手机号不能为空",
						Toast.LENGTH_SHORT).show();
				return;
			}
			HashMap<String, String> params2 = new HashMap<String, String>();
			params2.put("phoneNumber", phone);
			HttpUtil.getJSON(HttpAddress.ADDRESS + HttpAddress.PROJECT + HttpAddress.CLASS_USERINFO + HttpAddress.METHOD_SENDVALIDATECONDE, params2, new HttpCallbackListener() {
				
				@Override
				public void onFinish(Object response) {
					jsonObject = (JSONObject) response;
					try {
						int status = jsonObject.getInt("status");
						Log.d("my", "pass:"+jsonObject.toString());
						Message msg = handler.obtainMessage();
						msg.what = 1;
						msg.arg1 = status;
						handler.sendMessage(msg);
					} catch (JSONException e) {
						Toast.makeText(getApplicationContext(), "发送验证码返回数据结构出错", Toast.LENGTH_SHORT).show();
						e.printStackTrace();
					}
				}
				
				@Override
				public void onError(Exception e) {
					
				}
			});
			
			break;
		case R.id.btn_userinfo_verification_new:
			
			String newphone2 = ed_userinfo_newphone.getText().toString();
			
			// TODO 验证倒计时，获取新验证码
			if (null == newphone2 || "".equals(newphone2)) {
				Toast.makeText(getApplicationContext(), "手机号不能为空",
						Toast.LENGTH_SHORT).show();
				return;
			}
			HashMap<String, String> params3 = new HashMap<String, String>();
			params3.put("phoneNumber", newphone2);
			HttpUtil.getJSON(HttpAddress.ADDRESS + HttpAddress.PROJECT + HttpAddress.CLASS_APPUSER + HttpAddress.METHOD_SENDVALIDATECONDE, params3, new HttpCallbackListener() {
				
				@Override
				public void onFinish(Object response) {
					jsonObject = (JSONObject) response;
					try {
						int status = jsonObject.getInt("status");
						Log.d("my", "pass:"+jsonObject.toString());
						Message msg = handler.obtainMessage();
						msg.what = 3;
						msg.arg1 = status;
						handler.sendMessage(msg);
					} catch (JSONException e) {
						Toast.makeText(getApplicationContext(), "发送验证码返回数据结构出错", Toast.LENGTH_SHORT).show();
						e.printStackTrace();
					}
				}
				
				@Override
				public void onError(Exception e) {
					
				}
			});
			break;
		case R.id.btn_userinfo_over:
			// TODO 提交新手机号到服务器
			
			String newphone = ed_userinfo_newphone.getText().toString();
			String newyan = ed_userinfo_yan_new.getText().toString();
			
			if (null == newphone || "".equals(newphone)) {
				Toast.makeText(getApplicationContext(), "手机号不能为空",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (null == newyan || "".equals(newyan)) {
				Toast.makeText(getApplicationContext(), "请输入验证码",
						Toast.LENGTH_SHORT).show();
				return;
			}
			
			String token2 = sp.getString("token", "");
			if (token2.equals("")) {
				Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
				return;
			}
			
			HashMap<String, String> params4 = new HashMap<String, String>();
			params4.put("oldNumber", phone);
			params4.put("newNumber", newphone);
			params4.put("validateCode", newyan);
			params4.put("token", token2);
			HttpUtil.getJSON(HttpAddress.ADDRESS + HttpAddress.PROJECT + HttpAddress.CLASS_USERINFO + HttpAddress.METHOD_CHANGEPHONENUMBER, params4, new HttpCallbackListener() {
				
				@Override
				public void onFinish(Object response) {
					jsonObject = (JSONObject) response;
					try {
						int status = jsonObject.getInt("status");
						Log.d("my", "pass:"+jsonObject.toString());
						Message msg = handler.obtainMessage();
						msg.what = 4;
						msg.arg1 = status;
						handler.sendMessage(msg);
					} catch (JSONException e) {
						Toast.makeText(getApplicationContext(), "发送验证码返回数据结构出错", Toast.LENGTH_SHORT).show();
						e.printStackTrace();
					}
				}
				
				@Override
				public void onError(Exception e) {
					
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

	private void initView2() {
		// TODO 注册新手机号
		setContentView(R.layout.activity_user_info_alter_phone_new);
		
		actionBar_userinfo = (ActionBarView) findViewById(R.id.actionbar_userinfo_alter);
		actionBar_userinfo.setActionBar(R.string.back, -1, R.string.title_actionbar_userinfo, this);

		ed_userinfo_yan_new = (EditText) findViewById(R.id.ed_userinfo_newyan);
		btn_userinfo_over = (Button) findViewById(R.id.btn_userinfo_over);
		ed_userinfo_newphone = (EditText) findViewById(R.id.ed_userinfo_newphone);
		
		btn_userinfo_over.setOnClickListener(this);
		findViewById(R.id.btn_userinfo_verification_new).setOnClickListener(this);
		
	}
}
