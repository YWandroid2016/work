package com.example.happyfishing.adapter;

import java.util.ArrayList;

import com.example.happyfishing.R;
import com.example.happyfishing.entity.FishshopEntity;
import com.example.happyfishing.image.ImageRequestView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FishshopAdapter extends BaseAdapter{
	private ArrayList<FishshopEntity> arrayList ;
	private LayoutInflater layoutInflater;
	
	public FishshopAdapter (Context context){
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		arrayList = new ArrayList<FishshopEntity>();
	}
	
	public void add2Adapter(ArrayList<FishshopEntity> arrayList) {
		Log.d("debug", this.arrayList.size()+"");
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
		final ViewHolder_fishshop holder;
		if (convertView == null) {
			holder = new ViewHolder_fishshop();
			convertView = layoutInflater.inflate(R.layout.layoutinflater_fishshopshow, null);
			holder.img_fishshop_itemshow=(ImageRequestView) convertView.findViewById(R.id.img_fishshop_itemshow);
			holder.tv_fishshop_itemshow_title=(TextView) convertView.findViewById(R.id.tv_fishshop_itemshow_title);
			holder.tv_fishshop_itemshow_detail=(TextView) convertView.findViewById(R.id.tv_fishshop_itemshow_detail);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder_fishshop) convertView.getTag();
		}
		FishshopEntity entity = arrayList.get(position);
		if (entity.imgURL==null) {
			holder.img_fishshop_itemshow.setImageResource(R.drawable.meinv);
		}else {
			holder.img_fishshop_itemshow.setImageUrl(entity.imgURL);
		}
		holder.tv_fishshop_itemshow_title.setText(entity.fishshopName);
		holder.tv_fishshop_itemshow_detail.setText(entity.fishshopDetail);
		return convertView;
	}
	
	private static class ViewHolder_fishshop{
		ImageRequestView img_fishshop_itemshow;
		TextView tv_fishshop_itemshow_title;
		TextView tv_fishshop_itemshow_detail;
	}

}
