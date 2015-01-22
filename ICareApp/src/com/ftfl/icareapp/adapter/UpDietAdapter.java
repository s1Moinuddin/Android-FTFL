package com.ftfl.icareapp.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ftfl.icareapp.R;
import com.ftfl.icareapp.util.DietChart;

public class UpDietAdapter extends ArrayAdapter<DietChart> {

	Activity context;
	ArrayList<DietChart> dietChart;
	DietChart upDietChart;

	public UpDietAdapter(Activity context, ArrayList<DietChart> dietChart) {

		super(context, R.layout.upcomingrow, dietChart);

		this.context = context;
		this.dietChart = dietChart;
	}

	/********* Create a holder Class to contain inflated xml file elements *********/
	public static class ViewHolder {

		public TextView date;
		public TextView textId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		/***** Get each Model object from Array list ********/
		upDietChart = dietChart.get(position);
		ViewHolder holder = null;

		LayoutInflater inflater = context.getLayoutInflater();

		if (convertView == null) {

			convertView = inflater.inflate(R.layout.upcomingrow, null);
			holder = new ViewHolder();
			holder.date = (TextView) convertView.findViewById(R.id.dateSS);
			holder.textId = (TextView) convertView.findViewById(R.id.dbID2);

			convertView.setTag(holder);
		} else

			holder = (ViewHolder) convertView.getTag();
		holder.textId.setText(upDietChart.getID().toString());
		holder.date.setText(upDietChart.getDate().toString());

		return convertView;
	}

}
