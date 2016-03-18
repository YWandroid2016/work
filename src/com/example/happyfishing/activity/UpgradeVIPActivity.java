package com.example.happyfishing.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.example.happyfishing.R;
import com.example.happyfishing.R.id;
import com.example.happyfishing.R.layout;
import com.example.happyfishing.R.menu;
import com.example.happyfishing.tool.HttpAddress;
import com.example.happyfishing.tool.HttpCallbackListener;
import com.example.happyfishing.tool.HttpUtil;
import com.example.happyfishing.view.ActionBarView;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class UpgradeVIPActivity extends Activity implements OnClickListener{

	private ActionBarView actionBar_becomeVIP;
	private TextView tv_actionbar_right;
	private SharedPreferences sp;
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
		tv_actionbar_right = (TextView) findViewById(R.id.tv_actionbar_right);
		tv_actionbar_right.setText("会员特权");
		tv_actionbar_right.setCompoundDrawables(null, null, null, null);
		tv_actionbar_right.setOnClickListener(this);
		sp = getSharedPreferences("user", Context.MODE_PRIVATE);
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
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			long currentTime = System.currentTimeMillis();
			Date date = new Date(currentTime);
			String dateCreate = dateFormat.format(date);
			Log.d("deatCreate", dateCreate);
			final Bundle bundle = new Bundle();
			bundle.putString("dateCreate", dateCreate);
			HashMap<String, String> params = new HashMap<String, String>();
			String token = sp.getString("token", "");
			params.put("token", token);
			params.put("category", "month");
			params.put("totalFee", "500");
			HttpUtil.getJSON(HttpAddress.ADDRESS+HttpAddress.PROJECT+
					HttpAddress.CLASS_USERMEMBER+HttpAddress.METHOD_MAKEMEMBERORDER, 
					params, 
					new HttpCallbackListener() {
						
						@Override
						public void onFinish(Object response) {
							Log.d("memberOrder", response.toString());
							Intent intent1 = new Intent(UpgradeVIPActivity.this, BecomeVipOrderActivity.class);
							intent1.putExtras(bundle);
							intent1.putExtra("money", 500);
							intent1.putExtra("type", 1);
							startActivity(intent1);
						}
						
						@Override
						public void onError(Exception e) {
							// TODO Auto-generated method stub
							
						}
					});
			break;
		case R.id.tv_updateVIP_type_quater_become:
			
			SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			long currentTime2 = System.currentTimeMillis();
			Date date2 = new Date(currentTime2);
			String dateCreate2 = dateFormat2.format(date2);
			Log.d("deatCreate", dateCreate2);
			final Bundle bundle2 = new Bundle();
			bundle2.putString("dateCreate", dateCreate2);
			HashMap<String, String> params2 = new HashMap<String, String>();
			String token2 = sp.getString("token", "");
			params2.put("token", token2);
			params2.put("category", "season");
			params2.put("totalFee", "1200");
			HttpUtil.getJSON(HttpAddress.ADDRESS+HttpAddress.PROJECT+
					HttpAddress.CLASS_USERMEMBER+HttpAddress.METHOD_MAKEMEMBERORDER, 
					params2, 
					new HttpCallbackListener() {
						
						@Override
						public void onFinish(Object response) {
							Log.d("member", response.toString());
							Intent intent2 = new Intent(UpgradeVIPActivity.this, BecomeVipOrderActivity.class);
							intent2.putExtras(bundle2);
							intent2.putExtra("money", 1200);
							intent2.putExtra("type", 2);
							startActivity(intent2);
						}
						
						@Override
						public void onError(Exception e) {
							// TODO Auto-generated method stub
							
						}
					});
			break;
		case R.id.tv_updateVIP_type_year_become:
			SimpleDateFormat dateFormat3 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			long currentTime3 = System.currentTimeMillis();
			Date date3 = new Date(currentTime3);
			String dateCreate3 = dateFormat3.format(date3);
			Log.d("deatCreate", dateCreate3);
			final Bundle bundle3 = new Bundle();
			bundle3.putString("dateCreate", dateCreate3);
			HashMap<String, String> params3 = new HashMap<String, String>();
			String token3 = sp.getString("token", "");
			params3.put("token", token3);
			params3.put("category", "year");
			params3.put("totalFee", "2400");
			HttpUtil.getJSON(HttpAddress.ADDRESS+HttpAddress.PROJECT+
					HttpAddress.CLASS_USERMEMBER+HttpAddress.METHOD_MAKEMEMBERORDER, 
					params3, 
					new HttpCallbackListener() {
						
						@Override
						public void onFinish(Object response) {
							Log.d("member", response.toString());
							Intent intent3 = new Intent(UpgradeVIPActivity.this, BecomeVipOrderActivity.class);
							intent3.putExtras(bundle3);
							intent3.putExtra("money", 2400);
							intent3.putExtra("type", 3);
							startActivity(intent3);
						}
						
						@Override
						public void onError(Exception e) {
							// TODO Auto-generated method stub
							
						}
					});
			
			break;
		case R.id.tv_actionbar_right:
			break;
		
//		case :
//			
//			break;

		default:
			break;
		}
	}

}
