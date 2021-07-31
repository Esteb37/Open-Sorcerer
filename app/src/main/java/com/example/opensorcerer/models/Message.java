package com.example.opensorcerer.models;

import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Class for handling message objects for user chats
 */
@ParseClassName("Message")
public class Message extends ParseObject implements Parcelable {

    //Database keys
    private static final String KEY_CONVERSATION = "conversation";
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_CONTENT = "content";

    /**
     * Content getter
     */
    public String getContent() {
        return getString(KEY_CONTENT);
    }

    /**
     * Content setter
     */
    public void setContent(String content) {
        put(KEY_CONTENT, content);
    }

    /**
     * Conversation setter
     */
    public void setConversation(Conversation conversation) {
        put(KEY_CONVERSATION, conversation);
    }

    /**
     * Author getter
     */
    public ParseUser getAuthor() {
        return getParseUser(KEY_AUTHOR);
    }

    /**
     * Author setter
     */
    public void setAuthor(ParseUser author) {
        put(KEY_AUTHOR, author);
    }
}
