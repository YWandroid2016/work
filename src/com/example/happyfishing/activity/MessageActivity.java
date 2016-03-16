package com.example.happyfishing.activity;

import com.example.happyfishing.R;
import com.example.happyfishing.R.color;
import com.example.happyfishing.R.layout;
import com.example.happyfishing.R.menu;
import com.example.happyfishing.tool.UiUtil;
import com.example.happyfishing.view.ActionBarView;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

public class MessageActivity extends Activity implements OnClickListener {

	private ActionBarView actionBar_message;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);

		initView();

		loadData();
	}

	private void initView() {
		actionBar_message = (ActionBarView) findViewById(R.id.actionBar_message);
		actionBar_message.setActionBar(R.string.back, -1, R.string.title_actionbar_message, null);
		findViewById(R.id.tv_actionbar_left).setOnClickListener(this);
	}

	private void loadData() {
		UiUtil.setNewMessage(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.message, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_actionbar_left:
			MessageActivity.this.finish();
			break;

		default:
			break;
		}
	}

}
