package com.example.happyfishing.activity;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.happyfishing.R;
import com.example.happyfishing.adapter.ImagePagerAdapter;
import com.example.happyfishing.bannerview.CircleFlowIndicator;
import com.example.happyfishing.bannerview.ViewFlow;
import com.example.happyfishing.tool.HttpAddress;
import com.example.happyfishing.tool.HttpCallbackListener;
import com.example.happyfishing.tool.HttpUtil;
import com.example.happyfishing.view.ActionBarView;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class CommodityDetailShowActivity extends Activity implements OnClickListener{
	private String idString;
	private ActionBarView actionbar_commoditydetail;
	private TextView tv_actionbar_title;
	private Handler mainHandler;
	private ViewFlow mViewFlow;
	private CircleFlowIndicator mFlowIndicator;
	private ArrayList<String> imageUrlList;
	private ArrayList<String> linkUrlArray;
	private TextView tv_commodity_detail_detail;
	private TextView tv_commoditydetail_totalCount;
	private TextView tv_commoditydetail_sallCount;
	
	private double target_lat;
	private double target_lon;
//	private double my_lat;
//	private double my_lon;
//	private String cityString;
	private String nameString;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commodity_detail_show);
		mainHandler= new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					Bundle bundle = msg.getData();
					String detail = bundle.getString("detail");
					String saleCount = bundle.getString("saleCount");
					String totalCount = bundle.getString("totalCount");
					String imageUrl = bundle.getString("imageUrl");
					
					String name = bundle.getString("name");
					tv_actionbar_title.setText(name);
					tv_commodity_detail_detail.setText(detail);
					tv_commoditydetail_totalCount.setText(totalCount);
					tv_commoditydetail_sallCount.setText(saleCount);
					String[] imageUrls = imageUrl.split(",");
					for (int i = 0; i < imageUrls.length; i++) {
						imageUrlList.add(imageUrls[i]);
						linkUrlArray.add(imageUrls[i]);
					}
					mViewFlow.setAdapter(new ImagePagerAdapter(CommodityDetailShowActivity.this, imageUrlList, linkUrlArray).setInfiniteLoop(true));
					mViewFlow.setmSideBuffer(imageUrlList.size()); // 实际图片张数，
					mViewFlow.setFlowIndicator(mFlowIndicator);
					mViewFlow.setTimeSpan(4500);
					mViewFlow.setSelection(imageUrlList.size() * 1000); // 设置初始位置
					mViewFlow.startAutoFlowTimer(); // 启动自动播放
					break;
				case 5:
					Toast.makeText(CommodityDetailShowActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
				super.handleMessage(msg);
			}
		};
		
		Intent intent = getIntent();
		idString = intent.getStringExtra("id");
		Bundle bundle = intent.getExtras();
		target_lat = bundle.getDouble("target_lat");
		target_lon = bundle.getDouble("target_lon");
//		my_lat = bundle.getDouble("my_lat");
//		my_lon = bundle.getDouble("my_lon");
//		cityString = bundle.getString("city");
		nameString = bundle.getString("nameString");
		initView();
		
		initBanner();

		loadData();
	}

	private void initBanner() {
		imageUrlList = new ArrayList<String>();
		linkUrlArray = new ArrayList<String>();
		
		
	}

	private void initView() {
		
		actionbar_commoditydetail = (ActionBarView) findViewById(R.id.actionbar_commoditydetail);
		actionbar_commoditydetail.setActionBar(R.string.back, -1, R.string.action_settings, this);
		findViewById(R.id.tv_commodity_detail_daodiangoumai).setOnClickListener(this);
		tv_actionbar_title = (TextView) findViewById(R.id.tv_actionbar_title);
		tv_commodity_detail_detail = (TextView) findViewById(R.id.tv_commodity_detail_detail);
		tv_commoditydetail_totalCount = (TextView) findViewById(R.id.tv_commodity_detail_totalcount);
		tv_commoditydetail_sallCount = (TextView) findViewById(R.id.tv_commodity_detail_sales);
		
		mViewFlow = (ViewFlow) findViewById(R.id.viewflow);
		mFlowIndicator = (CircleFlowIndicator) findViewById(R.id.viewflowindic);
	}

	private void loadData() {

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("productId", idString);
		HttpUtil.getJSON(HttpAddress.ADDRESS+HttpAddress.PROJECT+
				HttpAddress.CLASS_FISHPRODUCT+HttpAddress.METHOD_PRODUCTDETAIL, 
				params, 
				new HttpCallbackListener() {
					
					@Override
					public void onFinish(Object response) {
						Log.d("commodityDetailShow", response.toString());
						JSONObject jsonObject = (JSONObject) response;
						try {
							JSONObject jsonObject2 = jsonObject.getJSONObject("product");
							String detail = jsonObject2.getString("detail");
							String saleCount = jsonObject2.getString("saleCount");
							String totalCount = jsonObject2.getString("totalCount");
							String name = jsonObject2.getString("name");
							String imageUrl = jsonObject.getString("images");
							String[] imageUrls = imageUrl.split(",");
							Log.d("imageUrls", imageUrl+" "+imageUrls.length );
							Bundle bundle = new Bundle();
							bundle.putString("detail", detail);
							bundle.putString("saleCount", saleCount);
							bundle.putString("totalCount", totalCount);
							bundle.putString("name", name);
							bundle.putString("imageUrl", imageUrl);
							Message message = new Message();
							message.what=1;
							message.setData(bundle);
							mainHandler.sendMessage(message);
						} catch (JSONException e) {
							mainHandler.sendEmptyMessage(5);
							e.printStackTrace();
						}
						
					}
					
					@Override
					public void onError(Exception e) {
						// TODO Auto-generated method stub
						
					}
				});
	}
	
	private boolean isInstalled(String packageName){
		Intent manager = getPackageManager().getLaunchIntentForPackage(packageName);
		Log.d("intent", manager+"");
		Log.d("exists", new File("/data/data/" + packageName).exists()+"");
		return new File("/data/data/" + packageName).exists();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.commodity_detail_show, menu);
		return true;
	}
	
	private PopupWindow popupWindow;
	private TextView tv_gao;
	private TextView tv_bai;
	private TextView tv_cancle;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_actionbar_left:
			CommodityDetailShowActivity.this.finish();
			break;
		case R.id.tv_commodity_detail_daodiangoumai:
//			Intent intent = new Intent(CommodityDetailShowActivity.this, LocationShowActivity.class);
//			Bundle bundle1 = new Bundle();
//			bundle1.putDouble("target_lat", target_lat);
//			bundle1.putDouble("target_lon", target_lon);
//			bundle1.putDouble("my_lat", my_lat);
//			bundle1.putDouble("my_lon", my_lon);
//			bundle1.putString("city", cityString);
//			intent.putExtras(bundle1);
//			startActivity(intent);

			View view = LayoutInflater.from(CommodityDetailShowActivity.this).inflate( R.layout.inflater_popwindow_map, null);
			tv_gao = (TextView) view.findViewById(R.id.tv_gao);
			tv_bai = (TextView) view.findViewById(R.id.tv_bai);
			tv_cancle = (TextView) view.findViewById(R.id.tv_cencal);
			
			final PackageManager pm = CommodityDetailShowActivity.this.getPackageManager();
			popupWindow = new PopupWindow(view,LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT,true);
			popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
			
			OnClickListener onclick = new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					switch (arg0.getId()) {
					case R.id.tv_gao:
						if (isInstalled("com.autonavi.minimap")) {
							try {
								PackageInfo packageInfo =  pm.getPackageInfo("com.autonavi.minimap", 0);
								Log.d("packageInfo", packageInfo.packageName+"应用名字："+packageInfo.applicationInfo.loadLabel(pm).toString());
							} catch (NameNotFoundException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							try  
					        {  
					            Intent intent = Intent.getIntent("androidamap://viewMap?sourceApplication=自渔自乐&poiname="+nameString+"&lat="+target_lat+""+"&lon="+target_lon+""+"&dev=0");  
					            startActivity(intent);   
					        } catch (URISyntaxException e)  
					        {  
					            e.printStackTrace();  
					        }  
						}else 
						{
							String url = "http://shouji.baidu.com/soft/item?docid=8992640&from=web_alad_6";
							Uri uri = Uri.parse(url);
							Intent intent_down1 = new Intent(Intent.ACTION_VIEW, uri);
							startActivity(intent_down1);
						}
						break;
					case R.id.tv_bai:
						if (isInstalled("com.baidu.BaiduMap")) {
							try {
								PackageInfo packageInfo =  pm.getPackageInfo("com.baidu.BaiduMap", 0);
								Log.d("packageInfo", packageInfo.packageName+"应用名字："+packageInfo.applicationInfo.loadLabel(pm).toString());
							} catch (NameNotFoundException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							try {
								Intent intent = Intent.getIntent("intent://map/marker?location="+target_lat+""+","+target_lon+""+"&title="+nameString+"&content=百度奎科大厦&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
								startActivity(intent);
							} catch (URISyntaxException e1) {
								e1.printStackTrace();
							}
						}else {
							String url = "http://wap.amap.com/?type=bdpz01";
							Uri uri = Uri.parse(url);
							Intent intent_down1 = new Intent(Intent.ACTION_VIEW, uri);
							startActivity(intent_down1);
						}
						break;
					case R.id.tv_cencal:
						if(null != popupWindow && popupWindow.isShowing()){
							popupWindow.dismiss();
						}
						break;

					default:
						break;
					}
				}
			};
			
			tv_gao.setOnClickListener(onclick);
			tv_bai.setOnClickListener(onclick);
			tv_cancle.setOnClickListener(onclick);
			
			break;
		default:
			break;
		}
	}

}
