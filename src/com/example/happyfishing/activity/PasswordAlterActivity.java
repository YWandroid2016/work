package com.example.happyfishing.activity;

import java.util.HashMap;

import com.example.happyfishing.R;
import com.example.happyfishing.R.color;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class PasswordAlterActivity extends Activity implements OnClickListener, OnTouchListener {

	private ActionBarView actionBar_passwordalter;
//	private TextView tv_passwordalter_old;
	private EditText edt_passwordalter_old;
//	private TextView tv_passwordalter_new;
	private EditText edt_passwordalter_new;
//	private TextView tv_passwordalter_newconfirm;
	private EditText edt_passwordalter_newconfirm;
	private InputMethodManager inputManager;

	public static int ALTER_PASSWORDBYOLD = 6;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password_alter);

		Intent intent = getIntent();
		boolean create = intent.getBooleanExtra("password_create", false);
//		if (create) {
//			findViewById(R.id.ll_passwordalter_3).setVisibility(View.GONE);
//		}
		findViewById(R.id.btn_passwordalter).setOnClickListener(this);

		initView();

		loadData();

	}

	private void initActionbar() {
		actionBar_passwordalter = (ActionBarView) findViewById(R.id.actionBar_passwordalter);
		actionBar_passwordalter.setActionBar(R.string.back, -1, R.string.title_actionbar_update_passward, this);

	}

	private void initView() {
		findViewById(R.id.ll_passwordalter_parent).setOnTouchListener(this);
		initActionbar();

//		tv_passwordalter_old = (TextView) findViewById(R.id.tv_passwordalter_old);
/*		tv_passwordalter_old.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tv_passwordalter_old.setVisibility(View.INVISIBLE);
				edt_passwordalter_old.setVisibility(View.VISIBLE);
				edt_passwordalter_old.requestFocus();
				inputManager = (InputMethodManager) edt_passwordalter_old.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

				inputManager.showSoftInput(edt_passwordalter_old, 0);

				tv_passwordalter_new.setVisibility(View.VISIBLE);
				tv_passwordalter_newconfirm.setVisibility(View.VISIBLE);
				edt_passwordalter_new.setVisibility(View.INVISIBLE);
				edt_passwordalter_newconfirm.setVisibility(View.INVISIBLE);
				
				edt_passwordalter_old.setOnEditorActionListener(new OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_DONE) {
							// 点击按钮隐藏键盘
							inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
							edt_passwordalter_old.setVisibility(View.INVISIBLE);
							tv_passwordalter_old.setVisibility(View.VISIBLE);
							return true;
						}
						return false;
					}
				});
			}
		});*/

		edt_passwordalter_old = (EditText) findViewById(R.id.edt_passwordalter_old);
		
//		tv_passwordalter_new = (TextView) findViewById(R.id.tv_passwordalter_new);
//		tv_passwordalter_new.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				tv_passwordalter_new.setVisibility(View.INVISIBLE);
//				edt_passwordalter_new.setVisibility(View.VISIBLE);
//				edt_passwordalter_new.requestFocus();
//				inputManager = (InputMethodManager) edt_passwordalter_new.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//
//				inputManager.showSoftInput(edt_passwordalter_new, 0);
//
//				edt_passwordalter_old.setVisibility(View.INVISIBLE);
//				edt_passwordalter_newconfirm.setVisibility(View.INVISIBLE);
//				tv_passwordalter_newconfirm.setVisibility(View.VISIBLE);
//				tv_passwordalter_old.setVisibility(View.VISIBLE);
//				
//				edt_passwordalter_new.setOnEditorActionListener(new OnEditorActionListener() {
//
//					@Override
//					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//						if (actionId == EditorInfo.IME_ACTION_DONE) {
//							// 点击按钮隐藏键盘
//							inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
//							edt_passwordalter_new.setVisibility(View.INVISIBLE);
//							tv_passwordalter_new.setVisibility(View.VISIBLE);
//							return true;
//						}
//						return false;
//					}
//				});
//			}
//		});
		edt_passwordalter_new = (EditText) findViewById(R.id.edt_passwordalter_new);
//		edt_passwordalter_new.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before, int count) {
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//				tv_passwordalter_new.setText(edt_passwordalter_new.getText().toString());
//			}
//		});

//		tv_passwordalter_newconfirm = (TextView) findViewById(R.id.tv_passwordalter_newconfirm);
//		tv_passwordalter_newconfirm.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				tv_passwordalter_newconfirm.setVisibility(View.INVISIBLE);
//				edt_passwordalter_newconfirm.setVisibility(View.VISIBLE);
//				edt_passwordalter_newconfirm.requestFocus();
//				inputManager = (InputMethodManager) edt_passwordalter_newconfirm.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//
//				inputManager.showSoftInput(edt_passwordalter_newconfirm, 0);
//				edt_passwordalter_new.setVisibility(View.INVISIBLE);
//				edt_passwordalter_old.setVisibility(View.INVISIBLE);
//				tv_passwordalter_old.setVisibility(View.VISIBLE);
//				tv_passwordalter_new.setVisibility(View.VISIBLE);
//				edt_passwordalter_newconfirm.setOnEditorActionListener(new OnEditorActionListener() {
//
//					@Override
//					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//						if (actionId == EditorInfo.IME_ACTION_DONE) {
//							// 点击按钮隐藏键盘
//							inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
//							edt_passwordalter_newconfirm.setVisibility(View.INVISIBLE);
//							tv_passwordalter_newconfirm.setVisibility(View.VISIBLE);
//							return true;
//						}
//						return false;
//					}
//				});
//			}
//		});
		edt_passwordalter_newconfirm = (EditText) findViewById(R.id.edt_passwordalter_newconfirm);
//		edt_passwordalter_newconfirm.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before, int count) {
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//				tv_passwordalter_newconfirm.setText(edt_passwordalter_newconfirm.getText().toString());
//			}
//		});
//
	}

	private void loadData() {

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.password_alter, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_actionbar_left:
			PasswordAlterActivity.this.finish();
			break;
		case R.id.btn_passwordalter:
			
			String newPassword1 = edt_passwordalter_new.getText().toString();
			String newPassword2 = edt_passwordalter_newconfirm.getText().toString();
			
			if (!newPassword1.equals(newPassword2)) {
				Toast.makeText(PasswordAlterActivity.this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
				break;
			}else {
				String oldPassword = edt_passwordalter_old.getText().toString();
				HashMap<String, String> params = new HashMap<String, String>();
				SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
				String phoneNumber = sp.getString("phoneNumber", "");
				params.put("phoneNumber", phoneNumber);
				params.put("oldPassword", oldPassword);
				params.put("newPassword", newPassword1);
				HttpUtil.getJSON(HttpAddress.ADDRESS+HttpAddress.PROJECT+
						HttpAddress.CLASS_APPUSER+HttpAddress.METHOD_CHANGEPASSWORDBYOLD,
						params, 
						new HttpCallbackListener() {
					
					@Override
					public void onFinish(Object response) {
						Log.d("response", response.toString());
						Intent intent1 = new Intent(PasswordAlterActivity.this, OrderResultActivity.class);
						intent1.putExtra("type", ALTER_PASSWORDBYOLD);
						startActivity(intent1);
					}
					
					@Override
					public void onError(Exception e) {
						// TODO Auto-generated method stub
						
					}
				});
				
			}
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		/*v.setFocusable(true);
		v.setFocusableInTouchMode(true);
		v.requestFocus();
		edt_passwordalter_old.setVisibility(View.INVISIBLE);
		edt_passwordalter_new.setVisibility(View.INVISIBLE);
		edt_passwordalter_newconfirm.setVisibility(View.INVISIBLE);
		tv_passwordalter_new.setVisibility(View.VISIBLE);
		tv_passwordalter_newconfirm.setVisibility(View.VISIBLE);
		tv_passwordalter_old.setVisibility(View.VISIBLE);*/
		return false;
	}

}
