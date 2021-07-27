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

    private static String KEY_CONVERSATION = "conversation";
    private static String KEY_AUTHOR = "author";
    private static String KEY_CONTENT = "content";


    public String getContent(){
        return getString(KEY_CONTENT);
    }

    public void setContent(String content){
        put(KEY_CONTENT,content);
    }

    public Conversation getConversation(){
        return (Conversation) getParseObject(KEY_CONVERSATION);
    }

    public void setConversation(Conversation conversation){
        put(KEY_CONVERSATION,conversation);
    }

    public ParseUser getAuthor() {
        return getParseUser(KEY_AUTHOR);
    }

    public void setAuthor(ParseUser author){
        put(KEY_AUTHOR, author);
    }
}
