package com.example.happyfishing.adapter;

import java.util.ArrayList;

import com.example.happyfishing.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FishPositionShowAdapter extends BaseAdapter{
	private int positionTotal;
	private LayoutInflater layoutInflater;
	private int fishPositionNum ;
	private ArrayList<Integer> orderedLocation;
	private int currentOrdered;
	public FishPositionShowAdapter (Context context , int positionTotal , int fishPositionNum , String []orderedPosition){
		orderedLocation = new ArrayList<Integer>();
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.positionTotal=positionTotal;
		this.fishPositionNum =fishPositionNum;
		if (orderedPosition.length>=0) {
			for (int i = 0; i < orderedPosition.length; i++) {
				orderedLocation.add(Integer.parseInt(orderedPosition[i]));
			}
		}
	}
	
	public void setCurrentOrder(int currentOrder) {
		this.currentOrdered = currentOrder;
	}
	
	public int getCurrentOrder(){
		return this.currentOrdered;
	}

	@Override
	public int getCount() {
		return positionTotal;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
			View view = layoutInflater.inflate(R.layout.layoutinflater_fishposition_show, null);
			view.setTag(true);
			TextView tv_fishposition_show = (TextView) view.findViewById(R.id.tv_layoutinflater_fishposition_show);
			tv_fishposition_show.setText((28*fishPositionNum+(position+1))+"");
			for (int i = 0; i < orderedLocation.size(); i++) {
				if ((28*fishPositionNum+position+1) == orderedLocation.get(i)) {
					tv_fishposition_show.setTextColor(Color.WHITE);
					tv_fishposition_show.setBackgroundResource(R.drawable.bgd_diaowei_noselected);
					view.setClickable(false);
					view.setTag(false);
				}
			}
			
		return view;
	}

}
