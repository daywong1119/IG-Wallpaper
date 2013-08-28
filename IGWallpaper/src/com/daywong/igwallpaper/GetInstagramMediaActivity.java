package com.daywong.igwallpaper;

import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class GetInstagramMediaActivity extends Activity {
	final private static String TAG = "GetInstagramMediaActivity";
	private String token_url;
	InputStream token;

	interface myCallback {
		void done(InputStream data);

		void error(String error);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_instagram_media);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			myCallback mCallback = new myCallback() {

				@Override
				public void error(String error) {
					Log.d("error", error);
				}

				@Override
				public void done(InputStream data) {
					Log.d("Done", "Get Data haha Data is :"+data.toString());
					TextView tv = (TextView)findViewById(R.id.textView1);
					tv.setText(data.toString());
				}
			};
			token_url = extras.getString("token_url");
			instagramMedia iMedia = new instagramMedia(mCallback);
			Log.d(TAG,"fetch Image runs ");
			iMedia.fetch(token_url);
		}
	}

	public class instagramMedia {
		myCallback callback;
		Thread mThread;

		public instagramMedia(myCallback userCallback) {
			this.callback = userCallback;
		}

		public void fetch(final String url) {
			mThread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						HttpClient httpclient = new DefaultHttpClient();
						HttpResponse response = httpclient.execute(new HttpGet(
								url));
						if(response != null){
							callback.done(response.getEntity().getContent());
						}else{
							callback.error( "Web response null" );
						}
					} catch (Exception e) {
						callback.error( e.toString());
					}
				}
			});
			mThread.start();
		}
	}
}
