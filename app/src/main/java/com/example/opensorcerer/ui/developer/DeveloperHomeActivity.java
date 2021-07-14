package com.example.opensorcerer.ui.developer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.opensorcerer.R;
import com.example.opensorcerer.application.OSApplication;
import com.example.opensorcerer.databinding.ActivityManagerHomeBinding;
import com.example.opensorcerer.models.users.roles.Developer;
import com.example.opensorcerer.ui.developer.fragments.ConversationsFragment;
import com.example.opensorcerer.ui.developer.fragments.FavoritesFragment;
import com.example.opensorcerer.ui.developer.fragments.ProfileFragment;
import com.example.opensorcerer.ui.developer.fragments.ProjectsFragment;
import com.example.opensorcerer.ui.login.LoginActivity;
import com.parse.ParseUser;

import org.kohsuke.github.GitHub;

import java.io.IOException;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class DeveloperHomeActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";
    private ActivityManagerHomeBinding app;
    private Context mContext;
    private GitHub mGitHub;

    private Developer mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_home);


        app = ActivityManagerHomeBinding.inflate(getLayoutInflater());
        setContentView(app.getRoot());

        mContext = this;

        mUser = Developer.fromParseUser(ParseUser.getCurrentUser());

        new Thread(new GitHubLoginTask()).start();

        final FragmentManager fragmentManager = getSupportFragmentManager();

        //Ensure that the id's of the navigation items are final for the switch
        final int actionHome = R.id.actionHome;
        final int actionProfile = R.id.actionProfile;
        final int actionFavorites = R.id.actionFavorites;
        final int actionChats = R.id.actionChats;

        app.bottomNav.setOnItemSelectedListener(item -> {
            Fragment fragment;

            //Navigate to a different fragment depending on the item selected
            //and update the item's icons to highlight the one selected
            switch(item.getItemId()){

                //Home Item selected
                case actionHome:
                    fragment = new ProjectsFragment();
                    break;

                //Post item selected
                case actionFavorites:
                    fragment = new FavoritesFragment();
                    break;

                //Profile item selected
                case actionProfile:

                    //Put the current user into the arguments of the fragment
                    fragment = new ProfileFragment();
                    break;

                //Profile item selected
                case actionChats:

                    //Put the current user into the arguments of the fragment
                    fragment = new ConversationsFragment();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + item.getItemId());
            }

            //Open the selected fragment
            fragmentManager.beginTransaction().replace(app.flContainer.getId(),fragment).commit();
            return true;
        });

        //Set the default window to be the Home
        app.bottomNav.setSelectedItemId(R.id.actionHome);
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


    public class GitHubLoginTask implements Runnable{
        @Override
        public void run() {
            try {
                ((OSApplication) getApplication()).buildGitHub(mUser.getGithubToken());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}