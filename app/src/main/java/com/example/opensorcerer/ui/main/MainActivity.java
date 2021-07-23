package com.example.opensorcerer.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.opensorcerer.R;
import com.example.opensorcerer.application.OSApplication;
import com.example.opensorcerer.databinding.ActivityHomeBinding;
import com.example.opensorcerer.models.Tools;
import com.example.opensorcerer.models.User;
import com.example.opensorcerer.ui.main.conversations.ConversationsFragment;
import com.example.opensorcerer.ui.main.create.CreateProjectImportFragment;
import com.example.opensorcerer.ui.main.home.HomeFragment;
import com.example.opensorcerer.ui.main.profile.ProfileFragment;
import com.example.opensorcerer.ui.main.projects.ProjectsFragment;
import com.parse.ParseUser;

import org.kohsuke.github.GitHub;

import java.util.Arrays;
import java.util.List;


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
    private ActivityHomeBinding mApp;

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

    /**
     * Sets up the activity's methods
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupLayout();

        getState();

        gitHubLogin();

        setupBottomNavigation();
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
     * Gets the current state of the member variables
     */
    private void getState() {
        mContext = this;
        mUser = User.fromParseUser(ParseUser.getCurrentUser());
    }

    /**
     * Initializes the GitHub API handler with the logged in user's OAuth token
     */
    private void gitHubLogin() {
        ((OSApplication) getApplication()).buildGitHub(mUser.getGithubToken());
    }

    /**
     * Sets up the bottom navigation bar
     */
    private void setupBottomNavigation() {

        //Ensure that the id's of the navigation items are final for the switch
        final int actionHome = R.id.actionHome;
        final int actionProfile = R.id.actionProfile;
        final int actionProjects = R.id.actionProjects;
        final int actionChats = R.id.actionChats;
        final int actionCreate = R.id.actionCreate;

        List<Integer> itemOrder = Arrays.asList(actionHome,actionProjects,actionCreate,actionChats,actionProfile);

        mApp.bottomNav.setOnItemSelectedListener(
                item -> {

                    Fragment fragment;

                    // Eliminate the Details fragment
                    hideDetailsFragment();

                    // Navigate to a different fragment depending on the item selected
                    switch (item.getItemId()) {

                        // Home Item selected
                        case actionHome:
                            fragment = new HomeFragment();
                            mApp.constraintLayoutDetails.setVisibility(View.VISIBLE);
                            mApp.horizontalScroller.setFeatureItems((HomeFragment) fragment);
                            break;

                        // Favorites item selected
                        case actionProjects:
                            fragment = new ProjectsFragment();
                            break;

                        // Create item selected
                        case actionCreate:
                            fragment = new CreateProjectImportFragment();
                            break;

                        // Profile item selected
                        case actionProfile:
                            fragment = new ProfileFragment(mUser);
                            break;

                        // Chats item selected
                        case actionChats:
                            fragment = new ConversationsFragment();
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + item.getItemId());
                    }

                    int itemPosition = itemOrder.indexOf(item.getItemId());

                    // Open the selected fragment
                    if(itemPosition > mLastPosition) {
                        Tools.navigateToFragment(mContext, fragment, mApp.flContainer.getId(),"right_to_left");
                    } else if (itemPosition < mLastPosition) {
                        Tools.navigateToFragment(mContext, fragment, mApp.flContainer.getId(), "left_to_right");
                    } else {
                        Tools.loadFragment(mContext, fragment, mApp.flContainer.getId());
                    }

                    mLastPosition = itemPosition;
                    return true;
                });

        //Set the default window to be the Home
        mApp.bottomNav.setSelectedItemId(R.id.actionHome);
    }

    public void hideDetailsFragment() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        mApp.constraintLayoutDetails.setVisibility(View.GONE);
        fragmentManager.findFragmentByTag("details");
        Fragment detailsFragment = fragmentManager.findFragmentByTag("details");
        if (detailsFragment != null)
            fragmentManager.beginTransaction().remove(detailsFragment).commit();
    }

    public void showDetailsFragment() {
        mApp.constraintLayoutDetails.setVisibility(View.VISIBLE);
    }
}