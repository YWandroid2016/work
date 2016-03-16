package com.example.happyfishing.image;

import com.example.happyfishing.image.ImageRequestCore.ImageCompleteHandle;
import com.example.happyfishing.tool.PublicUtils;

import android.content.Context;

import android.graphics.Bitmap;
import android.os.Message;



/**
 * 图像请求处理任务类
 * 
 * @todo 主要是在此新线程中去获取图像 {@link ImageRequestGet#getBitmap()}
 * @todo 每张图像是一个新任务, 一个CacheImage对象去获取图像
 * 
 * @author yuanc
 */
public class ImageRequestTask implements Runnable {
	private ImageRequestGet imageRequestGetAndCache;
	private ImageCompleteHandle completeHandle;
	private boolean cancel;
	private int imageWidth;
	private int imageHeight;

	protected ImageRequestTask(Context context, String url, ImageCompleteHandle completeHandle) {
		this.completeHandle = completeHandle;
		cancel = false;
		imageRequestGetAndCache = new ImageRequestGet(context, url);
		setImageMaxWidth(PublicUtils.screenWidth(context));
		setImageMaxheight(PublicUtils.screenWidth(context));
	}

	public void setImageMaxWidth(int width) {
		imageWidth = width;
	}

	public void setImageMaxheight(int height) {
		imageHeight = height;
	}

	protected void cancel() {
		this.cancel = true;
		if (imageRequestGetAndCache != null) {
			imageRequestGetAndCache.cancel();
		}
	}

	@Override
	public void run() {
		// 在此新线程内去获取图像(内存,本地,网络)
		if (!cancel && imageRequestGetAndCache != null) {
			Bitmap bitmap = imageRequestGetAndCache.getBitmap(imageWidth, imageHeight);
			if (bitmap != null) {
				Message msg = completeHandle.obtainMessage();
				msg.what = 1;
				msg.obj = bitmap;
				completeHandle.sendMessage(msg);
			} else {
				Message msg = completeHandle.obtainMessage();
				msg.what = 2;
				msg.obj = null;
				completeHandle.sendMessage(msg);
			}
		}
	}
}
