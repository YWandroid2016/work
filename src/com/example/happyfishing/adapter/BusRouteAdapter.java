package com.example.happyfishing.adapter;

import java.util.ArrayList;

import com.example.happyfishing.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class BusRouteAdapter extends BaseAdapter{

	private int busRouteNum;
	private LayoutInflater layoutInflater;
	public BusRouteAdapter (Context context , int busRouteNum){
		this.busRouteNum = busRouteNum;
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return busRouteNum;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = layoutInflater.inflate(R.layout.layouinflater_busroute, null);
		TextView tv_busroute = (TextView) view.findViewById(R.id.tv_inflater_busroute);
		tv_busroute.setText("方案 "+(position+1)+" :");
		return view;
	}

}
