package com.daywong.igwallpaper;

import java.io.File;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.preference.PreferenceActivity;
import android.support.v4.util.LruCache;
import android.util.Log;

public class BasePreferenceActivity extends PreferenceActivity {
	private static final String TAG = "BasePreferenceActivity";

	// Cache for bitmaps get from External memory
	LruCache<String, Bitmap> mCache_SDBitmaps;

	/**
	 * Get image from SD Card,Take path reference to Glovalvar.AppImageSetting
	 * 
	 * @return Bitmap of the file, return null is file is not found
	 * 
	 * */
	public Bitmap GetImageFromSD(Context context, String filename) {
		if (Gvar.DEBUG)
			Log.d(TAG, TAG
					+ " GetImageFromSD(Context context, String filename) ");
		Bitmap img = mCache_SDBitmaps.get(filename);
		if (img != null) {
			if (Gvar.DEBUG)
				Log.d(TAG, TAG + " Got image from cache, return image");
			return img;
		}

		img = GetImage(context, filename);
		if (img != null) {
			if (Gvar.DEBUG)
				Log.d(TAG, TAG + " Image is saving to cache memory");

			mCache_SDBitmaps.put(filename, img);
			return img;
		} else {
			return null;
		}
	}

	/**
	 * Get image from SD Card with specific Height and Width,Take path reference
	 * to Glovalvar.AppImageSetting
	 * 
	 * @return Bitmap of the file, return null is file is not found
	 * 
	 * */
	public Bitmap GetImageFromSD(Context context, String filename, int width,
			int height) {
		if (Gvar.DEBUG)
			Log.d(TAG,
					TAG
							+ " GetImageFromSD(Context context, String filename,int width,int height) ");

		Bitmap img = mCache_SDBitmaps.get(filename);
		if (img != null) {
			if (Gvar.DEBUG)
				Log.d(TAG, TAG + " Got image from cache, return image");
			return img;
		}

		img = GetImage(context, filename);

		if (img != null) {
			if (Gvar.DEBUG)
				Log.d(TAG, TAG + " Image is saving to cache memory");

			Bitmap scaledImg = Bitmap.createScaledBitmap(img, width, height,
					true);
			mCache_SDBitmaps.put(filename, scaledImg);
			return scaledImg;
		} else {
			return null;
		}
	}

	private Bitmap GetImage(Context context, String filename) {
		// try to get from Cache
		Bitmap img = mCache_SDBitmaps.get(filename);
		if (img != null)
			return img;

		// Try to get from SD
		String imageInSD = Gvar.APP_ASSET_FULLPATH + "/" + filename;
		File f = new File(imageInSD);
		if (!f.exists())
			return null;

		Options options = new BitmapFactory.Options();
		options.inScaled = false;
		return BitmapFactory.decodeFile(imageInSD, options);
	}

	protected void BaseSaveSettingToSP(String key, String value) {
		if(Gvar.DEBUG)
			Log.d(TAG,TAG+"BaseSaveSettingToSP() key="+key+" value="+value);
		SharedPreferences settings = getSharedPreferences(Gvar.FLAG_SHARED_PREF, 0);
		SharedPreferences.Editor PE = settings.edit();
		PE.putString(key, value);
		PE.commit();
	}

	protected String BaseGetSettingFromSP(String key) {
		SharedPreferences settings = getSharedPreferences(Gvar.FLAG_SHARED_PREF, 0);
		return settings.getString(key, "");
	}
}
