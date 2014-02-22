package com.daywong.igwallpaper;

import java.io.File;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.daywong.igwallpaper.model.BaseApplication;
import com.daywong.igwallpaper.model.ImageLoaderClass;
import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.parse.ParseException;
import com.parse.ParseUser;

public class GetInstagramMediaActivity extends Activity implements
		ImageLoaderClass.ImageLoaderCallback {
	final private static String TAG = "GetInstagramMediaActivity";
	private String token_url;
	private BaseApplication app;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// Log.d("New Thread", "Proccess Complete.");
			Bundle bun = msg.getData();
			String jsonResponse = bun.getString("json_response");
			app = (BaseApplication) getApplicationContext();
			try {
				JSONObject igJson = new JSONObject(jsonResponse);
				ArrayList<String> list = null;
				File saveTo = null;
				String wallStyle = app.getPref_wallStyle();
				Log.d(TAG, "Wall style is " + wallStyle);
				if (wallStyle.equalsIgnoreCase("largeimg")) {
					list = findImageUrl_standardRes(igJson);
					saveTo = BaseApplication.CACHE_PATH_L;
				} else if (wallStyle.equalsIgnoreCase("smallimg")) {
					list = findImageUrl_lowRes(igJson);
					saveTo = BaseApplication.CACHE_PATH_S;
				}
				if (list != null) {
					Thread t1 = new Thread(new ImageLoaderClass(list, saveTo,
							GetInstagramMediaActivity.this));
					t1.start();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_instagram_media);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			token_url = extras.getString("token_url");
			instagramMedia iMedia = new instagramMedia();
			iMedia.fetch(token_url);
		}
		Button btnBgRun = (Button) findViewById(R.id.btn_bg_run);
		btnBgRun.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		AdView ad = (AdView) findViewById(R.id.adView);
		ad.loadAd(new AdRequest());
	}

	public ArrayList<String> findImageUrl_standardRes(JSONObject json)
			throws JSONException {
		ArrayList<String> listImges = new ArrayList<String>();
		JSONArray recs = json.getJSONArray("data");
		if (recs != null) {
			for (int i = 0; i < recs.length(); i++) {
				JSONObject parent1 = recs.getJSONObject(i);
				JSONObject parent2 = parent1.getJSONObject("images");
				JSONObject parent3 = parent2
						.getJSONObject("standard_resolution");
				String url = parent3.getString("url");
				listImges.add(url);
				Log.d(TAG, url);
			}
		}
		return listImges;
	}

	public ArrayList<String> findImageUrl_thumRes(JSONObject json)
			throws JSONException {
		ArrayList<String> listImges = new ArrayList<String>();
		JSONArray recs = json.getJSONArray("data");
		if (recs != null) {
			for (int i = 0; i < recs.length(); i++) {
				JSONObject parent1 = recs.getJSONObject(i);
				JSONObject parent2 = parent1.getJSONObject("images");
				JSONObject parent3 = parent2.getJSONObject("thumbnail");
				String url = parent3.getString("url");
				listImges.add(url);
				Log.d(TAG, url);
			}
		}
		return listImges;
	}

	public ArrayList<String> findImageUrl_lowRes(JSONObject json)
			throws JSONException {
		ArrayList<String> listImges = new ArrayList<String>();
		JSONArray recs = json.getJSONArray("data");
		if (recs != null) {
			for (int i = 0; i < recs.length(); i++) {
				JSONObject parent1 = recs.getJSONObject(i);
				JSONObject parent2 = parent1.getJSONObject("images");
				JSONObject parent3 = parent2.getJSONObject("low_resolution");
				String url = parent3.getString("url");
				listImges.add(url);
				Log.d(TAG, url);
			}
		}
		return listImges;
	}

	public class instagramMedia {
		Thread mThread;
		String igUrl = BaseApplication.IG_LIKED_URL;
		String apiKey = "";

		public void fetch(final String url) {
			boolean foundEqualSign = false;
			for (Character c : url.toCharArray()) {
				if (foundEqualSign == true) {
					igUrl += c;
					apiKey += c;
				}
				if (c == '=')
					foundEqualSign = true;
			}
			try {
				Log.d(TAG, "API Key:" + apiKey);
				ParseUser pu = ParseUser.getCurrentUser();
				Log.d(TAG, "ParseUser is " + pu.toString());
				pu.put("igApi", apiKey);
				pu.save();
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			// get data from insgatram
			mThread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						HttpClient httpclient = new DefaultHttpClient();
						HttpResponse response = httpclient.execute(new HttpGet(
								igUrl));
						if (response != null) {
							HttpEntity entity = response.getEntity();
							if (entity != null) {
								Bundle bun = new Bundle();
								Message msg = new Message();
								bun.putString("json_response",
										EntityUtils.toString(entity));
								msg.setData(bun);
								handler.sendMessage(msg);
								mThread.join();
							}
						} else {
							Log.d(TAG, "response null");
							mThread.join();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			mThread.start();
		}
	}

	@Override
	public void done() {
		finish();
	}

	@Override
	public void error(String msg) {
	}
}
