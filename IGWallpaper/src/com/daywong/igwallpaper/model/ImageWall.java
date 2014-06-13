package com.daywong.igwallpaper.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.util.Log;

public class ImageWall {
	private static final String TAG = "ImageLooper";
	private int bmpW;
	private int bmpH;

	private File[] mListImages;
	private List<List<Integer>> mYmatric;
	private List<Integer> mYmatricPixelIndicator;

	public ImageWall() {
		File f = new File(Environment.getExternalStorageDirectory()+ "/IGWallpaper/cache_data/h");
		mListImages = f.listFiles();
		mYmatric = new ArrayList<List<Integer>>();
		mYmatricPixelIndicator = new ArrayList<Integer>();
		if(BaseApplication.DEBUG)Log.d(TAG,"mListImages size="+mListImages.length);
	}

	public void drawPhotos(Canvas canvas) {
		Bitmap b = getRandomPic();
		int mScreenW = canvas.getWidth();
		int mScreenH = canvas.getHeight();
		int mPicW = b.getWidth();
		int mPicH = b.getHeight();
		int cursorX = 0;
		int cursorY = 0;
		for(int y = 0 ; y < mScreenH; y += mPicH){
			
			List<Integer> matric = new ArrayList<Integer>();
			for (int x = 0; x < mScreenW; x += mPicW) {
				canvas.drawBitmap(b, cursorX, cursorY, null);
				matric.add(cursorX);
				cursorX += mPicW;
				b = getRandomPic();
			}
			mYmatric.add(matric);
			mYmatricPixelIndicator.add(cursorY);
			cursorX = -(int)(Math.random()*mPicW);//Define Next X
			cursorY += mPicH;				
		}
	}

	private Bitmap getRandomPic() {
		Bitmap bitmap;
		int picCount = (int) (Math.random() * (mListImages.length - 1));
		bitmap = BitmapFactory.decodeFile(mListImages[picCount].getAbsolutePath());
		return bitmap;
	}

	public void onTouch(float x, float y,Canvas canvas) {
//		int x = 0;
//		int y = 0;
//		for (int i = mYmatricPixelIndicator.size()-1; i >= 0; i--) {
//			
//		}
		
		
		canvas.drawBitmap(getRandomPic(), 10,10, null);
	}

}
