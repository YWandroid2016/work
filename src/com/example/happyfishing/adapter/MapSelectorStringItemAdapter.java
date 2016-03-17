package com.example.happyfishing.adapter;

import java.util.ArrayList;

import com.example.happyfishing.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MapSelectorStringItemAdapter extends BaseAdapter{

	
	private ArrayList<String> arrayList;
	private LayoutInflater layoutInflater;
	
	public MapSelectorStringItemAdapter (Context context){
		arrayList = new ArrayList<String>();
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void add2Adapter(ArrayList<String> arrayList) {
		this.arrayList = arrayList;
	}
	
	@Override
	public int getCount() {
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
	public View getView(int position, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.inflater_mapselector_string_item, null);
		}
		TextView textView = (TextView) convertView.findViewById(R.id.tv_mapselector);
		textView.setText(arrayList.get(position));
		return convertView;
	}

}
