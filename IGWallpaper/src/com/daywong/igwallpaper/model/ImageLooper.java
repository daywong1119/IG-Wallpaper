package com.daywong.igwallpaper.model;

import java.io.File;
import java.util.ArrayList;

import com.daywong.igwallpaper.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class ImageLooper {
	private Context mContext;
	private BaseApplication app;
	private static final String TAG = "ImageLooper";
	private static final int MAX_PHOTO_LIMITE = 15;
	private int sWidth;
	private int sHeight;
	private int bmpW;
	private int bmpH;

	private final int INITIAL_POS;
	private int Ay = 0;
	private ArrayList<ArrayList<Bitmap>> matrix = new ArrayList<ArrayList<Bitmap>>();
	private ArrayList<Integer> mCurrentX = new ArrayList<Integer>();
	private ArrayList<Integer> mInitX = new ArrayList<Integer>();
	private ArrayList<Integer> mSpeed = new ArrayList<Integer>();
	private File[] images;

	public ImageLooper(int screenWidth, int screenHeight, Context c) {
		super();
		// TODO
		mContext = c;
		app = (BaseApplication) c;
		if (app.getPref_wallStyle().equalsIgnoreCase("largeimg")) {
			images = BaseApplication.CACHE_PATH_L.listFiles();
		} else {
			images = BaseApplication.CACHE_PATH_S.listFiles();
		}

		sWidth = screenWidth;
		sHeight = screenHeight;

		Bitmap initBmp = getRandomPic();
		bmpW = initBmp.getWidth();
		bmpH = initBmp.getHeight();
		INITIAL_POS = -bmpW;

		for (int j = 0; j < screenHeight; j += bmpH) {
			// create Horizontal line
			ArrayList<Bitmap> hori = new ArrayList<Bitmap>();
			for (int i = 0; i < sWidth; i += bmpW) {
				hori.add(getRandomPic());
			}
			mSpeed.add((int) (Math.random() * 3));

			int ajustment = -bmpW - (int) (Math.random() * bmpW);
			mInitX.add(ajustment);
			mCurrentX.add(ajustment);
			matrix.add(hori);
		}
	}

	public void drawPhotos(Canvas canvas) {
		// clear old screen
		boolean valid = true;
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);

		// loop vertical line
		for (int k = 0; k < matrix.size(); k++) {
			ArrayList<Bitmap> hori = matrix.get(k);
			int Ax = mCurrentX.get(k);
			// append the first rec
			if (Ax > mInitX.get(k) + bmpW) {
				hori.add(0, getRandomPic());
				Ax = mInitX.get(k);

				// delete the end
				if (bmpW * (hori.size() - 1) > sWidth) {
					hori.remove(hori.size() - 1);
					valid = false;
				}
				valid = false;
			}
			// loop horizontal image
			int TempX = Ax;
			for (int x = 0; x < hori.size(); x++) {
				Bitmap img = hori.get(x);
				if(img != null){
					try {
						canvas.drawBitmap(img, TempX, Ay, null);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					Log.d(TAG,"A specific bitmap taken from Matrix is null");
				}
				// define next photo's X
				TempX += bmpW;
			}
			// define Next Line
			Ay += bmpH;
			// define next move
			mCurrentX.set(k, mCurrentX.get(k) + 1);
			if (!valid) {
				// Save
				matrix.set(k, hori);
				mCurrentX.set(k, mInitX.get(k));
			}
		}
		Ay = 0;
	}

	private Bitmap getRandomPic() {
		Bitmap bitmap ;
		int picCount = (int) (Math.random() * (images.length - 1));
		try{
			bitmap = BitmapFactory.decodeFile(images[picCount].getAbsolutePath());
		}catch(Exception e){
			Paint p = new Paint();
			p.setTextSize(80);
			p.setColor(Color.WHITE);
			bitmap = Bitmap.createBitmap(400, 400,  Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(bitmap);
			canvas.drawText("Loading image", 0,canvas.getHeight() / 2 + 90, p);
		}
		return bitmap;
	}
}
