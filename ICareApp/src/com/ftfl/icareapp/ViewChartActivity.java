package com.ftfl.icareapp;




import com.ftfl.icareapp.util.DietChart;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.EditText;

public class ViewChartActivity extends Activity {
	
	Bundle extras;
	EditText etMeal, etMenu, etDate, etTime;
	SQDataSource dbHelper;
	DietChart dietChart;
	int id_To_Update = 0;

	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_viewchart);
		
		etMeal = (EditText) findViewById(R.id.mealNameVW);
		etMenu = (EditText) findViewById(R.id.menuVW);
		etDate = (EditText) findViewById(R.id.dateVW);
		etTime = (EditText) findViewById(R.id.timeVW);

		dbHelper = new SQDataSource(this);
		extras = getIntent().getExtras();
		
		if (extras != null) {
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
					etMeal.setEnabled(false);
					etMeal.setFocusable(false);
					etMeal.setClickable(false);

					etMenu.setText(menu, null);
					etMenu.setEnabled(false);
					etMenu.setFocusable(false);
					etMenu.setClickable(false);

					etDate.setText(date, null);
					etDate.setEnabled(false);
					etDate.setFocusable(false);
					etDate.setClickable(false);

					etTime.setText(time, null);
					etTime.setEnabled(false);
					etTime.setFocusable(false);
					etTime.setClickable(false);
				}
			}

		}
		
		
	}

}
