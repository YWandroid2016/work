package com.example.happyfishing.adapter;

import java.util.ArrayList;

import com.example.happyfishing.R;
import com.example.happyfishing.entity.OrderEntity;
import com.example.happyfishing.image.ImageRequestView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class OrderSumaryFinishAdapter extends BaseAdapter{

	private ArrayList<OrderEntity> arrayList;
	private LayoutInflater layoutInflater;
	public OrderSumaryFinishAdapter(Context context){
		arrayList = new ArrayList<OrderEntity>();
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}
	public void add2Adapter(ArrayList<OrderEntity> arrayList) {
		this.arrayList = arrayList;
	}
	
	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}
	
	@Override
	public int getItemViewType(int position) {
		if (arrayList.get(position).orderType.equals("member")) {
			Log.d("type0", arrayList.get(position).merchantId);
			return 0;
		}else if (arrayList.get(position).orderType.equals("fish")) {
			return 1;
		}else {
			return -1;
		}
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
		ViewHolder_Order2 holder2 = null;
		int type = getItemViewType(position);
		if (convertView == null) {
			switch (type) {
			case 1:
				holder = new ViewHolder_Order();
				convertView = layoutInflater.inflate(R.layout.inflater_ordersumary_adapter, null);
				holder.imageRequestView = (ImageRequestView) convertView.findViewById(R.id.img_ordersumary_adapter);
				holder.tv_order_merchantname = (TextView) convertView.findViewById(R.id.tv_ordersumary_merchantname);
				holder.tv_order_merchantposition = (TextView) convertView.findViewById(R.id.tv_ordersumary_merchantposition);
				holder.tv_order_price = (TextView) convertView.findViewById(R.id.tv_ordersumary_price);
				holder.tv_order_name = (TextView) convertView.findViewById(R.id.tv_ordersumary_name);
				convertView.setTag(holder);
				break;
			case 0:
				holder2 = new ViewHolder_Order2();
				convertView = layoutInflater.inflate(R.layout.inflater_ordersumary_adapter2, null);
				holder2.img_memberOrder = (ImageView) convertView.findViewById(R.id.img_ordersumary_adapter2);
				holder2.tv_order_merchantname = (TextView) convertView.findViewById(R.id.tv_ordersumary_name2);
				holder2.tv_member_validTime = (TextView) convertView.findViewById(R.id.tv_ordersumary_merchantname2);
				holder2.tv_order_price = (TextView) convertView.findViewById(R.id.tv_ordersumary_price2);
				convertView.setTag(holder2);
				break;
			default:
				break;
			}
			
		}else {
			switch (type) {
			case 0:
				holder2 = (ViewHolder_Order2) convertView.getTag();
				break;
			case 1:
				holder = (ViewHolder_Order) convertView.getTag();
				break;
			default:
				break;
			}
		}
		switch (type) {
		case 0:
			OrderEntity entity2 = arrayList.get(position);
			holder2.img_memberOrder.setImageResource(R.drawable.ic_launcher);
			holder2.tv_order_merchantname.setText(entity2.name);
			holder2.tv_member_validTime.setText("我是有效期");
			holder2.tv_order_price.setText("￥"+entity2.totalFee+"元");
			break;
		case 1:
			OrderEntity entity = arrayList.get(position);
			holder.imageRequestView.setImageUrl(entity.picUrl) ;
			holder.tv_order_merchantname.setText(entity.merchantName);
			holder.tv_order_merchantposition.setText(entity.location+"号坑");
			holder.tv_order_name.setText(entity.name);
			holder.tv_order_price.setText("￥:"+entity.totalFee+"元");
			break;

		default:
			break;
		}
		
		return convertView;
	}
	
	private static class ViewHolder_Order {
		ImageRequestView imageRequestView;
		TextView tv_order_merchantname;
		TextView tv_order_merchantposition;
		TextView tv_order_price;
		TextView tv_order_name;
	} 
	
	private static class ViewHolder_Order2{
		ImageView img_memberOrder;
		TextView tv_order_merchantname;
		TextView tv_member_validTime;
		TextView tv_order_price;
	}

}
