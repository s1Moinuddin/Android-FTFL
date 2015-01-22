package com.ftfl.icareapp;

import java.util.ArrayList;

import com.ftfl.icareapp.adapter.AllDietAdapter;
import com.ftfl.icareapp.database.SQDataSource;
import com.ftfl.icareapp.util.DietChart;
import com.ftfl.icareapp.util.FTFLConstants;
import com.ftfl.icareapp.util.MyProfile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AllDietChartActivity extends Activity {

	RadioButton allBtn = null, weekBtn = null, monthBtn = null;
	RadioGroup radioGroup = null;
	ListView lViewAlChart = null;
	SQDataSource sqlSource = null;
	ArrayList<DietChart> dietArrayList = null;
	AlertDialog.Builder builder = null;
	MyProfile mprofile = null;
	TextView textId = null;
	int id_To_Update = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alldietchart);

		lViewAlChart = (ListView) findViewById(R.id.viewDietList);

		allBtn = (RadioButton) findViewById(R.id.radioButtonAll);
		weekBtn = (RadioButton) findViewById(R.id.radioButtonWk);
		monthBtn = (RadioButton) findViewById(R.id.radioButtonMonth);

		sqlSource = new SQDataSource(this);
		builder = new AlertDialog.Builder(this);

		dietArrayList = sqlSource.getAllDates();
		AllDietAdapter arrayAdapter = new AllDietAdapter(this, dietArrayList);
		// adding it to the list view.
		lViewAlChart.setAdapter(arrayAdapter);

		radioGroup = (RadioGroup) findViewById(R.id.rdGroup);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// find which radio button is selected
				if (checkedId == R.id.radioButtonWk) {
					dietArrayList = sqlSource.getWeeklyDates();
					AllDietAdapter arrayAdapter = new AllDietAdapter(
							AllDietChartActivity.this, dietArrayList);
					lViewAlChart.setAdapter(arrayAdapter);

				} else if (checkedId == R.id.radioButtonMonth) {
					dietArrayList = sqlSource.getMonthlyDates();
					AllDietAdapter arrayAdapter = new AllDietAdapter(
							AllDietChartActivity.this, dietArrayList);
					lViewAlChart.setAdapter(arrayAdapter);

				} else {
					dietArrayList = sqlSource.getAllDates();
					AllDietAdapter arrayAdapter = new AllDietAdapter(
							AllDietChartActivity.this, dietArrayList);
					lViewAlChart.setAdapter(arrayAdapter);
				}
			}

		});

		// how many options you want to give for Alert Dialog box
		final String[] option = new String[] { FTFLConstants.VIEW,
				FTFLConstants.EDIT, FTFLConstants.DELETE };

		ArrayAdapter<String> adapterDialog = new ArrayAdapter<String>(this,
				android.R.layout.select_dialog_item, option);// default layout

		builder.setAdapter(adapterDialog,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Log.e("Selected Item", String.valueOf(which));
						// position of the arrayList is "which"
						if (which == FTFLConstants.VIEW_ID_0) {
							viewProfile(id_To_Update);
						}
						if (which == FTFLConstants.VIEW_ID_1) {
							/*
							 * builder.setMessage(R.string.editProfile); to do
							 * this, you've to declare/create a new builder.
							 */
							editProfile(id_To_Update);
						}
						if (which == FTFLConstants.VIEW_ID_2) {

							builder.setMessage(R.string.deleteChart)
									.setPositiveButton(
											R.string.yes,
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int id) {
													deleteProfile(id_To_Update);
												}
											})
									.setNegativeButton(
											R.string.no,
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int id) {
													// User cancelled the dialog
												}
											});
							// create builder after declaring all its
							// attributes.
							AlertDialog delDialog = builder.create();
							delDialog.show();
						}
					}
				});
		final AlertDialog dialog = builder.create();// create builder after
													// declaring all its
													// attributes.
		dialog.setTitle(getString(R.string.option));

		lViewAlChart.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				// arg1 is used to get the view. dbID3 is declared in the
				// listrow, which is hidden/gone
				textId = (TextView) arg1.findViewById(R.id.dbID3);
				String proID = textId.getText().toString();
				// in order to use for view, delete and edit in DataBase
				id_To_Update = Integer.parseInt(proID);
				dialog.show(); // to show dialog box of edit and delete

			}
		});
	}

	/**
	 * to delete existing profile
	 */
	public void deleteProfile(int profileID) {

		sqlSource.deleteData(profileID);

		Toast.makeText(this, FTFLConstants.DEL_SUCCESS, Toast.LENGTH_SHORT)
				.show();

		Intent intent = new Intent(getApplicationContext(),
				AllDietChartActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * to edit existing profile
	 */
	public void editProfile(int profileID) {
		Bundle dataBundle = new Bundle();
		dataBundle.putInt(FTFLConstants.KEY_ID, profileID); // "id" is the
															// key...
		Intent intent = new Intent(this, DietChartActivity.class);
		intent.putExtras(dataBundle);
		startActivity(intent);
		finish();
	}

	/**
	 * to show the profileData
	 */
	public void viewProfile(int profileID) {
		Bundle dataBundle = new Bundle();
		dataBundle.putInt(FTFLConstants.KEY_ID, profileID); // "id" is the
															// key...
		Intent intent = new Intent(getApplicationContext(),
				ViewChartActivity.class);
		intent.putExtras(dataBundle);
		startActivity(intent);
	}

}
