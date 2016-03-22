package com.example.happyfishing;

import com.example.happyfishing.activity.UpgradeVIPActivity;
import com.example.happyfishing.view.ActionBarView;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MessageDesActivity extends Activity implements OnClickListener{
	
	private ActionBarView actionBar_message_des;
	private TextView title_tv;
	private TextView date_tv;
	private TextView des_tv;
	private TextView after_tv;
	private TextView content_tv;
	private Button btn_message;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_des);
		
		initView();
	}

	private void initView() {
		// TODO 初始化布局
		actionBar_message_des = (ActionBarView) findViewById(R.id.actionBar_messageDes);
		actionBar_message_des.setActionBar(R.string.back, -1, R.string.title_actionbar_message_info, this);
		btn_message = (Button) findViewById(R.id.btn_messageDes);
		btn_message.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO 单击监听
		
		switch (v.getId()) {
		case R.id.tv_actionbar_left:
			MessageDesActivity.this.finish();
			break;
			
		case R.id.btn_messageDes:
			Intent in = new Intent(MessageDesActivity.this, UpgradeVIPActivity.class);
			startActivity(in);
			break;
		default:
			break;
		}
		
	}

}
