package com.example.happyfishing.adapter;

import com.example.happyfishing.R;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class FishPositionGroupNumAdapter extends BaseAdapter{

	private LayoutInflater layoutInflater;
	private int groupNum;
	public FishPositionGroupNumAdapter (Context context, int groupNum){
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.groupNum = groupNum;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return groupNum;
	}
	
	public void setGroupNum(int groupNum) {
		this.groupNum = groupNum;
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
		if (convertView == null) {
			convertView=layoutInflater.inflate(R.layout.layoutinflater_fishposition_groupnum, null);
			TextView tv_groupNum = (TextView) convertView.findViewById(R.id.btn_groupnum);
			tv_groupNum.setText(50*position+1+"-"+50*(position+1));
			if (position == 0) {
				convertView.setBackgroundColor(Color.YELLOW);
			}
		}
		
		return convertView;
	}

}
