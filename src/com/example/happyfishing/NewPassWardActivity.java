package com.example.happyfishing;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.happyfishing.activity.FishpitActivity;
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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewPassWardActivity extends Activity implements OnClickListener{

	private ActionBarView actionBar_newPass;
	private EditText edt_phone;
	private EditText edt_pass;
	private Button btn_com;
	private JSONObject jsonObject;
	private Handler handler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_pass_ward);
		initView();
		handler = new Handler(NewPassWardActivity.this.getMainLooper()){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 2000:
					Toast.makeText(getApplicationContext(), "设置支付密码成功", Toast.LENGTH_SHORT).show();
					NewPassWardActivity.this.finish();
					
					break;
				case 2001:
					Toast.makeText(getApplicationContext(), "手机号未注册", Toast.LENGTH_SHORT).show();
					break;
				case 2003:
					Toast.makeText(getApplicationContext(), "设置支付密码失败", Toast.LENGTH_SHORT).show();
					break;

				default:
					Toast.makeText(getApplicationContext(), "未知错误", Toast.LENGTH_SHORT).show();
					break;
				}
			}
		};
	}

	private void initView() {
		actionBar_newPass = (ActionBarView) findViewById(R.id.actionBar_newPass);
		actionBar_newPass.setActionBar(R.string.back, -1, R.string.title_actionbar_newPass, this);
		edt_phone = (EditText) findViewById(R.id.edt_newPass_phone);
		edt_pass = (EditText) findViewById(R.id.edt_newPass_pass);
		btn_com = (Button) findViewById(R.id.btn_new_pass);
		btn_com.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_actionbar_left:
			NewPassWardActivity.this.finish();
			break;
		case R.id.btn_new_pass:
			
			String phone = edt_phone.getText().toString();
			String pass = edt_pass.getText().toString();
			
			if(null == phone || "".equals(phone)){
				Toast.makeText(getApplicationContext(), "手机号不能为空", Toast.LENGTH_SHORT).show();
				return ;
			}
			
			if(null == pass || "".equals(pass)){
				Toast.makeText(getApplicationContext(), "请输入6位数字密码", Toast.LENGTH_SHORT).show();
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
			params.put("payPwd", pass);
			HttpUtil.getJSON(HttpAddress.ADDRESS + HttpAddress.PROJECT + HttpAddress.CLASS_USERSAFE + HttpAddress.METHOD_SETPASS, params, new HttpCallbackListener() {
				
				@Override
				public void onFinish(Object response) {
					jsonObject = (JSONObject) response;
					try {
						int status = jsonObject.getInt("status");
						Log.d("my", "pass:"+jsonObject.toString());
						handler.sendEmptyMessage(status);
					} catch (JSONException e) {
						Toast.makeText(getApplicationContext(), "创建支付密码返回数据结构出错", Toast.LENGTH_SHORT).show();
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
