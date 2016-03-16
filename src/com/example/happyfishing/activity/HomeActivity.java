package com.example.happyfishing.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.happyfishing.R;

import com.example.happyfishing.R.color;
import com.example.happyfishing.adapter.ImagePagerAdapter;
import com.example.happyfishing.bannerview.CircleFlowIndicator;
import com.example.happyfishing.bannerview.ViewFlow;
import com.example.happyfishing.tool.HttpAddress;
import com.example.happyfishing.tool.HttpCallbackListener;
import com.example.happyfishing.tool.HttpUtil;
import com.example.happyfishing.tool.UiUtil;
import com.example.happyfishing.view.ActionBarView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity implements OnClickListener {

	private ViewFlow mViewFlow;
	private CircleFlowIndicator mFlowIndicator;
	private ArrayList<String> imageUrlList = new ArrayList<String>();
	private ArrayList<String> linkUrlArray = new ArrayList<String>();
	// private ArrayList<String> titleList = new ArrayList<String>();
	private ActionBarView actionBar_home;
	private TextView tv_actionbar_right;
	private JSONObject jsonObject;
	private TextView tv_home_bottom_left;
	private TextView tv_home_bottom_right;
	private static boolean isExit = false;
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		handler = new Handler(HomeActivity.this.getMainLooper()) {
			@Override
			public void handleMessage(Message msg) {

				switch (msg.what) {
				case 1:
					Bundle bundle = msg.getData();
					tv_home_bottom_left.setText(bundle.getString("nickname"));
					tv_home_bottom_right.setText(bundle.getString("userLevel"));
					findViewById(R.id.ll_home_bottom).setClickable(false);
					break;
				case 2:
					tv_home_bottom_left.setText("登录/注册");
					tv_home_bottom_right.setText("\t\t 自渔自乐，不一样的钓鱼体验");
					findViewById(R.id.ll_home_bottom).setClickable(true);
					break;
				case 5:
					Toast.makeText(HomeActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
					break;
				case 111:
					isExit = false;
					break;
				default:
					break;
				}
				super.handleMessage(msg);
			}
		};

		initView();

		initBannerList();

		initBanner();

		checkUpdate();
	}

	private void checkUpdate() {
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification();

	}

	@Override
	protected void onStart() {
		tv_actionbar_right = (TextView) findViewById(R.id.tv_actionbar_right);
		Drawable drawable = null;
		Log.d("boolean", UiUtil.getNewMessage() + "");
		if (UiUtil.getNewMessage()) {
			drawable = getResources().getDrawable(R.drawable.ic_message_hasnew);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		} else {
			drawable = getResources().getDrawable(R.drawable.ic_message_nonew);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		}
		tv_actionbar_right.setCompoundDrawables(drawable, null, null, null);
		initLogin();
		super.onStart();
	}

	private void initLogin() {
		// 发送请求 验证登陆

		SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
		String token = sp.getString("token", "");
		if (token.equals("")) {
			handler.sendEmptyMessage(2);
		}else {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", token);
			HttpUtil.getJSON(HttpAddress.ADDRESS + HttpAddress.PROJECT + HttpAddress.CLASS_APPUSER + HttpAddress.METHOD_USERVERIFY, params, new HttpCallbackListener() {
				
				@Override
				public void onFinish(Object response) {
					jsonObject = (JSONObject) response;
					JSONObject jsonObject1 = null;
					try {
						jsonObject1 = jsonObject.getJSONObject("appUser");
						Log.d("successs", response.toString());
					} catch (JSONException e) {
						handler.sendEmptyMessage(5);
						e.printStackTrace();
					}
					try {
						Log.d("success", "会员到期时间" + jsonObject1.getString("outOfDate"));
						Log.d("success", "会员开通时间" + jsonObject1.getString("startOfDate"));
						Log.d("success", "昵称" + jsonObject1.getString("nickname"));
						Log.d("success", "用户头像地址" + jsonObject1.getString("headImageUrl"));
						Log.d("success", "用户的唯一标识" + jsonObject1.getString("token"));
						Log.d("success", "注册用的电话" + jsonObject1.getString("phoneNumber"));
						Log.d("success", "经验值" + jsonObject1.getString("userExp"));
						Log.d("success", "用户积分" + jsonObject1.getString("userPoint"));
						Log.d("success", "是否为会员" + jsonObject1.getString("isMember"));
						Log.d("success", "会员类别" + jsonObject1.getString("category"));
						Log.d("success", "用户等级" + jsonObject1.getString("userRank"));
						SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
						Editor editor = sp.edit();
						// 比较两次userpoint值 如果变化则修改message图标为带红点
						String userPoint_previous = sp.getString("userPoint", "");
						String userPoint_now = jsonObject1.getString("userPoint");
						long current = System.currentTimeMillis();
						String outOfDate = jsonObject1.getString("outOfDate");
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-hh");
						try {
							Date date = dateFormat.parse(outOfDate);
							long outOfDatejiange = current - date.getTime();
							Log.d("outOfDatejiange", outOfDatejiange + "");
							if (outOfDatejiange < 259200000) {
								Log.d("outOfDatejiange", outOfDatejiange + "");
								UiUtil.setNewMessage(true);
							}
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						if (userPoint_now.equals(userPoint_previous)) {
						} else {
							UiUtil.setNewMessage(true);
						}
						
						editor.putString("outOfDate", jsonObject1.getString("outOfDate"));
						editor.putString("startOfDate", jsonObject1.getString("startOfDate"));
						editor.putString("nickname", jsonObject1.getString("nickname"));
						editor.putString("headImageUrl", jsonObject1.getString("headImageUrl"));
						editor.putString("token", jsonObject1.getString("token"));
						editor.putString("phoneNumber", jsonObject1.getString("phoneNumber"));
						editor.putString("userExp", jsonObject1.getString("userExp"));
						editor.putString("userPoint", jsonObject1.getString("userPoint"));
						editor.putString("isMember", jsonObject1.getString("isMember"));
						editor.putString("category", jsonObject1.getString("category"));
						editor.putString("userRank", jsonObject1.getString("userRank"));
						editor.commit();
						
						Message message = new Message();
						message.what = 1;
						Bundle bundle = new Bundle();
						bundle.putString("nickname", jsonObject1.getString("nickname"));
						bundle.putString("userLevel", jsonObject1.getString("userRank"));
						message.setData(bundle);
						handler.sendMessage(message);
					} catch (JSONException e) {
						handler.sendEmptyMessage(5);
					}
				}
				
				@Override
				public void onError(Exception e) {
					SharedPreferences sp2 = getSharedPreferences("user", Context.MODE_PRIVATE);
					Editor editor = sp2.edit();
					editor.clear();
					editor.commit();
					handler.sendEmptyMessage(5);
				}
			});
			
		}
	}

	private void initView() {
		mViewFlow = (ViewFlow) findViewById(R.id.viewflow);
		mFlowIndicator = (CircleFlowIndicator) findViewById(R.id.viewflowindic);
		actionBar_home = (ActionBarView) findViewById(R.id.actionBar_home);
		actionBar_home.setActionBar(-1, R.string.action_settings, R.string.title_actionbar_home, null);
		actionBar_home.setBackgroundColor(getResources().getColor(R.color.actionbar_background));

		findViewById(R.id.tv_homeactivity_jump2heikeng).setOnClickListener(this);
		findViewById(R.id.tv_homeactivity_jump2fishshop).setOnClickListener(this);
		findViewById(R.id.tv_homeactivity_jump2mywallet).setOnClickListener(this);
		findViewById(R.id.tv_actionbar_right).setOnClickListener(this);
		findViewById(R.id.ll_home_bottom).setOnClickListener(this);

		tv_home_bottom_left = (TextView) findViewById(R.id.tv_home_bottom_left);
		tv_home_bottom_right = (TextView) findViewById(R.id.tv_home_bottom_right);
	}

	private void initBannerList() {
		// 发送请求获取图片地址和点击跳转的网址
		imageUrlList.add("http://b.hiphotos.baidu.com/image/pic/item/d01373f082025aaf95bdf7e4f8edab64034f1a15.jpg");
		imageUrlList.add("http://g.hiphotos.baidu.com/image/pic/item/6159252dd42a2834da6660c459b5c9ea14cebf39.jpg");
		imageUrlList.add("http://d.hiphotos.baidu.com/image/pic/item/adaf2edda3cc7cd976427f6c3901213fb80e911c.jpg");
		imageUrlList.add("http://g.hiphotos.baidu.com/image/pic/item/b3119313b07eca80131de3e6932397dda1448393.jpg");

		linkUrlArray.add("http://blog.csdn.net/finddreams/article/details/44301359");
		linkUrlArray.add("http://blog.csdn.net/finddreams/article/details/43486527");
		linkUrlArray.add("http://blog.csdn.net/finddreams/article/details/44648121");
		linkUrlArray.add("http://blog.csdn.net/finddreams/article/details/44619589");
	}

	private void initBanner() {

		mViewFlow.setAdapter(new ImagePagerAdapter(this, imageUrlList, linkUrlArray).setInfiniteLoop(true));
		mViewFlow.setmSideBuffer(imageUrlList.size()); // 实际图片张数，

		mViewFlow.setFlowIndicator(mFlowIndicator);
		mViewFlow.setTimeSpan(4500);
		mViewFlow.setSelection(imageUrlList.size() * 1000); // 设置初始位置
		mViewFlow.startAutoFlowTimer(); // 启动自动播放
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_homeactivity_jump2heikeng:
			Intent intent1 = new Intent(HomeActivity.this, FishpitActivity.class);
			startActivity(intent1);
			break;
		case R.id.tv_homeactivity_jump2fishshop:
			Intent intent3 = new Intent(HomeActivity.this, FishingShopActivity.class);
			startActivity(intent3);
			break;
		case R.id.tv_homeactivity_jump2mywallet:
			Intent intent4 = new Intent(HomeActivity.this, MyWalletActivity.class);
			startActivity(intent4);
			break;
		case R.id.tv_actionbar_right:
			SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
			String token = sp.getString("token", "");
			if (token.equals("")) {
				Toast.makeText(HomeActivity.this, "尚未登录，请登录", Toast.LENGTH_SHORT).show();
			}else {
				Intent intent2 = new Intent(HomeActivity.this, MessageActivity.class);
				startActivity(intent2);
			}
			break;
		case R.id.ll_home_bottom:
			Intent intent5 = new Intent(HomeActivity.this, LoginActivity.class);

			startActivity(intent5);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void exit() {
		if (!isExit) {
			isExit = true;
			Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
			// 利用handler延迟发送更改状态信息
			handler.sendEmptyMessageDelayed(111, 2000);
		} else {
			finish();
			System.exit(0);
		}
	}
}
