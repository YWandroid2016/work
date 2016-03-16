package com.example.happyfishing.image;

import java.io.BufferedOutputStream;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.happyfishing.tool.JavaHttpUtil;
import com.example.happyfishing.tool.StreamUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.LruCache;
//import android.support.v4.util.LruCache;



/**
 * 图像请求获取及缓存处理类
 * 
 * @todo 本类核心为 {@link #getBitmap()}
 * 
 * @author yuanc
 */
public class ImageRequestGet {

	/** (static;共享唯一)用来做图像内存缓存 */
	private static LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(4 * 1024 * 1024) {
		@Override
		protected int sizeOf(String key, Bitmap value) {
			return value.getRowBytes() * value.getHeight();
		};
	};
	/** (static;共享唯一)用来做图像本地磁盘缓存时使用的线程池 (图像从网络获取到后将保存到本地目录内) */
	private static ExecutorService writerThread = Executors.newSingleThreadExecutor();
	/** (static;共享唯一)图像本地磁盘缓存时的目录路径 */
	private static String diskCachePath;
	/** (static;共享唯一)图像本地磁盘缓存时的目录路径 */
	private static boolean diskCacheEnabled;

	private String url;// 当前获取的图像的url
	private String cacheKey;// 当前获取的图像的缓存(L1,L2)KEY - 根据url获得的
	private boolean cancel; // 当前获取图像操作是否取消

	protected ImageRequestGet(Context context, String url) {
		this.url = url;
		cacheKey = getCacheKey(url);
		createDiskDir(context);
	}

	// 取消获取图像操作
	protected void cancel() {
		this.cancel = true;
	}

	// 创建本地磁盘缓存目录
	private void createDiskDir(Context context) {
		// 创建图像本地磁盘缓存时的目录
		if (diskCachePath == null || diskCachePath.equals("")) {
			String cacheDir = context.getCacheDir().getAbsolutePath();
			diskCachePath = cacheDir + "/" + "CacheImage";
			File cacheFileDir = new File(diskCachePath);
			if (!cacheFileDir.exists()) {
				cacheFileDir.mkdirs();
			}
			// 检测图像本地磁盘缓存时的目录是否存在
			diskCacheEnabled = cacheFileDir.exists();
			if (!diskCacheEnabled) {
				diskCachePath = null;
			}
		}
	}

	// 1 内存获取{@link #getBitmapFromMemory(String)
	// 2 本地获取{@link #getBitmapFromDisk(String)
	// 3 网络获取{@link #getBitmapFromUrl(String)
	/** 获取图像(根据RUL) */
	protected Bitmap getBitmap(int bitmapMaxWidth, int bitmapMaxHeight) {
		if (cancel) {
			return null;
		}
		// 1 内存获取
		Bitmap bitmap = getBitmapFromMemory(cacheKey);
		// 2 本地获取
		if (bitmap == null) {
			bitmap = getBitmapFromDisk(cacheKey);
		}
		// 3 网络获取
		if (bitmap == null) {
			bitmap = getBitmapFromUrl(url, bitmapMaxWidth, bitmapMaxHeight);
		}
		return bitmap;
	}

	// 获取图像 - 从本地磁盘缓存L2
	private Bitmap getBitmapFromDisk(String cacheKey) {
		if (cancel) {
			return null;
		}
		Bitmap bitmap = null;
		File file = new File(diskCachePath, cacheKey);
		if (file.exists()) {
			bitmap = BitmapFactory.decodeFile(file.getPath());
			if (bitmap != null) {
				saveToMemory(cacheKey, bitmap);
			}
		}
		return bitmap;
	}

	// 获取图像 - 从内存缓存L1
	private Bitmap getBitmapFromMemory(String cacheKey) {
		if (cancel) {
			return null;
		}
		Bitmap bitmap = lruCache.get(cacheKey);
		return bitmap;
	}

	// 获取图像 - 从网络获取
	private Bitmap getBitmapFromUrl(String url, int bitmapMaxWidth, int bitmapMaxHeight) {
		if (cancel) {
			return null;
		}
		InputStream inputStream = JavaHttpUtil.httpGet(url, null);
		if (inputStream != null) {
			Bitmap bitmap = StreamUtil.stream2Bitmap(inputStream, bitmapMaxWidth, bitmapMaxHeight);
			if (bitmap != null) {
				saveToDisk(cacheKey, bitmap);
				saveToMemory(cacheKey, bitmap);
				return bitmap;
			}
		}
		return null;
	}

	private void saveToMemory(String cacheKey, Bitmap bitmap) {
		lruCache.put(cacheKey, bitmap);
	}

	private void saveToDisk(final String cacheKey, final Bitmap bitmap) {
		if (!diskCacheEnabled) {
			return;
		}
		writerThread.execute(new Runnable() {
			@Override
			public void run() {
				if (cancel) {
					return;
				}
				FileOutputStream fos = null;
				BufferedOutputStream bos = null;
				try {
					File file = new File(diskCachePath, cacheKey);
					fos = new FileOutputStream(file);
					bos = new BufferedOutputStream(fos);
					bitmap.compress(CompressFormat.PNG, 90, bos);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						bos.flush();
						fos.flush();
						bos.close();
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	// 通过url取出key,去除.:等，替换成字符串+表示
	private static String getCacheKey(String url) {
		if (url == null) {
			throw new RuntimeException("Null url Exception");
		} else {
			return url.replaceAll("[.:/,%?&=]", "+").replaceAll("[+]+", "+");
		}
	}

}