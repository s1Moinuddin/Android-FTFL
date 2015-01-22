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
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends Activity {

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
		setContentView(R.layout.activity_profile);

		myProfile = new ArrayList<MyProfile>();
		gSonList = new ArrayList<MyProfile>();

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
			etEmail.setText(gSonList.get(0).getEmail());
			etDob.setText(gSonList.get(0).getDateOfBirth());
			etWeight.setText(gSonList.get(0).getWeight());
			etHeight.setText(gSonList.get(0).getHeight());
			etEye.setText(gSonList.get(0).getEyeColor());
			etDiseas.setText(gSonList.get(0).getMajorDisase());

			Button saveBtn = (Button) findViewById(R.id.save);
			saveBtn.setText(FTFLConstants.UPDATE);
			TextView editTittle = (TextView) findViewById(R.id.tittle);
			editTittle.setText(FTFLConstants.EDIT_PROFILE);

		}

	}

	public void saveProfile(View aView) {

		String name = etName.getText().toString();
		String email = etEmail.getText().toString();
		String dob = etDob.getText().toString();
		String weight = etWeight.getText().toString();
		String height = etHeight.getText().toString();
		String eye = etEye.getText().toString();
		String disease = etDiseas.getText().toString();

		Toast.makeText(this, FTFLConstants.SAVE, Toast.LENGTH_SHORT).show();

		myProfile.add(shuvo = new MyProfile(name, dob, height, weight, eye,
				disease, email));

		// Store arraylist in sharedpreference
		String nProfile = gSon.toJson(myProfile);
		Editor sEdit = sPrefs.edit();
		sEdit.putString(FTFLConstants.KEY_PROFILE, nProfile);
		sEdit.commit();

		Intent i = new Intent(this, DietChartActivity.class);
		startActivity(i);
		finish();

	}

}
