package com.example.happyfishing.activity;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.happyfishing.R;
import com.example.happyfishing.tool.HttpAddress;
import com.example.happyfishing.tool.HttpCallbackListener;
import com.example.happyfishing.tool.HttpUtil;
import com.example.happyfishing.view.ActionBarView;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class UserInfoAlter_NickName_Activity extends Activity implements
		OnClickListener {

	private ActionBarView actionbar_userinfo_alter;
	private EditText edit_nickname;
	private ImageView quxiao;
	private SharedPreferences sp;
	private JSONObject jsonObject;
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info_alter_nickname);
		sp = getSharedPreferences("user", Context.MODE_PRIVATE);
		initView();
		handler = new Handler(UserInfoAlter_NickName_Activity.this.getMainLooper()) {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 2000:
					Toast.makeText(getApplicationContext(),
							"昵称修改成功", Toast.LENGTH_SHORT)
							.show();
					sp.edit().putString("nickname", edit_nickname.getText().toString()).commit();
					UserInfoAlter_NickName_Activity.this.finish();
					break;
				case 2001:
					Toast.makeText(getApplicationContext(),
							"请先登录", Toast.LENGTH_SHORT)
							.show();
					break;
				case 2008:
					Toast.makeText(getApplicationContext(),
							"修改昵称返回数据结构出错", Toast.LENGTH_SHORT)
							.show();
					break;

				default:
					break;
				}
			}
		};
	}

	private void initView() {
		actionbar_userinfo_alter = (ActionBarView) findViewById(R.id.actionbar_userinfo_alter_nickname);
		edit_nickname = (EditText) findViewById(R.id.ed_userinfo_nickname);
		String nick = sp.getString("nickname", "未登陆");
		edit_nickname.setHint(nick);
		quxiao = (ImageView) findViewById(R.id.img_userinfo_quxiao);
		actionbar_userinfo_alter.setActionBar(R.string.cancl, R.string.queding,
				R.string.title_actionbar_alternick, 1, this);

		edit_nickname.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				if (arg0.length() == 0) {
					quxiao.setVisibility(View.INVISIBLE);
				} else {
					quxiao.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}
		});

		quxiao.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.img_userinfo_quxiao:
			String nick = sp.getString("nickname", "未登陆");
			edit_nickname.setHint(nick);
			break;
		case R.id.tv_actionbar_left:
			UserInfoAlter_NickName_Activity.this.finish();
			break;
		case R.id.tv_actionbar_right:
			// TODO 提交修改昵称

			String nickname = edit_nickname.getText().toString();
			
			String token = sp.getString("token", "");
			
			if (token.equals("")) {
				Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
				return;
			}

			if (null == nickname || "".equals(nickname)) {
				Toast.makeText(getApplicationContext(), "手机号不能为空",
						Toast.LENGTH_SHORT).show();
				return;
			}

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", token);
			params.put("nickname", nickname);
			HttpUtil.getJSON(HttpAddress.ADDRESS + HttpAddress.PROJECT
					+ HttpAddress.CLASS_USERINFO
					+ HttpAddress.METHOD_EDITNICKNAME, params,
					new HttpCallbackListener() {

						@Override
						public void onFinish(Object response) {
							jsonObject = (JSONObject) response;
							try {
								int status = jsonObject.getInt("status");
								Log.d("my", "pass:" + jsonObject.toString());
								handler.sendEmptyMessage(status);
							} catch (JSONException e) {
								
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
