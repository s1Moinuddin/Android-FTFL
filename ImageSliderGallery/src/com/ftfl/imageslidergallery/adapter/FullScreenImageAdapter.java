package com.ftfl.imageslidergallery.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.ftfl.imageslidergallery.R;
import com.ftfl.imageslidergallery.helper.TouchImageView;

public class FullScreenImageAdapter extends PagerAdapter {

	private Activity _mActivity = null;
    private ArrayList<String> _mImagePaths = null;
    private LayoutInflater mInflater = null;
 
    // constructor
    public FullScreenImageAdapter(Activity activity,
            ArrayList<String> imagePaths) {
        this._mActivity = activity;
        this._mImagePaths = imagePaths;
    }
 
    @Override
    public int getCount() {
        return this._mImagePaths.size();
    }
 
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }
     
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
    	TouchImageView imgDisplay;
        Button btnClose;
  
        mInflater = (LayoutInflater) _mActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = mInflater.inflate(R.layout.layout_fullscreen_image, container,
                false);
  
        imgDisplay = (TouchImageView) viewLayout.findViewById(R.id.imgDisplay);
        btnClose = (Button) viewLayout.findViewById(R.id.btnClose);
         
        BitmapFactory.Options options = new BitmapFactory.Options();
       // options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inSampleSize = 8; // for better performance. to stop memory out of bound
        Bitmap bitmap = BitmapFactory.decodeFile(_mImagePaths.get(position), options);
        imgDisplay.setImageBitmap(bitmap);
         
        // close button click event
        btnClose.setOnClickListener(new View.OnClickListener() {            
            @Override
            public void onClick(View v) {
                _mActivity.finish();
            }
        });
  
        ((ViewPager) container).addView(viewLayout);
  
        return viewLayout;
    }
     
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);
  
    }

}
