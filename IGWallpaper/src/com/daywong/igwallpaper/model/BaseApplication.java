package com.daywong.igwallpaper.model;

import java.io.File;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

public class BaseApplication extends Application {
	final public static boolean DEBUG = true;
	final public static File CACHE_PATH = new File(Environment.getExternalStorageDirectory()+ "/IGWallpaper/cache_data");
	final public static File CACHE_PATH_S = new File(Environment.getExternalStorageDirectory()+ "/IGWallpaper/cache_data/s");
	final public static File CACHE_PATH_L = new File(Environment.getExternalStorageDirectory()+ "/IGWallpaper/cache_data/h");
	final public static File APP_PATH = new File(
			Environment.getExternalStorageDirectory() + "/IGWallpaper");
	final public static String INSTAGRAM_API_URL = "https://instagram.com/oauth/authorize/?client_id=3b0795dbee7a4bc8a71ddab9bd476ca0&redirect_uri=http://igwallpaper.parseapp.com/&response_type=token";
	public static final String IG_LIKED_URL = "https://api.instagram.com/v1/users/self/media/liked?access_token=";
	public static int USER_MAX_PHOTO = 20;
	public int getPref_photoLimite() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		String numString = prefs.getString("numberOfPhoto", "0");
		int num = Integer.valueOf(numString);
		return num;
	}

	public String getPref_wallStyle() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		String pref = prefs.getString("wallStyle", "error");
		return pref;
	}

	public static boolean isAppFolder() {
		if (!BaseApplication.APP_PATH.exists()) {
			if (!BaseApplication.APP_PATH.mkdir()) {
				return false;
			}
		}
		if (!BaseApplication.CACHE_PATH.exists()) {
			if (BaseApplication.CACHE_PATH.mkdir()) {
				return true;
			}
			return false;
		}
		return true;
	}

	public void createFloderIfneeded() {
		if (!BaseApplication.APP_PATH.exists())
			BaseApplication.APP_PATH.mkdir();
		if (!BaseApplication.CACHE_PATH.exists())
			BaseApplication.CACHE_PATH.mkdir();
		if (!BaseApplication.CACHE_PATH_L.exists())
			BaseApplication.CACHE_PATH_L.mkdir();
		if (!BaseApplication.CACHE_PATH_S.exists())
			BaseApplication.CACHE_PATH_S.mkdir();
	}

	public static int cachePhotoCount(String style) {
		File target;
		if (style.equalsIgnoreCase("smallimg"))
			target = CACHE_PATH_S;
		else
			target = CACHE_PATH_L;

		File[] files = target.listFiles();
		return files.length;
	}
}
