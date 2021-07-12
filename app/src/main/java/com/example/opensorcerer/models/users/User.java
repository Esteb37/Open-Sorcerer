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

    public void setUsername(String username) { mUser.setUsername(username);}

    public String getEmail() {
        return mUser.getEmail();
    }

    public void setEmail(String email) {
        mUser.setEmail(email);
    }

    public ParseFile getProfilePicture() {
        return mUser.getProfilePicture();
    }

    public void setProfilePicture(ParseFile picture) {
        mUser.setProfilePicture(picture);
    }

    public String getExperience() {
        return mUser.getExperience();
    }

    public void setExperience(String experience){
        mUser.setExperience(experience);
    }

    public String getGithub() {
        return mUser.getGithub();
    }

    public void getGithub(String github){
        mUser.setGithub(github);
    }

    public String getRole() {
        return mUser.getRole();
    }

    public void setRole(String role){
        mUser.setRole(role);
    }

    public String getName() {
        return mUser.getName();
    }

    public void setName(String name){
        mUser.setName(name);
    }

    public String getBio() {
        return mUser.getBio();
    }

    public void setBio(String bio){
        mUser.setBio(bio);
    }

    public ParseRelation<Conversation> getConversations(){
        return mUser.getConversations();
    }

    public void setConversations(ParseRelation<Conversation> conversations){
        mUser.setConversations(conversations);
    }
}
