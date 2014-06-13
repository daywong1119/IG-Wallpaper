package com.daywong.igwallpaper;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.parse.ParseObject;

public class FeedbackActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		AdView ad = (AdView) findViewById(R.id.adView);
		ad.loadAd(new AdRequest());
		Button btnSubmit = (Button) findViewById(R.id.btnSubmit);
		Button btnCancel = (Button) findViewById(R.id.btnCancel);
		final EditText etText = (EditText) findViewById(R.id.et_feedback);
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String text = etText.getText().toString();
				if (text.length() > 0) {
					ParseObject po = new ParseObject("Feedback");
					po.add("content", text);
					po.saveEventually();
					Toast.makeText(getApplicationContext(),
							"Thank you! I will see your feedback soon.",
							Toast.LENGTH_LONG).show();
					finish();
				} else {
					Toast.makeText(getApplicationContext(),
							"Please input feedback", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
}
