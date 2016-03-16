package com.example.happyfishing.adapter;

import java.util.ArrayList;

import com.example.happyfishing.R;
import com.example.happyfishing.entity.BillEntity;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyBillAdapter extends BaseAdapter {

	private static int TYPE_ADD=0;
	private static int TYPE_REDUCE=1;
	
	private LayoutInflater layoutInflater;
	private ArrayList<BillEntity> arrayList;
	public MyBillAdapter(Context context) {
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		arrayList = new ArrayList<BillEntity>();
	}
	
	public void add2Adapter(ArrayList<BillEntity> arrayList) {
		this.arrayList = arrayList;
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
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder_Bill holder;
		if (convertView == null) {
			holder = new ViewHolder_Bill();
			convertView = layoutInflater.inflate(R.layout.layoutinflater_billshow, null);
			holder.tv_billshow_billdetail = (TextView) convertView.findViewById(R.id.tv_billshow_billdetail);
			holder.tv_billshow_billdate=(TextView) convertView.findViewById(R.id.tv_billshow_billdate);
			holder.tv_billshow_billnumber =(TextView) convertView.findViewById(R.id.tv_billshow_billnumber);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder_Bill) convertView.getTag();
		}
		BillEntity billEntity = arrayList.get(position);
		holder.tv_billshow_billdetail.setText(billEntity.name);
		holder.tv_billshow_billdate.setText(billEntity.date);
		
		if (billEntity.jifenType == TYPE_ADD) {
			holder.tv_billshow_billnumber.setTextColor(Color.BLUE);
			holder.tv_billshow_billnumber.setText("+"+billEntity.pointUse);
		}else {
			holder.tv_billshow_billnumber.setText("-"+billEntity.pointUse);
			holder.tv_billshow_billnumber.setTextColor(Color.RED);
		}
		return convertView;
	}
	
	private static class ViewHolder_Bill {
		private TextView tv_billshow_billdetail;
		private TextView tv_billshow_billdate;
		private TextView tv_billshow_billnumber;
	}

}
