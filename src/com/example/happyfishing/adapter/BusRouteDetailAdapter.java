package com.example.happyfishing.adapter;

import java.util.ArrayList;

import com.example.happyfishing.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BusRouteDetailAdapter extends BaseAdapter{

	private ArrayList<String> arrayList;
	private LayoutInflater layoutInflater;
	
	public BusRouteDetailAdapter (Context context){
		arrayList = new ArrayList<String>();
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void add2Adapter(ArrayList<String> arrayList) {
		this.arrayList=arrayList;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrayList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = layoutInflater.inflate(R.layout.layouinflater_busroute, null);
		TextView tv_busroute = (TextView) view.findViewById(R.id.tv_inflater_busroute);
		tv_busroute.setText(arrayList.get(position));
		return view;
	}

}
