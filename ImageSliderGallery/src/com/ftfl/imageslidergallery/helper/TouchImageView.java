package com.ftfl.imageslidergallery.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

@SuppressLint("ClickableViewAccessibility")
public class TouchImageView extends ImageView {

	Matrix matrix = null;

	// We can be in one of these 3 states
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;

	// Remember some things for zooming
	PointF mLast = new PointF();
	PointF mStart = new PointF();
	float mMinScale = 1f;
	float mMaxScale = 3f;
	float[] m;

	int mViewWidth = 0, mViewHeight = 0;
	static final int CLICK = 3;
	float mSaveScale = 1f;
	protected float origWidth = 0f, origHeight = 0f;
	int oldMeasuredWidth = 0, oldMeasuredHeight = 0;

	ScaleGestureDetector mScaleDetector = null;

	Context context;

	public TouchImageView(Context context) {
		super(context);
		sharedConstructing(context);
	}

	public TouchImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		sharedConstructing(context);
	}

	private void sharedConstructing(Context context) {
		super.setClickable(true);
		this.context = context;
		mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
		matrix = new Matrix();
		m = new float[9];
		setImageMatrix(matrix);
		setScaleType(ScaleType.MATRIX);

		setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mScaleDetector.onTouchEvent(event);
				PointF curr = new PointF(event.getX(), event.getY());

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mLast.set(curr);
					mStart.set(mLast);
					mode = DRAG;
					break;

				case MotionEvent.ACTION_MOVE:
					if (mode == DRAG) {
						float deltaX = curr.x - mLast.x;
						float deltaY = curr.y - mLast.y;
						float fixTransX = getFixDragTrans(deltaX, mViewWidth,
								origWidth * mSaveScale);
						float fixTransY = getFixDragTrans(deltaY, mViewHeight,
								origHeight * mSaveScale);
						matrix.postTranslate(fixTransX, fixTransY);
						fixTrans();
						mLast.set(curr.x, curr.y);
					}
					break;

				case MotionEvent.ACTION_UP:
					mode = NONE;
					int xDiff = (int) Math.abs(curr.x - mStart.x);
					int yDiff = (int) Math.abs(curr.y - mStart.y);
					if (xDiff < CLICK && yDiff < CLICK)
						performClick();
					break;

				case MotionEvent.ACTION_POINTER_UP:
					mode = NONE;
					break;
				}

				setImageMatrix(matrix);
				invalidate();
				return true; // indicate event was handled
			}

		});
	}

	public void setMaxZoom(float x) {
		mMaxScale = x;
	}

	private class ScaleListener extends
			ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector) {
			mode = ZOOM;
			return true;
		}

		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			float mScaleFactor = detector.getScaleFactor();
			float origScale = mSaveScale;
			mSaveScale *= mScaleFactor;
			if (mSaveScale > mMaxScale) {
				mSaveScale = mMaxScale;
				mScaleFactor = mMaxScale / origScale;
			} else if (mSaveScale < mMinScale) {
				mSaveScale = mMinScale;
				mScaleFactor = mMinScale / origScale;
			}

			if (origWidth * mSaveScale <= mViewWidth
					|| origHeight * mSaveScale <= mViewHeight)
				matrix.postScale(mScaleFactor, mScaleFactor, mViewWidth / 2,
						mViewHeight / 2);
			else
				matrix.postScale(mScaleFactor, mScaleFactor,
						detector.getFocusX(), detector.getFocusY());

			fixTrans();
			return true;
		}
	}

	void fixTrans() {
		matrix.getValues(m);
		float transX = m[Matrix.MTRANS_X];
		float transY = m[Matrix.MTRANS_Y];

		float fixTransX = getFixTrans(transX, mViewWidth, origWidth
				* mSaveScale);
		float fixTransY = getFixTrans(transY, mViewHeight, origHeight
				* mSaveScale);

		if (fixTransX != 0 || fixTransY != 0)
			matrix.postTranslate(fixTransX, fixTransY);
	}

	float getFixTrans(float trans, float viewSize, float contentSize) {
		float minTrans, maxTrans;

		if (contentSize <= viewSize) {
			minTrans = 0;
			maxTrans = viewSize - contentSize;
		} else {
			minTrans = viewSize - contentSize;
			maxTrans = 0;
		}

		if (trans < minTrans)
			return -trans + minTrans;
		if (trans > maxTrans)
			return -trans + maxTrans;
		return 0;
	}

	float getFixDragTrans(float delta, float viewSize, float contentSize) {
		if (contentSize <= viewSize) {
			return 0;
		}
		return delta;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mViewWidth = MeasureSpec.getSize(widthMeasureSpec);
		mViewHeight = MeasureSpec.getSize(heightMeasureSpec);

		//
		// Rescales image on rotation
		//
		if (oldMeasuredHeight == mViewWidth && oldMeasuredHeight == mViewHeight
				|| mViewWidth == 0 || mViewHeight == 0)
			return;
		oldMeasuredHeight = mViewHeight;
		oldMeasuredWidth = mViewWidth;

		if (mSaveScale == 1) {
			// Fit to screen.
			float scale;

			Drawable drawable = getDrawable();
			if (drawable == null || drawable.getIntrinsicWidth() == 0
					|| drawable.getIntrinsicHeight() == 0)
				return;
			int bmWidth = drawable.getIntrinsicWidth();
			int bmHeight = drawable.getIntrinsicHeight();

			Log.d("bmSize", "bmWidth: " + bmWidth + " bmHeight : " + bmHeight);

			float scaleX = (float) mViewWidth / (float) bmWidth;
			float scaleY = (float) mViewHeight / (float) bmHeight;
			scale = Math.min(scaleX, scaleY);
			matrix.setScale(scale, scale);

			// Center the image
			float redundantYSpace = (float) mViewHeight
					- (scale * (float) bmHeight);
			float redundantXSpace = (float) mViewWidth
					- (scale * (float) bmWidth);
			redundantYSpace /= (float) 2;
			redundantXSpace /= (float) 2;

			matrix.postTranslate(redundantXSpace, redundantYSpace);

			origWidth = mViewWidth - 2 * redundantXSpace;
			origHeight = mViewHeight - 2 * redundantYSpace;
			setImageMatrix(matrix);
		}
		fixTrans();
	}

}
