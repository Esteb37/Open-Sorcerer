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

    public void setProfilePicture(ParseFile profilePicture) {
        put(KEY_PROFILE_PICTURE,profilePicture);
    }

    public String getExperience() {
        return getString(KEY_EXPERIENCE);
    }

    public void setExperience(String experience) {
        put(KEY_EXPERIENCE,experience);
    }

    public String getGithub() {
        return getString(KEY_GITHUB);
    }

    public void setGithub(String github) {
        put(KEY_GITHUB,github);
    }

    public String getRole() {
        return ((ParseRole) getParseObject(KEY_ROLE)).getName();
    }

    public void setRole(String role) {
        put(KEY_ROLE,new ParseRole(role));
    }

    public String getName() {
        return getString(KEY_NAME);
    }

    public void setName(String name) {
        put(KEY_NAME,name);
    }

    public String getBio() {
        return getString(KEY_BIO);
    }

    public void setBio(String bio) {
        put(KEY_BIO,bio);
    }

    public ParseRelation<Conversation> getConversations(){
        return getRelation(KEY_CONVERSATIONS);
    }

    public void setConversations(ParseRelation<Conversation> conversation) {
        put(KEY_CONVERSATIONS,conversation);
    }
}
