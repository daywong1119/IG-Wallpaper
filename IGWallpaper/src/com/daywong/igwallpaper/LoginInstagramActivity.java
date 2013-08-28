package com.daywong.igwallpaper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class LoginInstagramActivity extends Activity {
	private final int TOKEN_URL_CODE = 999;
	public final static String MY_DOMAIN = "mrcollection.co";
	private final static String TAG = "LoginInstagramActivity";
	private WebView web;
	private Bundle bundle = new Bundle();
	@SuppressLint("JavascriptInterface")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_instagram);
		web = (WebView) findViewById(R.id.webview);
		web.getSettings().setJavaScriptEnabled(true);
		web.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
		web.setWebViewClient(new MyWebViewClient());
		web.loadUrl("https://instagram.com/oauth/authorize/?client_id=3b0795dbee7a4bc8a71ddab9bd476ca0&redirect_uri=http://mrcollection.co/IG/index.php&response_type=token");

	}

	private class MyWebViewClient extends WebViewClient {
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
//            String ht = "javascript:window.droid.print(document.getElementsByTagName('html')[0].innerHTML);";
//            web.loadUrl(ht);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.d(TAG, "Domain is " + Uri.parse(url).getHost());
			if (Uri.parse(url).getHost().equalsIgnoreCase(MY_DOMAIN)) {
//				Toast.makeText(getApplicationContext(), "Link is "+url, Toast.LENGTH_LONG).show();
				bundle.putString("token_url", url.toString());
				Intent intent = new Intent();
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent);
				finish();
				return false;
			} else {
				view.loadUrl(url);
				return true;
			}
		}
	}

	private class MyJavaScriptInterface {
		@SuppressWarnings("unused")
		public void processHTML(String html) {
			// process the html as needed by the app
		}
	}
}
