package com.example.opensorcerer.ui.main.projects;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.opensorcerer.R;
import com.example.opensorcerer.adapters.ProjectsGridAdapter;
import com.example.opensorcerer.application.OSApplication;
import com.example.opensorcerer.databinding.FragmentCreatedProjectsBinding;
import com.example.opensorcerer.models.Project;
import com.example.opensorcerer.models.Tools;
import com.example.opensorcerer.models.User;
import com.example.opensorcerer.ui.main.MainActivity;
import com.example.opensorcerer.ui.main.home.HomeFragment;
import com.parse.ParseQuery;

import org.jetbrains.annotations.NotNull;
import org.kohsuke.github.GitHub;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class CreatedProjectsFragment extends Fragment {

    /**
     * Tag for logging
     */
    private static final String TAG = "MyProjectsFragment";

    /**
     * Binder object for ViewBinding
     */
    private FragmentCreatedProjectsBinding mApp;

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
     * Adapter for the RecyclerView
     */
    private ProjectsGridAdapter mAdapter;

    /**
     * The user's created project list to display
     */
    private List<Project> mProjects;

    /**
     * The user whose projects to show
     */
    private final User mProfileUser;


    public CreatedProjectsFragment(User projectUser) {
        mProfileUser = projectUser;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Inflates the fragment's layout
     */
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mApp = FragmentCreatedProjectsBinding.inflate(inflater, container, false);
        return mApp.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getState();

        setupRecyclerView();

        queryProjects();
    }

    /**
     * Gets the current state for the member variables.
     */
    private void getState() {
        mContext = getContext();

        mUser = User.getCurrentUser();

        mGitHub = ((OSApplication) requireActivity().getApplication()).getGitHub();
    }

    /**
     * Sets up the created projects recycler view
     */
    private void setupRecyclerView() {
        mProjects = new ArrayList<>();

        ProjectsGridAdapter.OnClickListener clickListener = position -> {
            Tools.navigateToFragment(mContext, new HomeFragment(mProjects, position), R.id.flContainer, "right_to_left");
            ((MainActivity) requireActivity()).showDetailsFragment();
        };

        mAdapter = new ProjectsGridAdapter(mProjects, mContext, clickListener);
        mApp.recyclerViewProjects.setAdapter(mAdapter);
        mApp.recyclerViewProjects.setLayoutManager(new GridLayoutManager(mContext, 2));
    }

    /**
     * Gets the list of projects created by the user
     */
    public void queryProjects() {
        ParseQuery<Project> query = ParseQuery.getQuery(Project.class).whereContains("manager", mProfileUser.getObjectId());
        query.addDescendingOrder("createdAt");
        query.findInBackground((projects, e) -> {
            if (e == null) {
                if (projects.size() > 0) {
                    mAdapter.addAll(projects);
                } else {
                    mApp.textViewNoProjects.setVisibility(View.VISIBLE);
                }
                mApp.progressBar.setVisibility(View.GONE);
            } else {
                Log.d(TAG, "Unable to load projects.");
            }
        });
    }
}