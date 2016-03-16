package com.example.happyfishing.activity;

import com.example.happyfishing.R;
import com.example.happyfishing.R.id;
import com.example.happyfishing.R.layout;
import com.example.happyfishing.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

public class UserAddressAlterActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_address_alter);
		
		
		findViewById(R.id.btn_useraddress).setOnClickListener(this);
		Intent intent = getIntent();
		boolean have = intent.getBooleanExtra("have", false);
		if (have) {
			findViewById(R.id.liv_useraddress).setVisibility(View.VISIBLE);
			findViewById(R.id.btn_useraddress).setVisibility(View.GONE);
		}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_address_alter, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_useraddress:
			Intent intent1 = new Intent(UserAddressAlterActivity.this, AddressAddActivity.class);
			startActivity(intent1);
			break;

		default:
			break;
		}
	}

}
