package com.example.opensorcerer.models.users.roles;


import com.example.opensorcerer.models.Project;
import com.example.opensorcerer.models.users.User;
import com.example.opensorcerer.models.users.UserHandler;
import com.parse.ParseRelation;
import com.parse.ParseUser;

/**
 * Class for handling User objects with the Developer Role
 */
public class Developer extends User {

    //Database keys.
    private static final String KEY_FAVORITES = "favorites";

    /**
     * Creates a custom Developer Object from a ParseUser object
     *
     * @param user The ParseUser object
     * @return a casted custom developer object
     */
    public static Developer fromParseUser(ParseUser user){
        Developer developer = new Developer();
        developer.setUser(UserHandler.fromParseUser(user));
        return developer;
    }

    public static Developer getCurrentUser(){
        return fromParseUser(ParseUser.getCurrentUser());
    }
    public void setUser(UserHandler user) {
        mUser = user;
    }

    public ParseRelation<Project> getFavorites(){
        return mUser.getRelation(KEY_FAVORITES);
    }


}
