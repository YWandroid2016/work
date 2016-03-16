package com.example.happyfishing.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.happyfishing.entity.FishpitSumaryEntity;

import android.content.Context;
import android.widget.ArrayAdapter;

public class ACTVAdapter<T> extends ArrayAdapter<T>{



	public ACTVAdapter(Context context, int textViewResourceId, List<T> objects) {
		super(context, textViewResourceId, objects);
		arrayList = new ArrayList<String>();
	}

	private ArrayList<String> arrayList;
	
	
	public void add2Adapter(ArrayList<String> arrayList) {
		this.arrayList = arrayList;
	}

	
}
