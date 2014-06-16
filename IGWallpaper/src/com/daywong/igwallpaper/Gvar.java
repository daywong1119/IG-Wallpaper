package com.daywong.igwallpaper;

import android.os.Environment;

public class Gvar {
	public final static boolean DEBUG = true;
	
	//Path of local storage on user's device
	public final static String APP_ASSET_PATH = "/com.daywong.gridwall";
	public final static String APP_ASSET_FULLPATH = Environment.getExternalStorageDirectory()+"/com.daywong.gridwall";

	public static final String FLAG_SHARED_PREF = "SharedPref_Gridwall";

	public static final String SP_IMAGE = "";
}
