package com.ftfl.icareapp;

import java.util.ArrayList;

import com.ftfl.icareapp.adapter.DietAdapter;
import com.ftfl.icareapp.adapter.UpDietAdapter;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class HomeScreenActivity extends Activity {

	ListView lstViewTdy, lstViewUp;
	SQDataSource sqlSource;
	int id_To_Update = 0;
	AlertDialog.Builder builder;
	MyProfile mprofile = null;
	TextView textId = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homescreen);

		sqlSource = new SQDataSource(this);
		builder = new AlertDialog.Builder(this);

		ArrayList<DietChart> dietProfiles = sqlSource.getDietProfiles();

		DietAdapter adapterTdy = new DietAdapter(this, dietProfiles);

		// adding it to the list view.
		lstViewTdy = (ListView) findViewById(R.id.todayList);
		lstViewTdy.setAdapter(adapterTdy);

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
							viewChart(id_To_Update);
						}
						if (which == FTFLConstants.VIEW_ID_1) {
							/*
							 * builder.setMessage(R.string.editProfile); to do
							 * this, you've to declare/create a new builder.
							 */
							editChart(id_To_Update);
						}
						if (which == FTFLConstants.VIEW_ID_2) {

							builder.setMessage(R.string.deleteChart)
									.setPositiveButton(
											R.string.yes,
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int id) {
													deleteChart(id_To_Update);
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
		dialog.setTitle("Select Option");

		lstViewTdy.setOnItemClickListener(new OnItemClickListener() {

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

		ArrayList<DietChart> nextProfiles = sqlSource.getUpcomingProfiles();

		UpDietAdapter adapterUp = new UpDietAdapter(this, nextProfiles);

		// adding it to the list view.
		lstViewUp = (ListView) findViewById(R.id.upcomingList);
		lstViewUp.setAdapter(adapterUp);

		lstViewUp.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				// arg1 is used to get the view. dbID2 is declared in the
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
	public void deleteChart(int profileID) {

		sqlSource.deleteData(profileID);

		Toast.makeText(this, FTFLConstants.DEL_SUCCESS, Toast.LENGTH_SHORT)
				.show();

		Intent intent = new Intent(getApplicationContext(),
				HomeScreenActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * to edit existing profile
	 */
	public void editChart(int profileID) {

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
	public void viewChart(int profileID) {

		Bundle dataBundle = new Bundle();
		dataBundle.putInt(FTFLConstants.KEY_ID, profileID); // "id" is the
															// key...
		Intent intent = new Intent(getApplicationContext(),
				ViewChartActivity.class);
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
			Intent intent = new Intent(getApplicationContext(),
					ViewProfileActivity.class);
			startActivity(intent);
			return true;
		case R.id.Add_Chart:
			Intent intentC = new Intent(getApplicationContext(),
					DietChartActivity.class);
			startActivity(intentC);
			return true;
		case R.id.All_Diet:
			Intent intentAD = new Intent(getApplicationContext(),
					AllDietChartActivity.class);
			startActivity(intentAD);
			return true;
		default:
			return super.onOptionsItemSelected(item);

		}
	}

}
