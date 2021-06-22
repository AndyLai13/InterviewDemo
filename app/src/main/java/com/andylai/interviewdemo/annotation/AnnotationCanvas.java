package com.andylai.interviewdemo.annotation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import static android.graphics.Path.Direction.CW;

public class AnnotationCanvas extends View {
	private final String TAG = AnnotationCanvas.class.getSimpleName();

	/**
	 * Multi-touch point maximum is 20 for now.
	 */
	private static final int MAXIMUM_MULTI_TOUCH_POINT = 20;

	/**
	 * Palm eraser major size is 40 for now.
	 */
	private static final int PALM_ERASER_MAJOR_SIZE = 40;

	/**
	 * Last point of each stroke
	 */
	private final SparseArray<PointF> mLastTouchPoints = new SparseArray<>(MAXIMUM_MULTI_TOUCH_POINT);
	private final SparseArray<PointF> mLastMiddlePoints = new SparseArray<>(MAXIMUM_MULTI_TOUCH_POINT);

	/**
	 * Record all strokes at same time.
	 */
	private ArrayList<Stroke> mStrokes = new ArrayList<>();

	/**
	 * Record all historical strokes after drawing.
	 */
	private final ArrayList<ArrayList<Stroke>> mStrokesList = new ArrayList<>();

	/**
	 * Record temporary removed items when Undo and Redo is operating.
	 */
	private final ArrayList<ArrayList<Stroke>> mComplementaryList = new ArrayList<>();

	/**
	 * Record current stokes list when clear is operating, then it is able to
	 * recover if undo is operating.
	 */
	private final ArrayList<ArrayList<Stroke>> mClearList = new ArrayList<>();

	private final PalmEraser mPalmEraser;
	private Bitmap mBitmap;
	private Canvas mCanvas;
	private Paint mStrokePaint;
	private boolean mIsPalmEraser = false;

	public AnnotationCanvas(Context context) {
		this(context, null);
	}

	public AnnotationCanvas(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public AnnotationCanvas(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initPaint();
		setPenMode();
		mPalmEraser = new PalmEraser();
	}

	// region view override implementation
	//-------------------------------------------------------------------------
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mCanvas != null) {
			mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
			for (ArrayList<Stroke> strokes : mStrokesList) {
				for (Stroke stroke : strokes) {
					mCanvas.drawPath(stroke, stroke.getPaint());
				}
			}
			for (Stroke stroke : mStrokes) {
				mCanvas.drawPath(stroke, stroke.getPaint());
			}
			canvas.drawBitmap(mBitmap, 0, 0, null);
			if (mIsPalmEraser) {
				mPalmEraser.draw(canvas);
			}
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		print("event = " + event.getAction());
		int actionIndex = event.getActionIndex();
		int id = event.getPointerId(actionIndex);
		if (id >= MAXIMUM_MULTI_TOUCH_POINT) return false;
		switch (event.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				mStrokes = new ArrayList<>();
				float touchMajor = event.getTouchMajor(actionIndex);
				print("touchMajor = " + touchMajor);
				if (touchMajor > PALM_ERASER_MAJOR_SIZE) {
					mIsPalmEraser = true;
					palmDown(event);
				} else {
					penDown(event);
				}
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				if (!mIsPalmEraser) {
					penDown(event);
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if (mIsPalmEraser) {
					palmMove(event);
				} else {
					penMove(event);
				}
				break;
			case MotionEvent.ACTION_POINTER_UP:
				if (!mIsPalmEraser) {
					penUp(event);
				}
				break;
			case MotionEvent.ACTION_UP:
				if (mIsPalmEraser) {
					palmUp(event);
				} else {
					penUp(event);
				}
				saveStrokes(mStrokes);
				break;
			case MotionEvent.ACTION_CANCEL:
				mIsPalmEraser = false;
//				AcceleratorManager.getInstance().onTouchCancel();
				break;
		}
//		if (!AcceleratorManager.getInstance().isAccelerating()) {
		invalidate();
//		}
		return true;
	}

	public void clear() {
		mClearList.addAll(mStrokesList);
		mStrokesList.clear();
		invalidate();
	}

	public void undo() {
		if (!mClearList.isEmpty()) {
			print("clear undo");
			mStrokesList.addAll(mClearList);
			mClearList.clear();
		} else if (mStrokesList.size() > 0) {
			ArrayList<Stroke> target = mStrokesList.get(mStrokesList.size() - 1);
			mStrokesList.remove(target);
			mComplementaryList.add(target);
		}
		invalidate();
	}

	public void redo() {
		if (reDoable()) {
			ArrayList<Stroke> target = mComplementaryList.get(mComplementaryList.size() - 1);
			mComplementaryList.remove(target);
			mStrokesList.add(target);
			invalidate();
		}
	}

	public void setPenColor(int color) {
		PaintManager.setPenColor(color);
	}

	public void setHighlightColor(int color) {
		PaintManager.setHighlightColor(color);
	}

	public void setPenMode() {
		PaintManager.setDrawMode(DrawMode.DRAW);
		PaintManager.setRestoreDrawMode(DrawMode.ERASER);
	}

	public void setHighlighterMode() {
		PaintManager.setDrawMode(DrawMode.HIGHLIGHTER);
		PaintManager.setRestoreDrawMode(DrawMode.ERASER);
	}

	public void setEraserMode() {
		PaintManager.setDrawMode(DrawMode.ERASER);
		PaintManager.setRestoreDrawMode(DrawMode.ERASER);
	}

	public void setPalmEraserMode() {
		PaintManager.setDrawMode(DrawMode.PALM_ERASER);
	}

	private static class Stroke extends Path {
		private final Paint mPaint;

		// Use this.paint = paint lead to passing reference then stroke
		// always get same paint object, here new constructor has been used
		// to copy Paint object and attributes.
		public Stroke(Paint paint) {
			mPaint = new Paint(paint);
		}

		public Paint getPaint() {
			return mPaint;
		}
	}

	private void initPaint() {
		mStrokePaint = PaintManager.getPaint();
		post(() -> {
			mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
			mCanvas = new Canvas(mBitmap);
		});
	}

	private void print(String message) {
		System.out.println(message);
		Log.d(TAG, message);
	}

	private void penDown(MotionEvent event) {
//		if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
//			AcceleratorManager.getInstance().onTouchDown(event, mStrokePaint, false);
//		} else {
//			AcceleratorManager.getInstance().onTouchPointerDown(event, mStrokePaint);
//		}

		int id = event.getPointerId(event.getActionIndex());
		mComplementaryList.clear();
		mLastTouchPoints.put(id, new PointF(event.getX(id), event.getY(id)));
		mLastMiddlePoints.put(id, new PointF(event.getX(id), event.getY(id)));
		mStrokes.add(id, new Stroke(mStrokePaint));
		mStrokes.get(id).moveTo(mLastTouchPoints.get(id).x, mLastTouchPoints.get(id).y);
	}

	private void penMove(MotionEvent event) {
//		AcceleratorManager.getInstance().onTouchMove(event, mStrokePaint);

		for (int i = 0; i < event.getPointerCount(); i++) {
			int id = event.getPointerId(i);
			float touchX = event.getX(i);
			float touchY = event.getY(i);
			float middleX = (mLastTouchPoints.get(id).x + event.getX(i)) / 2;
			float middleY = (mLastTouchPoints.get(id).y + event.getY(i)) / 2;

			mStrokes.get(id).cubicTo(mLastMiddlePoints.get(id).x, mLastMiddlePoints.get(id).y,
					mLastTouchPoints.get(id).x, mLastTouchPoints.get(id).y,
					middleX, middleY);

			mLastTouchPoints.get(id).x = touchX;
			mLastTouchPoints.get(id).y = touchY;
			mLastMiddlePoints.get(id).x = middleX;
			mLastMiddlePoints.get(id).y = middleY;
		}
	}

	private void penUp(MotionEvent event) {
		int id = event.getPointerId(event.getActionIndex());
//		if (event.getActionMasked() == MotionEvent.ACTION_POINTER_UP) {
//			AcceleratorManager.getInstance().onTouchPointerUp(event, mStrokePaint, mStrokes.get(id));
//		} else {
//			AcceleratorManager.getInstance().onTouchUp(event, mStrokePaint, mStrokes.get(id));
//		}
	}

	private void palmDown(MotionEvent event) {
//		AcceleratorManager.getInstance().onTouchDown(event, mStrokePaint, true);

		print("[palmDown] isPalmEraser = " + mIsPalmEraser);
		setPalmEraserMode();

		int id = 0;
		float touchX = event.getX(id);
		float touchY = event.getY(id);
		mComplementaryList.clear();
		mLastTouchPoints.put(id, new PointF(touchX, touchY));
		mStrokes.add(id, new Stroke(mStrokePaint));
		mStrokes.get(id).moveTo(mLastTouchPoints.get(id).x, mLastTouchPoints.get(id).y);

		mPalmEraser.updatePalmEraserSize(event.getTouchMajor());
		mPalmEraser.startTouch(touchX, touchY);
		mStrokes.get(id).addRoundRect(mPalmEraser.getBoundingBox(), 5, 5, CW);
	}

	private void palmMove(MotionEvent event) {
//		AcceleratorManager.getInstance().onTouchMove(event, mStrokePaint);

		int id = 0;
		float touchX = event.getX(id);
		float touchY = event.getY(id);

		mPalmEraser.moveTouch(touchX, touchY);
		mStrokes.get(id).addRoundRect(mPalmEraser.getBoundingBox(), 5, 5, CW);
	}

	private void palmUp(MotionEvent event) {
//		AcceleratorManager.getInstance().onTouchUp(event, mStrokePaint, mStrokes.get(0));

		PaintManager.restoreStrokePaint();
		mIsPalmEraser = false;
		print("[palmUp] isPalmEraser = " + mIsPalmEraser);

		mPalmEraser.upTouch();
	}

	private void saveStrokes(ArrayList<Stroke> strokes) {
		mStrokesList.add(new ArrayList<>(strokes));
		mClearList.clear();
		mStrokes.clear();
	}

	private boolean unDoable() {
		return !mClearList.isEmpty() || mStrokesList.size() > 0;
	}

	private boolean reDoable() {
		return mComplementaryList.size() > 0;
	}
}