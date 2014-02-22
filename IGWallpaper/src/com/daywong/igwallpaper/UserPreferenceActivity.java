package com.daywong.igwallpaper;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.daywong.igwallpaper.model.BaseApplication;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

public class UserPreferenceActivity extends PreferenceActivity {
	private static final String TAG = "UserPreferenceActivity";
	private final int TOKEN_URL_CODE = 999;
	public String token_url;
	private BaseApplication app;
	private Preference photoPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// init
		Log.d(TAG, "UserPreferenceActivity OnCreate");
		Parse.initialize(this, "KtYxWalxiCNFxdc7LcPjuPqMlXGLFS6FwELx69Hv",
				"4GYXO1FGfiphAmn4RdsTKyuWoCxT3FERGUg2auPT");
		ParseAnalytics.trackAppOpened(getIntent());
		addPreferencesFromResource(R.xml.prefs);
		app = (BaseApplication) getApplicationContext();
		app.createFloderIfneeded();
		// autoLogin();

		// retrieve parse data
		ParseUser.enableAutomaticUser();
		ParseUser pu = ParseUser.getCurrentUser();
		if (pu.isNew()) {
			ParseInstallation.getCurrentInstallation().saveInBackground();
			pu.put("photoCache", 20);
			pu.put("maxPhotoCache", 20);
			pu.saveEventually();
		}
		if (pu == null) {
			// Cat 1
			Preference loginInstagramPref = findPreference("loginInstagram");
			loginInstagramPref
					.setOnPreferenceClickListener(loginInstagranListener);

			// Cat 2
			Preference downloadImages = findPreference("downloadPhotos");
			downloadImages.setEnabled(false);
			photoPref = getPreferenceScreen().findPreference("numberOfPhoto");
			photoPref.setEnabled(false);
			Preference gotoSetting = findPreference("gotoSetting");
			gotoSetting.setEnabled(false);
			Preference stylePref = getPreferenceScreen().findPreference(
					"wallStyle");
			stylePref.setEnabled(false);
			Preference aboutPref = getPreferenceScreen()
					.findPreference("about");
			aboutPref.setOnPreferenceClickListener(aboutListener);
		} else {
			int max = 20;
			// (Integer) pu.getNumber("maxPhotoCache");
			if (max > 0)
				BaseApplication.USER_MAX_PHOTO = max;

			// Cat 1
			Preference loginInstagramPref = findPreference("loginInstagram");
			Preference downloadImages = findPreference("downloadPhotos");
			loginInstagramPref
					.setOnPreferenceClickListener(loginInstagranListener);
			downloadImages.setOnPreferenceClickListener(loginInstagranListener);

			// Cat 2
			// photoPref =
			// getPreferenceScreen().findPreference("numberOfPhoto");
			// photoPref.setOnPreferenceChangeListener(numofPhotoListener);
			// photoPref.setSummary(app.getPref_photoLimite()
			// + " Photo caching, max is "
			// + BaseApplication.USER_MAX_PHOTO);
			Preference gotoSetting = findPreference("gotoSetting");
			gotoSetting.setOnPreferenceClickListener(gotoSettingListener);
			Preference stylePref = getPreferenceScreen().findPreference(
					"wallStyle");
			stylePref.setOnPreferenceChangeListener(photowallStyleListener);
			Preference feedbackPref = getPreferenceScreen()
					.findPreference("feedback");
			feedbackPref.setOnPreferenceClickListener(feedbackListener);
			Preference aboutPref = getPreferenceScreen()
					.findPreference("about");
			aboutPref.setOnPreferenceClickListener(aboutListener);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case TOKEN_URL_CODE:
			if (resultCode == RESULT_OK) {
				Bundle extras = data.getExtras();
				token_url = extras.getString("token_url");
				// go Step 2 ; Load user images
				Intent i = new Intent(getApplicationContext(),
						GetInstagramMediaActivity.class);
				i.putExtra("token_url", token_url);
				startActivity(i);
			}
			break;
		}
	}

	OnPreferenceClickListener loginInstagranListener = new OnPreferenceClickListener() {
		@Override
		public boolean onPreferenceClick(Preference preference) {
			startActivityForResult(new Intent(getApplicationContext(),
					LoginInstagramActivity.class), TOKEN_URL_CODE);
			return false;
		}
	};
	OnPreferenceClickListener gotoSettingListener = new OnPreferenceClickListener() {
		@Override
		public boolean onPreferenceClick(Preference preference) {
			startActivity(new Intent(
					WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER));
			return false;
		}
	};
	OnPreferenceChangeListener photowallStyleListener = new OnPreferenceChangeListener() {

		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			if (newValue != null) {
				Log.d(TAG, "Pref changed to " + newValue.toString());
				ParseUser pu = ParseUser.getCurrentUser();
				pu.put("wallStyle", newValue.toString());
				pu.saveEventually();
				return true;
			}
			return false;
		}
	};
	OnPreferenceClickListener aboutListener = new OnPreferenceClickListener() {

		@Override
		public boolean onPreferenceClick(Preference preference) {
			startActivity(new Intent(UserPreferenceActivity.this,
					AboutActivity.class));
			return false;
		}
	};
	OnPreferenceClickListener feedbackListener = new OnPreferenceClickListener() {

		@Override
		public boolean onPreferenceClick(Preference preference) {
			startActivity(new Intent(UserPreferenceActivity.this,
					FeedbackActivity.class));
			return false;
		}
	};
	public void autoLogin() {
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		final String IMEI = tm.getDeviceId();
		if (ParseUser.getCurrentUser() == null) {
			try {
				Log.d(TAG, "Login user IMEI" + IMEI);
				ParseUser.logIn(IMEI, IMEI);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if (ParseUser.getCurrentUser() == null) {
				Log.d(TAG, "Create parse user");
				try {
					ParseUser pu = new ParseUser();
					pu.setPassword(IMEI);
					pu.setUsername(IMEI);
					pu.put("photoCache", 20);
					pu.put("maxPhotoCache", 20);
					pu.signUp();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}
