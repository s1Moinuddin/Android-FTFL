package com.ftfl.icareapp.database;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ftfl.icareapp.util.DietChart;

public class SQDataSource {

	// Database fields
	private SQLiteDatabase dietDataBase;
	SQLiteHelper dietDBhelper;

	List<DietChart> dietList = new ArrayList<DietChart>();
	public String mCurrentDate = "";

	public SQDataSource(Context context) {
		dietDBhelper = new SQLiteHelper(context);

	}

	// open a method for writable database
	public void open() throws SQLException {
		dietDataBase = dietDBhelper.getWritableDatabase();
	}

	// close database connection
	public void close() {
		dietDBhelper.close();
	}

	// taking current date from system
	// @SuppressLint("SimpleDateFormat")
	public void currentDate() {
		DateFormat dateFormat = new SimpleDateFormat("dd/M/yyyy",
				Locale.getDefault());
		Date date = new Date();
		mCurrentDate = dateFormat.format(date);
	}

	// insert data into the database.

	public long insert(DietChart eChart) {

		this.open();

		ContentValues values = new ContentValues();

		values.put(SQLiteHelper.COLUMNL_NAME_FIELD, eChart.getEventName());
		values.put(SQLiteHelper.COLUMNL_DATE_FIELD, eChart.getDate());
		values.put(SQLiteHelper.COLUMNL_TIME_FIELD, eChart.getTime());
		values.put(SQLiteHelper.COLUMNL_MENU_FIELD, eChart.getMenu());

		long inserted = dietDataBase.insert(SQLiteHelper.TABLE_NAME, null,
				values);
		dietDataBase.close();
		return inserted;
	}

	public Cursor getData(int id) {
		this.open();
		Cursor cursor = dietDataBase.rawQuery(
				"select * from diet_chart where _id=" + id + "", null);
		return cursor;
	}

	// update database by Id
	public long updateData(Integer id, DietChart eChart) {
		this.open();
		ContentValues values = new ContentValues();

		values.put(SQLiteHelper.COLUMNL_NAME_FIELD, eChart.getEventName());
		values.put(SQLiteHelper.COLUMNL_DATE_FIELD, eChart.getDate());
		values.put(SQLiteHelper.COLUMNL_TIME_FIELD, eChart.getTime());
		values.put(SQLiteHelper.COLUMNL_MENU_FIELD, eChart.getMenu());

		long updated = 0;

		try {
			updated = dietDataBase.update(SQLiteHelper.TABLE_NAME, values,
					SQLiteHelper.COLUMNL_ID_FIELD + "=" + id, null);

		} catch (Exception ex) {
			Log.e("ERROR", "data insertion problem");
		}

		dietDataBase.close();
		return updated;
	}

	// delete data form database.
	public Integer deleteData(Integer id) {
		this.open();
		return dietDataBase.delete(SQLiteHelper.TABLE_NAME,
				SQLiteHelper.COLUMNL_ID_FIELD + " = ? ",
				new String[] { Integer.toString(id) });

	}

	/*
	 * using cursor for display present data from database.
	 */
	public ArrayList<DietChart> getDietProfiles() {

		ArrayList<DietChart> dietProfiles = new ArrayList<DietChart>();
		this.open();
		currentDate();

		Cursor cursor = dietDataBase.query(SQLiteHelper.TABLE_NAME, null,
				SQLiteHelper.COLUMNL_DATE_FIELD + "= '" + mCurrentDate + "' ",
				null, null, null, null);

		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			for (int i = 0; i < cursor.getCount(); i++) {

				int id = cursor.getInt(cursor
						.getColumnIndex(SQLiteHelper.COLUMNL_ID_FIELD));
				String name = cursor.getString(cursor
						.getColumnIndex(SQLiteHelper.COLUMNL_NAME_FIELD));
				String date = cursor.getString(cursor
						.getColumnIndex(SQLiteHelper.COLUMNL_DATE_FIELD));
				String time = cursor.getString(cursor
						.getColumnIndex(SQLiteHelper.COLUMNL_TIME_FIELD));
				String menu = cursor.getString(cursor
						.getColumnIndex(SQLiteHelper.COLUMNL_MENU_FIELD));

				DietChart fItem = new DietChart(name, date, menu, time, id);
				dietProfiles.add(fItem);
				cursor.moveToNext();
			}
		}
		cursor.close();
		dietDataBase.close();

		return dietProfiles;
	}

	/*
	 * using cursor for display upcomingDiet data from database.
	 */
	public ArrayList<DietChart> getUpcomingProfiles() {

		ArrayList<DietChart> dietProfiles = new ArrayList<DietChart>();
		this.open();
		currentDate();

		Cursor cursor = dietDataBase.query(SQLiteHelper.TABLE_NAME, null,
				SQLiteHelper.COLUMNL_DATE_FIELD + "> '" + mCurrentDate + "' ",
				null, null, null, null);

		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			for (int i = 0; i < cursor.getCount(); i++) {

				int id = cursor.getInt(cursor
						.getColumnIndex(SQLiteHelper.COLUMNL_ID_FIELD));
				String name = cursor.getString(cursor
						.getColumnIndex(SQLiteHelper.COLUMNL_NAME_FIELD));
				String date = cursor.getString(cursor
						.getColumnIndex(SQLiteHelper.COLUMNL_DATE_FIELD));
				String time = cursor.getString(cursor
						.getColumnIndex(SQLiteHelper.COLUMNL_TIME_FIELD));
				String menu = cursor.getString(cursor
						.getColumnIndex(SQLiteHelper.COLUMNL_MENU_FIELD));

				DietChart fItem = new DietChart(name, date, menu, time, id);
				dietProfiles.add(fItem);
				cursor.moveToNext();
			}
		}
		cursor.close();
		dietDataBase.close();

		return dietProfiles;
	}

	/*
	 * using cursor for display weekly data from database.
	 */
	public ArrayList<DietChart> getWeeklyDates() {
		this.open();
		currentDate();

		ArrayList<DietChart> weeklyDates = new ArrayList<DietChart>();

		Cursor cursor = dietDataBase.query(SQLiteHelper.TABLE_NAME, null,
				SQLiteHelper.COLUMNL_DATE_FIELD + "<= '" + mCurrentDate
						+ "' ORDER BY date DESC Limit 7", null, null, null,
				null);
		/*
		 * alternate way Cursor cursor = dietDataBase.rawQuery(
		 * "SELECT DISTINCT date FROM diet_chart  WHERE date <= '" +
		 * mCurrentDate + "' ORDER BY date DESC Limit 7", null);
		 */

		if (cursor != null) {
			if (cursor.moveToFirst()) {

				do {
					int id = cursor.getInt(cursor
							.getColumnIndex(SQLiteHelper.COLUMNL_ID_FIELD));
					String name = cursor.getString(cursor
							.getColumnIndex(SQLiteHelper.COLUMNL_NAME_FIELD));
					String date = cursor.getString(cursor
							.getColumnIndex(SQLiteHelper.COLUMNL_DATE_FIELD));
					String time = cursor.getString(cursor
							.getColumnIndex(SQLiteHelper.COLUMNL_TIME_FIELD));
					String menu = cursor.getString(cursor
							.getColumnIndex(SQLiteHelper.COLUMNL_MENU_FIELD));

					DietChart fItem = new DietChart(name, date, menu, time, id);
					weeklyDates.add(fItem);

				} while (cursor.moveToNext());
			}
			cursor.close();
		}
		this.close();
		return weeklyDates;
	}

	/*
	 * using cursor for display monthly data from database.
	 */
	public ArrayList<DietChart> getMonthlyDates() {
		this.open();
		currentDate();

		ArrayList<DietChart> monthlyDates = new ArrayList<DietChart>();

		Cursor cursor = dietDataBase.query(SQLiteHelper.TABLE_NAME, null,
				SQLiteHelper.COLUMNL_DATE_FIELD + "<= '" + mCurrentDate
						+ "' ORDER BY date DESC Limit 30", null, null, null,
				null);

		if (cursor != null) {
			if (cursor.moveToFirst()) {

				do {
					int id = cursor.getInt(cursor
							.getColumnIndex(SQLiteHelper.COLUMNL_ID_FIELD));
					String name = cursor.getString(cursor
							.getColumnIndex(SQLiteHelper.COLUMNL_NAME_FIELD));
					String date = cursor.getString(cursor
							.getColumnIndex(SQLiteHelper.COLUMNL_DATE_FIELD));
					String time = cursor.getString(cursor
							.getColumnIndex(SQLiteHelper.COLUMNL_TIME_FIELD));
					String menu = cursor.getString(cursor
							.getColumnIndex(SQLiteHelper.COLUMNL_MENU_FIELD));

					DietChart fItem = new DietChart(name, date, menu, time, id);
					monthlyDates.add(fItem);

				} while (cursor.moveToNext());
			}
			cursor.close();
		}
		this.close();
		return monthlyDates;
	}

	/*
	 * using cursor for display All data from database.
	 */
	public ArrayList<DietChart> getAllDates() {
		this.open();

		ArrayList<DietChart> allDates = new ArrayList<DietChart>();

		Cursor cursor = dietDataBase.query(SQLiteHelper.TABLE_NAME, null, null,
				null, null, null, null);

		if (cursor != null) {
			if (cursor.moveToFirst()) {

				do {
					int id = cursor.getInt(cursor
							.getColumnIndex(SQLiteHelper.COLUMNL_ID_FIELD));
					String name = cursor.getString(cursor
							.getColumnIndex(SQLiteHelper.COLUMNL_NAME_FIELD));
					String date = cursor.getString(cursor
							.getColumnIndex(SQLiteHelper.COLUMNL_DATE_FIELD));
					String time = cursor.getString(cursor
							.getColumnIndex(SQLiteHelper.COLUMNL_TIME_FIELD));
					String menu = cursor.getString(cursor
							.getColumnIndex(SQLiteHelper.COLUMNL_MENU_FIELD));

					DietChart fItem = new DietChart(name, date, menu, time, id);
					allDates.add(fItem);

				} while (cursor.moveToNext());
			}
			cursor.close();
		}
		this.close();
		return allDates;
	}

}
