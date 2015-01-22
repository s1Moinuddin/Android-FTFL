package com.ftfl.icareapp;

import com.ftfl.icareapp.database.SQDataSource;
import com.ftfl.icareapp.database.SQLiteHelper;
import com.ftfl.icareapp.util.DietChart;
import com.ftfl.icareapp.util.FTFLConstants;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.EditText;

public class ViewChartActivity extends Activity {

	Bundle extras = null;
	EditText etMeal = null, etMenu = null, etDate = null, etTime = null;
	SQDataSource sqlSource = null;
	DietChart dietChart = null;
	int id_To_Update = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_viewchart);

		etMeal = (EditText) findViewById(R.id.mealNameVW);
		etMenu = (EditText) findViewById(R.id.menuVW);
		etDate = (EditText) findViewById(R.id.dateVW);
		etTime = (EditText) findViewById(R.id.timeVW);

		sqlSource = new SQDataSource(this);
		extras = getIntent().getExtras();

		if (extras != null) {
			int value = extras.getInt(FTFLConstants.KEY_ID);
			if (value > 0) {
				// means this is the view part not the add contact part.
				Cursor cursor = sqlSource.getData(value);
				id_To_Update = value;
				if (cursor.moveToFirst()) {

					String name = cursor.getString(cursor
							.getColumnIndex(SQLiteHelper.COLUMNL_NAME_FIELD));
					String date = cursor.getString(cursor
							.getColumnIndex(SQLiteHelper.COLUMNL_DATE_FIELD));
					String time = cursor.getString(cursor
							.getColumnIndex(SQLiteHelper.COLUMNL_TIME_FIELD));
					String menu = cursor.getString(cursor
							.getColumnIndex(SQLiteHelper.COLUMNL_MENU_FIELD));

					etMeal.setText(name);
					etMeal.setEnabled(false);
					etMeal.setFocusable(false);
					etMeal.setClickable(false);

					etMenu.setText(menu);
					etMenu.setEnabled(false);
					etMenu.setFocusable(false);
					etMenu.setClickable(false);

					etDate.setText(date);
					etDate.setEnabled(false);
					etDate.setFocusable(false);
					etDate.setClickable(false);

					etTime.setText(time);
					etTime.setEnabled(false);
					etTime.setFocusable(false);
					etTime.setClickable(false);
				}
			}

		}

	}

}
