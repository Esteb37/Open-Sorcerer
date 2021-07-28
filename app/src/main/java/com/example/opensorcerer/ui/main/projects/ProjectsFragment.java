package com.example.opensorcerer.ui.main.projects;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.opensorcerer.R;
import com.example.opensorcerer.adapters.ProjectsPagerAdapter;
import com.example.opensorcerer.application.OSApplication;
import com.example.opensorcerer.databinding.FragmentProjectsBinding;
import com.example.opensorcerer.models.Project;
import com.example.opensorcerer.models.User;
import com.example.opensorcerer.ui.main.MainActivity;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;
import org.kohsuke.github.GitHub;


/**
 * Fragment for displaying a user's liked and created projects.
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class ProjectsFragment extends Fragment {

    /**
     * Tag for logging
     */
    private static final String TAG = "ProjectsFragment";

    /**
     * Fragment pager adapter
     */
    private ProjectsPagerAdapter mPagerAdapter;

    /**
     * Binder object for ViewBinding
     */
    private FragmentProjectsBinding mApp;

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


    public ProjectsFragment() {
        // Required empty public constructor
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
        mApp = FragmentProjectsBinding.inflate(inflater, container, false);
        return mApp.getRoot();
    }

    /**
     * Sets up the fragment's methods
     */
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getState();

        setupPagerView();
    }

    /**
     * Gets the current state for the member variables.
     */
    private void getState() {
        mContext = getContext();

        mUser = User.getCurrentUser();

        mGitHub = ((OSApplication) requireActivity().getApplication()).getGitHub();

        ((MainActivity) mContext).hideDetailsFragment();
    }

    /**
     * Sets up the fragment pager
     */
    private void setupPagerView() {

        //Set the adapter
        mPagerAdapter = new ProjectsPagerAdapter(this, mUser);
        mApp.viewPager.setAdapter(mPagerAdapter);

        //Set the tab icons
        new TabLayoutMediator(mApp.tabLayout, mApp.viewPager,
                (tab, position) -> {
                    tab.setIcon(position == 0
                            ? R.drawable.ic_dashboard_black_24dp
                            : R.drawable.ufi_heart_active);

                    tab.setText(position == 0
                            ? "My Projects"
                            : "Favorites");
                }
        ).attach();
    }
}