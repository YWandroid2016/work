package com.example.happyfishing.adapter;

import java.util.List;

import com.example.happyfishing.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageAdapter extends BaseAdapter{

	private List<?> list;
	private LayoutInflater inflater;
	
	public MessageAdapter(Context context, List<?> list) {
		super();
		this.list = list;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return 6;
//		return list.size();
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
		ViewHolder vh = null;
		if(null == convertView){
			vh = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_message, null);
			vh.img = (ImageView) convertView.findViewById(R.id.img_message);
			vh.name = (TextView) convertView.findViewById(R.id.name_message);
			vh.date = (TextView) convertView.findViewById(R.id.date_message);
			vh.des = (TextView) convertView.findViewById(R.id.des_message);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}

	
	class ViewHolder{
		ImageView img;
		TextView name;
		TextView date;
		TextView des;
		
	}
}
