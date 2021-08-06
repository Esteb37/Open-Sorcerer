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
import androidx.recyclerview.widget.RecyclerView;

import com.example.opensorcerer.R;
import com.example.opensorcerer.adapters.ProjectsGridAdapter;
import com.example.opensorcerer.databinding.FragmentCreatedProjectsBinding;
import com.example.opensorcerer.models.EndlessRecyclerViewScrollListener;
import com.example.opensorcerer.models.Project;
import com.example.opensorcerer.models.Tools;
import com.example.opensorcerer.models.User;
import com.example.opensorcerer.ui.main.MainActivity;
import com.example.opensorcerer.ui.main.home.HomeFragment;
import com.parse.ParseQuery;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment to display a user's created projects
 */
public class CreatedProjectsFragment extends Fragment {

    /**
     * Tag for logging
     */
    private static final String TAG = "CreatedProjectsFragment";

    /**
     * Amount of projects to retrieve at a time
     */
    private static final int QUERY_LIMIT = 20;

    /**
     * The user whose projects to show
     */
    private final User mProfileUser;

    /**
     * Binder object for ViewBinding
     */
    private FragmentCreatedProjectsBinding mApp;

    /**
     * Fragment's context
     */
    private Context mContext;

    /**
     * Adapter for the RecyclerView
     */
    private ProjectsGridAdapter mAdapter;

    /**
     * The user's created project list to display
     */
    private List<Project> mProjects;

    /**
     * The recycler view layout manager
     */
    private GridLayoutManager mLayoutManager;

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

    /**
     * Sets up the fragment's methods
     */
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getState();

        setupRecyclerView();

        queryProjects(0);
    }

    /**
     * Gets the current state for the member variables.
     */
    private void getState() {
        mContext = getContext();
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

        //Set the adapter
        mAdapter = new ProjectsGridAdapter(mProjects, mContext, clickListener);
        mApp.recyclerViewProjects.setAdapter(mAdapter);

        //Set the layout
        mLayoutManager = new GridLayoutManager(mContext, 2);
        mApp.recyclerViewProjects.setLayoutManager(mLayoutManager);

        //Setup endless scrolling
        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                queryProjects(page);
            }
        };

        // Adds the scroll listener to RecyclerView
        mApp.recyclerViewProjects.addOnScrollListener(scrollListener);
    }

    /**
     * Gets the list of projects created by the user
     */
    public void queryProjects(int page) {
        ParseQuery<Project> query = ParseQuery.getQuery(Project.class).whereContains("manager", mProfileUser.getObjectId());
        query.addDescendingOrder("createdAt");


        //Setup pagination
        query.setLimit(QUERY_LIMIT);
        query.setSkip(QUERY_LIMIT * page);

        query.findInBackground((projects, e) -> {
            if (e == null) {
                if (projects.size() > 0) {
                    mAdapter.addAll(projects);
                }                 mApp.progressBar.setVisibility(View.GONE);
            } else {
                Log.d(TAG, "Unable to load projects.");
            }

            if(mProjects.size() > 0){
                mApp.textViewNoProjects.setVisibility(View.GONE);
            } else {
                mApp.textViewNoProjects.setVisibility(View.VISIBLE);
            }
        });
    }
}