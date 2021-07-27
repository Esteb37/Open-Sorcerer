package com.example.opensorcerer.models;

import android.os.Parcelable;
import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Class for handling Conversation objects for user chats
 */
@SuppressWarnings("unused")
@ParseClassName("Conversation")
public class Conversation extends ParseObject implements Parcelable {

    // Database keys
    private static final String KEY_PARTICIPANTS = "participants";
    private static final String KEY_MESSAGES = "messages";

    /**
     * Gets an active conversation between the current user and another, if it exists
     */
    public static Conversation findConversationWithUser(User opposite){

        User current = User.getCurrentUser();

        ParseQuery<Conversation> queryFirst = ParseQuery.getQuery(Conversation.class)
                .whereContainedIn("participants", Arrays.asList(current,opposite));
        try {
            return queryFirst.getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Messages getter
     */
    public List<String> getMessages() {
        return getList(KEY_MESSAGES);
    }

    /**
     * Messages setter
     */
    public void setMessages(List<String> messages) {
        put(KEY_MESSAGES, messages);
        update();
    }

    /**
     * Participants getter
     */
    public List<User> getParticipants() {
        return User.toUserArray(Objects.requireNonNull(getList(KEY_PARTICIPANTS)));
    }

    /**
     * Participants setter
     */
    public void setParticipants(List<User> participants) {
        put(KEY_PARTICIPANTS,User.toParseUserArray(participants));
    }

    /**
     * Gets the user that is not the current user in this conversation
     */
    public User getOpposite() {
        List<User> participants = getParticipants();
        User current = User.getCurrentUser();
        return participants.get(0).getObjectId().equals(current.getObjectId())
                ? participants.get(1)
                : participants.get(0);
    }

    /**
     * Adds a single message to the conversation
     */
    public void addMessage(Message message){
        List<String> messages = getMessages();
        if(messages == null){
            messages = new ArrayList<>();
        }
        messages.add(message.getObjectId());
        setMessages(messages);
    }

    /**
     * Saves the conversation into the database
     */
    private void update() {
        saveInBackground(e -> {
            if (e == null) {
                Log.d("Conversation", "Conversation updated");
            } else {
                e.printStackTrace();
            }
        });
    }
}
