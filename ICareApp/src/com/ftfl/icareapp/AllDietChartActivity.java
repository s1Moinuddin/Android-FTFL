package com.ftfl.icareapp;

import java.util.ArrayList;

import com.ftfl.icareapp.adapter.ThirdAdapter;
import com.ftfl.icareapp.util.DietChart;
import com.ftfl.icareapp.util.Profile;




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
	
	RadioButton allBtn = null, weekBtn= null, monthBtn = null;
	RadioGroup radioGroup;
	ListView listView3;
	SQDataSource dbHelper;
	ArrayList<DietChart> dietProfiles;
	int id_To_Update = 0;
	AlertDialog.Builder builder;
	Profile mprofile;
	TextView textId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alldietchart);
		
		listView3 = (ListView) findViewById(R.id.viewDietList);
		
		allBtn = (RadioButton) findViewById(R.id.radioButtonAll);
		weekBtn = (RadioButton) findViewById(R.id.radioButtonWk);
		monthBtn = (RadioButton) findViewById(R.id.radioButtonMonth);
		
		dbHelper = new SQDataSource(this);
		builder = new AlertDialog.Builder(this);

		dietProfiles = dbHelper.getAllDates();
		ThirdAdapter arrayAdapter = new ThirdAdapter(this, dietProfiles);
		// adding it to the list view.
		listView3.setAdapter(arrayAdapter);
		
		radioGroup = (RadioGroup) findViewById(R.id.rdGroup);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// find which radio button is selected
				if(checkedId == R.id.radioButtonWk) {
					dietProfiles = dbHelper.getWeeklyDates();
					ThirdAdapter arrayAdapter = new ThirdAdapter(AllDietChartActivity.this, dietProfiles);
					listView3.setAdapter(arrayAdapter);
					
				} else if(checkedId == R.id.radioButtonMonth) {
					dietProfiles = dbHelper.getMonthlyDates();
					ThirdAdapter arrayAdapter = new ThirdAdapter(AllDietChartActivity.this, dietProfiles);
					listView3.setAdapter(arrayAdapter);
					
				} else {
					dietProfiles = dbHelper.getAllDates();
					ThirdAdapter arrayAdapter = new ThirdAdapter(AllDietChartActivity.this, dietProfiles);
					listView3.setAdapter(arrayAdapter);
				}
			}
			
		});
		
		// how many options you want to give for Alert Dialog box
		final String[] option = new String[] { "View", "Edit", "Delete" };
		ArrayAdapter<String> adapterDialog = new ArrayAdapter<String>(this,
				android.R.layout.select_dialog_item, option);// default layout

		builder.setAdapter(adapterDialog,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Log.e("Selected Item", String.valueOf(which));
						// position of the arrayList is "which"
						if (which == 0) {
							viewProfile(id_To_Update);
						}
						if (which == 1) {
							/*
							 * builder.setMessage(R.string.editProfile); to do
							 * this, you've to declare/create a new builder.
							 */
							editProfile(id_To_Update);
						}
						if (which == 2) {

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
							AlertDialog del = builder.create();
							del.show();
						}
					}
				});
		final AlertDialog dialog = builder.create();// create builder after
													// declaring all its
													// attributes.
		dialog.setTitle("Select Option");

		listView3.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				// arg1 is used to get the view. dbID is declared in the
				// listrow, which is hidden/gone
				textId = (TextView) arg1.findViewById(R.id.dbID3);
				String proID = textId.getText().toString();
				// in order to use for delete and edit in DataBase
				id_To_Update = Integer.parseInt(proID);
				dialog.show(); // to show dialog box of edit and delete

			}
		});
	}
	
	/**
	 * to delete existing profile
	 */
	public void deleteProfile(int profileID) {

		dbHelper.deleteData(profileID);

		Toast.makeText(this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
		
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
		dataBundle.putInt("id", profileID); // "id" is the key...
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
		dataBundle.putInt("id", profileID); // "id" is the key...
		Intent intent = new Intent(getApplicationContext(), ViewChartActivity.class);
		intent.putExtras(dataBundle);
		startActivity(intent);
	}

}
