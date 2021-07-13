package com.example.opensorcerer.ui.manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.opensorcerer.R;
import com.example.opensorcerer.application.OSApplication;
import com.example.opensorcerer.databinding.ActivityManagerHomeBinding;
import com.example.opensorcerer.models.users.User;
import com.example.opensorcerer.models.users.roles.Manager;
import com.example.opensorcerer.ui.developer.DeveloperHomeActivity;
import com.example.opensorcerer.ui.login.LoginActivity;
import com.parse.ParseUser;

import org.kohsuke.github.GHMyself;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import java.io.IOException;

public class ManagerHomeActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";
    private ActivityManagerHomeBinding app;
    private Context mContext;
    private GitHub mGitHub;

    private User mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_home);


        app = ActivityManagerHomeBinding.inflate(getLayoutInflater());
        setContentView(app.getRoot());

        mContext = this;

        mUser = Manager.fromParseUser(ParseUser.getCurrentUser());

        new BuildGitHubTask().execute();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.btnLogout){
            ParseUser.logOutInBackground(e -> {
                if(e == null){
                    Intent i = new Intent(mContext, LoginActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(mContext, "Unable to log out.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            });

        }
        return super.onOptionsItemSelected(item);
    }


    class BuildGitHubTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                mGitHub = ((OSApplication) getApplication()).buildGitHub(mUser.getGithubToken());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }



}