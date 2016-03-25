package com.example.happyfishing.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.happyfishing.R;
import com.example.happyfishing.activity.BecomeVipOrderActivity;
import com.example.happyfishing.activity.OrderInformationActivity;
import com.example.happyfishing.activity.UpgradeVIPActivity;
import com.example.happyfishing.entity.DingEntity;
import com.example.happyfishing.entity.OrderEntity;
import com.example.happyfishing.image.ImageRequestView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class OrderSumaryFinishAdapter extends BaseAdapter {
	private static final int FISH = 1;
	private static final int WAIT = 2;
	private static final int AFTER = 3;
	private static final int ALL = 4;
	private List<DingEntity> arrayList;
	private LayoutInflater layoutInflater;
	private int payType;
	private Context context;

	public OrderSumaryFinishAdapter(Context context) {
		arrayList = new ArrayList<DingEntity>();
		layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
	}

	public void add2Adapter(List<DingEntity> arrayList, int payType) {
		this.arrayList = arrayList;
		this.payType = payType;
	}

	@Override
	public int getCount() {
		return arrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return arrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder_Order holder = null;
		if (convertView == null) {
			holder = new ViewHolder_Order();
			convertView = layoutInflater.inflate(
					R.layout.inflater_ordersumary_adapter, null);
			holder.imageRequestView = (ImageRequestView) convertView
					.findViewById(R.id.img_ordersumary_adapter);
			holder.tv_order_merchanttype = (TextView) convertView
					.findViewById(R.id.tv_ordersumary_type);
			holder.tv_order_merchantname = (TextView) convertView
					.findViewById(R.id.tv_ordersumary_name);
			holder.tv_order_status = (TextView) convertView
					.findViewById(R.id.tv_ordersumary_status);
			holder.tv_order_price = (TextView) convertView
					.findViewById(R.id.tv_ordersumary_moeny);
			holder.tv_order_date = (TextView) convertView
					.findViewById(R.id.tv_ordersumary_date);
			holder.tv_order_pay = (TextView) convertView
					.findViewById(R.id.tv_ordersumary_pay);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder_Order) convertView.getTag();
		}
		final DingEntity entity = arrayList.get(position);
		String imgUrl = entity.getPicUrl();
		if(null == imgUrl || "".equals(imgUrl)){
			holder.imageRequestView.setImageResource(R.drawable.ic_launcher);
			
		} else {
			holder.imageRequestView.setImageUrl(entity.getPicUrl());
			
		}
		holder.tv_order_merchantname.setText(entity.getMerchantName());
		String type = entity.getType();
		if ("member".equals(type)) {
			holder.tv_order_merchanttype.setText("购买会员");
			String category = entity.getCategory();
			if ("month".equals(category)) {
				category = "月度会员";
			} else if ("season".equals(category)) {
				category = "季度会员";
			} else if ("year".equals(category)) {
				category = "年度会员";
			}
			holder.tv_order_date.setText("会员类型：" + category);

			holder.tv_order_pay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 会员支付订单
					
//					SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//					long currentTime2 = System.currentTimeMillis();
//					Date date2 = new Date(currentTime2);
//					String dateCreate2 = dateFormat2.format(date2);
//					Log.d("deatCreate", dateCreate2);
//					Bundle bundle2 = new Bundle();
//					bundle2.putString("dateCreate", dateCreate2);
//					Intent intent1 = new Intent(context, BecomeVipOrderActivity.class);
//					intent1.putExtras(bundle);
//					intent1.putExtra("money", 500);
//					intent1.putExtra("type", 1);
//					startActivity(intent1);
					Toast.makeText(context, "vip暂未开通，尽请期待。。。", Toast.LENGTH_SHORT).show();
					
				}
			});

		} else {
			holder.tv_order_merchanttype.setText("预约钓位");
			holder.tv_order_date.setText("有效期：" + entity.getReserveTime());

			holder.tv_order_pay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 鱼坑支付订单	UpgradeVIPActivity
					
					
					SharedPreferences sp = context.getSharedPreferences("user",
							Context.MODE_PRIVATE);

					String token = sp.getString("token", "");
					String orderId = entity.getCode();
					String merchantId = ""+entity.getMerchantId();
					String userjifen = sp.getString("userPoint", "");
					Long userPoint = Long.parseLong(userjifen);
					String name = entity.getMerchantName();
					String date = entity.getReserveTime();
					String location = ""+entity.getLocation();
					int orderPosition = Integer.parseInt(location);
					String phoneNumber = sp.getString("phoneNumber", "");
					Intent intent1 = new Intent(context,
							OrderInformationActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("token", token);
					bundle.putString("orderId", orderId);
					bundle.putString("merchantId", merchantId);
					bundle.putString("date", date);
					bundle.putInt("location", orderPosition);
					bundle.putString("name", name);
					bundle.putString("phone", phoneNumber);
					bundle.putLong("userPoint", userPoint);
					intent1.putExtras(bundle);
					context.startActivity(intent1);
				}
			});

		}
		holder.tv_order_price.setText("￥" + entity.getTotalFee());
		holder.tv_order_pay.setVisibility(View.GONE);
		switch (payType) {
		case FISH:
			holder.tv_order_status.setText("可使用");
			break;
		case WAIT:
			holder.tv_order_status.setText("待支付");
			holder.tv_order_pay.setVisibility(View.VISIBLE);
			break;
		case AFTER:
			holder.tv_order_status.setText("已过期");
			break;
		case ALL:

			String str = entity.getStrStatus();

			if ("waitCash".equals(str) || "waitUse".equals(str)) {
				holder.tv_order_status.setText("可使用");

			} else if ("waitPay".equals(str)) {
				holder.tv_order_status.setText("待支付");
				holder.tv_order_pay.setVisibility(View.VISIBLE);

			} else if ("expiredCash".equals(str) || "expiredUse".equals(str)) {
				holder.tv_order_status.setText("已过期");

			} else if ("finished".equals(str)) {
				holder.tv_order_status.setText("已完成");
			}
			break;

		default:
			break;
		}

		return convertView;
	}

	private static class ViewHolder_Order {
		ImageRequestView imageRequestView;
		TextView tv_order_merchantname;
		TextView tv_order_merchanttype;
		TextView tv_order_status;
		TextView tv_order_price;
		TextView tv_order_date;
		TextView tv_order_pay;
	}

}
