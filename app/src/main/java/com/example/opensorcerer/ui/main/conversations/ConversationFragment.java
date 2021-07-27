package com.example.opensorcerer.ui.main.conversations;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.opensorcerer.R;
import com.example.opensorcerer.models.EndlessRecyclerViewScrollListener;
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
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Fragment for displaying a single one-on-one conversation
 */
@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class ConversationFragment extends Fragment {

    /**
     * Tag for logging
     */
    private static final String TAG = "ConversationFragment";

    /**
     * Amount of conversations to load at a time
     */
    private static final int QUERY_LIMIT = 20;

    /**
     * The handler for the fetching messages loop
     */
    private static final Handler myHandler = new Handler(Looper.getMainLooper());

    /**
     * The current conversation
     */
    final private Conversation mConversation;

    /**
     * The interval for fetching new messages
     */
    private final long POLL_INTERVAL = TimeUnit.SECONDS.toMillis(1);

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
     * The other user in this conversation
     */
    private User mOpposite;

    /**
     * List of this conversation's messages
     */
    private ArrayList<Message> mMessages;

    /**
     * Adapter for the conversations recyclerview
     */
    private MessagesAdapter mAdapter;

    /**
     * Layout manager for the conversations recyclerview
     */
    private LinearLayoutManager mLayoutManager;

    /**
     * Sets the specified conversation
     */
    public ConversationFragment(Conversation conversation) {
        mConversation = conversation;
    }

    public ConversationFragment(User oppositeUser) {
        mConversation = Conversation.getConversationWithUser(oppositeUser);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Sets up the fragment's methods
     */
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        mApp = FragmentConversationBinding.inflate(inflater, container, false);
        return mApp.getRoot();
    }

    /**
     * Sets up the fragment's methods
     */
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getState();

        setupConversation();

        setupRecyclerView();

        setupSendMessageListener();

        setupLiveQuery();

        hideNavigationBars();

        queryMessages(0);
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
            if (oppositeProfilePicture != null) {
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

        if (mMessages == null) {
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
     * Sets up the listener for the "send message" button
     */
    void setupSendMessageListener() {
        mApp.buttonSend.setOnClickListener(v -> {

            //Get the composed content
            String content = mApp.editTextCompose.getText().toString();

            if (content.length() > 0) {

                //Create a new message with the content and this user as the author
                Message newMessage = new Message();
                newMessage.setAuthor(mUser.getHandler());
                newMessage.setContent(content);
                newMessage.setConversation(mConversation);

                //Save the message in the database and in the conversation
                newMessage.saveInBackground(e -> {
                    if (e == null) {

                        mConversation.addMessage(newMessage);

                        mApp.editTextCompose.setText("");
                    } else {
                        Toast.makeText(mContext, "Unable to send message", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                });
            }
        });
    }

    /**
     * Sets up the Parse Live Query for when the user sends a new message
     */
    private void setupLiveQuery() {

        //Setup the LiveQuery object
        String websocketUrl = "wss://opensorcerer.b4a.io/";
        ParseLiveQueryClient parseLiveQueryClient = null;
        try {
            parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient(new URI("wss://opensorcerer.b4a.io/"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        ParseQuery<Message> parseQuery = ParseQuery.getQuery(Message.class);
        // This query can even be more granular (i.e. only refresh if the entry was added by some other user)
        // parseQuery.whereNotEqualTo(USER_ID_KEY, ParseUser.getCurrentUser().getObjectId());

        // Connect to Parse server
        assert parseLiveQueryClient != null;
        SubscriptionHandling<Message> subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery);

        // Listen for CREATE events on the Message class
        subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE, (query, newMessage) -> {

            // RecyclerView updates need to be run on the UI thread
            requireActivity().runOnUiThread(() -> {
                mAdapter.addMessage(newMessage);
                mApp.recyclerViewMessages.scrollToPosition(mMessages.size() - 1);
            });
        });
    }

    /**
     * Hides the bottom navigation and details bottom navigation bar if any
     */
    private void hideNavigationBars() {
        requireActivity().findViewById(R.id.bottomNav).setVisibility(View.GONE);

        if (requireActivity().findViewById(R.id.bottomNavDetails) != null) {
            requireActivity().findViewById(R.id.bottomNavDetails).setVisibility(View.GONE);
        }
    }

    /**
     * Gets the list of messages from this conversation
     */
    private void queryMessages(int page) {

        List<String> messages = mConversation.getMessages();

        if(messages != null){
            //Get a query in descending order
            ParseQuery<Message> query = ParseQuery.getQuery(Message.class)
                    .whereContainedIn("objectId", messages);

            //Setup pagination
            query.setLimit(QUERY_LIMIT);
            query.setSkip(page * QUERY_LIMIT);

            query.findInBackground((newMessages, e) -> {
                if (e == null) {
                    if (page == 0) {
                        mAdapter.clear();
                    }
                    if (messages.size() > 0) {
                        mAdapter.addAll(newMessages);
                    }

                    mApp.recyclerViewMessages.scrollToPosition(mMessages.size() - 1);
                } else {
                    Log.d(TAG, "Unable to load messages.");
                }
            });
        }
    }

    /**
     * Fetches new messages, if any, every specified interval only when the application is running
     */
    @Override
    public void onResume() {
        super.onResume();
        // Only start checking for new messages when the app becomes active in foreground
        myHandler.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {

                        //Query all messages and reset the function for another loop
                        queryMessages(0);
                        myHandler.postDelayed(this, POLL_INTERVAL);
                    }
                }, POLL_INTERVAL);
    }
}