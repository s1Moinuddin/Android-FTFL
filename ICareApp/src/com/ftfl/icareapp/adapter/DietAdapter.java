package com.ftfl.icareapp.adapter;

import java.util.ArrayList;

import com.ftfl.icareapp.R;
import com.ftfl.icareapp.util.DietChart;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DietAdapter extends ArrayAdapter<DietChart> {
	
	private final Activity context;
	ArrayList<DietChart> dietProfile;

	public DietAdapter(Activity context, ArrayList<DietChart> dietProfile) {

		super(context, R.layout.todayrow, dietProfile);

		this.context = context;
		this.dietProfile = dietProfile;
	}
	
	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(int position, View view, ViewGroup parent) {

		DietChart mprofile;
		mprofile = dietProfile.get(position); // why it is used??

		LayoutInflater inflater = context.getLayoutInflater();

		View rowView = inflater.inflate(R.layout.todayrow, null, true);

		TextView meal = (TextView) rowView.findViewById(R.id.meal);
		TextView time = (TextView) rowView.findViewById(R.id.time);
		TextView menu = (TextView) rowView.findViewById(R.id.menu);

		TextView textId = (TextView) rowView.findViewById(R.id.dbID);
		textId.setText(mprofile.getID().toString()); // in order to use for
														// delete and edit in
														// DataBase

		time.setText(mprofile.getTime().toString());
		meal.setText(mprofile.getEventName().toString());
		menu.setText(mprofile.getMenu().toString());

		return rowView;
	}
}
