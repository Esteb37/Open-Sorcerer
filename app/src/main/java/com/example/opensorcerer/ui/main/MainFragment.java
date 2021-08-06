package com.example.opensorcerer.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.opensorcerer.R;
import com.example.opensorcerer.application.OSApplication;
import com.example.opensorcerer.databinding.FragmentMainBinding;
import com.example.opensorcerer.models.Project;
import com.example.opensorcerer.models.Tools;
import com.example.opensorcerer.models.User;
import com.example.opensorcerer.ui.main.conversations.ConversationsFragment;
import com.example.opensorcerer.ui.main.create.CreateProjectImportFragment;
import com.example.opensorcerer.ui.main.home.HomeFragment;
import com.example.opensorcerer.ui.main.profile.ProfileFragment;
import com.example.opensorcerer.ui.main.projects.ProjectsFragment;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * Main fragment for displaying most of the app's windows
 */
public class MainFragment extends Fragment {

    /**
     * Binder object for ViewBinding
     */
    private FragmentMainBinding mApp;

    /**
     * Fragment's context
     */
    private Context mContext;

    /**
     * Current logged in user
     */
    private User mUser;

    private Fragment mFragment;

    private int mLastPosition = 0;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Inflates the fragment and sets up view binding
     */
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mApp = FragmentMainBinding.inflate(inflater, container, false);
        return mApp.getRoot();
    }

    /**
     * Sets up the fragment's methods
     */
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getState();

        gitHubLogin();

        setupBottomNavigation();
    }

    /**
     * Gets the current state of the member variables
     */
    private void getState() {
        mContext = getContext();
        mUser = User.fromParseUser(ParseUser.getCurrentUser());
    }

    /**
     * Initializes the GitHub API handler with the logged in user's OAuth token
     */
    private void gitHubLogin() {
        ((OSApplication) requireActivity().getApplication()).buildGitHub(mUser.getGithubToken());
    }


    /**
     * Sets up the bottom navigation bar
     */
    private void setupBottomNavigation() {

        //Ensure that the id's of the navigation items are final for the switch
        final int actionHome = R.id.actionHome;
        final int actionProfile = R.id.actionProfile;
        final int actionProjects = R.id.actionProjects;
        final int actionChats = R.id.actionMessage;
        final int actionCreate = R.id.actionCreate;


        List<Integer> itemOrder = Arrays.asList(actionHome, actionProjects, actionCreate, actionChats, actionProfile);
        ((MainActivity) requireActivity()).hideDetailsFragment();


        mApp.bottomNav.setOnItemSelectedListener(
                item -> {

                    // Navigate to a different fragment depending on the item selected
                    switch (item.getItemId()) {

                        // Home Item selected
                        case actionHome:
                            mFragment = new HomeFragment();
                            ((MainActivity) requireActivity()).showDetailsFragment();
                            break;

                        // Favorites item selected
                        case actionProjects:
                            mFragment = new ProjectsFragment();
                            break;

                        // Create item selected
                        case actionCreate:
                            mFragment = new CreateProjectImportFragment();
                            break;

                        // Profile item selected
                        case actionProfile:
                            mFragment = new ProfileFragment(mUser);
                            break;

                        // Chats item selected
                        case actionChats:
                            mFragment = new ConversationsFragment();
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + item.getItemId());
                    }

                    int itemPosition = itemOrder.indexOf(item.getItemId());

                    // Open the selected fragment
                    if (itemPosition > mLastPosition) {
                        Tools.navigateToFragment(mContext, mFragment, mApp.flContainer.getId(), "right_to_left");
                    } else if (itemPosition < mLastPosition) {
                        Tools.navigateToFragment(mContext, mFragment, mApp.flContainer.getId(), "left_to_right");
                    } else {
                        Tools.loadFragment(mContext, mFragment, mApp.flContainer.getId());
                    }

                    mLastPosition = itemPosition;
                    return true;
                });

        //Set the default window to be the Home
        mApp.bottomNav.setSelectedItemId(R.id.actionHome);
    }

    /**
     * Gets the project currently being displayed to the user
     */
    public Project getCurrentProject() {
        if(mFragment.getClass().getSimpleName().equals("HomeFragment")){
            return ((HomeFragment) mFragment).getCurrentProject();
        } else if (mFragment.getClass().getSimpleName().equals("ProjectsFragment")) {
            return ((ProjectsFragment) mFragment).getCurrentProject();
        } else {
            return null;
        }
    }
}