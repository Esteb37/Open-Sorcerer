package com.example.opensorcerer.ui.main.details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.opensorcerer.R;
import com.example.opensorcerer.databinding.FragmentHomepageBinding;
import com.example.opensorcerer.models.Project;

import org.jetbrains.annotations.NotNull;

/**
 * Fragment for displaying a project's homepage
 */
public class HomepageFragment extends Fragment {

    /**
     * Binder object for ViewBinding
     */
    private FragmentHomepageBinding mApp;

    /**
     * Project being displayed
     */
    private final Project mProject;

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

        showBottomNavigation();

        loadWebsite();
    }

    /**
     * Gets the current state for the member variables.
     */
    private void showBottomNavigation() {

        requireActivity().findViewById(R.id.bottomNavDetails).setVisibility(View.VISIBLE);
    }

    /**
     * Loads the GitHub repository into the WebViewer
     */
    private void loadWebsite() {
        mApp.webView.loadUrl(mProject.getWebsite());
        mApp.progressBar.setVisibility(View.GONE);
    }

}