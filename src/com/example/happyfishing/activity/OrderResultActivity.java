package com.example.happyfishing.activity;

import com.example.happyfishing.R;

import com.example.happyfishing.R.color;
import com.example.happyfishing.R.layout;
import com.example.happyfishing.R.menu;
import com.example.happyfishing.view.ActionBarView;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class OrderResultActivity extends Activity implements OnClickListener {

	private ActionBarView actionBar_result;
	private TextView tv_result_top;
	private TextView tv_result_bottom;
	private TextView tv_actionbar_right;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_result);

		initActionbar();

		initView();

		loadData();

	}

	private void initActionbar() {
		Intent intent = getIntent();
		int type = intent.getIntExtra("type", 0);
		Bundle bundle = intent.getExtras();
		Log.d("extra", "type" + type + "money");
		actionBar_result = (ActionBarView) findViewById(R.id.actionbar_result);
		switch (type) {
		case 1:
			// 注册成功界面
			actionBar_result.setActionBar(-1, R.string.title_actionbar_login, R.string.title_actionbar_register_success, this);
			tv_result_top = (TextView) findViewById(R.id.tv_orderresult_top);
			tv_result_bottom = (TextView) findViewById(R.id.tv_orderresult_bottom);
			tv_result_top.setText("注册成功。");
			tv_result_bottom.setVisibility(View.INVISIBLE);
			findViewById(R.id.tv_actionbar_right).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent1 = new Intent(OrderResultActivity.this, LoginActivity.class);
					startActivity(intent1);
				}
			});
			break;
		case 3:
			// 密码找回成功界面
			actionBar_result.setActionBar(-1, R.string.title_actionbar_login, R.string.title_actionbar_zhaohui_success, this);
			tv_result_top = (TextView) findViewById(R.id.tv_orderresult_top);
			tv_result_bottom = (TextView) findViewById(R.id.tv_orderresult_bottom);
			tv_result_top.setText("找回密码成功。");
			tv_result_bottom.setVisibility(View.INVISIBLE);
			findViewById(R.id.tv_actionbar_right).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent1 = new Intent(OrderResultActivity.this, LoginActivity.class);
					startActivity(intent1);
				}
			});
			break;
		// 渔坑支付成功界面
		case 4:
			actionBar_result.setActionBar(-1, R.string.close, R.string.title_actionbar_pay_success, this);
			tv_result_top = (TextView) findViewById(R.id.tv_orderresult_top);
			tv_result_bottom = (TextView) findViewById(R.id.tv_orderresult_bottom);
			tv_result_top.setText("支付成功！  请按时到坑消费");
			tv_result_bottom.setText("");
			break;
		// 修改个人信息界面
		case 5:
			actionBar_result.setActionBar(-1, R.string.close, R.string.finish, this);
			tv_result_top = (TextView) findViewById(R.id.tv_orderresult_top);
			tv_result_bottom = (TextView) findViewById(R.id.tv_orderresult_bottom);
			tv_result_top.setText("修改成功！ ");
			tv_result_bottom.setText("");
			break;
		case 6:
			actionBar_result.setActionBar(-1, R.string.close, R.string.title_actionbar_alterpassword, this);
			tv_result_top = (TextView) findViewById(R.id.tv_orderresult_top);
			tv_result_bottom = (TextView) findViewById(R.id.tv_orderresult_bottom);
			tv_result_top.setText("修改成功！ ");
			tv_result_bottom.setText("");
			break;
		case 7:
			actionBar_result.setActionBar(-1, R.string.close, R.string.title_actionbar_alterpassword, this);
			tv_result_top = (TextView) findViewById(R.id.tv_orderresult_top);
			tv_result_bottom = (TextView) findViewById(R.id.tv_orderresult_bottom);
			tv_result_top.setText("修改手机号成功！ ");
			tv_result_bottom.setText("");
			break;
		default:
			break;
		}
	}

	private void initView() {
		tv_actionbar_right = (TextView) findViewById(R.id.tv_actionbar_right);
		tv_actionbar_right.setTextColor(getResources().getColor(R.color.white));
		tv_actionbar_right.setCompoundDrawables(null, null, null, null);
	}

	private void loadData() {

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.order_result, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_actionbar_right:
			Intent intent1 = new Intent(OrderResultActivity.this, HomeActivity.class);
			OrderResultActivity.this.finish();
			startActivity(intent1);
			break;

		default:
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(OrderResultActivity.this, HomeActivity.class);
			startActivity(intent);
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

}
