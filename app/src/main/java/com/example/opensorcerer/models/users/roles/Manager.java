package com.example.opensorcerer.models.users.roles;

import com.example.opensorcerer.models.Project;
import com.example.opensorcerer.models.users.User;
import com.parse.ParseRelation;
import com.parse.ParseUser;


/**
 * Class for handling User objects with the Manager role
 */
@SuppressWarnings("unused")
public class Manager extends User {

    //Database keys.
    private static final String KEY_PROJECTS = "projects";

    public Manager(){
        setRole("manager");
    }
    /**
     * Creates a custom Manager Object from a ParseUser object
     *
     * @param user The ParseUser object
     * @return a casted custom manager object
     */
    public static Manager fromParseUser(ParseUser user){
        Manager manager = new Manager();
        manager.setHandler(user);
        return manager;
    }

    public static Manager getCurrentUser(){
        return fromParseUser(ParseUser.getCurrentUser());
    }

    public ParseRelation<Project> getProjects(){
        return mUser.getRelation(KEY_PROJECTS);
    }

    public void setProjects(ParseRelation<Project> projects) {
        mUser.put(KEY_PROJECTS,projects);
    }
}
