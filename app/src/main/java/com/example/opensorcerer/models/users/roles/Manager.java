package com.example.opensorcerer.models.users.roles;

import com.example.opensorcerer.models.Project;
import com.example.opensorcerer.models.users.User;
import com.example.opensorcerer.models.users.UserHandler;
import com.parse.ParseRelation;
import com.parse.ParseUser;

/**
 * Class for handling User objects with the Manager role
 */
public class Manager extends User {

    //Database keys.
    private static final String KEY_PROJECTS = "projects";

    /**
     * Creates a custom Manager Object from a ParseUser object
     *
     * @param user The ParseUser object
     * @return a casted custom manager object
     */
    public static Manager fromParseUser(ParseUser user){
        Manager manager = new Manager();
        manager.setUser(UserHandler.fromParseUser(user));
        return manager;
    }

    public ParseRelation<Project> getFavorites(){
        return mUser.getRelation(KEY_PROJECTS);
    }

}
