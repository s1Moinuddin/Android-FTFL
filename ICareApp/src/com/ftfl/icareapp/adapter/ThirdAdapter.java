package com.ftfl.icareapp.adapter;

import java.util.ArrayList;

import com.ftfl.icareapp.R;
import com.ftfl.icareapp.util.DietChart;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ThirdAdapter extends ArrayAdapter<DietChart> {
	
	private final Activity context;
	ArrayList<DietChart> nextProfile;
	OnClickListener onClickListener;

	public ThirdAdapter(Activity context, ArrayList<DietChart> nextProfile) {

		super(context, R.layout.alldietrow, nextProfile);

		this.context = context;
		this.nextProfile = nextProfile;
	}
	

	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(int position, View view, ViewGroup parent) {

		DietChart nprofile;
		nprofile = nextProfile.get(position); // why it is used??

		LayoutInflater inflater = context.getLayoutInflater();

		View rowView = inflater.inflate(R.layout.alldietrow, null, true);

		TextView date = (TextView) rowView.findViewById(R.id.dateFF);
		TextView mealName = (TextView) rowView.findViewById(R.id.mealNameF);

		TextView textId = (TextView) rowView.findViewById(R.id.dbID3);
		textId.setText(nprofile.getID().toString()); // in order to use for
														// delete and edit in
														// DataBase

		date.setText(nprofile.getDate().toString());
		mealName.setText(nprofile.getEventName().toString());

		return rowView;
	}


}
