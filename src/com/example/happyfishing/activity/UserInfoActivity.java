package com.example.happyfishing.activity;

import com.example.happyfishing.R;

import com.example.happyfishing.photo.Bimp;
import com.example.happyfishing.photo.ImageItem;
import com.example.happyfishing.tool.FileUtils;
import com.example.happyfishing.view.ActionBarView;

import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.input.InputManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class UserInfoActivity extends Activity implements OnClickListener {

	
	private GridView noScrollgridview;
	private View parentView;
	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	public static Bitmap bimap;
	private ImageView img_usericon;
	private InputMethodManager inputManager;
	
	private TextView tv_userinfo_nickname;
	private TextView tv_userinfo_phonenumber;
	private ActionBarView actionBar_userinfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		parentView = getLayoutInflater().inflate(R.layout.activity_user_info, null);
		setContentView(parentView);

		initView();

		loadData();

	}

	private void initView() {
		actionBar_userinfo = (ActionBarView) findViewById(R.id.actionBar_userinfo);
		actionBar_userinfo.setActionBar(R.string.back, -1, R.string.title_actionbar_userinfo, this);
		
		findViewById(R.id.userinfo_usernickname).setOnClickListener(this);
		findViewById(R.id.userinfo_userphone).setOnClickListener(this);
		SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
		
		String nickName = sp.getString("nickname", "获取失败，请重试");
		String phoneNumber = sp.getString("phoneNumber", "获取失败，请重试");
		
		tv_userinfo_nickname = (TextView) findViewById(R.id.tv_userinfo_nickname);
		tv_userinfo_nickname.setText(nickName);
		
		tv_userinfo_phonenumber = (TextView) findViewById(R.id.tv_userinfo_phone);
		
		tv_userinfo_phonenumber.setText(phoneNumber);
	}

	private void loadData() {

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_info, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.userinfo_usernickname:
			Intent intent2 = new Intent(UserInfoActivity.this, UserInfoAlter_NickName_Activity.class);
			startActivity(intent2);
			break;
		case R.id.userinfo_userphone:
			Intent intent1 = new Intent(UserInfoActivity.this, UserInfoAlterActivity.class);
			intent1.putExtra("phone", true);
			startActivity(intent1);
			break;
		case R.id.tv_actionbar_left:
			UserInfoActivity.this.finish();
			break;
		default:
			break;
		}
	}

	private void initChangeUserPhone() {
		
	}
	// 用户更换昵称
	private void initChangeUserNickName() {
		
//		inputManager = (InputMethodManager) edt_userinfo_nickname.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//		edt_userinfo_nickname.setOnEditorActionListener(new OnEditorActionListener() {
//
//			@Override
//			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//				if (actionId == EditorInfo.IME_ACTION_DONE) {
//					inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
//					Toast.makeText(UserInfoActivity.this, "aaa", Toast.LENGTH_SHORT).show();
//					edt_userinfo_nickname.setVisibility(View.INVISIBLE);
//					tv_userinfo_nickname.setVisibility(View.VISIBLE);
//					tv_userinfo_nickname.setText(edt_userinfo_nickname.getText().toString());
//					return true;
//				}
//				return false;
//			}
//		});
//		tv_userinfo_nickname.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				tv_userinfo_nickname.setVisibility(View.INVISIBLE);
//				edt_userinfo_nickname.setVisibility(View.VISIBLE);
//				edt_userinfo_nickname.requestFocus();
//				inputManager = (InputMethodManager) edt_userinfo_nickname.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//				inputManager.showSoftInput(edt_userinfo_nickname, 0);
//			}
//		});
	}

	// 用户更换头像
	private void initChangeUserIcon() {
		pop = new PopupWindow(UserInfoActivity.this);
		View view = getLayoutInflater().inflate(R.layout.item_popupwindows_usericon, null);
		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup_usericon);
		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);

		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent_usericon);
		Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
		parent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				photo();
				pop.dismiss();
				ll_popup.clearAnimation();
			}

			private void photo() {
				Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(openCameraIntent, TAKE_PICTURE);
			}
		});
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(UserInfoActivity.this, AlbumActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});

	}

	private static final int TAKE_PICTURE = 0x000001;

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (Bimp.tempSelectBitmap != null && resultCode == RESULT_OK) {

				String fileName = String.valueOf(System.currentTimeMillis());
				Bitmap bm = (Bitmap) data.getExtras().get("data");
				FileUtils.saveBitmap(bm, fileName);
				img_usericon.setImageBitmap(bm);
			}
			break;
		}
	}

}
