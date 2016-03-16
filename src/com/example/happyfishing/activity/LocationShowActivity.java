package com.example.happyfishing.activity;

import java.util.ArrayList;
import java.util.List;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.overlay.BusRouteOverlay;
import com.amap.api.maps2d.overlay.DrivingRouteOverlay;
import com.amap.api.maps2d.overlay.WalkRouteOverlay;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.BusStep;
import com.amap.api.services.route.Doorway;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteBusLineItem;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.RouteSearch.BusRouteQuery;
import com.amap.api.services.route.RouteSearch.DriveRouteQuery;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.RouteSearch.WalkRouteQuery;
import com.amap.api.services.route.WalkRouteResult;
import com.amap.api.services.route.RouteSearch.FromAndTo;
import com.example.happyfishing.R;
import com.example.happyfishing.adapter.BusRouteAdapter;
import com.example.happyfishing.adapter.BusRouteDetailAdapter;
import com.example.happyfishing.mapManager.Locating;
import com.example.happyfishing.view.ActionBarView;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

public class LocationShowActivity extends Activity implements OnClickListener{
	
	private MapView mapView;
	private Locating locating;
	private int mode;
	private Marker marker;
	private MarkerOptions markerOptions;
	private double target_latitude;
	private double target_longitude;
	private double my_latitude;
	private double my_longitude;
	private String cityName;
	private static int busMode = 1;
	private static int driveMode = 2;
	private static int walkMode = 3;
	private LatLng myLatLng;
	private LatLng targetLatLng;
	private AMap aMap;
	private WalkRouteResult walkRouteResult;
	private BusRouteResult busRouteResult;
	private DriveRouteResult driveRouteResult;
	private RouteSearch routeSearch;
	private FromAndTo ft;
	private HorizontalScrollView scrollView;
	private ListView liv_bustoute;
	private BusPath busPath;
	private RouteBusLineItem item;
	private List<BusStep> busSteps;
	private List<BusPath> busPaths;
	private BusRouteDetailAdapter adapter_itemdetail;
	private ActionBarView actionBar_location;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loacation_show);
		mapView = (MapView) findViewById(R.id.mav_locationshow);
		mapView.onCreate(savedInstanceState);
		
		aMap = mapView.getMap();
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		target_latitude = bundle.getDouble("target_lat");
		target_longitude = bundle.getDouble("target_lon");
		my_latitude = bundle.getDouble("my_lat");
		my_longitude = bundle.getDouble("my_lon");
		cityName = bundle.getString("city");
		
		initView();
		
		loadData();
		
		
		
	}

	
	@Override
	protected void onStart() {
		
		super.onStart();
	}

	private void initView() {
		
		actionBar_location = (ActionBarView) findViewById(R.id.actionBar_loacationshow);
		actionBar_location.setActionBar(R.string.back, -1, R.string.title_actionbar_address, this);
		scrollView = (HorizontalScrollView) findViewById(R.id.hsv_busroute);
		liv_bustoute = (ListView) findViewById(R.id.liv_busroute_show);
	}

	private void loadData() {
//		locating = Locating.getInstance(LocationShowActivity.this);
//		locating.locate();
		myLatLng = new LatLng(my_latitude, my_longitude);
		aMap.addMarker(new MarkerOptions().position(myLatLng).
				title("您的位置"));
		targetLatLng = new LatLng(target_latitude, target_longitude);
		aMap.addMarker(new MarkerOptions().position(targetLatLng).title("目的地"));
		
		LatLonPoint from = new LatLonPoint(my_latitude, my_longitude);
		LatLonPoint to = new LatLonPoint(target_latitude, target_longitude);
		searchRoute(from,to);
//		markerOptions = new MarkerOptions();
//		marker = aMap.addMarker(markerOptions);
//		marker.setPositionByPixels(400, 400);
//		marker.setVisible(true);
	}

	
	private void searchRoute(LatLonPoint from, LatLonPoint to) {
		ft = new FromAndTo(from,to);
		routeSearch = new RouteSearch(LocationShowActivity.this);
		routeSearch.setRouteSearchListener(onRouteSearchListener);
//		if (mode == driveMode) {
//			// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
//			DriveRouteQuery query = new DriveRouteQuery(ft, driveMode, null, null, "");
//			routeSearch.calculateDriveRouteAsyn(query);
//		}
//		if (mode == busMode) {
//			// 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
//			BusRouteQuery query = new BusRouteQuery(ft, busMode, cityName, 0);
//			routeSearch.calculateBusRouteAsyn(query);
//		}
//		if (mode == walkMode) {
//			// 第一个参数表示路径规划的起点和终点，第二个参数表示步行查询模式
//			WalkRouteQuery query = new WalkRouteQuery(ft, walkMode);
//			routeSearch.calculateWalkRouteAsyn(query);
//		}
	}


	public void button_drive(View view) {
		DriveRouteQuery query = new DriveRouteQuery(ft, driveMode, null, null, "");
		routeSearch.calculateDriveRouteAsyn(query);
	}
	public void button_bus(View view) {
		BusRouteQuery query = new BusRouteQuery(ft, busMode, cityName, 0);
		routeSearch.calculateBusRouteAsyn(query);
	}
	public void button_walk(View view) {
		WalkRouteQuery query = new WalkRouteQuery(ft, walkMode);
		routeSearch.calculateWalkRouteAsyn(query);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.loacation_show, menu);
		return true;
	}
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		mapView.onDestroy();
		super.onDestroy();
	}
	
	public OnRouteSearchListener onRouteSearchListener = new OnRouteSearchListener() {
		@Override
		public void onWalkRouteSearched(WalkRouteResult result, int rCode) {
			if (rCode == 0) {
				if (result != null && result.getPaths() != null && result.getPaths().size() > 0) {
					
					scrollView.setVisibility(View.INVISIBLE);
					mapView.setVisibility(View.VISIBLE);
					liv_bustoute.setVisibility(View.INVISIBLE);
					walkRouteResult = result;
					aMap.clear();
					List<WalkPath> walkPaths = walkRouteResult.getPaths();
					WalkPath path = walkPaths.get(0);
					WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(LocationShowActivity.this, aMap, path, walkRouteResult.getStartPos(), walkRouteResult.getTargetPos());
					walkRouteOverlay.removeFromMap();
					walkRouteOverlay.addToMap();
					walkRouteOverlay.zoomToSpan();
				}
			} else {
				Toast.makeText(LocationShowActivity.this, "搜索出现异常，请重试 ", Toast.LENGTH_SHORT).show();

			}
		}
		
		@Override
		public void onDriveRouteSearched(DriveRouteResult result, int rCode) {
			if (rCode == 0) {
				if (result != null && result.getPaths() != null && result.getPaths().size() > 0) {
					scrollView.setVisibility(View.INVISIBLE);
					mapView.setVisibility(View.VISIBLE);
					liv_bustoute.setVisibility(View.INVISIBLE);
					
					driveRouteResult = result;
					DrivePath drivePath = driveRouteResult.getPaths().get(0);
					aMap.clear();
					DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(LocationShowActivity.this, aMap, drivePath, driveRouteResult.getStartPos(), driveRouteResult.getTargetPos());
					drivingRouteOverlay.removeFromMap();
					drivingRouteOverlay.addToMap();
					drivingRouteOverlay.zoomToSpan();
				}
			}
			else {
				Toast.makeText(LocationShowActivity.this, "搜索出现异常，请重试 ", Toast.LENGTH_SHORT).show();

			}
		}
		
		@Override
		public void onBusRouteSearched(BusRouteResult result, int rCode) {
			if (rCode == 0) {
				if (result != null && result.getPaths() != null && result.getPaths().size() > 0) {
					busRouteResult = result;
					busPaths = busRouteResult.getPaths();
//					List<BusStep> busSteps = busPaths.get(0).getSteps();
//					Log.d("busStep", busSteps.get(0).getEntrance()+"");
					
					
					scrollView.setVisibility(View.VISIBLE);
					mapView.setVisibility(View.INVISIBLE);
					liv_bustoute.setVisibility(View.VISIBLE);
					GridView grv_busroute = (GridView) findViewById(R.id.grv_busroute_show);
					int groupNum = busPaths.size();
					grv_busroute.setAdapter(new BusRouteAdapter(LocationShowActivity.this,groupNum ));
					DisplayMetrics dm = new DisplayMetrics();
					getWindowManager().getDefaultDisplay().getMetrics(dm);
					float density = dm.density;
					// 设置positionGroupGridView在界面显示时的属性
					int allWidth2 = (int) (90 * groupNum * density);
					int itemSize2 = (int) (75 * density);
					LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(allWidth2, LinearLayout.LayoutParams.MATCH_PARENT);
					grv_busroute.setLayoutParams(params2);
					grv_busroute.setColumnWidth(itemSize2);
					grv_busroute.setHorizontalSpacing(10);
					grv_busroute.setStretchMode(GridView.NO_STRETCH);
					grv_busroute.setNumColumns(groupNum);
					grv_busroute.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							busPath = busPaths.get(position);
							Log.d("detail", "第"+position+"条方案"+busPath.getDistance()+"米");
							busSteps = busPath.getSteps();
							ArrayList<String> itemDetail = new ArrayList<String>();
							for (int j = 0; j < busSteps.size(); j++) {
								item = busSteps.get(j).getBusLine();
								if (item != null) {
									String itemString = "乘坐"+item.getBusLineName()+
											" 从"+item.getDepartureBusStation().getBusStationName()+
											" 上车 "+ " 经过"+ item.getPassStationNum()+" 站 ,在"
											+item.getArrivalBusStation().getBusStationName()+" 下车";
									Log.d("itemDetail", itemString);
									itemDetail.add(itemString);
								}
							}
							adapter_itemdetail = new BusRouteDetailAdapter(LocationShowActivity.this);
							adapter_itemdetail.add2Adapter(itemDetail);
							liv_bustoute.setAdapter(adapter_itemdetail);
						}
					});
					
					
					for (int i = 0; i < busPaths.size(); i++) {
						
					}
//					RouteBusLineItem item = busSteps.get(0).getBusLine();
//					String startStationName = item.getDepartureBusStation().getBusStationName();
//					String endStationName = item.getArrivalBusStation().getBusStationName();
//					Log.d("detail", item.getPassStationNum()+""+item.getDepartureBusStation().getBusStationName()+item.getArrivalBusStation().getBusStationName()+"");
//					aMap.clear();
//					BusRouteOverlay busRouteOverlay = new BusRouteOverlay(LocationShowActivity.this, aMap, busPath, busRouteResult.getStartPos(), busRouteResult.getTargetPos());
//					busRouteOverlay.removeFromMap();
//					busRouteOverlay.addToMap();
//					busRouteOverlay.zoomToSpan();
				}
			}
			else {
				Toast.makeText(LocationShowActivity.this, "搜索出现异常，请重试 ", Toast.LENGTH_SHORT).show();
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_actionbar_left:
			LocationShowActivity.this.finish();
			break;
		default:
			break;
		}
	}

}
