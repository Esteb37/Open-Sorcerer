package com.example.opensorcerer.ui.main.details;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.opensorcerer.application.OSApplication;
import com.example.opensorcerer.databinding.FragmentHomepageBinding;
import com.example.opensorcerer.models.Project;
import com.example.opensorcerer.models.User;

import org.jetbrains.annotations.NotNull;
import org.kohsuke.github.GitHub;
import org.parceler.Parcels;

/**
 * Fragment for displaying a project's homepage
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class HomepageFragment extends Fragment {

    /**
     * Tag for logging
     */
    private static final String TAG = "HomepageFragment";

    /**
     * Binder object for ViewBinding
     */
    private FragmentHomepageBinding mApp;

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

    /**
     * Project being displayed
     */
    private Project mProject;

    public HomepageFragment(Project project) {
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
        mApp = FragmentHomepageBinding.inflate(inflater, container, false);
        return mApp.getRoot();
    }

    /**
     * Sets up the fragment's methods
     */
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getState();

        loadWebsite();
    }

    /**
     * Gets the current state for the member variables.
     */
    private void getState() {

        mContext = getContext();

        mGitHub = ((OSApplication) requireActivity().getApplication()).getGitHub();

        assert getArguments() != null;
        mProject = Parcels.unwrap(getArguments().getParcelable("project"));
    }

    /**
     * Loads the GitHub repository into the WebViewer
     */
    private void loadWebsite() {
        mApp.webView.loadUrl(mProject.getWebsite());
        mApp.progressBar.setVisibility(View.GONE);
    }

}