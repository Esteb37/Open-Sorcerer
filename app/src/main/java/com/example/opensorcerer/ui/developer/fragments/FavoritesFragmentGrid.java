package com.example.opensorcerer.ui.developer.fragments;

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

import com.example.opensorcerer.adapters.ProjectsGridAdapter;
import com.example.opensorcerer.application.OSApplication;
import com.example.opensorcerer.databinding.FragmentFavoritesGridBinding;
import com.example.opensorcerer.models.Project;
import com.example.opensorcerer.models.users.roles.Developer;
import com.parse.ParseQuery;

import org.jetbrains.annotations.NotNull;
import org.kohsuke.github.GitHub;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment for displaying the user's liked projects in grid format
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class FavoritesFragmentGrid extends Fragment {


    /**Tag for logging*/
    private static final String TAG = "FavoritesFragment";

    /**Binder object for ViewBinding*/
    private FragmentFavoritesGridBinding app;

    /**Fragment's context*/
    private Context mContext;

    /**Current logged in user*/
    private Developer mUser;

    /**GitHub API handler*/
    private GitHub mGitHub;

    /**Adapter for the RecyclerView*/
    private ProjectsGridAdapter mAdapter;

    /**The user's created project list to display*/
    private List<Project> mProjects;


    public FavoritesFragmentGrid() {
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
        app = FragmentFavoritesGridBinding.inflate(inflater,container,false);
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

        queryProjects();
    }

    /**
     * Gets the current state for the member variables.
     */
    private void getState() {
        mContext = getContext();

        mUser = Developer.getCurrentUser();

        mGitHub = ((OSApplication) requireActivity().getApplication()).getGitHub();
    }

    /**
     * Sets up the created projects recycler view
     */
    private void setupRecyclerView() {
        mProjects = new ArrayList<>();

        //Set the adapter
        mAdapter = new ProjectsGridAdapter(mProjects,mContext);
        app.recyclerViewFavorites.setAdapter(mAdapter);

        //Set the layout
        app.recyclerViewFavorites.setLayoutManager(new GridLayoutManager(mContext,2));
    }


    /**
     * Gets the list of projects created by the user
     */
    public void queryProjects(){
        app.progressBar.setVisibility(View.VISIBLE);
        ParseQuery<Project> query = ParseQuery.getQuery(Project.class).whereContainedIn("objectId", mUser.getFavorites());
        query.addDescendingOrder("createdAt");
        query.findInBackground((projects, e) -> {
            if(e==null){
                mAdapter.clear();
                mAdapter.addAll(projects);
                app.progressBar.setVisibility(View.GONE);
            } else {
                Log.d(TAG,"Unable to load projects.");
            }
        });
    }

   /* public void setSearchListener(){
        //Set listener for searchbar input
        app.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override

            //Whenever a new character is entered
            public boolean onQueryTextChange(String newText) {
                if(newText.length()>0) {
                    searchProject(newText);
                } else {
                    queryProjects();
                }

                return false;
            }
        });
    }*/

    private void searchProject(String search) {
        ParseQuery<Project> query = ParseQuery.getQuery(Project.class).whereMatches("title","("+search+")","i");
        query.addDescendingOrder("createdAt");
        query.findInBackground((projects, e) -> {
            if(e==null){
                mAdapter.clear();
                mAdapter.addAll(projects);
                app.progressBar.setVisibility(View.GONE);
            } else {
                Log.d(TAG,"Unable to load projects.");
            }
        });
    }
}