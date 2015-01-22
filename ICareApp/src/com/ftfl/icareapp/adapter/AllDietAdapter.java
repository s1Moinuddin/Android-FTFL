package com.ftfl.icareapp.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ftfl.icareapp.R;
import com.ftfl.icareapp.util.DietChart;

public class AllDietAdapter extends ArrayAdapter<DietChart> {

	Activity context;
	ArrayList<DietChart> dietChart;
	DietChart alDietChart;
	OnClickListener onClickListener;

	public AllDietAdapter(Activity context, ArrayList<DietChart> dietChart) {

		super(context, R.layout.alldietrow, dietChart);

		this.context = context;
		this.dietChart = dietChart;
	}

	/********* Create a holder Class to contain inflated xml file elements *********/
	public static class ViewHolder {

		public TextView dateTxt;
		public TextView mealTxt;
		public TextView textId;

	}

	/****** Depends upon data size called for each row , Create each ListView row *****/
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		/***** Get each Model object from Array list ********/
		alDietChart = dietChart.get(position); 
		
		ViewHolder holder = null;

		/*********** Layout inflator to call external xml layout () ***********/
		LayoutInflater inflater = context.getLayoutInflater();

		if (convertView == null) {

			/****** Inflate tabitem.xml file for each row ( Defined below ) *******/
			convertView = inflater.inflate(R.layout.alldietrow, null);

			/****** View Holder Object to contain tabitem.xml file elements ******/
			holder = new ViewHolder();
			holder.dateTxt = (TextView) convertView.findViewById(R.id.dateFF);
			holder.mealTxt = (TextView) convertView
					.findViewById(R.id.mealNameF);
			holder.textId = (TextView) convertView.findViewById(R.id.dbID3);
			
			/************ Set holder with LayoutInflater ************/
			convertView.setTag(holder);
		} else
			
			holder = (ViewHolder) convertView.getTag();

		/*
		 * textId to use for delete and edit in DataBase
		 */
		holder.textId.setText(alDietChart.getID().toString());
		holder.dateTxt.setText(alDietChart.getDate().toString());
		holder.mealTxt.setText(alDietChart.getEventName().toString());

		return convertView;
	}

}
