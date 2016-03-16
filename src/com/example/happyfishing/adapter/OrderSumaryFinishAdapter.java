package com.example.happyfishing.adapter;

import java.util.ArrayList;

import com.example.happyfishing.R;
import com.example.happyfishing.entity.OrderEntity;
import com.example.happyfishing.image.ImageRequestView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
		final ViewHolder_Order holder;
		if (convertView == null) {
			holder = new ViewHolder_Order();
			convertView = layoutInflater.inflate(R.layout.inflater_ordersumary_adapter, null);
			holder.imageRequestView = (ImageRequestView) convertView.findViewById(R.id.img_ordersumary_adapter);
			holder.tv_order_merchantname = (TextView) convertView.findViewById(R.id.tv_ordersumary_merchantname);
			holder.tv_order_merchantposition = (TextView) convertView.findViewById(R.id.tv_ordersumary_merchantposition);
			holder.tv_order_price = (TextView) convertView.findViewById(R.id.tv_ordersumary_price);
			holder.tv_order_name = (TextView) convertView.findViewById(R.id.tv_ordersumary_name);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder_Order) convertView.getTag();
		}
		OrderEntity entity = arrayList.get(position);
		
		holder.imageRequestView.setImageUrl(entity.picUrl) ;
		holder.tv_order_merchantname.setText(entity.merchantName);
		holder.tv_order_merchantposition.setText(entity.location+"号坑");
		holder.tv_order_name.setText(entity.name);
		holder.tv_order_price.setText("￥:"+entity.totalFee+"元");
		return convertView;
	}
	
	private static class ViewHolder_Order {
		ImageRequestView imageRequestView;
		TextView tv_order_merchantname;
		TextView tv_order_merchantposition;
		TextView tv_order_price;
		TextView tv_order_name;
	} 

}
