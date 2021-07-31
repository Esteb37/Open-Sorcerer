package com.example.opensorcerer.ui.main.conversations;

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
import com.example.opensorcerer.adapters.ConversationsAdapter;
import com.example.opensorcerer.databinding.FragmentConversationsBinding;
import com.example.opensorcerer.models.Conversation;
import com.example.opensorcerer.models.EndlessRecyclerViewScrollListener;
import com.example.opensorcerer.models.Tools;
import com.example.opensorcerer.models.User;
import com.parse.ParseQuery;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 * Fragment for displaying the user's list of active conversations
 */
public class ConversationsFragment extends Fragment {

    /**
     * Tag for logging
     */
    private static final String TAG = "ConversationsFragment";

    /**
     * Amount of conversations to load at a time
     */
    private static final int QUERY_LIMIT = 20;

    /**
     * Binder object for ViewBinding
     */
    private FragmentConversationsBinding mApp;

    /**
     * Fragment's context
     */
    private Context mContext;

    /**
     * Current logged in user
     */
    private User mUser;

    /**
     * List of user's active conversations
     */
    private ArrayList<Conversation> mConversations;

    /**
     * Adapter for the conversations recyclerview
     */
    private ConversationsAdapter mAdapter;

    /**
     * Layout manager for the conversations recyclerview
     */
    private LinearLayoutManager mLayoutManager;

    public ConversationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Sets up the fragment's layout
     */
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        mApp = FragmentConversationsBinding.inflate(inflater, container, false);
        return mApp.getRoot();
    }

    /**
     * Sets up the fragment's methods
     */
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupWindowBars();

        getState();

        setupRecyclerView();

        queryConversations(0);
    }

    /**
     * Sets up the fragment's toolbar and shows the bottom navigation
     */
    private void setupWindowBars() {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(mApp.toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("Conversations");

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

        if (mConversations == null) {
            mConversations = new ArrayList<>();
        }

        //Click listener for travelling to a conversation
        ConversationsAdapter.OnClickListener clickListener = position ->
                Tools.navigateToFragment(mContext, new ConversationFragment(mConversations.get(position)), R.id.flContainer, "right_to_left");

        //Set adapter
        mAdapter = new ConversationsAdapter(mConversations, mContext, clickListener);
        mApp.recyclerViewConversations.setAdapter(mAdapter);

        //Set layout manager
        mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        mApp.recyclerViewConversations.setLayoutManager(mLayoutManager);

        //Sets up the endless scroller listener
        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                queryConversations(page);
            }
        };

        // Adds the scroll listener to RecyclerView
        mApp.recyclerViewConversations.addOnScrollListener(scrollListener);
    }

    /**
     * Gets the list of projects from the developer's timeline
     */
    private void queryConversations(int page) {

        //Get a query in descending order
        ParseQuery<Conversation> query = ParseQuery.getQuery(Conversation.class)
                .whereContainsAll("participants", Collections.singletonList(mUser.getHandler()));

        query.addDescendingOrder("createdAt");

        //Setup pagination
        query.setLimit(QUERY_LIMIT);
        query.setSkip(page * QUERY_LIMIT);

        query.findInBackground((conversations, e) -> {
            if (e == null) {
                if (page == 0) {
                    mAdapter.clear();
                }
                if (conversations.size() > 0) {
                    mAdapter.addAll(conversations);
                }
            } else {
                Log.d(TAG, "Unable to load conversations");
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