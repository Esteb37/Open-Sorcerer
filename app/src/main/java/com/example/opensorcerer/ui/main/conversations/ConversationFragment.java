package com.example.opensorcerer.ui.main.conversations;

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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.opensorcerer.R;
import com.example.opensorcerer.adapters.EndlessRecyclerViewScrollListener;
import com.example.opensorcerer.adapters.MessagesAdapter;
import com.example.opensorcerer.databinding.FragmentConversationBinding;
import com.example.opensorcerer.models.Conversation;
import com.example.opensorcerer.models.Message;
import com.example.opensorcerer.models.User;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;

import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class ConversationFragment extends Fragment {

    /**
     * Tag for logging
     */
    private static final String TAG = "ConversationFragment";

    /**
     * Binder object for ViewBinding
     */
    private FragmentConversationBinding mApp;

    /**
     * Fragment's context
     */
    private Context mContext;

    /**
     * Current logged in user
     */
    private User mUser;

    /**
     * List of this conversation's messages
     */
    private ArrayList<Message> mMessages;

    /**
     * The current conversation
     */
    final private Conversation mConversation;

    /**
     * Adapter for the conversations recyclerview
     */
    private MessagesAdapter mAdapter;

    /**
     * Layout manager for the conversations recyclerview
     */
    private LinearLayoutManager mLayoutManager;

    /**
     * Amount of conversations to load at a time
     */
    private static final int QUERY_LIMIT = 20;

    /**
     * The other user int his conversation
     */
    private User mOpposite;

    public ConversationFragment(Conversation conversation) {
        mConversation = conversation;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        mApp = FragmentConversationBinding.inflate(inflater, container, false);
        return mApp.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getState();

        setupConversation();

        setupLiveQuery();

        setupRecyclerView();

        queryMessages(0);

        requireActivity().findViewById(R.id.bottomNav).setVisibility(View.INVISIBLE);
    }

    private void setupLiveQuery() {

        String websocketUrl = "wss://opensorcerer.b4a.io/";
        ParseLiveQueryClient parseLiveQueryClient = null;
        parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient();

        ParseQuery<Message> parseQuery = ParseQuery.getQuery(Message.class);
        // This query can even be more granular (i.e. only refresh if the entry was added by some other user)
        // parseQuery.whereNotEqualTo(USER_ID_KEY, ParseUser.getCurrentUser().getObjectId());

        // Connect to Parse server
        SubscriptionHandling<Message> subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery);

        // Listen for CREATE events on the Message class
        subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE, (query, newMessage) -> {
            mAdapter.addMessage(newMessage);

            // RecyclerView updates need to be run on the UI thread
            requireActivity().runOnUiThread(() -> {
                mAdapter.notifyDataSetChanged();
                mApp.recyclerViewMessages.scrollToPosition(0);
            });
        });
    }

    /**
     * Gets the current state for the member variables.
     */
    private void getState() {
        mContext = getContext();

        mUser = User.getCurrentUser();
    }

    /**
     * Loads the details of this conversation
     */
    private void setupConversation() {
        try {
            mOpposite = mConversation.getOpposite().fetchIfNeeded();

            mApp.textViewUsername.setText(mOpposite.getUsername());

            ParseFile oppositeProfilePicture = mOpposite.getProfilePicture();
            if(oppositeProfilePicture != null){
                Glide.with(mContext)
                        .load(oppositeProfilePicture.getUrl())
                        .into(mApp.imageViewProfilePicture);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets up the timeline's Recycler View
     */
    private void setupRecyclerView() {

        if(mMessages == null){
            mMessages = new ArrayList<>();
        }

        //Set adapter
        mAdapter = new MessagesAdapter(mMessages, mContext);
        mApp.recyclerViewMessages.setAdapter(mAdapter);

        //Set layout manager
        mLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        mApp.recyclerViewMessages.setLayoutManager(mLayoutManager);

        //Sets up the endless scroller listener
        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                queryMessages(page);
            }
        };

        // Adds the scroll listener to RecyclerView
        mApp.recyclerViewMessages.addOnScrollListener(scrollListener);
    }

    /**
     * Gets the list of projects from the developer's timeline
     */
    private void queryMessages(int page) {

        //Get a query in descending order
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);


        //Setup pagination
        query.setLimit(20);
        query.setSkip(page * QUERY_LIMIT);

        query.findInBackground((messages, e) -> {
            Log.d("Test",""+messages.size());
            if (e == null) {
                if(page == 0 ){
                    mAdapter.clear();
                }
                if (messages.size() > 0) {
                    mAdapter.addAll(messages);
                }
            } else {
                Log.d(TAG, "Unable to load messages.");
            }
        });
    }
}