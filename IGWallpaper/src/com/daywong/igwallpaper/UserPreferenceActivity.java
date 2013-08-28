package com.daywong.igwallpaper;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.widget.Toast;

public class UserPreferenceActivity extends PreferenceActivity {
	private final int TOKEN_URL_CODE = 999;
	public String token_url;
	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    addPreferencesFromResource( R.xml.prefs);

	    // We want to add a validator to the number of circles so that it only
	    // accepts numbers
	    Preference circlePreference = getPreferenceScreen().findPreference("numberOfCircles");
	    // Add the validator
	    circlePreference.setOnPreferenceChangeListener(numberCheckListener);
	    Preference loginInstagramPref = findPreference( "loginInstagram" );
		loginInstagramPref.setOnPreferenceClickListener( loginInstagranListener );
	  }
	  @Override
	  protected void onActivityResult(int requestCode, int resultCode,
	  Intent data) {
	      super.onActivityResult(requestCode, resultCode, data);
	          switch(requestCode){
	        case TOKEN_URL_CODE:
	          if(resultCode==RESULT_OK){
	              Bundle extras = data.getExtras();
	              token_url = extras.getString("token_url");
	              Toast.makeText(getApplicationContext(), "Link is "+token_url, Toast.LENGTH_LONG).show();
	              //go Step 2 ; Load user images
	              
	              Intent i = new Intent(getApplicationContext(),GetInstagramMediaActivity.class);
	              i.putExtra("token_url", token_url);
	              startActivity(i);
	          }
	          break;
	      }
	  }
	  OnPreferenceClickListener loginInstagranListener = new OnPreferenceClickListener() {
		@Override
		public boolean onPreferenceClick(Preference preference) {
			startActivityForResult(new Intent(getApplicationContext(),LoginInstagramActivity.class),TOKEN_URL_CODE);
			return false;
		}
	};
	  Preference.OnPreferenceChangeListener numberCheckListener = new OnPreferenceChangeListener() {

	    @Override
	    public boolean onPreferenceChange(Preference preference, Object newValue) {
	      // Check that the string is an integer
	      if (newValue != null && newValue.toString().length() > 0
	          && newValue.toString().matches("\\d*")) {
	        return true;
	      }
	      // If now create a message to the user
	      Toast.makeText(getApplicationContext(), "Invalid Input",
	          Toast.LENGTH_SHORT).show();
	      return false;
	    }
	  };
	} 