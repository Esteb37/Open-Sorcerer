package com.example.opensorcerer.models.users;

import com.example.opensorcerer.models.Conversation;
import com.parse.ParseFile;
import com.parse.ParseRelation;
import com.parse.ParseRole;

public abstract class User {

    protected UserHandler mUser;

    public User(){
        mUser = new UserHandler();
    }

    public void setUser(UserHandler user) {
        mUser = user;
    }

    public String getObjectId(){
        return mUser.getObjectId();
    }

    public String getUsername(){
        return mUser.getUsername();
    }

    public String getEmail() {
        return mUser.getEmail();
    }

    public ParseFile getProfilePicture() {
        return mUser.getProfilePicture();
    }

    public String getExperience() {
        return mUser.getExperience();
    }

    public String getGithub() {
        return mUser.getGithub();
    }

    public ParseRole getRole() {
        return (ParseRole) mUser.getRole();
    }

    public String getName() {
        return mUser.getName();
    }

    public String getBio() {
        return mUser.getBio();
    }

    public ParseRelation<Conversation> getConversations(){
        return mUser.getConversations();
    }
}
