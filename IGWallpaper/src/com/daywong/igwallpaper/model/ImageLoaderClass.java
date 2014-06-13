package com.daywong.igwallpaper.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;

import android.util.Log;



public class ImageLoaderClass implements Runnable {
	public interface ImageLoaderCallback {
		public void done();
		public void error(String msg);
	}
	private static final String TAG = "ImageLoaderClass";
	ImageLoaderCallback callback;
	ArrayList<String> mList;
	File mSaveTo;
	int done = 0;

	public ImageLoaderClass(ArrayList<String> list, File saveTo,
			ImageLoaderCallback c) {
		mList = list;
		mSaveTo = saveTo;
		callback = c;

	}

	@Override
	public void run() {
		BaseApplication.isAppFolder();
		InputStream input = null;
		for (int x = 0; x < mList.size(); x++) {
			try {
				URL url = new URL(mList.get(x));
				Log.d(TAG, " current Url is" + url.toString());
				input = url.openStream();
				OutputStream output = new FileOutputStream(new File(mSaveTo,
						"cache_" + x));
				try {
					byte[] buffer = new byte[128];
					int bytesRead = 0;
					while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
						output.write(buffer, 0, bytesRead);
					}
				} finally {
					output.close();
					callback.error("Error on #54");
				}
			} catch (IOException e) {
				e.printStackTrace();
				callback.error(e.toString());
			}
		}
		callback.done();
	}
}
