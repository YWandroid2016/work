package com.example.happyfishing.view;
import com.example.happyfishing.R;


import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;


public class ActionBarView extends FrameLayout {
	private TextView  tv_actionbar_left;
	private TextView  tv_actionbar_right;
	private TextView  txt_title;
	private View view;

	public ActionBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		view = inflate(context, R.layout.actionbarview, null);
		View tmpView = view.findViewById(R.id.tv_actionbar_left);
		if (tmpView instanceof TextView) {
			tv_actionbar_left = (TextView) tmpView;
		}
		tmpView = view.findViewById(R.id.tv_actionbar_right);
		if (tmpView instanceof TextView) {
			tv_actionbar_right = (TextView) tmpView;
		}
		tmpView = view.findViewById(R.id.tv_actionbar_title);
		if (tmpView instanceof TextView) {
			txt_title = (TextView) tmpView;
		}
		addView(view);
	}
	

	public void setActionBar(int leftResId, int rightResId, int titleResId, OnClickListener clickListener) {
		
		if (leftResId == -1) {
			tv_actionbar_left.setVisibility(View.INVISIBLE);
		}
		if (rightResId == -1) {
			tv_actionbar_right.setVisibility(View.INVISIBLE);
		}
		if (titleResId == -1) {
			txt_title.setVisibility(View.INVISIBLE);
		}
		if (leftResId != -1) {
			
			tv_actionbar_left.setText(leftResId);
			tv_actionbar_left.setOnClickListener(clickListener);
		}
		if (rightResId != -1) {
			tv_actionbar_right.setText(rightResId);
			tv_actionbar_right.setOnClickListener(clickListener);
		}
		if (titleResId != -1) {
			txt_title.setText(titleResId);
			txt_title.setOnClickListener(clickListener);
		}
	}
	
	public void setActionBar(int leftResId, int rightResId, int titleResId, int rz, OnClickListener clickListener) {
		
		if (leftResId == -1) {
			tv_actionbar_left.setVisibility(View.INVISIBLE);
		}
		if (rightResId == -1) {
			tv_actionbar_right.setVisibility(View.INVISIBLE);
		}
		if (titleResId == -1) {
			txt_title.setVisibility(View.INVISIBLE);
		}
		if (leftResId != -1) {
			
			tv_actionbar_left.setText(leftResId);
			tv_actionbar_left.setOnClickListener(clickListener);
		}
		if (rightResId != -1 && rz != -1) {
			tv_actionbar_right.setText(rightResId);
			tv_actionbar_right.setTextColor(Color.WHITE);
			tv_actionbar_right.setCompoundDrawables(null, null, null, null);
			tv_actionbar_right.setOnClickListener(clickListener);
		}
		if (titleResId != -1) {
			txt_title.setText(titleResId);
			txt_title.setOnClickListener(clickListener);
		}
	}
}