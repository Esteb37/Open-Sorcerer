package com.example.opensorcerer.models;

import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.List;

/**
 * Class for handling Conversation objects for user chats
 */
@ParseClassName("Conversation")
public class Conversation extends ParseObject implements Parcelable {

    private static String KEY_MESSAGES = "messages";
    private static String KEY_SECOND = "second";
    private static String KEY_FIRST = "first";

    public List<String> getMessages(){
        return getList(KEY_MESSAGES);
    }

    public void setMessages(List<String> messages){
        put(KEY_MESSAGES,messages);
    }

    public User getFirst(){
        return User.fromParseUser(getParseUser(KEY_FIRST));
    }

    public void setFirst(User first){
        put(KEY_FIRST,first.getHandler());
    }

    public User getSecond(){
        return User.fromParseUser(getParseUser(KEY_SECOND));
    }

    public void setSecond(User second){
        put(KEY_SECOND,second.getHandler());
    }

    public User getOpposite(){
        return User.getCurrentUser().getObjectId().equals(getFirst().getObjectId()) ? getSecond() : getFirst();
    }

}
