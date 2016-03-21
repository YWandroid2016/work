package com.example.happyfishing.view;

import com.example.happyfishing.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MBtn extends RelativeLayout {

	private Context context;
	private TextView tv;
	private TextView line;
	private boolean isShow; 

	@SuppressLint("NewApi")
	public MBtn(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public MBtn(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public MBtn(Context context) {
		super(context);
		initView(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	private void initView(Context context) {

		this.context = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.s_btn, this);

		tv = (TextView) view.findViewById(R.id.tv_text);
		line = (TextView) view.findViewById(R.id.tv_line);

		M_drawline();

	}

	public void setTv(String text) {
		tv.setText(text);
		M_drawline();
	}

	private void M_drawline() {
		tv.measure(tv.getWidth(), tv.getHeight());

		LayoutParams para = (LayoutParams) line.getLayoutParams();// 获取按钮的布局
		para.width = tv.getMeasuredWidth() + 20;// 修改宽度
		if(para.width>40){
			line.setLayoutParams(para); // 设置修改后的布局。
		} else {
			para.width = 0;
			line.setLayoutParams(para); // 设置修改后的布局。
		}
		
	}
	
	public boolean isShow(){
		return isShow;
	}

	public void dispalyLine(boolean visibility){
		if(visibility){
			line.setVisibility(View.VISIBLE);
		} else {
			line.setVisibility(View.INVISIBLE);
		}
	}
	
}
