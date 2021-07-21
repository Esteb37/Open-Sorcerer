package com.example.opensorcerer.ui.main.projects;

import android.content.Context;
import android.os.Bundle;
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
import com.example.opensorcerer.adapters.EndlessRecyclerViewScrollListener;
import com.example.opensorcerer.adapters.ProjectsCardAdapter;
import com.example.opensorcerer.application.OSApplication;
import com.example.opensorcerer.databinding.FragmentProjectsBinding;
import com.example.opensorcerer.models.Project;
import com.example.opensorcerer.models.User;
import com.parse.ParseQuery;

import org.jetbrains.annotations.NotNull;
import org.kohsuke.github.GitHub;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class ProjectsFragment extends Fragment {

    /**Tag for logging*/
    private static final String TAG = "ProjectsFragment";

    /**Amount of items to query per call*/
    private static final int QUERY_LIMIT = 5;

    /**Binder object for ViewBinding*/
    private FragmentProjectsBinding app;

    /**Fragment's context*/
    private Context mContext;

    /**Current logged in user*/
    private User mUser;

    /**GitHub API handler*/
    private GitHub mGitHub;

    /**Adapter for the Recycler View*/
    private ProjectsCardAdapter mAdapter;

    /**Layout manager for the Recycler View*/
    private LinearLayoutManager mLayoutManager;

    /**Snap helper for the Recycler View*/
    private PagerSnapHelper mSnapHelper;

    /**The list of projects to display*/
    private List<Project> mProjects;

    public ProjectsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        app = FragmentProjectsBinding.inflate(inflater,container,false);
        return app.getRoot();
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

        setupSwipeRefresh();
    }

    private void setupSwipeRefresh() {
        app.swipeContainer.setOnRefreshListener(() -> queryProjects(0));

        app.swipeContainer.setColorSchemeResources(R.color.darker_blue,
                R.color.dark_blue,
                R.color.light_blue,
                android.R.color.holo_red_light);
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
     * Sets up the timeline's Recycler View
     */
    private void setupRecyclerView() {

        //Prepare list of projects
        mProjects = new ArrayList<>();

        ProjectsCardAdapter.OnClickListener clickListener = position -> {

        };

        ProjectsCardAdapter.OnDoubleTapListener doubleTapListener = position ->
                mUser.toggleLike(mProjects.get(position));

        //Set adapter
        mAdapter = new ProjectsCardAdapter(mProjects,mContext, clickListener,doubleTapListener);
        app.rvProjects.setAdapter(mAdapter);

        //Set snap helper
        mSnapHelper = new PagerSnapHelper();
        mSnapHelper.attachToRecyclerView(app.rvProjects);

        //Set layout manager
        mLayoutManager = new LinearLayoutManager(mContext,RecyclerView.VERTICAL,false);
        app.rvProjects.setLayoutManager(mLayoutManager);

        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                queryProjects(page);
            }
        };
        // Adds the scroll listener to RecyclerView
        app.rvProjects.addOnScrollListener(scrollListener);
    }


    /**
     * Gets the list of projects from the developer's timeline
     */
    private void queryProjects(int page){

        ParseQuery<Project> query = ParseQuery.getQuery(Project.class);
        query.addDescendingOrder("createdAt");
        query.setLimit(QUERY_LIMIT);
        query.setSkip(page*QUERY_LIMIT);
        query.findInBackground((projects, e) -> {
            mAdapter.addAll(projects);
            app.progressBar.setVisibility(View.GONE);
            app.swipeContainer.setRefreshing(false);
        });
    }

    /**
     * @return The project currently being displayed to the user
     */
    public Project getCurrentProject(){
        View snapView = mSnapHelper.findSnapView(mLayoutManager);
        assert snapView != null;
        return mProjects.get(mLayoutManager.getPosition(snapView));
    }

}