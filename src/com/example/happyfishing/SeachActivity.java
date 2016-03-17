package com.example.happyfishing;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SeachActivity extends Activity {
	private TextView tv_seach;
	private ListView lv_seach;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seach);
		initView();
	}
	
	private void initView(){
		
		lv_seach = (ListView) findViewById(R.id.lv_seach);
		
		findViewById(R.id.img_seach_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				SeachActivity.this.finish();
			}
		});
		
		
		tv_seach = (TextView) findViewById(R.id.tv_seach_seach);
		tv_seach.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//TODO 网络请求
			}
		});
		
	}

}
