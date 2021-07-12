package com.example.opensorcerer.parse;

import android.app.Application;

import com.example.opensorcerer.R;

import com.example.opensorcerer.models.Conversation;
import com.example.opensorcerer.models.Project;
import com.parse.Parse;
import com.parse.ParseObject;

/**
    Class for handling the Database
 */
public class ParseApplication extends Application {

    /**
        Sets up the Parse Application
     */
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
}
