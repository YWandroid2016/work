package com.example.happyfishing.image;

import com.example.happyfishing.R;
import com.example.happyfishing.image.ImageRequestCore.ImageCompleteHandle;

import android.annotation.SuppressLint;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;


public class ImageRequestView extends ImageView {
	/** 当前图像加载线程(注意非静态,每张图像一个线程任务),防止重复开启线程 */
	private ImageRequestTask imageRequestTask;
	// 加载中和加载失败的图像
	private static Drawable loadDrawable, failDrawable;

	public ImageRequestView(Context context) {
		super(context);
		loadDrawable = context.getResources().getDrawable(R.drawable.load_loading);
		failDrawable = context.getResources().getDrawable(R.drawable.load_failure);
	}

	public ImageRequestView(Context context, AttributeSet attrs) {
		super(context, attrs);
		loadDrawable = context.getResources().getDrawable(R.drawable.load_loading);
		failDrawable = context.getResources().getDrawable(R.drawable.load_failure);
	}

	public void setImageUrl(String url) {
		// 当前ImageView先显示加载中的图像(再做图像请求,如下)
		setImageDrawable(loadDrawable);
		// 图像请求核心操作
		ImageRequestCore imageRequestCore = ImageRequestCore.getInstance(getContext());
		// 每张ImageView图像一个线程任务
		if (imageRequestTask != null) {
			imageRequestTask.cancel();
			imageRequestTask = null;
		}
		imageRequestTask = new ImageRequestTask(getContext(), url, new MyCompleteHandle());
		// 图像请求
		imageRequestCore.requestImage(imageRequestTask);
	}

	/** 当前图像请求处理完成的Handle */
	@SuppressLint("HandlerLeak")
	private class MyCompleteHandle extends ImageCompleteHandle {
		public MyCompleteHandle() {
			super(getContext());
		}

		@Override
		public void onSucceed(Bitmap bitmap) {
			setImageBitmap(bitmap);
		}

		@Override
		public void onFail() {
			setImageDrawable(failDrawable);
		}
	}

	public void setSucceedDrawable(Drawable succeedDrawable) {
		ImageRequestView.loadDrawable = succeedDrawable;
	}

	public void setFailDrawable(Drawable failDrawable) {
		ImageRequestView.failDrawable = failDrawable;
	}
}
