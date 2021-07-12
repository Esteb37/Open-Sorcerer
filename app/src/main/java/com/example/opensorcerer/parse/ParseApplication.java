package com.example.instagram.parse;

import android.app.Application;

import com.example.instagram.R;
import com.example.instagram.models.Comment;
import com.example.instagram.models.Post;
import com.example.instagram.models.User;
import com.parse.Parse;
import com.parse.ParseObject;

/*
    Class for handling the Database
 */
public class ParseApplication extends Application {

    /*
        Sets up the Parse Application
     */
    @Override
    public void onCreate() {
        super.onCreate();

        //Register custom objects into the database
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Comment.class);
        ParseObject.registerSubclass(User.class);

        //Initialize the Parse database application
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build());
    }
}
