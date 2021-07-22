package com.example.opensorcerer.ui.signup;

import android.content.Context;
import android.os.Bundle;


import com.example.opensorcerer.databinding.ActivitySignupBinding;
import com.example.opensorcerer.ui.signup.fragments.SignupRoleFragment;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.opensorcerer.R;
import com.parse.ParseUser;

/**
 * Activity for signing a new user in
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class SignupActivity extends AppCompatActivity {

    /**Tag for logging*/
    private static final String TAG = "SignupActivity";

    /**Binder for ViewBinding*/
    private ActivitySignupBinding app;

    /**Activity's context*/
    private Context mContext;

    /**Configuration for the Navigation Graph*/
    private AppBarConfiguration mAppBarConfiguration;

    /**
     * Sets up the activity's methods
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        //Make sure no user is logged in
        ParseUser.logOut();

        setupViewBinding();

        setupNavController();

        loadFirstFragment();
    }

    /**
     * Sets up the fragment's layout
     */
    private void setupViewBinding() {
        app = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(app.getRoot());
    }

    /**
     * Sets up the controller for the navigation graph
     */
    private void setupNavController() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_login);
        mAppBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
    }

    /**
     * Load the first fragment from the Signup Process
     */
    private void loadFirstFragment() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new SignupRoleFragment();
        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
    }

    /**
     * Setup the navigation behavior with the nav controller
     */
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_login);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}