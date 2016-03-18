package com.example.happyfishing.activity;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.happyfishing.R;
import com.example.happyfishing.view.ActionBarView;
import com.example.happyfishing.view.RushBuyCountDownTimerView;

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

public class BecomeVipOrderActivity extends Activity implements OnClickListener{

	private ActionBarView actionbar_becomevip;
	private TextView tv_becomevip_order_money;
	private TextView tv_becomevip_order_time;
	private TextView tv_becomevip_phone;
	private TextView tv_becomevip_order_paymoney;
	private RushBuyCountDownTimerView tv_timeUp;
	private String dateCreate;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_become_vip_order);
		
		initView();
		
		loadData();
	}

	private void initView() {
		tv_timeUp = (RushBuyCountDownTimerView) findViewById(R.id.timeup_vipordershow);
		actionbar_becomevip = (ActionBarView) findViewById(R.id.actionbar_becomevip);
		actionbar_becomevip.setActionBar(R.string.cancl, -1, R.string.title_actionbar_order_detail, this);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		dateCreate = bundle.getString("dateCreate");
		
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			Log.d("dateCreate", dateCreate+"   ");
			Date daojishi = dateFormat.parse(dateCreate);
			String creatString = dateFormat.format(daojishi);
			String currentString = dateFormat.format(new Date(System.currentTimeMillis()));
			SimpleDateFormat dateFormat_minute= new SimpleDateFormat("mm");
			SimpleDateFormat dateFormat_second = new SimpleDateFormat("ss");
			Date daojishiCurrent = dateFormat.parse(currentString);
			long daojishiTime = 15*60*1000 - (daojishiCurrent.getTime() - daojishi.getTime());
			String minute = dateFormat_minute.format(new Date(daojishiTime));
			String second = dateFormat_second.format(new Date(daojishiTime));
			tv_timeUp.setTime(0, Integer.parseInt(minute), Integer.parseInt(second));
			tv_timeUp.start();
			Log.d("daojishi", minute+"  "+second);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//设置所花金额和有效期的文本
		int money = intent.getIntExtra("money", -1);
		int type = intent.getIntExtra("type", -1);
		tv_becomevip_order_money = (TextView) findViewById(R.id.tv_becomevip_order_money);
		tv_becomevip_order_paymoney = (TextView) findViewById(R.id.tv_becomevip_order_paymoney);
		tv_becomevip_order_money.setText("￥"+money);
		tv_becomevip_order_paymoney.setText("￥"+money);
		tv_becomevip_order_paymoney.setTextColor(getResources().getColor(R.color.red));
		tv_becomevip_order_time = (TextView) findViewById(R.id.tv_becomevip_order_time);
		tv_becomevip_phone = (TextView) findViewById(R.id.tv_becomevip_orer_phone);
		switch (type) {
		case 1:
			tv_becomevip_order_time.setText("一个月");
			break;
		case 2:
			tv_becomevip_order_time.setText("三个月");
			break;
		case 3:
			tv_becomevip_order_time.setText("一年");
			break;

		default:
			break;
		}
		
		
	}

	private void loadData() {
		SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
		String phoneNumber = sp.getString("phoneNumber", "获取失败");
		tv_becomevip_phone.setText(phoneNumber);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.become_vip_order, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_actionbar_left:
			BecomeVipOrderActivity.this.finish();
			break;

		default:
			break;
		}
	}

}
