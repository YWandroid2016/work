package com.example.happyfishing.adapter;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.example.happyfishing.R;
import com.example.happyfishing.entity.OrderDateEntity;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FishpitOrderDateAdapter2 extends BaseAdapter{
	
	private LayoutInflater layoutInflater;
	private ArrayList<Long> list;
	
	SimpleDateFormat format_week = new SimpleDateFormat("EEEE");
	SimpleDateFormat format_date = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat format_date2 = new SimpleDateFormat("MM-dd");
	
	String weekString;
	String dateString;
	String dateString2;
	
	public FishpitOrderDateAdapter2(Context context) {
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		list = new ArrayList();
		Long currentTime = System.currentTimeMillis();
		list.add(currentTime);
		for (int i = 0; i < 4; i++) {
			currentTime = currentTime + 86400000 * i;
			list.add(currentTime);
		}
		
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder_orderDate holder;
		if (convertView == null) {
			holder = new ViewHolder_orderDate();
			convertView = layoutInflater.inflate(R.layout.item_yuyue_fishpit_date, null);
			holder.tv_orderDate_date = (TextView) convertView.findViewById(R.id.tv_item_yuyue_fishfit_date);
			convertView.setTag(holder);
			if (position == 0) {
				convertView.setBackgroundColor(Color.YELLOW);
			}
		}else {
			holder=(ViewHolder_orderDate) convertView.getTag();
		}
		
		Date date = new Date(list.get(position));
		
		weekString = format_week.format(date);
		dateString = format_date.format(date);
		dateString2 = format_date2.format(date);
		
		if(position == 0){
			holder.tv_orderDate_date.setText(dateString2+"\n("+"今天)");
		} else {
			holder.tv_orderDate_date.setText(dateString2+"\n("+weekString+")");
		}
		
		
		return convertView;
	}

	
	
	private static class ViewHolder_orderDate{
		TextView tv_orderDate_date;
	}
}
