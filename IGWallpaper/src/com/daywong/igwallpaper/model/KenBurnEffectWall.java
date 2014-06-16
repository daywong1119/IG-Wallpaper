package com.daywong.igwallpaper.model;

import java.io.File;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import com.daywong.igwallpaper.Gvar;

public class KenBurnEffectWall {
	private static final String TAG = "KenBurnEffectWall";
	Bitmap bitmap;
	Context mContext ;
	
	//Define how many times for each photo
	int FRAME_FOR_PHOTO = 100;
	float MAX_SCALE = 1.5f;
	float mLastSX = 1;
	float mLastSY = 1;
	int counter = 1;
	public KenBurnEffectWall(Context c) {
		mContext = c;
		
	}

	public void drawPhotos(Canvas canvas) {
		String imaggPath = GetSettingFromSP(mContext, Gvar.SP_IMAGE);
		Log.d(TAG,TAG+" image path fomr SP value ="+imaggPath);
		
		File f = new File(imaggPath);
		if(f.exists())
			Log.d(TAG,TAG+" file exist");
		else
			Log.d(TAG,TAG+" file not exist");
		
		bitmap = GetImage(mContext, imaggPath);
		
		Matrix m = new Matrix();
		
		m.postScale(mLastSX, mLastSY);
		if(counter == FRAME_FOR_PHOTO){
			counter = 0;
			mLastSX = 1;
			mLastSY = 1;
		}else{
			counter ++;;
			mLastSX += MAX_SCALE/FRAME_FOR_PHOTO;
			mLastSY += MAX_SCALE/FRAME_FOR_PHOTO;
		}
		
		int aX = - Math.abs(((bitmap.getWidth()/2) - (canvas.getWidth()/2)));
		int aY = - (int) (counter*1.1);
		Bitmap scaleImage = Bitmap.createBitmap(bitmap, 0, 0,bitmap.getWidth(), bitmap.getHeight(), m, true);
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		
		canvas.drawBitmap(scaleImage, aX, aY, paint);
	}

	private String GetSettingFromSP(Context c, String key) {
		SharedPreferences settings = c.getSharedPreferences(
				Gvar.FLAG_SHARED_PREF, 0);
		return settings.getString(key, "");
	}

	private Bitmap GetImage(Context context, String filename) {
		// Try to get from SD
		String imageInSD = filename;
		File f = new File(imageInSD);
		if (!f.exists())
			return null;

		Options options = new BitmapFactory.Options();
		options.inScaled = false;
		return BitmapFactory.decodeFile(imageInSD, options);
	}
}
