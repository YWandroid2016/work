package com.example.happyfishing.image;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

// 核心操作类
public class ImageRequestCore {
	private Context context;
	private static ExecutorService threadPool = Executors.newFixedThreadPool(3);
	private ImageRequestTask imageRequestTask;

	private ImageRequestCore(Context context) {
		this.context = context;
	}

	private static ImageRequestCore requestCore;

	public static ImageRequestCore getInstance(Context context) {
		if (requestCore == null) {
			synchronized (ImageRequestCore.class) {
				if (requestCore == null) {
					requestCore = new ImageRequestCore(context);
				}
			}
		}
		return requestCore;
	}

	/** 请求图像数据 */
	public void requestImage(ImageRequestTask imageRequestTask) {
		threadPool.execute(imageRequestTask);
	}

	/** 请求图像数据 */
	public void requestImage(String url, ImageCompleteHandle completeHandle) {
		if (imageRequestTask != null) {
			imageRequestTask.cancel();
			imageRequestTask = null;
		}
		imageRequestTask = new ImageRequestTask(context, url, completeHandle);
		threadPool.execute(imageRequestTask);
	}

	/** 取消所有请求任务 */
	public static void cancelAllRequestTasks() {
		threadPool.shutdown();
		threadPool = null;
		threadPool = Executors.newFixedThreadPool(3);
		System.gc();
		Thread.yield();
	}

	public static abstract class ImageCompleteHandle extends Handler {
		public ImageCompleteHandle(Context context) {
			super(context.getApplicationContext().getMainLooper());
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				Bitmap bitmap = (Bitmap) msg.obj;
				onSucceed(bitmap);
				break;
			default:
				onFail();
				break;
			}
		}

		/** 当此任务成功获取图像将来回调的方法 */
		public abstract void onSucceed(Bitmap bitmap);

		/** 当此任务获取图像失败将来回调的方法 */
		public abstract void onFail();
	}
}
