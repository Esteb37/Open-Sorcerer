package com.example.opensorcerer.application;

import android.app.Application;

import com.example.opensorcerer.R;
import com.example.opensorcerer.models.Conversation;
import com.example.opensorcerer.models.Message;
import com.example.opensorcerer.models.Project;
import com.parse.Parse;
import com.parse.ParseObject;

import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Class for hosting the application and handling the Parse database and the GitHub API
 */
@SuppressWarnings("unused")
public class OSApplication extends Application {

    /**
     * The GitHub API handler
     */
    GitHub mGitHub;

    @Override
    public void onCreate() {
        super.onCreate();
        setupParse();
    }

    /**
     * Sets up the Parse Application
     */
    private void setupParse() {

        Parse.enableLocalDatastore(this);

        //Register custom objects into the database
        ParseObject.registerSubclass(Conversation.class);
        ParseObject.registerSubclass(Message.class);
        ParseObject.registerSubclass(Project.class);


        //Initialize the Parse database application
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build());
    }

    /**
     * GitHub API handler getter
     */
    public GitHub getGitHub() {
        return mGitHub;
    }

    /**
     * Builds the GitHub application in the background with the user's OAuth token
     */
    public GitHub buildGitHub(String token) {
        Thread thread = new Thread(() -> {
            try {
                mGitHub = new GitHubBuilder().withJwtToken(token).build();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return mGitHub;
    }

}
