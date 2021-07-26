package com.example.opensorcerer.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.opensorcerer.R;
import com.example.opensorcerer.models.Tools;
import com.example.opensorcerer.ui.main.details.DetailsFragment;
import com.example.opensorcerer.ui.main.home.HomeFragment;

/**
 * Custom Horizontal Scrolling View to display a project's details when swiped left
 * Code fragments taken from:
 * Title: Android: Creating a “Snapping” Horizontal Scroll View
 * Author: Velir: Mobile Responsive Design
 * Date: Nov 17, 2010
 * Availability: https://www.velir.com/ideas/2010/11/17/android-creating-a-snapping-horizontal-scroll-view
 */
public class HorizontalScroller extends HorizontalScrollView {

    private static final int SWIPE_THRESHOLD_VELOCITY = 100;
    private static final int SWIPE_MIN_DISTANCE = 5;
    /**
     * The scrolling view's context
     */
    Context mContext;
    private GestureDetector mGestureDetector;
    private int mActiveFeature = 0;


    private HomeFragment mHomeFragment;

    public HorizontalScroller(Context context) {
        super(context);
        mContext = context;
    }

    public HorizontalScroller(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public HorizontalScroller(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (l >= 200) {
            startDetails();
        } else {
            endDetails();
        }
    }

    private void endDetails() {
        FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag("details");
        if (fragment != null)
            fragmentManager.beginTransaction().remove(fragment).commit();
    }

    private void startDetails() {
        DetailsFragment fragment = new DetailsFragment(mHomeFragment.getCurrentProject());
        Tools.loadFragment(mContext, fragment, R.id.flContainerDetails);
    }


    public void setFeatureItems(HomeFragment fragment) {
        mHomeFragment = fragment;
        setOnTouchListener((v, event) -> {
            v.performClick();
            if (mGestureDetector.onTouchEvent(event)) {
                return true;
            } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                int scrollX = getScrollX();
                int featureWidth = v.getMeasuredWidth();
                mActiveFeature = ((scrollX + (featureWidth / 2)) / featureWidth);
                int scrollTo = mActiveFeature * featureWidth;
                smoothScrollTo(scrollTo, 0);
                return true;
            } else {
                return false;
            }

        });

        //noinspection deprecation
        mGestureDetector = new GestureDetector(new FlingDetector());
    }

    class FlingDetector extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(e1 != null && e2 != null){
                try {
                    //right to left
                    if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                        int featureWidth = getMeasuredWidth();
                        mActiveFeature = (mActiveFeature < (getChildCount() - 1)) ? mActiveFeature + 1 : getChildCount() - 1;
                        smoothScrollTo(mActiveFeature * featureWidth, 0);
                        return true;
                    }
                    //left to right
                    else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                        int featureWidth = getMeasuredWidth();
                        mActiveFeature = (mActiveFeature > 0) ? mActiveFeature - 1 : 0;
                        smoothScrollTo(mActiveFeature * featureWidth, 0);
                        return true;
                    }
                } catch (Exception e) {
                    Log.e("Fling", "There was an error processing the Fling event:" + e.getMessage());
                }
            }
            return false;
        }
    }
}