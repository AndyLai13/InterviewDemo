package com.andylai.interviewdemo.annotation;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

@SuppressWarnings("FieldCanBeLocal")
public class PalmEraser {
	private final String TAG = PalmEraser.class.getSimpleName();

	private final float ERASER_SIZE_RATE = 5.5f;
	private final float ASPECT_RATIO = 0.66f;
	private final float MAX_ERASER_HEIGHT = 120.0f;
	private final float CORNER_RADIUS = 15;

	private final Paint mStrokePaint;
	private final Paint mFillPaint;
	private float mEraserWidth;
	private float mEraserHeight;
	private float mShapeInterval;
	private PointF mCenter;
	private RectF mBoundingBox;

	public PalmEraser() {
		mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
		mStrokePaint.setStyle(Paint.Style.STROKE);
		mStrokePaint.setColor(Color.BLACK);
		mStrokePaint.setStrokeWidth(5);
		mStrokePaint.setStrokeJoin(Paint.Join.ROUND);
		mStrokePaint.setStrokeCap(Paint.Cap.ROUND);
		mFillPaint = new Paint(mStrokePaint);
		mFillPaint.setStyle(Paint.Style.FILL);
		mFillPaint.setColor(Color.WHITE);
	}

	public void updatePalmEraserSize(float touchMajor) {
		// min <= mEraserHeight <= max
		mEraserHeight = Math.min(touchMajor * ERASER_SIZE_RATE, MAX_ERASER_HEIGHT);
		mEraserWidth = mEraserHeight * ASPECT_RATIO;
		mShapeInterval = mEraserWidth / 5;
	}

	public void draw(Canvas canvas) {
		Log.d(TAG, "eraser draw");
		if (mCenter != null) {
			float innerRectWidth = mShapeInterval;
			float innerRectHeight = mEraserHeight - mShapeInterval - mShapeInterval;

			//draw outer RoundRect
			float orgX = mBoundingBox.left;
			float orgY = mBoundingBox.top;

			//draw inner left RoundRect
			orgX += mShapeInterval;
			orgY += mShapeInterval;
			RectF innerLeftRect = new RectF(orgX, orgY, orgX + innerRectWidth, orgY + innerRectHeight);

			//draw inner right RoundRect
			orgX = orgX + mShapeInterval + mShapeInterval;
			RectF innerRightRect = new RectF(orgX, orgY, orgX + innerRectWidth, orgY + innerRectHeight);

			canvas.drawRoundRect(mBoundingBox, CORNER_RADIUS, CORNER_RADIUS, mFillPaint);
			canvas.drawRoundRect(mBoundingBox, CORNER_RADIUS, CORNER_RADIUS, mStrokePaint);
			canvas.drawRoundRect(innerLeftRect, CORNER_RADIUS, CORNER_RADIUS, mStrokePaint);
			canvas.drawRoundRect(innerRightRect, CORNER_RADIUS, CORNER_RADIUS, mStrokePaint);
		}
	}

	public RectF getBoundingBox() {
		//draw outer RoundRect
		float l = mCenter.x - (mEraserWidth / 2);
		float t = mCenter.y - (mEraserHeight / 2);
		float r = mCenter.x + (mEraserWidth / 2);
		float b = mCenter.y + (mEraserHeight / 2);

		mBoundingBox = new RectF(l, t, r, b);
		return mBoundingBox;
	}

	public void startTouch(float x, float y) {
		Log.d(TAG, "moveTouch");
		mCenter = new PointF(x, y);
		mBoundingBox = getBoundingBox();
	}

	public void moveTouch(float x, float y) {
		Log.d(TAG, "moveTouch");
		mCenter.set(x, y);
		mBoundingBox = getBoundingBox();
	}

	public void upTouch() {
		Log.d(TAG, "upTouch");
		mCenter = null;
		mBoundingBox = null;
	}
}
