package com.example.opensorcerer.models.users;

import android.os.Parcelable;

import com.example.opensorcerer.models.Conversation;
import com.parse.ParseFile;
import com.parse.ParseRelation;
import com.parse.ParseRole;
import com.parse.ParseUser;

import org.parceler.Parcel;


public abstract class User {

    //Database keys
    private static final String KEY_PROFILE_PICTURE = "profilePicture";
    private static final String KEY_CONVERSATIONS = "conversations";
    private static final String KEY_EXPERIENCE = "experience";
    private static final String KEY_GITHUB_TOKEN = "ghToken";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_GITHUB = "github";
    private static final String KEY_ROLE = "role";
    private static final String KEY_NAME = "name";
    private static final String KEY_BIO = "bio";

    protected ParseUser mUser;

    public User(){
        mUser = new ParseUser();
    }

    public void setHandler(ParseUser user) {
        mUser = user;
    }

    public ParseUser getHandler() { return mUser; }

    public String getObjectId(){
        return mUser.getObjectId();
    }

    public String getUsername(){
        return mUser.getUsername();
    }

    public void setUsername(String username) { mUser.setUsername(username);}

    public String getEmail() {
        return mUser.getEmail();
    }

    public void setEmail(String email) {
        mUser.setEmail(email);
    }

    public void setPassword(String password) {
        mUser.setPassword(password);
    }

    public ParseFile getProfilePicture() {
        return mUser.getParseFile(KEY_PROFILE_PICTURE);
    }

    public void setProfilePicture(ParseFile profilePicture) {
        mUser.put(KEY_PROFILE_PICTURE,profilePicture);
    }

    public String getExperience() {
        return mUser.getString(KEY_EXPERIENCE);
    }

    public void setExperience(String experience) {
        mUser.put(KEY_EXPERIENCE,experience);
    }

    public String getGithub() {
        return mUser.getString(KEY_GITHUB);
    }

    public void setGithub(String github) {
        mUser.put(KEY_GITHUB,github);
    }

    public String getGithubToken() {
        return mUser.getString(KEY_GITHUB_TOKEN);
    }

    public void setGithubToken(String token) {
        mUser.put(KEY_GITHUB_TOKEN,token);
    }

    public String getRole() {
        return mUser.getString(KEY_ROLE);
    }

    public void setRole(String role) {
        mUser.put(KEY_ROLE,role);
    }

    public String getName() {
        return mUser.getString(KEY_NAME);
    }

    public void setName(String name) {
        mUser.put(KEY_NAME,name);
    }

    public String getBio() {
        return mUser.getString(KEY_BIO);
    }

    public void setBio(String bio) {
        mUser.put(KEY_BIO,bio);
    }

    public ParseRelation<Conversation> getConversations(){
        return mUser.getRelation(KEY_CONVERSATIONS);
    }

    public void setConversations(ParseRelation<Conversation> conversation) {
        mUser.put(KEY_CONVERSATIONS,conversation);
    }
}
