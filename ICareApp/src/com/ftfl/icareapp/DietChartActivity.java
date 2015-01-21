package com.ftfl.icareapp;


import java.util.Calendar;

import com.ftfl.icareapp.util.DietChart;




import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class DietChartActivity extends Activity implements OnTimeSetListener,OnDateSetListener{

	EditText etMeal, etMenu, etDate, etTime;
	SQDataSource dbHelper;
	DietChart dietChart;
	int id_To_Update = 0;
	
	Integer mSetHour = 0;
	Integer mSetMinute = 0;
	int mHour = 0;
	int mMinute = 0;
	final Calendar mCalendar = Calendar.getInstance();
	
	int mYear = 0;
	int mDay = 0;
	int mMonth = 0;
	CheckBox cbAlarm = null;
	
	Bundle extras;
	Button saveBtn;

	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dietchart);

		etMeal = (EditText) findViewById(R.id.mealNameET);
		etMenu = (EditText) findViewById(R.id.menuET);
		etDate = (EditText) findViewById(R.id.dateET);
		etTime = (EditText) findViewById(R.id.timeET);
		cbAlarm = (CheckBox) findViewById(R.id.checkBoxAlarm);

		dbHelper = new SQDataSource(this);
		extras = getIntent().getExtras();
		

		 saveBtn = (Button) findViewById(R.id.addBtn);
			
			if (extras != null) {
				saveBtn.setText("Update");
				int Value = extras.getInt("id");
				if (Value > 0) {
					// means this is the view part not the add contact part.
					Cursor cursor = dbHelper.getData(Value);
					id_To_Update = Value;
					if (cursor.moveToFirst()) { 
						/*int id = cursor
								.getInt(cursor
										.getColumnIndex(dbHelper.dietDBhelper.COLUMNL_ID_FIELD));*/
						String name = cursor
								.getString(cursor
										.getColumnIndex(dbHelper.dietDBhelper.COLUMNL_NAME_FIELD));
						String date = cursor
								.getString(cursor
										.getColumnIndex(dbHelper.dietDBhelper.COLUMNL_DATE_FIELD));
						String time = cursor
								.getString(cursor
										.getColumnIndex(dbHelper.dietDBhelper.COLUMNL_TIME_FIELD));
						String menu = cursor
								.getString(cursor
										.getColumnIndex(dbHelper.dietDBhelper.COLUMNL_MENU_FIELD));

						etMeal.setText(name, null);
						etMeal.setEnabled(true);
						etMeal.setFocusable(true);
						etMeal.setClickable(true);

						etMenu.setText(menu, null);
						etMenu.setEnabled(true);
						etMenu.setFocusable(true);
						etMenu.setClickable(true);

						etDate.setText(date, null);
						etDate.setEnabled(true);
						etDate.setFocusable(true);
						etDate.setClickable(true);

						etTime.setText(time, null);
						etTime.setEnabled(true);
						etTime.setFocusable(true);
						etTime.setClickable(true);
					}
				}

			}

		saveBtn.setOnClickListener(new OnClickListener() {

			@TargetApi(Build.VERSION_CODES.GINGERBREAD)
			@SuppressLint("InlinedApi")
			@Override
			public void onClick(View v) {


					String name = etMeal.getText().toString();
					String menu = etMenu.getText().toString();
					String date = etDate.getText().toString();
					String time = etTime.getText().toString();
					if (extras != null) {
						dietChart = new DietChart(name, date, menu, time);
					long updated = dbHelper.updateData(id_To_Update,dietChart);
						if(updated >= 0)
						{
							Toast.makeText(getApplicationContext(), "Data inserted", Toast.LENGTH_LONG).show();
							Intent i = new Intent(getApplicationContext(),
									HomeScreenActivity.class);
							startActivity(i);
							finish();
						}                                                                                                                                                          
						else
							Toast.makeText(getApplicationContext(), "Data Insertion Problem", Toast.LENGTH_LONG).show();
						
					} else{
						dietChart = new DietChart(name, date, menu, time);
						long inserted = dbHelper.insert(dietChart);
						if(inserted >= 0)
						{
							Toast.makeText(getApplicationContext(), "Data inserted", Toast.LENGTH_LONG).show();
							Intent i = new Intent(getApplicationContext(),
									HomeScreenActivity.class);
							startActivity(i);
							finish();
						}                                                                                                                                                          
						else
							Toast.makeText(getApplicationContext(), "Data Insertion Problem", Toast.LENGTH_LONG).show();
							
					}
					
					//Setting Alarm
					if (cbAlarm.isChecked()) {
					Intent alarmIntent = new Intent(AlarmClock.ACTION_SET_ALARM);
	                alarmIntent.putExtra(AlarmClock.EXTRA_HOUR, mSetHour);
	                alarmIntent.putExtra(AlarmClock.EXTRA_MINUTES, mSetMinute);
	                alarmIntent.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
	                alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	                startActivity(alarmIntent);
				}
			}
		});
		

	}
	
	public void setDate(View view){
		
		mYear = mCalendar.get(Calendar.YEAR);
		mMonth = mCalendar.get(Calendar.MONTH);
		mDay = mCalendar.get(Calendar.DAY_OF_MONTH);

		DatePickerDialog dialog = new DatePickerDialog(DietChartActivity.this, this, mYear,
				mMonth, mDay);
		dialog.show();
		
	}
	
	public void setTime(View view){
		
		// Process to get Current Time
		mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
		mMinute = mCalendar.get(Calendar.MINUTE);
		
		// Launch Time Picker Dialog
		TimePickerDialog tpd = new TimePickerDialog(DietChartActivity.this, this, mHour,mMinute,false);
		tpd.show();
		
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub
		
		mSetHour = hourOfDay;
		mSetMinute = minute;
		int hour = 0;
		String st = "";
		if (hourOfDay > 12) {
			hour = hourOfDay - 12;
			st = "PM";
		}

		else if (hourOfDay == 12) {
			hour = hourOfDay;
			st = "PM";
		}

		else if (hourOfDay == 0) {
			hour = hourOfDay + 12;
			st = "AM";
		} else {
			hour = hourOfDay;
			st = "AM";
		}
		etTime.setText(new StringBuilder().append(hour).append(" : ")
				.append(minute).append(" ").append(st));
	}
	
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		// TODO Auto-generated method stub
		etDate.setText(new StringBuilder()
		// Month is 0 based so add 1
		.append(dayOfMonth).append("/").append(monthOfYear + 1)
		.append("/").append(year));
	}
}
