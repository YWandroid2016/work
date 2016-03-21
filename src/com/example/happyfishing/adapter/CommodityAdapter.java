package com.example.happyfishing.adapter;

import java.util.ArrayList;

import com.example.happyfishing.R;
import com.example.happyfishing.entity.CommodityEntity;
import com.example.happyfishing.image.ImageRequestView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CommodityAdapter extends BaseAdapter{
	
	private ArrayList<CommodityEntity> arrayList = new ArrayList<CommodityEntity>();
	private LayoutInflater layoutInflater;
	public CommodityAdapter (Context context){
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		arrayList = new ArrayList<CommodityEntity>();
	}
	
	public void add2Adapter(ArrayList<CommodityEntity> arrayList) {
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
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder_Commodity holder;
		if (convertView ==null) {
			holder = new ViewHolder_Commodity();
			convertView = layoutInflater.inflate(R.layout.layoutinflater_commodity, null);
			holder.imageRequestView = (ImageRequestView) convertView.findViewById(R.id.img_commodity);
			holder.tv_commodity_sumary = (TextView) convertView.findViewById(R.id.tv_commodity_sumary);
			holder.tv_commodity_price = (TextView) convertView.findViewById(R.id.tv_commodity_price);
			holder.tv_commodity_sellNum = (TextView) convertView.findViewById(R.id.tv_commodity_sellNum);
			convertView.setTag(holder);
		}else {
			holder=(ViewHolder_Commodity) convertView.getTag();
		}
		CommodityEntity entity = arrayList.get(position);
		if (entity.imageURL != null) {
			holder.imageRequestView.setImageUrl(entity.imageURL);
		}else {
			holder.imageRequestView.setImageResource(R.drawable.ic_launcher);
		}
		holder.tv_commodity_sumary.setText(entity.commoditySumary);
		holder.tv_commodity_price.setText(entity.price);
		holder.tv_commodity_sellNum.setText(entity.sellNum);
		return convertView;
	}
	
	public static class ViewHolder_Commodity {
		ImageRequestView imageRequestView;
		TextView tv_commodity_sumary;
		TextView tv_commodity_price;
		TextView tv_commodity_sellNum;
	}

}
