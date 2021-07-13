package com.example.opensorcerer.github;

import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;

public class GitHubClient{

    private static GitHubClient instance = null;

    GitHub mHandler;
    GHUser mUser;

    private GitHubClient(){

    }

    public static GitHubClient getInstance(){
        if (instance == null)
            instance = new GitHubClient();

        return instance;
    }




}
