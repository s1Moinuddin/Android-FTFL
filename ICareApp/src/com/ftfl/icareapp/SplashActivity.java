package com.ftfl.icareapp;

import com.ftfl.icareapp.util.FTFLConstants;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class SplashActivity extends Activity {

	SharedPreferences sPrefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		sPrefs = getSharedPreferences(FTFLConstants.MyPREFERENCES,
				Context.MODE_PRIVATE);

		Thread background = new Thread() {
			public void run() {

				try {
					// Thread will sleep for 3 seconds
					sleep(3 * 1000);

					if (!sPrefs.contains(FTFLConstants.KEY_PROFILE)) {
						Intent i = new Intent(SplashActivity.this,
								ProfileActivity.class);
						startActivity(i);
					} else {
						Intent i = new Intent(SplashActivity.this,
								HomeScreenActivity.class);
						startActivity(i);
					}
					// Remove activity
					finish(); // so that, it will not get back in the previous
								// file.

				} catch (Exception e) {

				}
			}
		};

		// start thread
		background.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
