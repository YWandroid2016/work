package com.example.happyfishing.activity;

import com.example.happyfishing.R;
import com.example.happyfishing.R.id;
import com.example.happyfishing.R.layout;
import com.example.happyfishing.R.menu;
import com.example.happyfishing.view.ActionBarView;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

public class UpgradeVIPActivity extends Activity implements OnClickListener{

	private ActionBarView actionBar_becomeVIP;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upgrade_vip);
		
		initView();
		
		loadData();
	}

	private void initView() {
		actionBar_becomeVIP = (ActionBarView) findViewById(R.id.actionBar_becomeVIP);
		actionBar_becomeVIP.setActionBar(R.string.back, R.string.title_actionbar_memberdetail, R.string.title_actionbar_becomeVIP, this);
		findViewById(R.id.tv_updateVIP_type_month_become).setOnClickListener(this);
		findViewById(R.id.tv_updateVIP_type_quater_become).setOnClickListener(this);
		findViewById(R.id.tv_updateVIP_type_year_become).setOnClickListener(this);
	
	}

	private void loadData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.upgrade_vi, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_actionbar_left:
			UpgradeVIPActivity.this.finish();
			break;
		case R.id.tv_updateVIP_type_month_become:
			Intent intent1 = new Intent(UpgradeVIPActivity.this, BecomeVipOrderActivity.class);
			intent1.putExtra("money", 500);
			intent1.putExtra("type", 1);
			startActivity(intent1);
			break;
		case R.id.tv_updateVIP_type_quater_become:
			Intent intent2 = new Intent(UpgradeVIPActivity.this, BecomeVipOrderActivity.class);
			intent2.putExtra("money", 1200);
			intent2.putExtra("type", 2);
			startActivity(intent2);
			break;
		case R.id.tv_updateVIP_type_year_become:
			Intent intent3 = new Intent(UpgradeVIPActivity.this, BecomeVipOrderActivity.class);
			intent3.putExtra("money", 2400);
			intent3.putExtra("type", 3);
			startActivity(intent3);
			break;
		
//		case :
//			
//			break;

		default:
			break;
		}
	}

}
