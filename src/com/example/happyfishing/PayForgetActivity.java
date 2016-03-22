package com.example.happyfishing;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.happyfishing.activity.FishpitActivity;
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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PayForgetActivity extends Activity implements OnClickListener{

	private ActionBarView actionBar_payPassAlter;
	private EditText edt_phone;
	private EditText edt_pass;
	private EditText edt_new;
	private Button btn_com;
	private JSONObject jsonObject;
	private Handler handler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_forget);
		initView();
		handler = new Handler(PayForgetActivity.this.getMainLooper()){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 2000:
					Toast.makeText(getApplicationContext(), "修改支付密码成功", Toast.LENGTH_SHORT).show();
					Intent data = new Intent();
					data.putExtra("status", 2000);
					onActivityResult(0, 2000, data);
					PayForgetActivity.this.finish();
					
					break;
				case 2001:
					Toast.makeText(getApplicationContext(), "手机号未注册", Toast.LENGTH_SHORT).show();
					break;
				case 2003:
					Toast.makeText(getApplicationContext(), "重置支付密码失败", Toast.LENGTH_SHORT).show();
					break;
				case 2006:
					Toast.makeText(getApplicationContext(), "验证码不正确", Toast.LENGTH_SHORT).show();
					break;

				default:
					Toast.makeText(getApplicationContext(), "未知错误", Toast.LENGTH_SHORT).show();
					break;
				}
			}
		};
		
	}

	private void initView() {
		actionBar_payPassAlter = (ActionBarView) findViewById(R.id.actionBar_payForget);
		actionBar_payPassAlter.setActionBar(R.string.back, -1, R.string.title_actionbar_newPass, this);
		edt_phone = (EditText) findViewById(R.id.edt_payForget_phone);
		edt_pass = (EditText) findViewById(R.id.edt_payForget_pass);
		edt_new = (EditText) findViewById(R.id.edt_payForget_new);
		btn_com = (Button) findViewById(R.id.btn_payForget);
		btn_com.setOnClickListener(this);
		findViewById(R.id.btn_payForget_verification).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_actionbar_left:
			PayForgetActivity.this.finish();
			break;
		case R.id.btn_payForget:
			
			String phone = edt_phone.getText().toString();
			String pass = edt_pass.getText().toString();
			String pass_new = edt_new.getText().toString();
			
			if(null == phone || "".equals(phone)){
				Toast.makeText(getApplicationContext(), "手机号不能为空", Toast.LENGTH_SHORT).show();
				return ;
			}
			
			if(null == pass || "".equals(pass)){
				Toast.makeText(getApplicationContext(), "请输入验证码", Toast.LENGTH_SHORT).show();
				return ;
			}
			
			if(null == pass_new || "".equals(pass_new)){
				Toast.makeText(getApplicationContext(), "请输入新支付密码", Toast.LENGTH_SHORT).show();
				return ;
			}
			
			SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
			String token = sp.getString("token", "");
			if (token.equals("")) {
				Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
				return;
			}
			
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", token);
			params.put("phoneNumber", phone);
			params.put("validateCode", pass);
			params.put("newPayPwd", pass_new);
			HttpUtil.getJSON(HttpAddress.ADDRESS + HttpAddress.PROJECT + HttpAddress.CLASS_USERSAFE + HttpAddress.METHOD_CHANGEPAYBYPHONE, params, new HttpCallbackListener() {
				
				@Override
				public void onFinish(Object response) {
					jsonObject = (JSONObject) response;
					try {
						int status = jsonObject.getInt("status");
						Log.d("my", "pass:"+jsonObject.toString());
						handler.sendEmptyMessage(status);
					} catch (JSONException e) {
						Toast.makeText(getApplicationContext(), "修改支付密码返回数据结构出错", Toast.LENGTH_SHORT).show();
						e.printStackTrace();
					}
				}
				
				@Override
				public void onError(Exception e) {
					
				}
			});
			
			break;
		case R.id.btn_payForget_verification:
			
			String phone2 = edt_phone.getText().toString();
			
			if(null == phone2 || "".equals(phone2)){
				Toast.makeText(getApplicationContext(), "手机号不能为空", Toast.LENGTH_SHORT).show();
				return ;
			}
			
			HashMap<String, String> params2 = new HashMap<String, String>();
			params2.put("phoneNumber", phone2);
			HttpUtil.getJSON(HttpAddress.ADDRESS + HttpAddress.PROJECT + HttpAddress.CLASS_USERINFO + HttpAddress.METHOD_SENDVALIDATECONDE, params2, new HttpCallbackListener() {
				
				@Override
				public void onFinish(Object response) {
					jsonObject = (JSONObject) response;
					try {
						int status = jsonObject.getInt("status");
						Log.d("my", "pass:"+jsonObject.toString());
						if(2000 == status){
							Toast.makeText(getApplicationContext(), "验证码发送成功", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getApplicationContext(), "验证码发送失败", Toast.LENGTH_SHORT).show();
						}
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
		default:
			break;
		}
	}


}

