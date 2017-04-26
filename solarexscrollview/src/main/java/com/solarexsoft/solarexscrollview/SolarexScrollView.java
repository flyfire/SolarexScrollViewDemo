package com.solarexsoft.solarexscrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Scroller;

/**
 * Created by houruhou on 23/04/2017.
 */

public class SolarexScrollView extends ViewGroup {

    private int mScreenHeight;
    private Scroller mScroller;
    private int mLastY;
    private int mStart;
    private int mEnd;

    public SolarexScrollView(Context context) {
        super(context);
        initView(context);
    }

    public SolarexScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SolarexScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        ViewGroup.MarginLayoutParams marginLayoutParams = (MarginLayoutParams) getLayoutParams();
        marginLayoutParams.height = mScreenHeight * childCount;
        setLayoutParams(marginLayoutParams);
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() != View.GONE) {
                childView.layout(l, i * mScreenHeight, r, (i + 1) * mScreenHeight);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = y;
                mStart = getScrollY();
                Log.d("Solarex", "down: y: " + mLastY + ",mStart: " + mStart);
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mScroller.isFinished()){
                    mScroller.abortAnimation();
                }
                int dY = mLastY - y;
                Log.d("Solarex", "move: dY = " + dY + ",y = " + y + ",scrollY = " + getScrollY() + ",getHeight = " + getHeight());
                if (getScrollY() < 0) {
                    dY = 0;
                }
                if (getScrollY() > getHeight() - mScreenHeight){
                    dY = 0;
                }
                scrollBy(0, dY);
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
//                int dScrollY = checkAlignment();
//                Log.d(TAG, "up | dScrollY = " + dScrollY + ",scrollY = " + getScrollY());
//                if (dScrollY > 0){
//                    if (dScrollY < mScreenHeight/3){
//                        Log.d(TAG, "1");
//                        mScroller.startScroll(0, getScrollY(), 0, -dScrollY);
//                    } else {
//                        Log.d(TAG, "2");
//                        mScroller.startScroll(0, getScrollY(), 0, mScreenHeight - dScrollY);
//                    }
//                } else {
//                    if (-dScrollY < mScreenHeight/3){
//                        Log.d(TAG, "3");
//                        mScroller.startScroll(0, getScrollY(), 0, -dScrollY);
//                    } else {
//                        Log.d(TAG, "4");
//                        mScroller.startScroll(0, getScrollY(), 0, -mScreenHeight-dScrollY);
//                    }
//                }
                int mEnd = getScrollY();
                Log.d("Solarex", "scrollY = " + mEnd);
                boolean isUp = (mEnd - mStart) > 0 ? true : false;
                int dScrollY = mEnd - mStart;
                Log.d("Solarex", "dScrollY = " + dScrollY + ",isUp = " + isUp);
                if (isUp){
                    if (dScrollY < mScreenHeight/3){
                        Log.d("Solarex", "1");
                        mScroller.startScroll(0, getScrollY(), 0, -dScrollY);
                    } else {
                        Log.d("Solarex", "2");
                        mScroller.startScroll(0, getScrollY(), 0, mScreenHeight - dScrollY);
                    }
                } else {
                    if (-dScrollY < mScreenHeight/3){
                        Log.d("Solarex", "3");
                        mScroller.startScroll(0, getScrollY(), 0, -dScrollY);
                    } else {
                        Log.d("Solarex", "4");
                        mScroller.startScroll(0, getScrollY(), 0, -mScreenHeight-dScrollY);
                    }
                }
                break;
        }
        postInvalidate();
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()){
            int y = mScroller.getCurrY();
            Log.d("Solarex","currY = " + y);
            scrollTo(0, y);
            postInvalidate();
        }
    }

    private void initView(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context
                .WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        mScreenHeight = displayMetrics.heightPixels;
        mScroller = new Scroller(context);
        Log.d("Solarex", "initView:mScreenHeight = " + mScreenHeight);
    }

//    private int checkAlignment(){
//        mEnd = getScrollY();
//        boolean isUp = ((mEnd - mStart) > 0) ? true : false;
//        Log.d("Solarex", "UP: scrollY = " + mEnd + ",isUp = " + isUp);
//        int result = 0;
//        int lastPrev = mEnd % mScreenHeight;
//        int lastNext = mScreenHeight - lastPrev;
//        if (isUp){
//            result = lastPrev;
//        } else {
//            result = -lastNext;
//        }
//        Log.d("Solarex", "UP: result = " + result);
//        return result;
//    }
}
