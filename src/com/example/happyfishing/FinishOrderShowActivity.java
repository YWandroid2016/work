package com.example.happyfishing;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class FinishOrderShowActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finish_order_show);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.finish_order_show, menu);
		return true;
	}

}
