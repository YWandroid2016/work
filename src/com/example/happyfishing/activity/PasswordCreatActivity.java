package com.example.happyfishing.activity;

import com.example.happyfishing.R;
import com.example.happyfishing.R.color;
import com.example.happyfishing.R.layout;
import com.example.happyfishing.R.menu;
import com.example.happyfishing.view.ActionBarView;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class PasswordCreatActivity extends Activity implements OnClickListener,OnTouchListener{
	
	private ActionBarView actionBar_PasswordCreat;
	private Button btn_password_creat_verfication;
	private Button btn_password_creat;
	private TextView tv_passwordcreat_phone1;
	private TextView tv_passwordcreat_verification1;
	private EditText edt_passwordcreat_phone1;
	private EditText edt_passwordcreat_verification1;
	private InputMethodManager inputManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password_creat);
		
		findViewById(R.id.btn_passwordcreate).setOnClickListener(this);
		
		
		
		initView();
		
		loadData();
		
	}

	private void initActionbar() {
		actionBar_PasswordCreat = (ActionBarView) findViewById(R.id.actionBar_passwordCreate);
		Intent intent = getIntent();
		int key = intent.getIntExtra("type", 0);
		switch (key) {
		case 2:
			actionBar_PasswordCreat.setActionBar(-1, -1, R.string.title_actionbar_zhaohui, this);
			findViewById(R.id.tv_passwordcreat_phone).setVisibility(View.GONE);
			findViewById(R.id.tv_passwordcreat_verification).setVisibility(View.GONE);
			
			btn_password_creat_verfication = (Button) findViewById(R.id.btn_passwordcreate_verfication);
			btn_password_creat_verfication.setText("发送验证码");
			btn_password_creat = (Button) findViewById(R.id.btn_passwordcreate);
			btn_password_creat.setText("下一步");
			btn_password_creat.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent1 = new Intent(PasswordCreatActivity.this, PasswordAlterActivity.class);
					intent1.putExtra("password_create", true);
					startActivity(intent1);
				}
			});
			break;

		default:
			break;
		}
	}

	private void initView() {
		findViewById(R.id.ll_passwordcreatparent).setOnTouchListener(this);
		initActionbar();
		
		tv_passwordcreat_phone1 = (TextView) findViewById(R.id.tv_passwordcreat_phone1);
		tv_passwordcreat_phone1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tv_passwordcreat_phone1.setVisibility(View.INVISIBLE);
				edt_passwordcreat_phone1.setVisibility(View.VISIBLE);
				edt_passwordcreat_phone1.requestFocus();
				inputManager = (InputMethodManager) edt_passwordcreat_phone1.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

				inputManager.showSoftInput(edt_passwordcreat_phone1, 0);	
				tv_passwordcreat_verification1.setVisibility(View.VISIBLE);
				edt_passwordcreat_verification1.setVisibility(View.INVISIBLE);
				edt_passwordcreat_phone1.setOnEditorActionListener(new OnEditorActionListener() {
					
					@Override
					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_DONE) {
//							点击按钮隐藏键盘
							inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
							edt_passwordcreat_phone1.setVisibility(View.INVISIBLE);
							tv_passwordcreat_phone1.setVisibility(View.VISIBLE);
							return true;
						}
						return false;
					}
				});
			}
		});
		edt_passwordcreat_phone1=(EditText) findViewById(R.id.edt_passwordcreat_phone1);
		edt_passwordcreat_phone1.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				tv_passwordcreat_phone1.setText(edt_passwordcreat_phone1.getText().toString());
			}
		});
		
		
		tv_passwordcreat_verification1=(TextView) findViewById(R.id.tv_passwordcreat_verification1);
		tv_passwordcreat_verification1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tv_passwordcreat_verification1.setVisibility(View.INVISIBLE);
				edt_passwordcreat_verification1.setVisibility(View.VISIBLE);
				edt_passwordcreat_verification1.requestFocus();
				inputManager = (InputMethodManager) edt_passwordcreat_verification1.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

				inputManager.showSoftInput(edt_passwordcreat_verification1, 0);	
				tv_passwordcreat_phone1.setVisibility(View.VISIBLE);
				edt_passwordcreat_phone1.setVisibility(View.INVISIBLE);
				edt_passwordcreat_verification1.setOnEditorActionListener(new OnEditorActionListener() {
					
					@Override
					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_DONE) {
//							点击按钮隐藏键盘
							inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
							edt_passwordcreat_verification1.setVisibility(View.INVISIBLE);
							tv_passwordcreat_verification1.setVisibility(View.VISIBLE);
							return true;
						}
						return false;
					}
				});
			}
		});
		edt_passwordcreat_verification1=(EditText) findViewById(R.id.edt_passwordcreat_verification1);
		edt_passwordcreat_verification1.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				tv_passwordcreat_verification1.setText
				(edt_passwordcreat_verification1.getText().toString());
			}
		});
	}

	private void loadData() {
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.password_creat_activity, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.btn_passwordcreate:
//			Intent intent1 = new Intent(PasswordCreatActivity.this, PasswordAlterActivity.class);
//			intent1.putExtra("password_create", true);
//			startActivity(intent1);
//			break;
		default:
			break;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		v.setFocusable(true);
		v.setFocusableInTouchMode(true);
		v.requestFocus();
		edt_passwordcreat_phone1.setVisibility(View.INVISIBLE);
		tv_passwordcreat_phone1.setVisibility(View.VISIBLE);
		edt_passwordcreat_verification1.setVisibility(View.INVISIBLE);
		tv_passwordcreat_verification1.setVisibility(View.VISIBLE);
		return false;
	}

}
