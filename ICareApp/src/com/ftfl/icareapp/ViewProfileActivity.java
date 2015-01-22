package com.ftfl.icareapp;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.ftfl.icareapp.util.FTFLConstants;
import com.ftfl.icareapp.util.MyProfile;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class ViewProfileActivity extends Activity {

	ArrayList<MyProfile> myProfile = null;
	ArrayList<MyProfile> gSonList = null;
	EditText etName = null, etEmail = null, etDob = null, etWeight = null,
			etHeight = null, etEye = null, etDiseas = null;
	MyProfile shuvo = null;
	SharedPreferences sPrefs = null;
	Gson gSon = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_viewprofile);

		myProfile = new ArrayList<MyProfile>();
		// gSonList = new ArrayList<Profile>();

		sPrefs = getSharedPreferences(FTFLConstants.MyPREFERENCES,
				Context.MODE_PRIVATE);
		gSon = new Gson();

		etName = (EditText) findViewById(R.id.nameET);
		etEmail = (EditText) findViewById(R.id.emailET);
		etDob = (EditText) findViewById(R.id.dobET);
		etWeight = (EditText) findViewById(R.id.weightET);
		etHeight = (EditText) findViewById(R.id.heightET);
		etEye = (EditText) findViewById(R.id.ecolorET);
		etDiseas = (EditText) findViewById(R.id.majordiseaseET);

		if (sPrefs.contains(FTFLConstants.KEY_PROFILE)) {

			String listRead = sPrefs.getString(FTFLConstants.KEY_PROFILE, "");

			Type type = new TypeToken<ArrayList<MyProfile>>() {
			}.getType(); // to get the type of the encryption, to decrypt it.

			gSonList = gSon.fromJson(listRead, type);

			etName.setText(gSonList.get(0).getName());
			etName.setEnabled(false);
			etName.setFocusable(false);

			etEmail.setText(gSonList.get(0).getEmail());
			etEmail.setEnabled(false);
			etEmail.setFocusable(false);

			etDob.setText(gSonList.get(0).getDateOfBirth());
			etDob.setEnabled(false);
			etDob.setFocusable(false);

			etWeight.setText(gSonList.get(0).getWeight());
			etWeight.setEnabled(false);
			etWeight.setFocusable(false);

			etHeight.setText(gSonList.get(0).getHeight());
			etHeight.setEnabled(false);
			etHeight.setFocusable(false);

			etEye.setText(gSonList.get(0).getEyeColor());
			etEye.setEnabled(false);
			etEye.setFocusable(false);

			etDiseas.setText(gSonList.get(0).getMajorDisase());
			etDiseas.setEnabled(false);
			etDiseas.setFocusable(false);

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.viewprofile, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case R.id.Edit_Profile:
			Intent intent = new Intent(getApplicationContext(),
					ProfileActivity.class);
			startActivity(intent);
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);

		}
	}

}
