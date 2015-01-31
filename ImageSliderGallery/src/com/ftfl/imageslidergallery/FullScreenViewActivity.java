package com.ftfl.imageslidergallery;

import com.ftfl.imageslidergallery.adapter.FullScreenImageAdapter;
import com.ftfl.imageslidergallery.helper.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

public class FullScreenViewActivity extends Activity {

	private Utils mUtils = null;
	private FullScreenImageAdapter mAdapter = null;
	private ViewPager mViewPager = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fullscreen_view);

		mViewPager = (ViewPager) findViewById(R.id.pager);

		mUtils = new Utils(getApplicationContext());

		Intent intent = getIntent();
		int position = intent.getIntExtra("position", 0);

		mAdapter = new FullScreenImageAdapter(FullScreenViewActivity.this,
				mUtils.getFilePaths());

		mViewPager.setAdapter(mAdapter);

		// displaying selected image first
		mViewPager.setCurrentItem(position);
	}

}
