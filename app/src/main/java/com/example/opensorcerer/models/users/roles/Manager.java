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

    /** Gets the current logged in user as a Manager object
     * @return the current logged in Manager
     */
    public static Manager getCurrentUser(){
        return fromParseUser(ParseUser.getCurrentUser());
    }

    /**Projects list getter*/
    public ParseRelation<Project> getProjects(){
        return mHandler.getRelation(KEY_PROJECTS);
    }

    /**Projects list setter*/
    public void setProjects(ParseRelation<Project> projects) {
        mHandler.put(KEY_PROJECTS,projects);
        update();
    }

    /**
     * Adds a project to the user's "created projects" list
     */
    public void addProject(Project project){
        ParseRelation<Project> projects = getProjects();
        projects.add(project);
        setProjects(projects);
        update();
    }


}
