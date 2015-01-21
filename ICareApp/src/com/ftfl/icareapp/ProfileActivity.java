package com.ftfl.icareapp;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.ftfl.icareapp.util.Profile;
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
import android.widget.Toast;

public class ProfileActivity extends Activity {
	
	ArrayList<Profile> myProfile;
	ArrayList<Profile> gSonList;
	EditText etName, etEmail, etDob, etWeight, etHeight, etEye,
			etDiseas;
	//Profile nProfile;
	Profile shuvo;
	SharedPreferences sPrefs;
	Gson gSon;
	
	public static final String MyPREFERENCES = "MyPrefs";
	public static final String PROFILE = "Profile";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		myProfile = new ArrayList<Profile>();
		// gSonList = new ArrayList<Profile>();

		sPrefs = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		gSon = new Gson();

		etName = (EditText) findViewById(R.id.nameET);
		etEmail = (EditText) findViewById(R.id.emailET);
		etDob = (EditText) findViewById(R.id.dobET);
		etWeight = (EditText) findViewById(R.id.weightET);
		etHeight = (EditText) findViewById(R.id.heightET);
		etEye = (EditText) findViewById(R.id.ecolorET);
		etDiseas = (EditText) findViewById(R.id.majordiseaseET);
		
		if (sPrefs.contains(PROFILE)) {

			String listRead = sPrefs.getString(PROFILE, "");

			Type type = new TypeToken<ArrayList<Profile>>() {
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
			saveBtn.setText("Update");

		}

	}
	
	public void save(View aView) {

		String name = etName.getText().toString();
		String email = etEmail.getText().toString();
		String dob = etDob.getText().toString();
		String weight = etWeight.getText().toString();
		String height = etHeight.getText().toString();
		String eye = etEye.getText().toString();
		String disease = etDiseas.getText().toString();

		Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show();

		myProfile.add(shuvo = new Profile(name, dob, height, weight,
				eye,disease, email));

		// Store arraylist in sharedpreference
		String nProfile = gSon.toJson(myProfile);
		Editor sEdit = sPrefs.edit();
		sEdit.putString("Profile", nProfile);
		sEdit.commit();

		Intent i = new Intent(this, DietChartActivity.class);
		startActivity(i);
		finish();

	}


}
