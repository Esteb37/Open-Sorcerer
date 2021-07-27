package com.example.opensorcerer.ui.main.details;

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
import com.example.opensorcerer.databinding.FragmentDetailsBinding;
import com.example.opensorcerer.models.Project;
import com.example.opensorcerer.models.Tools;
import com.example.opensorcerer.models.User;

import org.jetbrains.annotations.NotNull;
import org.kohsuke.github.GitHub;

/**
 * Fragment for displaying a project's details
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class DetailsFragment extends Fragment {

    /**
     * Tag for logging
     */
    private static final String TAG = "DetailsFragment";

    /**
     * Project being displayed
     */
    private final Project mProject;

    /**
     * Binder object for ViewBinding
     */
    private FragmentDetailsBinding mApp;

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

    public DetailsFragment(Project project) {
        mProject = project;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Inflates the fragment and sets up ViewBinding
     */
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mApp = FragmentDetailsBinding.inflate(inflater, container, false);
        return mApp.getRoot();
    }

    /**
     * Sets up the fragment's methods
     */
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getState();

        setupBottomNavigation();
    }

    /**
     * Gets the current state for the member variables.
     */
    private void getState() {

        mContext = getContext();

        mGitHub = ((OSApplication) requireActivity().getApplication()).getGitHub();
    }

    /**
     * Sets up the bottom navigation bar
     */
    private void setupBottomNavigation() {

        requireActivity().findViewById(R.id.bottomNavDetails).setVisibility(View.VISIBLE);

        //Ensure that the id's of the navigation items are final for the switch
        final int actionDetails = R.id.actionDetails;
        final int actionGithub = R.id.actionGithub;

        mApp.bottomNavDetails.setOnItemSelectedListener(item -> {
            Fragment fragment;

            // Navigate to a different fragment depending on the item selected
            switch (item.getItemId()) {
                case actionDetails:
                    fragment = new InformationFragment(mProject);
                    break;

                case actionGithub:
                    fragment = new HomepageFragment(mProject);
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + item.getItemId());
            }

            // Open the selected fragment
            Tools.loadFragment(mContext, fragment, mApp.flContainerDetailsInternal.getId());
            return true;
        });

        mApp.bottomNavDetails.setSelectedItemId(R.id.actionDetails);
    }
}