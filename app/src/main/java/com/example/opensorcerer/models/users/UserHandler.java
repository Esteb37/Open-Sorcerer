package com.example.opensorcerer.models.users;

import android.os.Parcelable;

import com.example.opensorcerer.models.Conversation;
import com.parse.ParseFile;
import com.parse.ParseRelation;
import com.parse.ParseRole;
import com.parse.ParseUser;

/**
 * Custom class for handling Parse user objects
 */
public class UserHandler extends ParseUser implements Parcelable {

    //Database keys
    private static final String KEY_PROFILE_PICTURE = "profilePicture";
    private static final String KEY_CONVERSATIONS = "conversations";
    private static final String KEY_EXPERIENCE = "experience";
    private static final String KEY_GITHUB = "github";
    private static final String KEY_ROLE = "role";
    private static final String KEY_NAME = "name";
    private static final String KEY_BIO = "bio";

    /**
     * Creates a custom User Object from a ParseUser object
     *
     * @param user The ParseUser object
     * @return a casted custom user object
     */
    public static UserHandler fromParseUser(ParseUser user){
        return (UserHandler) user;
    }

    /**
     * Gets the current logged in user
     * @return the user object of the logged in user
     */
    public static UserHandler getCurrentUser(){
        return fromParseUser(ParseUser.getCurrentUser());
    }

    public ParseFile getProfilePicture() {
        return getParseFile(KEY_PROFILE_PICTURE);
    }

    public String getExperience() {
        return getString(KEY_EXPERIENCE);
    }

    public String getGithub() {
        return getString(KEY_GITHUB);
    }

    public ParseRole getRole() {
        return (ParseRole) getParseObject(KEY_ROLE);
    }

    public String getName() {
        return getString(KEY_NAME);
    }

    public String getBio() {
        return getString(KEY_BIO);
    }

    public ParseRelation<Conversation> getConversations(){
        return getRelation(KEY_CONVERSATIONS);
    }
}
