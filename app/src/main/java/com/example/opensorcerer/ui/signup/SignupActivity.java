package com.example.opensorcerer.ui.signup;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.opensorcerer.R;
import com.example.opensorcerer.databinding.ActivitySignupBinding;
import com.parse.ParseUser;

/**
 * Activity for signing a new user in
 */
public class SignupActivity extends AppCompatActivity {

    /**
     * Configuration for the Navigation Graph
     */
    private AppBarConfiguration mAppBarConfiguration;

    /**
     * Sets up the activity's methods
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Make sure no user is logged in
        ParseUser.logOut();

        setupViewBinding();

        setupNavController();
    }

    /**
     * Sets up the fragment's layout
     */
    private void setupViewBinding() {
        ActivitySignupBinding app = ActivitySignupBinding.inflate(getLayoutInflater());
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
     * Setup the navigation behavior with the nav controller
     */
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_login);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}