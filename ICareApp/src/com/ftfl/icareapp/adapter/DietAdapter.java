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

public class DietAdapter extends ArrayAdapter<DietChart> {
	
	Activity context;
	ArrayList<DietChart> dietChart;
	DietChart todayChart;

	public DietAdapter(Activity context, ArrayList<DietChart> dietChart) {

		super(context, R.layout.todayrow, dietChart);

		this.context = context;
		this.dietChart = dietChart;
	}
	
	/********* Create a holder Class to contain inflated xml file elements *********/
	public static class ViewHolder {

		public TextView meal;
		public TextView time;
		public TextView textId;
		public TextView menu;

	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		/***** Get each Model object from Array list ********/
		todayChart = dietChart.get(position);
		ViewHolder holder = null;
		
		LayoutInflater inflater = context.getLayoutInflater();
		
		if (convertView == null) {

			convertView = inflater.inflate(R.layout.todayrow, null);
			holder = new ViewHolder();
			holder.meal = (TextView) convertView.findViewById(R.id.meal);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.menu = (TextView) convertView.findViewById(R.id.menu);
			holder.textId = (TextView) convertView.findViewById(R.id.dbID);
		
		/************ Set holder with LayoutInflater ************/
		convertView.setTag(holder);
	} else
		
		holder = (ViewHolder) convertView.getTag();
		
		holder.textId.setText(todayChart.getID().toString()); 
		holder.time.setText(todayChart.getTime().toString());
		holder.meal.setText(todayChart.getEventName().toString());
		holder.menu.setText(todayChart.getMenu().toString());

		return convertView;
	}
}
