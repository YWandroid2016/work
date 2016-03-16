package com.example.happyfishing.adapter;

import java.util.ArrayList;


import com.example.happyfishing.R;
import com.example.happyfishing.entity.FishpitSumaryEntity;
import com.example.happyfishing.image.ImageRequestGet;
import com.example.happyfishing.image.ImageRequestView;
import com.example.happyfishing.tool.HttpUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FishpitFishpitAdapter extends BaseAdapter{
	
	private ArrayList<FishpitSumaryEntity> arrayList ;
	private LayoutInflater layoutInflater;
	private ImageRequestView imageRequestView;
	
	public FishpitFishpitAdapter (Context context){
		this.arrayList=new ArrayList<FishpitSumaryEntity>();
		layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageRequestView=new ImageRequestView(context);
	}

	public void addToAdapter(ArrayList<FishpitSumaryEntity> arrayList,boolean flush) {
		if (flush) {
			this.arrayList.clear();
		}
		this.arrayList.addAll(arrayList);
	}
	
	public ArrayList<FishpitSumaryEntity> getArrayList() {
		return this.arrayList;
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
		final ViewHolder_FishPit holder;
		if (convertView==null) {
			holder = new ViewHolder_FishPit();
			convertView = layoutInflater.inflate(R.layout.layoutinflater_heikengshow, null);
			holder.img_fishpit_itemshow=(ImageRequestView) convertView.findViewById(R.id.img_fishpit_itemshow);
			holder.tv_fishpit_itemshow_title=(TextView) convertView.findViewById(R.id.tv_fishpit_itemshow_title);
			holder.tv_fishpit_itemshow_detail=(TextView) convertView.findViewById(R.id.tv_fishpit_itemshow_detail);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder_FishPit) convertView.getTag();
		}
		FishpitSumaryEntity entity = arrayList.get(position);
		if (entity.headImageURL==null) {
			holder.img_fishpit_itemshow.setImageResource(R.drawable.meinv);
		}else {
			holder.img_fishpit_itemshow.setImageUrl(entity.headImageURL);
		}
		holder.tv_fishpit_itemshow_title.setText(entity.name);
		holder.tv_fishpit_itemshow_detail.setText(entity.fishpitSumary);
		
		return convertView;
	}
	
	private static class ViewHolder_FishPit {

		ImageRequestView img_fishpit_itemshow;
		TextView tv_fishpit_itemshow_title;
		TextView tv_fishpit_itemshow_detail;
		
	}

}
