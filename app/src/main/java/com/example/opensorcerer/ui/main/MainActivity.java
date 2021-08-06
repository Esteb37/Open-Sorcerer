package com.example.opensorcerer.ui.main;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.opensorcerer.R;
import com.example.opensorcerer.adapters.MainPagerAdapter;
import com.example.opensorcerer.databinding.ActivityHomeBinding;
import com.example.opensorcerer.models.User;
import com.parse.ParseUser;

import okhttp3.Headers;

/**
 * Main activity
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    /**
     * Minimum scroll distance to begin loading details information
     */
    private final double MIN_SCROLL_OFFSET = 0.01;

    //Client for HTTP requests
    private final AsyncHttpClient mClient = new AsyncHttpClient();

    /**
     * Binder object for ViewBinding
     */
    private ActivityHomeBinding mApp;

    /**
     * Current logged in user
     */
    private User mUser;

    /**
     * The pager adapter for the main activity
     */
    private MainPagerAdapter mPagerAdapter;

    /**
     * Sets up the activity's methods
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //startScraper();

        setupLayout();

        getState();

        setupPager();

        loadPredictScores();
    }

    /**
     * Gets the current state of the member variables
     */
    private void getState() {
        mUser = User.fromParseUser(ParseUser.getCurrentUser());
    }

    /**
     * Sets up the activity's layout
     */
    private void setupLayout() {
        setContentView(R.layout.activity_home);
        mApp = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(mApp.getRoot());
    }

    /**
     * Sets up the main viewpager
     */
    private void setupPager() {
        //Set the adapter
        mPagerAdapter = new MainPagerAdapter(this);
        mApp.viewPagerMain.setAdapter(mPagerAdapter);

        //Listener for when the user swipes between pages
        mApp.viewPagerMain.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            //Loads the details of a project when the user begins swiping left
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (mApp.viewPagerMain.getCurrentItem() == 0 && positionOffset > MIN_SCROLL_OFFSET) {
                    mPagerAdapter.updateProject();
                } else if (mApp.viewPagerMain.getCurrentItem() == 0 && positionOffset < MIN_SCROLL_OFFSET) {
                    try {
                        mPagerAdapter.cleanDetailsFragment();
                    } catch (IllegalArgumentException ignored) {
                    }
                }
            }

            //Adds a swipe to the project's swipe count when the user accesses its details page
            @Override
            public void onPageSelected(int position) {

                if (position == 1) {
                    mPagerAdapter.addSwipeToProject();
                } else {
                    mPagerAdapter.cleanDetailsFragment();
                }
                super.onPageSelected(position);
            }
        });
    }

    /**
     * Sets the current's user predict scores to be the last session's learned scores
     */
    private void loadPredictScores() {
        mUser.setPredictScores(mUser.getLearnScores());
    }

    /**
     * Hides the details fragment page to prevent scrolling
     */
    public void hideDetailsFragment() {
        mPagerAdapter.hideDetailsFragment();
    }

    /**
     * Shows the details fragment page to allow scrolling
     */
    public void showDetailsFragment() {
        mPagerAdapter.showDetailsFragment();
    }

    /**
     * Overrides the back press action to swipe right when the user is in the Project Information view,
     * and to go to the last fragment on every other case
     */
    @Override
    public void onBackPressed() {
        if (mPagerAdapter.isInformationFragmentVisible()) {
            mApp.viewPagerMain.setCurrentItem(0, true);
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    /**
     * Calls the Python backend server to begin the project scraping
     */
    public void startScraper() {
        mClient.post(getString(R.string.backend_server_ip) + "scrape_projects", new JsonHttpResponseHandler() {

            //If the request is successful
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(TAG, "Scraper called successfully.");
            }

            //If the request is not successfully
            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "Failed to call scraper.");
                throwable.printStackTrace();
            }
        });
    }
}