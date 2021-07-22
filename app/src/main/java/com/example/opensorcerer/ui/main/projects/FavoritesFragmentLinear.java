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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.opensorcerer.adapters.EndlessRecyclerViewScrollListener;
import com.example.opensorcerer.adapters.ProjectsCardAdapter;
import com.example.opensorcerer.application.OSApplication;
import com.example.opensorcerer.databinding.FragmentFavoritesLinearBinding;
import com.example.opensorcerer.models.Project;
import com.example.opensorcerer.models.User;
import com.parse.ParseQuery;

import org.jetbrains.annotations.NotNull;
import org.kohsuke.github.GitHub;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment for displaying the user's liked projects in linear card format
 */
@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class FavoritesFragmentLinear extends Fragment {

    /**Tag for logging*/
    private static final String TAG = "FavoritesFragmentLinear";

    /**Amount of items to query per call*/
    private static final int QUERY_LIMIT = 5;

    /**Binder object for ViewBinding*/
    private FragmentFavoritesLinearBinding app;

    /**Fragment's context*/
    private Context mContext;

    /**Current logged in user*/
    private User mUser;

    /**GitHub API handler*/
    private GitHub mGitHub;

    /**Adapter for the RecyclerView*/
    private ProjectsCardAdapter mAdapter;

    /**Layout manager for the RecyclerView*/
    private LinearLayoutManager mLayoutManager;

    /**The user's created project list to display*/
    private List<Project> mProjects;

    /**Snap helper for the recyclerview*/
    private PagerSnapHelper mSnapHelper;


    public FavoritesFragmentLinear() {
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
        app = FragmentFavoritesLinearBinding.inflate(inflater,container,false);
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

        //Create the listeners
        ProjectsCardAdapter.OnClickListener clickListener = position -> {

        };

        ProjectsCardAdapter.OnDoubleTapListener doubleTapListener = position ->
                mUser.toggleLike(mProjects.get(position));

        //Set the adapter
        mAdapter = new ProjectsCardAdapter(mProjects,mContext,clickListener,doubleTapListener);
        app.recyclerViewFavorites.setAdapter(mAdapter);

        //Set snap helper
        mSnapHelper = new PagerSnapHelper();
        mSnapHelper.attachToRecyclerView(app.recyclerViewFavorites);

        //Set the layout manager
        mLayoutManager = new LinearLayoutManager(mContext);
        app.recyclerViewFavorites.setLayoutManager(mLayoutManager);

        //Setup endless scrolling
        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                queryProjects(page);
            }
        };
        // Adds the scroll listener to RecyclerView
        app.recyclerViewFavorites.addOnScrollListener(scrollListener);
    }

    /**
     * Gets the list of projects liked by the user
     */
    public void queryProjects(int page){

        app.progressBar.setVisibility(View.VISIBLE);
        List<String> favorites = mUser.getFavorites();
        if(favorites!=null) {

            //Get a query from the user's favorites
            ParseQuery<Project> query = ParseQuery.getQuery(Project.class).whereContainedIn("objectId", favorites);
            query.addDescendingOrder("createdAt");

            //Setup pagination
            query.setLimit(QUERY_LIMIT);
            query.setSkip(QUERY_LIMIT*page);

            //Get the liked projects
            query.findInBackground((projects, e) -> {
                if (e == null) {
                    mAdapter.addAll(projects);
                    app.progressBar.setVisibility(View.GONE);
                } else {
                    Log.d(TAG, "Unable to load projects.");
                }
            });
        } else {
            app.progressBar.setVisibility(View.GONE);
        }
    }
}