package com.example.happyfishing.activity;

import com.example.happyfishing.NewPassWardActivity;
import com.example.happyfishing.PayPassAlterActivity;
import com.example.happyfishing.R;
import com.example.happyfishing.R.layout;
import com.example.happyfishing.R.menu;
import com.example.happyfishing.tool.DataCleanManager;
import com.example.happyfishing.view.ActionBarView;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class MyConfigActivity extends Activity implements OnClickListener {

	private ActionBarView actionBar_config;
	private TextView tv_myconfig_canche;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_config);
		sp = getSharedPreferences("user", Context.MODE_PRIVATE);
//		findViewById(R.id.ll_myconfig_loginpassword).setOnClickListener(this);
		findViewById(R.id.btn_logout).setOnClickListener(this);
		
		
		initView();
		
		loadData();

	}

	private void initView() {
		tv_myconfig_canche = (TextView) findViewById(R.id.tv_myconfig_canche);
		try {
			tv_myconfig_canche.setText(DataCleanManager.getTotalCacheSize(getApplicationContext()));
		} catch (Exception e) {
			tv_myconfig_canche.setText("0K");
		}
		actionBar_config = (ActionBarView) findViewById(R.id.actionBar_config);
		actionBar_config.setActionBar(R.string.back, -1, R.string.title_actionbar_myconfig, this);
		
		findViewById(R.id.ll_myconfig_loginpassword).setOnClickListener(this); //登陆密码
		findViewById(R.id.ll_myconfig_paypassword).setOnClickListener(this);	//支付密码
		findViewById(R.id.ll_myconfig_update).setOnClickListener(this);		//版本更新
		findViewById(R.id.ll_myconfig_cancle).setOnClickListener(this);		//清除缓存
		
		
	}

	private void loadData() {
		// TODO 加载数据
		
	}

	@Override
	public void onClick(View v) {
		Intent intent = new  Intent();
		switch (v.getId()) {
		case R.id.tv_actionbar_left:
			MyConfigActivity.this.finish();
			break;
		case R.id.ll_myconfig_loginpassword:
			String token = sp.getString("token", "");
			if (token.equals("")) {
				intent.setClass(MyConfigActivity.this, LoginActivity.class);
			} else {
				intent.setClass(MyConfigActivity.this, PasswordAlterActivity.class);
			}
			startActivity(intent);
			break;
			
		case R.id.ll_myconfig_paypassword:
			//TODO 支付密码业务
			
			String haspay = sp.getString("hasPayPass", "0");
			Intent in = new Intent();
			
			if(!"0".equals(haspay)){
				
				//TODO 修改支付密码
				in.setClass(MyConfigActivity.this, PayPassAlterActivity.class);
				startActivity(in);
				
			} else {
				
				//TODO 创建支付密码
				in.setClass(MyConfigActivity.this, NewPassWardActivity.class);
				startActivity(in);
			}
			
			
			break;
			
		case R.id.ll_myconfig_update:
			//待开发
			break;
		
		case R.id.ll_myconfig_cancle:
			DataCleanManager.clearAllCache(getApplicationContext());
			try {
				tv_myconfig_canche.setText(DataCleanManager.getTotalCacheSize(getApplicationContext()));
			} catch (Exception e) {
				tv_myconfig_canche.setText("0K");
			}
			break;
			
		case R.id.btn_logout:
			if (sp.getString("token", null) == null) {
				Toast.makeText(MyConfigActivity.this, "您还尚未登录", Toast.LENGTH_SHORT).show();
				break;
			}
			AlertDialog.Builder builder = new AlertDialog.Builder(MyConfigActivity.this);
			builder.setMessage("是否退出登录？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
		           @Override
		           public void onClick(DialogInterface dialog, int which) {
		        	   SharedPreferences sp1 =getSharedPreferences("user", Context.MODE_PRIVATE);
		        	   Editor editor = sp1.edit();
		        	   editor.clear();
		        	   editor.commit();
		        	   Toast.makeText(MyConfigActivity.this, "您已退出登录", Toast.LENGTH_SHORT).show();
		        	   Intent intent = new Intent(MyConfigActivity.this, HomeActivity.class);
		        	   startActivity(intent);
		           }
		       }).setNegativeButton("取消",
		       new DialogInterface.OnClickListener() {
		           @Override
		           public void onClick(DialogInterface dialog, int which) {
		                
		           }
		       }).show();
			break;
		default:
			break;
		}
	}

}
