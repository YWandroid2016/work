package com.example.happyfishing.activity;

import com.example.happyfishing.R;
import com.example.happyfishing.view.ActionBarView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UserInfoAlterActivity extends Activity implements OnClickListener {

	private EditText ed_userinfo_yan;
	private EditText ed_userinfo_yan_new;
	private EditText ed_userinfo_newphone;
	private Button btn_userinfo_next;
	private Button btn_userinfo_over;
	private TextView tv_phone;
	
	private ActionBarView actionBar_userinfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initView();

	}

	private void initView() {

		setContentView(R.layout.activity_user_info_alter_phone);
		
		actionBar_userinfo = (ActionBarView) findViewById(R.id.actionbar_userinfo_alter);
		actionBar_userinfo.setActionBar(R.string.back, -1, R.string.title_actionbar_userinfo, this);

		ed_userinfo_yan = (EditText) findViewById(R.id.ed_userinfo_yan);
		btn_userinfo_next = (Button) findViewById(R.id.btn_userinfo_next);
		tv_phone = (TextView) findViewById(R.id.tv_userinfo_current_phone);

		// TODO 获取用户手机号并加载到tv_phone

		btn_userinfo_next.setOnClickListener(this);

		findViewById(R.id.btn_userinfo_verification).setOnClickListener(this);

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_userinfo_next:
			// TODO 下一步

			initView2();

			break;
		case R.id.btn_userinfo_verification:
			// TODO 验证倒计时，获取验证码
			break;
		case R.id.btn_userinfo_verification_new:
			// TODO 验证倒计时，获取新验证码
			break;
		case R.id.btn_userinfo_over:
			// TODO 提交新手机号到服务器
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
		
		findViewById(R.id.btn_userinfo_verification_new).setOnClickListener(this);
		
	}
}
