package com.example.happyfishing.adapter;


import java.util.ArrayList;

import com.example.happyfishing.R;
import com.example.happyfishing.entity.OrderDateEntity;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FishpitOrderDateAdapter extends BaseAdapter{

	
	private ArrayList<OrderDateEntity> arrayList;
	private LayoutInflater layoutInflater;
	
	public FishpitOrderDateAdapter(Context context) {
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		arrayList = new ArrayList<OrderDateEntity>();
	}
	
	public void add2Adapter(ArrayList<OrderDateEntity> arrayList) {
		this.arrayList =arrayList;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrayList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return arrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder_orderDate holder;
		if (convertView == null) {
			holder = new ViewHolder_orderDate();
			convertView = layoutInflater.inflate(R.layout.layoutinflater_orderdate, null);
			holder.tv_orderDate_date = (TextView) convertView.findViewById(R.id.tv_inflater_orderdate_date);
			holder.tv_orderDate_day = (TextView) convertView.findViewById(R.id.tv_inflater_orderdate_day);
			convertView.setTag(holder);
			if (position == 0) {
				convertView.setBackgroundColor(Color.YELLOW);
			}
		}else {
			holder=(ViewHolder_orderDate) convertView.getTag();
		}
		OrderDateEntity entity = arrayList.get(position);
		holder.tv_orderDate_date.setText(entity.date);
		holder.tv_orderDate_day.setText(entity.day);
		return convertView;
	}

	
	
	private static class ViewHolder_orderDate{
		TextView tv_orderDate_date;
		TextView tv_orderDate_day;
	}
}
