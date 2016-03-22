package com.example.happyfishing.activity;

import java.util.ArrayList;

import com.example.happyfishing.MessageDesActivity;
import com.example.happyfishing.R;
import com.example.happyfishing.R.color;
import com.example.happyfishing.R.layout;
import com.example.happyfishing.R.menu;
import com.example.happyfishing.adapter.MessageAdapter;
import com.example.happyfishing.tool.UiUtil;
import com.example.happyfishing.view.ActionBarView;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MessageActivity extends Activity implements OnClickListener {

	private ActionBarView actionBar_message;
	private ListView lv_message;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		initView();
		loadData();
	}

	private void initView() {
		lv_message = (ListView) findViewById(R.id.lv_message);
		actionBar_message = (ActionBarView) findViewById(R.id.actionBar_message);
		actionBar_message.setActionBar(R.string.back, -1, R.string.title_actionbar_message, this);
		lv_message.setAdapter(new MessageAdapter(this, new ArrayList()));
		lv_message.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent in = new Intent(MessageActivity.this, MessageDesActivity.class);
				startActivity(in);
			}
		});
	}

	private void loadData() {
		UiUtil.setNewMessage(false);
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
