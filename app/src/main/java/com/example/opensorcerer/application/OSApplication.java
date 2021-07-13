package com.example.opensorcerer.application;

import android.app.Activity;
import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.example.opensorcerer.R;

import com.example.opensorcerer.models.Conversation;
import com.example.opensorcerer.models.Project;
import com.parse.Parse;
import com.parse.ParseObject;

import org.kohsuke.github.GHMyself;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import java.io.IOException;

/**
    Class for handling the Database
 */
public class OSApplication extends Application {

    /**
        Sets up the Parse Application
     */

    GitHub mGitHub;
    GHMyself mGHMyself;

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);

        //Register custom objects into the database
        ParseObject.registerSubclass(Conversation.class);
        ParseObject.registerSubclass(Project.class);


        //Initialize the Parse database application
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build());
    }

    public GitHub getGitHub(){
        return mGitHub;
    }

    public GitHub buildGitHub(String token) throws IOException{
        GitHubBuilder builder = new GitHubBuilder();
        mGitHub = new GitHubBuilder().withJwtToken(token).build();
        mGHMyself = mGitHub.getMyself();
        return getGitHub();
    }

    public GHMyself getGHMyself(){
        return mGHMyself;
    }
}
