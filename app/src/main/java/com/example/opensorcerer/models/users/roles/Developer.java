package com.example.opensorcerer.models.users.roles;


import com.example.opensorcerer.models.Project;
import com.example.opensorcerer.models.users.User;
import com.parse.ParseRelation;
import com.parse.ParseUser;

/**
 * Class for handling User objects with the Developer Role
 */
@SuppressWarnings("unused")
public class Developer extends User {

    //Database keys.
    private static final String KEY_FAVORITES = "favorites";

    /**
     * Constructor that sets the user's role
     */
    public Developer(){
        setRole("developer");
    }

    /**
     * Creates a custom Developer Object from a ParseUser object
     *
     * @param user The ParseUser object
     * @return a casted custom developer object
     */
    public static Developer fromParseUser(ParseUser user){
        Developer developer = new Developer();
        developer.setHandler(user);
        return developer;
    }

    /**
     * Gets the current logged in user as a Developer object
     *
     * @return the logged in developer
     */
    public static Developer getCurrentUser(){
        return fromParseUser(ParseUser.getCurrentUser());
    }

    /**Favorites list getter*/
    public ParseRelation<Project> getFavorites(){
        return mHandler.getRelation(KEY_FAVORITES);
    }


}
