package com.example.happyfishing.activity;

import com.example.happyfishing.R;
import com.example.happyfishing.tool.UiUtil;
import com.example.happyfishing.view.ActionBarView;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MyWalletActivity extends Activity implements OnClickListener {

	private TextView tv_myorder;
	private TextView tv_mycollection;
	private TextView tv_mysetting;

	private TextView tv_homeactivity_heikeng;
	private TextView tv_homeactivity_fishingshop;
	private TextView tv_homeactivity_mywallet;
	private TextView tv_homeactivity_jump2home;
	private TextView tv_actionbar_right;

	private ActionBarView actionbar_mywallet;
	private TextView tv_mywallet_usablejifen;
	private TextView tv_mywallet_confirm;
	private Button btn_bocomeVIP;
	private SharedPreferences sp ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_wallet);
		
		sp = getSharedPreferences("user", Context.MODE_PRIVATE);
		initView();

		loadData();
	}
	
	
	@Override
	protected void onStart() {
		Log.d("boolean", "newMessage"+UiUtil.getNewMessage()+"");
		tv_actionbar_right = (TextView) findViewById(R.id.tv_actionbar_right);
		Drawable drawable = null;
		if (UiUtil.getNewMessage()) {
			drawable = getResources().getDrawable(R.drawable.ic_message_hasnew);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		}else {
			drawable = getResources().getDrawable(R.drawable.ic_message_nonew);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		}
		tv_actionbar_right.setCompoundDrawables(drawable, null, null, null);
		super.onStart();
	}

	private void initView() {
		Drawable drawable1 = getResources().getDrawable(R.drawable.ic_menu_home_default);
		drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
		tv_homeactivity_jump2home = (TextView) findViewById(R.id.tv_homeactivity_jump2home);
		tv_homeactivity_jump2home.setOnClickListener(this);
		tv_homeactivity_jump2home.setCompoundDrawables(null, drawable1, null, null);

		Drawable drawable2 = getResources().getDrawable(R.drawable.ic_menu_fishshop_default);
		drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
		tv_homeactivity_fishingshop = (TextView) findViewById(R.id.tv_homeactivity_fishingshop);
		tv_homeactivity_fishingshop.setOnClickListener(this);
		tv_homeactivity_fishingshop.setCompoundDrawables(null, drawable2, null, null);

		Drawable drawable3 = getResources().getDrawable(R.drawable.ic_menu_fishpit_default);
		drawable3.setBounds(0, 0, drawable3.getMinimumWidth(), drawable3.getMinimumHeight());
		tv_homeactivity_heikeng = (TextView) findViewById(R.id.tv_homeactivity_heikeng);
		tv_homeactivity_heikeng.setOnClickListener(this);
		tv_homeactivity_heikeng.setCompoundDrawables(null, drawable3, null, null);

		Drawable drawable4 = getResources().getDrawable(R.drawable.ic_menu_mywallet_selected);
		drawable4.setBounds(0, 0, drawable4.getMinimumWidth(), drawable4.getMinimumHeight());
		tv_homeactivity_mywallet = (TextView) findViewById(R.id.tv_homeactivity_mywallet);
		tv_homeactivity_mywallet.setTextColor(Color.parseColor("#3086f2"));
		tv_homeactivity_mywallet.setOnClickListener(this);
		tv_homeactivity_mywallet.setCompoundDrawables(null, drawable4, null, null);

		// 顶部actionbar的监听
		actionbar_mywallet = (ActionBarView) findViewById(R.id.actionbar_mywallet);
		actionbar_mywallet.setActionBar(-1, R.string.action_settings, R.string.title_actionbar_mywallet, this);

		findViewById(R.id.tv_homeactivity_fishingshop).setOnClickListener(this);
		findViewById(R.id.tv_homeactivity_heikeng).setOnClickListener(this);
		findViewById(R.id.tv_homeactivity_jump2home).setOnClickListener(this);
		findViewById(R.id.tv_mywallet_userinfo).setOnClickListener(this);
		findViewById(R.id.tv_mywallet_bill).setOnClickListener(this);

		// tv_myjifen=(TextView) findViewById(R.id.tv_my);
		// tv_myjifen.setOnClickListener(this);
		tv_mycollection = (TextView) findViewById(R.id.tv_mywallet_mycollection);
		tv_mycollection.setOnClickListener(this);
		tv_myorder = (TextView) findViewById(R.id.tv_mywallet_myorder);
		tv_myorder.setOnClickListener(this);
		tv_mysetting = (TextView) findViewById(R.id.tv_mywallet_setting);
		tv_mysetting.setOnClickListener(this);
		btn_bocomeVIP = (Button) findViewById(R.id.btn_bocomeVIP);
		btn_bocomeVIP.setOnClickListener(this);
		
		tv_mywallet_usablejifen = (TextView) findViewById(R.id.tv_mywallet_usablejifen);
		tv_mywallet_confirm = (TextView) findViewById(R.id.tv_mywallet_confirm);
		
		String token = sp.getString("token", null);
		if (token != null) {
			
			String jifen = sp.getString("userPoint", "0");
			tv_mywallet_usablejifen.setText(jifen);
			tv_mywallet_confirm.setText("可用积分");
		} else {
			findViewById(R.id.tv_mywallet_bill).setVisibility(View.INVISIBLE);
			findViewById(R.id.img_right).setVisibility(View.INVISIBLE);
			tv_mywallet_confirm.setVisibility(View.GONE);
			tv_mywallet_usablejifen.setText("您还未登录，请先登录");
			btn_bocomeVIP.setText("登录");
		}
	}

	private void loadData() {

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_wallet, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		String token = sp.getString("token", "");
		switch (v.getId()) {
		// 底部button的点击事件
		case R.id.tv_homeactivity_heikeng:
			intent.setClass(MyWalletActivity.this, FishpitActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_homeactivity_fishingshop:
			intent.setClass(MyWalletActivity.this, FishingShopActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_homeactivity_jump2home:
			intent.setClass(MyWalletActivity.this, HomeActivity.class);
			startActivity(intent);
			MyWalletActivity.this.finish();
			break;
		// 钱包界面的点击事件
		case R.id.tv_mywallet_bill:
			intent.setClass(MyWalletActivity.this, MyBillActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_mywallet_userinfo:
			if (token.equals("")) {
				intent.setClass(MyWalletActivity.this, LoginActivity.class);
			} else {
				intent.setClass(MyWalletActivity.this, UserInfoActivity.class);
			}
			startActivity(intent);
			break;
		case R.id.tv_mywallet_mycollection:
			if (token.equals("")) {
				intent.setClass(MyWalletActivity.this, LoginActivity.class);
			} else {
				intent.setClass(MyWalletActivity.this, MyCollectionActivity.class);
			}
			startActivity(intent);
			break;
		case R.id.tv_mywallet_myorder:
			if (token.equals("")) {
				intent.setClass(MyWalletActivity.this, LoginActivity.class);
			} else {
				intent.setClass(MyWalletActivity.this, MyOrderActivity.class);
			}
			startActivity(intent);
			break;
		case R.id.tv_mywallet_setting:
			Intent intent7 = new Intent(MyWalletActivity.this, MyConfigActivity.class);
			startActivity(intent7);
			break;
		case R.id.btn_bocomeVIP:
			if (token.equals("")) {
				intent.setClass(MyWalletActivity.this, LoginActivity.class);
			}else {
				intent.setClass(MyWalletActivity.this, UpgradeVIPActivity.class);
			}
			startActivity(intent);
			break;
		case R.id.tv_actionbar_left:
			MyWalletActivity.this.finish();
			break;
		case R.id.tv_actionbar_right:
			
			if (token.equals("")) {
				intent.setClass(MyWalletActivity.this, LoginActivity.class);
			}else {
				intent.setClass(MyWalletActivity.this, MessageActivity.class);
			}
			startActivity(intent);
			break;
		default:
			break;
		}
	}

}
