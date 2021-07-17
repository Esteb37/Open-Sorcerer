package com.example.opensorcerer.ui.manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.opensorcerer.R;
import com.example.opensorcerer.application.OSApplication;
import com.example.opensorcerer.databinding.ActivityManagerHomeBinding;
import com.example.opensorcerer.models.users.roles.Manager;
import com.example.opensorcerer.ui.login.LoginActivity;
import com.example.opensorcerer.ui.manager.fragments.ConversationsFragment;
import com.example.opensorcerer.ui.manager.fragments.CreateProjectFragment;
import com.example.opensorcerer.ui.manager.fragments.MyProjectsFragment;
import com.example.opensorcerer.ui.manager.fragments.ProfileFragment;
import com.parse.ParseUser;

import org.kohsuke.github.GitHub;

import java.util.Objects;


/**
 * Main activity for the manager users
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class ManagerHomeActivity extends AppCompatActivity {

    /**Tag for logging*/
    private static final String TAG = "DeveloperManagerActivity";

    /**Binder object for ViewBinding*/
    private ActivityManagerHomeBinding app;

    /**Fragment's context*/
    private Context mContext;

    /**Current logged in user*/
    private Manager mUser;

    /**GitHub API handler*/
    private GitHub mGitHub;

    /**
     * Sets up the activity's methods
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupLayout();

        getState();

        gitHubLogin();

        setupBottomNavigation();
    }

    /**
     * Sets up the Activity's layout
     */
    private void setupLayout() {
        app = ActivityManagerHomeBinding.inflate(getLayoutInflater());
        setContentView(app.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_manager_main);
    }

    /**
     * Gets the current state of the member variables
     */
    private void getState() {
        mContext = this;
        mUser = Manager.fromParseUser(ParseUser.getCurrentUser());
    }

    /**
     * Initializes the GitHub API handler with the logged in user's OAuth token
     */
    private void gitHubLogin() {
        ((OSApplication) getApplication()).buildGitHub(mUser.getGithubToken());
    }

    /**
     * Sets up the bottom navigation bar
     */
    private void setupBottomNavigation() {

        final FragmentManager fragmentManager = getSupportFragmentManager();

        //Ensure that the id's of the navigation items are final for the switch
        final int actionHome = R.id.actionHome;
        final int actionProfile = R.id.actionProfile;
        final int actionNew = R.id.actionNew;
        final int actionChats = R.id.actionChats;
        
        app.bottomNav.setOnItemSelectedListener(item -> {
            Fragment fragment = null;

            //Navigate to a different fragment depending on the item selected
            //and update the item's icons to highlight the one selected
            switch (item.getItemId()) {

                //Home Item selected
                case actionHome:
                    fragment = new MyProjectsFragment();
                    break;

                //New project item selected
                case actionNew:
                    fragment = new CreateProjectFragment();
                    break;

                //Profile item selected
                case actionProfile:
                    fragment = new ProfileFragment();
                    break;

                //Chats item selected
                case actionChats:
                    fragment = new ConversationsFragment();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + item.getItemId());
            }

            //Open the selected fragment
            fragmentManager.beginTransaction().replace(app.flContainer.getId(), fragment).commit();
            return true;
        });

        //Set the default window to be the Home
        app.bottomNav.setSelectedItemId(R.id.actionHome);

         
    }

    /**
     * Inflates the options menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    /**
     * Detects the Options Menu item that was clicked and responds accordingly
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        //If the logout button was clicked
        if(item.getItemId() == R.id.btnLogout){

            //Log the user out
            ParseUser.logOutInBackground(e -> {

                //Go to the login activity
                if(e == null){
                    Intent i = new Intent(mContext, LoginActivity.class);
                    startActivity(i);
                    finish();
                } else { //Notify error
                    Toast.makeText(mContext, "Unable to log out.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }
}