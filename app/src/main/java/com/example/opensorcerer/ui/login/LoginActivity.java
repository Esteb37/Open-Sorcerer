package com.example.opensorcerer.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.opensorcerer.R;
import com.example.opensorcerer.databinding.ActivityLoginBinding;
import com.example.opensorcerer.models.User;
import com.example.opensorcerer.ui.main.MainActivity;
import com.example.opensorcerer.ui.signup.SignupActivity;
import com.parse.ParseUser;

import java.util.Objects;

/**
 * Activity for logging a user in
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class LoginActivity extends AppCompatActivity {

    /**
     * Tag for Logging
     */
    private static final String TAG = "LoginActivity";

    /**
     * Binder for ViewBinding
     */
    private ActivityLoginBinding mApp;

    /**
     * Current context
     */
    private Context mContext;

    /**
     * Sets up the activity's methods
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Set current context
        mContext = this;

        checkUserLogin();

        setupViewBinding();

        setupLoginButtonListener();

        setupSignupButtonListener();
    }

    /**
     * Verifies if a user has already logged in into the application and if so
     * sends them directly to their home activity depending on their role
     */
    private void checkUserLogin() {
        if (ParseUser.getCurrentUser() != null) {
            String role = User.fromParseUser(ParseUser.getCurrentUser()).getRole();
            navigateToMain();
        }
    }

    /**
     * Sets up the View Binder
     */
    private void setupViewBinding() {
        mApp = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(mApp.getRoot());
    }


    /**
     * Sets up the listener for the "Log in" button
     */
    private void setupLoginButtonListener() {

        mApp.buttonLogin.setOnClickListener(v -> {

            //Get the credentials
            String username = Objects.requireNonNull(mApp.editTextUsername.getText()).toString();
            String password = Objects.requireNonNull(mApp.editTextPassword.getText()).toString();

            //Attempt to log the user in with the credentials
            ParseUser.logInInBackground(username, password, (user, e) -> {

                //If the login is successful
                if (e == null) {

                    //Go to their according home activity
                    String role = User.fromParseUser(ParseUser.getCurrentUser()).getRole();
                    navigateToMain();
                } else {
                    Log.d(TAG, "Issue with login", e);

                    //Notify the user that the login was unsuccessful
                    Toast.makeText(mContext, "Username or password is incorrect.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    /**
     * Sets up the "Setup" button listener
     */
    private void setupSignupButtonListener() {
        mApp.buttonSignup.setOnClickListener(v -> {
            Intent i = new Intent(mContext, SignupActivity.class);
            startActivity(i);
        });
    }

    /**
     * Navigates to the corresponding home activity depending on the user's role
     */
    private void navigateToMain() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

}