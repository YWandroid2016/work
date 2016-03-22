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
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PayPassAlterActivity extends Activity implements OnClickListener{

	private ActionBarView actionBar_payPassAlter;
	private EditText edt_phone;
	private EditText edt_pass;
	private EditText edt_new;
	private Button btn_com;
	private JSONObject jsonObject;
	private InputMethodManager inputManager;
	private Handler handler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_pass_alter);
		initView();
		handler = new Handler(PayPassAlterActivity.this.getMainLooper()){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 2000:
					Toast.makeText(getApplicationContext(), "修改支付密码成功", Toast.LENGTH_SHORT).show();
					PayPassAlterActivity.this.finish();
					break;
				case 2001:
					Toast.makeText(getApplicationContext(), "手机号未注册", Toast.LENGTH_SHORT).show();
					break;
				case 2003:
					Toast.makeText(getApplicationContext(), "重置支付密码失败", Toast.LENGTH_SHORT).show();
					break;
				case 2004:
					Toast.makeText(getApplicationContext(), "旧支付密码不正确", Toast.LENGTH_SHORT).show();
					break;
					
				default:
					Toast.makeText(getApplicationContext(), "未知错误", Toast.LENGTH_SHORT).show();
					break;
				}
			}
		};
	}

	private void initView() {
		actionBar_payPassAlter = (ActionBarView) findViewById(R.id.actionBar_payPassAlter);
		actionBar_payPassAlter.setActionBar(R.string.back, -1, R.string.title_actionbar_newPass, this);
		edt_phone = (EditText) findViewById(R.id.edt_payPassAlter_phone);
		edt_pass = (EditText) findViewById(R.id.edt_payPassAlter_pass);
		edt_new = (EditText) findViewById(R.id.edt_payPassAlter_new);
		btn_com = (Button) findViewById(R.id.btn_payPassAlter);
		btn_com.setOnClickListener(this);
		findViewById(R.id.tv_pay_alter_forget).setOnClickListener(this);
		inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_actionbar_left:
			PayPassAlterActivity.this.finish();
			break;
		case R.id.btn_payPassAlter:
			
			inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
			
			String phone = edt_phone.getText().toString();
			String pass = edt_pass.getText().toString();
			String pass_new = edt_new.getText().toString();
			
			if(null == phone || "".equals(phone)){
				Toast.makeText(getApplicationContext(), "手机号不能为空", Toast.LENGTH_SHORT).show();
				return ;
			}
			
			if(null == pass || "".equals(pass)){
				Toast.makeText(getApplicationContext(), "请输入当前密码", Toast.LENGTH_SHORT).show();
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
			params.put("oldPayPwd", pass);
			params.put("newPayPwd", pass_new);
			HttpUtil.getJSON(HttpAddress.ADDRESS + HttpAddress.PROJECT + HttpAddress.CLASS_USERSAFE + HttpAddress.METHOD_CHANGEPAYBYOLD, params, new HttpCallbackListener() {
				
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
			
		case R.id.tv_pay_alter_forget:
			Intent in = new Intent(PayPassAlterActivity.this, PayForgetActivity.class);
			startActivityForResult(in, 0);
			break;
		default:
			break;
		}
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 0 && resultCode == 20000){
			PayPassAlterActivity.this.finish();
		}
	}
}
