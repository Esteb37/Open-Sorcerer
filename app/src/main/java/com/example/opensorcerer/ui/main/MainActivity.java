package com.example.opensorcerer.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.opensorcerer.R;
import com.example.opensorcerer.adapters.MainPagerAdapter;
import com.example.opensorcerer.databinding.ActivityHomeTestBinding;
import com.example.opensorcerer.models.User;
import com.parse.ParseUser;

import org.kohsuke.github.GitHub;


/**
 * Main activity
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class MainActivity extends AppCompatActivity {

    /**
     * Tag for logging
     */
    private static final String TAG = "MainActivity";

    /**
     * Binder object for ViewBinding
     */
    private ActivityHomeTestBinding mApp;

    /**
     * Fragment's context
     */
    private Context mContext;

    /**
     * Current logged in user
     */
    private User mUser;

    /**
     * GitHub API handler
     */
    private GitHub mGitHub;

    private int mLastPosition = 0;

    private MainPagerAdapter mPagerAdapter;

    private final double MIN_SCROLL_OFFSET = 0.1;

    private final double MIN_SWIPE_OFFSET = 0.9;

    /**
     * Sets up the activity's methods
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupLayout();

        getState();

        setupPager();

        loadPredictScores();
    }

    /**
     * Gets the current state of the member variables
     */
    private void getState() {
        mContext = this;
        mUser = User.fromParseUser(ParseUser.getCurrentUser());
    }
    /**
     * Sets up the activity's layout
     */
    private void setupLayout() {
        setContentView(R.layout.activity_home);
        mApp = ActivityHomeTestBinding.inflate(getLayoutInflater());
        setContentView(mApp.getRoot());
    }



    private void setupPager() {
        //Set the adapter
        mPagerAdapter = new MainPagerAdapter(this);
        mApp.viewPagerMain.setAdapter(mPagerAdapter);

        mApp.viewPagerMain.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if(positionOffset > MIN_SCROLL_OFFSET) {
                    mPagerAdapter.updateProject();
                }
            }

            @Override
            public void onPageSelected(int position) {

                if (position == 1) {
                    mPagerAdapter.addSwipeToProject();

                }

                super.onPageSelected(position);
            }
        });
    }

    private void loadPredictScores() {

        mUser.setPredictScores(mUser.getLearnScores());

    }

    @Override
    public void onBackPressed() {
        if(mPagerAdapter.isInformationFragmentVisible()){
            mApp.viewPagerMain.setCurrentItem(0,true);
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    public void hideDetailsFragment() {
        mPagerAdapter.hideDetailsFragment();
    }

    public void showDetailsFragment() {
        mPagerAdapter.showDetailsFragment();
    }
}