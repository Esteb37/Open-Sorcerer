package com.example.opensorcerer.ui.developer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.opensorcerer.R;
import com.example.opensorcerer.adapters.FavoritesPagerAdapter;
import com.example.opensorcerer.application.OSApplication;
import com.example.opensorcerer.databinding.FragmentFavoritesBinding;
import com.example.opensorcerer.models.users.roles.Developer;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;
import org.kohsuke.github.GitHub;


/**
 * Fragment for displaying a user's liked projects.
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class FavoritesFragment extends Fragment {

    /**Tag for logging*/
    private static final String TAG = "FavoritesFragment";

    /**Binder object for ViewBinding*/
    private FragmentFavoritesBinding app;

    /**Fragment's context*/
    private Context mContext;

    /**Current logged in user*/
    private Developer mUser;

    /**GitHub API handler*/
    private GitHub mGitHub;

    /**Fragment pager adapter*/
    FavoritesPagerAdapter mPagerAdapter;


    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        app = FragmentFavoritesBinding.inflate(inflater,container,false);
        return app.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getState();

        setupPagerView();
    }

    /**
     * Sets up the fragment pager
     */
    private void setupPagerView() {

        //Set the adapter
        mPagerAdapter = new FavoritesPagerAdapter(this);
        app.viewPager.setAdapter(mPagerAdapter);

        //Set the tab icons
        new TabLayoutMediator(app.tabLayout, app.viewPager,
                (tab, position) -> tab.setIcon(position == 0
                        ? R.drawable.ic_dashboard_black_24dp
                        : R.drawable.menu)
        ).attach();
    }


    /**
     * Gets the current state for the member variables.
     */
    private void getState() {
        mContext = getContext();

        mUser = Developer.getCurrentUser();

        mGitHub = ((OSApplication) requireActivity().getApplication()).getGitHub();
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

    /*private void searchProject(String search) {
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
    }*/


}