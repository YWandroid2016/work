package com.example.happyfishing.activity;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.happyfishing.R;
import com.example.happyfishing.tool.HttpAddress;
import com.example.happyfishing.tool.HttpCallbackListener;
import com.example.happyfishing.tool.HttpUtil;
import com.example.happyfishing.view.ActionBarView;
import com.example.happyfishing.view.TimeButton;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.app.Activity;
import android.content.Intent;

public class PasswordCreatActivity extends Activity implements OnClickListener{
	
	private EditText edt_phone;
	private EditText edt_verification;
	private EditText edt_password;
	private TimeButton btn_register_verification;
	private Button forget_login;
	private ActionBarView actionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password_creat);
		initView();
	}
	
	private void initView(){
		
		edt_phone = (EditText) findViewById(R.id.edt_forget_phone);
		edt_verification = (EditText) findViewById(R.id.edt_forget_verification);
		edt_password = (EditText) findViewById(R.id.edt_forget_password);
		btn_register_verification = (TimeButton) findViewById(R.id.btn_register_verification);
		forget_login = (Button) findViewById(R.id.btn_forget_login);
		actionBar = (ActionBarView) findViewById(R.id.actionBar_passwordCreate);
		
		actionBar.setActionBar(R.string.back, -1, R.string.title_actionbar_zhaohui, this);
		
		btn_register_verification.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String phoneNumber = edt_phone.getText().toString();
				if(null == phoneNumber || "".equals(phoneNumber)){
					Toast.makeText(PasswordCreatActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				HashMap<String , String> params = new HashMap<String, String>();
				params.put("phoneNumber", phoneNumber);
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
							e1.printStackTrace();
						}
						if (code == 2000) {
							Toast.makeText(PasswordCreatActivity.this, "验证码发送成功", Toast.LENGTH_SHORT).show();
						}else {
							Toast.makeText(PasswordCreatActivity.this, "验证码发送失败", Toast.LENGTH_SHORT).show();
						}
					}
					@Override
					public void onError(Exception e) {
						Toast.makeText(PasswordCreatActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
		
		
		/**
		 * 
		 * 测试网络请求
		 * 
		 * 
		 */
		
		forget_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String phone = edt_phone.getText().toString();
				String ver = edt_verification.getText().toString();
				String pass = edt_password.getText().toString();
				
				if(null == phone || "".equals(phone)){
					Toast.makeText(PasswordCreatActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
					return;
				}
				if(null == ver || "".equals(ver)){
					Toast.makeText(PasswordCreatActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
					return;
				}
				if(null == pass || "".equals(pass)){
					Toast.makeText(PasswordCreatActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
					return;
				}
				
				HashMap<String , String> params = new HashMap<String, String>();
				params.put("phoneNumber", phone);
				params.put("validateCode", ver);
				params.put("newPassword", pass);
				HttpUtil.getJSON(HttpAddress.ADDRESS+HttpAddress.PROJECT+
						HttpAddress.CLASS_APPUSER+HttpAddress.METHOD_CHANGEPASSWORDBYPHONE, 
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
							e1.printStackTrace();
						}
						if (code == 2000) {
							popupwindow();
						} else {
							Toast.makeText(PasswordCreatActivity.this, statisString, Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onError(Exception e) {
						
					}
				});
			}
		});
	}
	
	private TextView tv_pop;
	private TextView btn_pop;
	private PopupWindow popupWindow;
	
	public void popupwindow(){
		View v = LayoutInflater.from(this).inflate(R.layout.popup, null);
		popupWindow = new PopupWindow(v, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
		tv_pop = (TextView) v.findViewById(R.id.tv_pop);
		tv_pop.setText("密码重置成功！");
		btn_pop = (TextView) v.findViewById(R.id.btn_pop);
		btn_pop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				popupWindow.dismiss();
				Intent intent3 = new Intent(PasswordCreatActivity.this, LoginActivity.class);
				startActivity(intent3);
			}
		});
		
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.tv_actionbar_left:
			PasswordCreatActivity.this.finish();
			break;
		default:
			break;
		}
	}

}
