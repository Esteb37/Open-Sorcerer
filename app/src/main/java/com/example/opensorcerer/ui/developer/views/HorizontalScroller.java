package com.example.opensorcerer.ui.developer.views;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telecom.Call;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.opensorcerer.R;
import com.example.opensorcerer.ui.developer.DetailsFragment;
import com.example.opensorcerer.ui.developer.fragments.ProjectsFragment;

import org.parceler.Parcels;


public class HorizontalScroller extends HorizontalScrollView {
    Context mContext;

    private static final int SWIPE_MIN_DISTANCE = 5;
    private static final int SWIPE_THRESHOLD_VELOCITY = 10000;

    private GestureDetector mGestureDetector;
    private int mActiveFeature = 0;


    private ProjectsFragment mProjectsFragment;
    private DetailsFragment mDetailsFragment;

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
        if(l>=200){
            startDetails();
        } else{
            endDetails();
        }
    }

    private void endDetails() {
        FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag("details");
        if(fragment != null)
            fragmentManager.beginTransaction().remove(fragment).commit();
    }

    private void startDetails() {
        FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
        mDetailsFragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("project", Parcels.wrap(mProjectsFragment.getCurrentProject()));
        mDetailsFragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.flContainerDetails,mDetailsFragment,"details").commit();
    }


    public void setFeatureItems(ProjectsFragment fragment){
        mProjectsFragment = fragment;
        setOnTouchListener((v, event) -> {
            v.performClick();
            if (mGestureDetector.onTouchEvent(event)) {
                return true;
            }
            else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL ){
                int scrollX = getScrollX();
                int featureWidth = v.getMeasuredWidth();
                mActiveFeature = ((scrollX + (featureWidth/2))/featureWidth);
                int scrollTo = mActiveFeature*featureWidth;
                smoothScrollTo(scrollTo, 0);
                Log.d("Scrolling",""+scrollTo);
                return true;
            }
            else{
                return false;
            }

        });
        mGestureDetector = new GestureDetector(new FlingDetector());
    }

    class FlingDetector extends GestureDetector.SimpleOnGestureListener {


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
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
            return false;
        }
    }
}