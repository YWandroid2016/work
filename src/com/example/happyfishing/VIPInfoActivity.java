package com.example.happyfishing;

import com.example.happyfishing.view.ActionBarView;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;

public class VIPInfoActivity extends Activity implements OnClickListener{

	private ActionBarView actionBar_becomeVIP;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vipinfo);
		actionBar_becomeVIP = (ActionBarView) findViewById(R.id.actionBar_becomeVIP);
		actionBar_becomeVIP.setActionBar(R.string.back, -1, R.string.title_actionbar_memberdetail, this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_actionbar_left:
			VIPInfoActivity.this.finish();
			break;

		default:
			break;
		}
	}


}
