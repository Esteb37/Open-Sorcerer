package com.example.opensorcerer.ui.main.details.manager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.opensorcerer.R;
import com.example.opensorcerer.adapters.UsersAdapter;
import com.example.opensorcerer.databinding.FragmentInterestedUsersBinding;
import com.example.opensorcerer.models.EndlessRecyclerViewScrollListener;
import com.example.opensorcerer.models.Project;
import com.example.opensorcerer.models.Tools;
import com.example.opensorcerer.models.User;
import com.example.opensorcerer.ui.main.profile.ProfileFragment;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InterestedUsersFragment extends Fragment {

    private static final String TAG = "InterestedUsersFragment";

    private static final int QUERY_LIMIT = 20;

    /**
     * Project being displayed
     */
    private final Project mProject;

    /**
     * Binder object for ViewBinding
     */
    private FragmentInterestedUsersBinding mApp;

    /**
     * Fragment's context
     */
    private Context mContext;

    private List<User> mUsers;

    private UsersAdapter mAdapter;

    private LinearLayoutManager mLayoutManager;

    public InterestedUsersFragment(Project project) {
        mProject = project;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mApp = FragmentInterestedUsersBinding.inflate(inflater, container, false);
        return mApp.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getState();

        setupRecyclerView();

        queryUsers(0);
    }

    /**
     * Gets the current state for the member variables.
     */
    private void getState() {
        mContext = getContext();

        ((AppCompatActivity) requireActivity()).setSupportActionBar(mApp.toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("Interested developers");

    }

    /**
     * Sets up the timeline's Recycler View
     */
    private void setupRecyclerView() {

        if (mUsers == null) {
            mUsers = new ArrayList<>();
        }

        //Click listener for travelling to a conversation
        UsersAdapter.OnClickListener clickListener = position ->
                Tools.navigateToFragment(mContext, new ProfileFragment(mUsers.get(position)), R.id.flContainerDetails, "right_to_left");

        //Set adapter
        mAdapter = new UsersAdapter(mUsers, mContext, mProject, clickListener);
        mApp.recyclerViewUsers.setAdapter(mAdapter);

        //Set layout manager
        mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        mApp.recyclerViewUsers.setLayoutManager(mLayoutManager);

        //Sets up the endless scroller listener
        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                queryUsers(page);
            }
        };

        // Adds the scroll listener to RecyclerView
        mApp.recyclerViewUsers.addOnScrollListener(scrollListener);
    }

    private void queryUsers(int page) {

        List<String> userLikes = mProject.getUserLikes();

        if(userLikes == null) {
            return;
        }

        //Get a query in descending order
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class)
                .whereContainedIn("objectId", userLikes);

        query.addDescendingOrder("createdAt");

        //Setup pagination
        query.setLimit(QUERY_LIMIT);
        query.setSkip(page * QUERY_LIMIT);

        query.findInBackground((users, e) -> {
            if (e == null) {
                if (page == 0) {
                    mAdapter.clear();
                }
                if (users.size() > 0) {
                    mAdapter.addAll(User.toUserArray(users));
                }
            } else {
                Log.d(TAG, "Unable to load users");
            }
        });
    }

    /**
     * Inflates the toolbar
     */
    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.conversations_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}