package com.ftfl.icareapp;

import java.util.ArrayList;

import com.ftfl.icareapp.adapter.DietAdapter;
import com.ftfl.icareapp.adapter.SecondAdapter;
import com.ftfl.icareapp.util.DietChart;
import com.ftfl.icareapp.util.Profile;




import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class HomeScreenActivity extends Activity{
	
	ListView listView, listV2;
	SQDataSource dbHelper;
	int id_To_Update = 0;
	AlertDialog.Builder builder;
	Profile mprofile;
	TextView textId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homescreen);
		
		dbHelper = new SQDataSource(this);
		builder = new AlertDialog.Builder(this);

		ArrayList<DietChart> dietProfiles = dbHelper.getDietProfiles();

		DietAdapter arrayAdapter = new DietAdapter(this, dietProfiles);

		// adding it to the list view.
		listView = (ListView) findViewById(R.id.todayList);
		listView.setAdapter(arrayAdapter);
		
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

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				// arg1 is used to get the view. dbID is declared in the
				// listrow, which is hidden/gone
				textId = (TextView) arg1.findViewById(R.id.dbID);
				String proID = textId.getText().toString();
				// in order to use for delete and edit in DataBase
				id_To_Update = Integer.parseInt(proID);
				dialog.show(); // to show dialog box of edit and delete

			}
		});
		
		ArrayList<DietChart> nextProfiles = dbHelper.getUpcomingProfiles();

		SecondAdapter adapter2 = new SecondAdapter(this, nextProfiles);

		// adding it to the list view.
		listV2 = (ListView) findViewById(R.id.upcomingList);
		listV2.setAdapter(adapter2);
		
		listV2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				// arg1 is used to get the view. dbID is declared in the
				// listrow, which is hidden/gone
				textId = (TextView) arg1.findViewById(R.id.dbID2);                                                                                                                                                                                                                   
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
				HomeScreenActivity.class);
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.homescreen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case R.id.Preview_Profile:
			Intent intent = new Intent(getApplicationContext(),ViewProfileActivity.class);
			startActivity(intent);
			return true;
		case R.id.Add_Chart:
			Intent intent2 = new Intent(getApplicationContext(),DietChartActivity.class);
			startActivity(intent2);
			return true;
		case R.id.All_Diet:
			Intent intent3 = new Intent(getApplicationContext(),AllDietChartActivity.class);
			startActivity(intent3);
			return true;
		default:
			return super.onOptionsItemSelected(item);

		}
	}

}
