package com.loften.animationsample;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class MyRelativeLayout extends RelativeLayout {

    private float mPosX = 0;
    private float mPosY = 0;
    private float mCurPosX = 0;
    private float mCurPosY = 0;

    //分别记录上次滑动的坐标
    private int mLastX = 0;
    private int mLastY = 0;
    //分别记录上次滑动的坐标（onInterceptTouchEvent）
    private int mLastXIntercept = 0;
    private int mLastYIntercept = 0;

    private boolean mDisallowInterceptTouchEvent = true;

    public MyRelativeLayout(Context context) {
        super(context);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        int intercepted = 0;
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastXIntercept = x;
                mLastYIntercept = y;
                mLastX = x;
                mLastY = y;
                intercepted = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastXIntercept;
                int deltaY = y - mLastYIntercept;
                if(Math.abs(deltaY) <= Math.abs(deltaX)){
                    intercepted = 0;
                }else {
                    if(deltaY > 0 && mOnSlideListener.onShow()){
                        //向下滑动 , 登录模块已显示
                        intercepted = 0;
                    }else if(deltaY >0 && !mOnSlideListener.onShow()){
                        //向下滑动， 登录模块未显示
                        if(mDisallowInterceptTouchEvent){
                            intercepted = 0;
                        }else {
                            intercepted = 1;
                        }
                    }else if(deltaY < 0 && mOnSlideListener.onHide()){
                        //向上滑动， 登录模块已显示
                        if(mDisallowInterceptTouchEvent){
                            intercepted = 0;
                        }else {
                            intercepted = 1;
                        }
                    }else if(deltaY < 0 && !mOnSlideListener.onHide()){
                        //向上滑动， 登录模块未显示
                        intercepted = 0;
                    }

                }
                break;
            case MotionEvent.ACTION_UP:
                intercepted = 0;
                mLastXIntercept = mLastYIntercept = 0;
                break;
            default:
                break;
        }

        return intercepted != 0;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                mPosX = event.getX();
                mPosY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mCurPosX = event.getX();
                mCurPosY = event.getY();

                break;
            case MotionEvent.ACTION_UP:
                if (mCurPosY - mPosY > 0
                        && (Math.abs(mCurPosY - mPosY) > 25)) {
                    //向下滑動
                    mOnSlideListener.onShow();
                } else if (mCurPosY - mPosY < 0
                        && (Math.abs(mCurPosY - mPosY) > 25)) {
                    //向上滑动
                    mOnSlideListener.onHide();
                }

                break;
        }
        return super.onTouchEvent(event);
    }

    private OnSlideListener mOnSlideListener;

    public interface OnSlideListener{
        boolean onShow();
        boolean onHide();
    }

    public void setOnSlideListener(OnSlideListener l){
        mOnSlideListener = l;
    }

    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept){
        mDisallowInterceptTouchEvent = disallowIntercept;
    }
}
