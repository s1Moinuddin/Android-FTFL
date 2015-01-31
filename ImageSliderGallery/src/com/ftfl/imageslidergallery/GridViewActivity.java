package com.ftfl.imageslidergallery;

import java.util.ArrayList;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.GridView;

import com.ftfl.imageslidergallery.adapter.GridViewImageAdapter;
import com.ftfl.imageslidergallery.helper.FTFLConstants;
import com.ftfl.imageslidergallery.helper.Utils;

public class GridViewActivity extends Activity {

	private Utils mUtils = null;
	private ArrayList<String> mImagePaths = null;
	private GridViewImageAdapter mAdapter = null;
	private GridView mGridView = null;
	private int mColumnWidth = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grid_view);

		mGridView = (GridView) findViewById(R.id.grid_view);

		mUtils = new Utils(this);
		mImagePaths = new ArrayList<String>();

		// Initilizing Grid View
		InitilizeGridLayout();

		// loading all image paths from SD card
		mImagePaths = mUtils.getFilePaths();

		// Gridview mAdapter
		mAdapter = new GridViewImageAdapter(GridViewActivity.this, mImagePaths,
				mColumnWidth);

		// setting grid view mAdapter
		mGridView.setAdapter(mAdapter);
	}

	private void InitilizeGridLayout() {
		Resources r = getResources();
		float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				FTFLConstants.GRID_PADDING, r.getDisplayMetrics());

		mColumnWidth = (int) ((mUtils.getScreenWidth() - ((FTFLConstants.NUM_OF_COLUMNS + 1) * padding)) / FTFLConstants.NUM_OF_COLUMNS);

		mGridView.setNumColumns(FTFLConstants.NUM_OF_COLUMNS);
		mGridView.setColumnWidth(mColumnWidth);
		mGridView.setStretchMode(GridView.NO_STRETCH);
		mGridView.setPadding((int) padding, (int) padding, (int) padding,
				(int) padding);
		mGridView.setHorizontalSpacing((int) padding);
		mGridView.setVerticalSpacing((int) padding);
	}

}
