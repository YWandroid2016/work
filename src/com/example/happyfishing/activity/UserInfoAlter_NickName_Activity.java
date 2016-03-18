package com.example.happyfishing.activity;

import com.example.happyfishing.R;
import com.example.happyfishing.view.ActionBarView;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

public class UserInfoAlter_NickName_Activity extends Activity implements OnClickListener{

	private ActionBarView actionbar_userinfo_alter;
	private EditText edit_nickname;
	private ImageView quxiao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info_alter_nickname);
		initView();
	}
	
	private void initView(){
		actionbar_userinfo_alter = (ActionBarView) findViewById(R.id.actionbar_userinfo_alter_nickname);
		edit_nickname = (EditText) findViewById(R.id.ed_userinfo_nickname);
		quxiao = (ImageView) findViewById(R.id.img_userinfo_quxiao);
		actionbar_userinfo_alter.setActionBar(R.string.cancl, R.string.queding, R.string.title_actionbar_alternick, 1, this);
		
		
		edit_nickname.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				if(arg0.length() == 0){
					quxiao.setVisibility(View.INVISIBLE);
				} else {
					quxiao.setVisibility(View.VISIBLE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				
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
			edit_nickname.setText("");
			break;
		case R.id.tv_actionbar_left:
			UserInfoAlter_NickName_Activity.this.finish();
			break;
		case R.id.tv_actionbar_right:
			//TODO 提交修改昵称
			break;
			
		default:
			break;
		}
	}
	
}
