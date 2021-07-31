package com.example.opensorcerer.ui.main.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.opensorcerer.R;
import com.example.opensorcerer.adapters.ProjectsCardAdapter;
import com.example.opensorcerer.databinding.FragmentHomeBinding;
import com.example.opensorcerer.models.EndlessRecyclerViewScrollListener;
import com.example.opensorcerer.models.Project;
import com.example.opensorcerer.models.User;
import com.parse.ParseQuery;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment for displaying the user's timeline of projects
 */
public class HomeFragment extends Fragment {

    /**
     * Tag for logging
     */
    private static final String TAG = "HomeFragment";

    /**
     * Amount of items to query per call
     */
    private static final int QUERY_LIMIT = 5;

    /**
     * Binder object for ViewBinding
     */
    private FragmentHomeBinding mApp;

    /**
     * Fragment's context
     */
    private Context mContext;

    /**
     * Current logged in user
     */
    private User mUser;


    /**
     * Adapter for the Recycler View
     */
    private ProjectsCardAdapter mAdapter;

    /**
     * Layout manager for the Recycler View
     */
    private LinearLayoutManager mLayoutManager;

    /**
     * Snap helper for the Recycler View
     */
    private PagerSnapHelper mSnapHelper;

    /**
     * The list of projects to display
     */
    private List<Project> mProjects;

    /**
     * The position to scroll to
     */
    private int mPosition = -1;

    public HomeFragment() {
        // Required empty public constructor
    }

    public HomeFragment(List<Project> projects, int position) {
        mProjects = projects;
        mPosition = position;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mApp = FragmentHomeBinding.inflate(inflater, container, false);
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

        if (mPosition == -1) {
            queryProjects(0);
        } else {
            loadProjects();
        }

        setupSwipeRefresh();

        requireActivity().findViewById(R.id.bottomNav).setVisibility(View.VISIBLE);
    }

    /**
     * Gets the current state for the member variables.
     */
    private void getState() {
        mContext = getContext();

        mUser = User.getCurrentUser();
    }

    /**
     * Sets up the timeline's Recycler View
     */
    private void setupRecyclerView() {

        if (mProjects == null) {
            mProjects = new ArrayList<>();
        }

        ProjectsCardAdapter.OnDoubleTapListener doubleTapListener = position -> mUser.toggleLike(mProjects.get(position));

        //Set adapter
        mAdapter = new ProjectsCardAdapter(mProjects, mContext, doubleTapListener);
        mApp.recyclerViewProjects.setAdapter(mAdapter);

        //Set snap helper
        mSnapHelper = new PagerSnapHelper();
        mSnapHelper.attachToRecyclerView(mApp.recyclerViewProjects);

        //Set layout manager
        mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        mApp.recyclerViewProjects.setLayoutManager(mLayoutManager);

        //Sets up the endless scroller listener
        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {

            private Project lastProject = new Project();

            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                queryProjects(page);
            }

            @Override
            public void onScrolled(@NotNull RecyclerView view, int dx, int dy) {

                Project currentProject = getCurrentProject();

                if (lastProject != currentProject) {
                    mUser.registerScrolledProject(lastProject);
                    lastProject = currentProject;
                    currentProject.ignoredByUser(false);
                    currentProject.addView();
                }

                super.onScrolled(view, dx, dy);
            }
        };

        // Adds the scroll listener to RecyclerView
        mApp.recyclerViewProjects.addOnScrollListener(scrollListener);
    }

    /**
     * Gets the list of projects from the developer's timeline and filters it by the developer's behavior
     */
    private void queryProjects(int page) {

        //Get a query in descending order
        ParseQuery<Project> query = ParseQuery.getQuery(Project.class);
        query.addDescendingOrder("createdAt");
        query.setLimit(QUERY_LIMIT);
        query.setSkip(page * QUERY_LIMIT);

        query.findInBackground((projects, e) -> {
            if (e == null) {
                if (page == 0) {
                    mAdapter.clear();
                }
                for (Project project : projects) {
                    if (mUser.probablyLikes(project)) {
                        mAdapter.add(project);
                    }
                }
                mApp.swipeContainer.setRefreshing(false);
                mApp.progressBar.setVisibility(View.GONE);
            } else {
                Log.d(TAG, "Unable to load projects.");
            }
        });

    }

    /**
     * Loads the selected list of projects
     */
    private void loadProjects() {
        mAdapter.notifyDataSetChanged();
        mApp.recyclerViewProjects.scrollToPosition(mPosition);
        mApp.swipeContainer.setRefreshing(false);
        mApp.swipeContainer.setEnabled(false);
    }

    /**
     * Sets up the swipe down to refresh interaction
     */
    private void setupSwipeRefresh() {
        mApp.swipeContainer.setOnRefreshListener(() -> queryProjects(0));

        mApp.swipeContainer.setColorSchemeResources(R.color.darker_blue,
                R.color.dark_blue,
                R.color.light_blue);
    }

    /**
     * @return The project currently being displayed to the user
     */
    public Project getCurrentProject() {
        View snapView = mSnapHelper.findSnapView(mLayoutManager);
        assert snapView != null;
        return mProjects.get(mLayoutManager.getPosition(snapView));
    }

}