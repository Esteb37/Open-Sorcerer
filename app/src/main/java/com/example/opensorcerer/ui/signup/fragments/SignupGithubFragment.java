package com.example.opensorcerer.ui.signup.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.opensorcerer.databinding.FragmentSignupGithubBinding;
import com.example.opensorcerer.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Fragment for linking a user to their GitHub account
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class SignupGithubFragment extends Fragment {

    /**Tag for logging*/
    private static final String TAG = "SignupGithubFragment";

    /**Binder for ViewBinding*/
    private FragmentSignupGithubBinding app;

    /**Fragment's context*/
    private Context mContext;

    /**Newly created user for signup*/
    private User mNewUser;

    /**
     * Inflates the fragment
     */
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        app = FragmentSignupGithubBinding.inflate(inflater, container, false);
        return app.getRoot();
    }

    /**
     * Sets up the fragment's methods
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getContext();

        //Get the user created in the credentials signup fragment
        mNewUser = SignupGithubFragmentArgs.fromBundle(getArguments()).getUser();

        setupButtonListeners();
    }

    /**
     * Sets up the listeners for the buttons
     */
    private void setupButtonListeners() {

        //Setup "Back" button listener
        app.buttonBack.setOnClickListener(v -> navigateBack());

        //Setup "Finish" button listener
        app.buttonFinish.setOnClickListener(v -> {

            //Get credentials
            mNewUser.setGithubToken(Objects.requireNonNull(app.editTextToken.getText()).toString());

            //Sign the user up into the database
            mNewUser.getHandler().signUpInBackground(e -> {
                if(e==null){
                    navigateForward();
                }
                else{
                    e.printStackTrace();
                }
            });
        });
    }

    /**
     * Navigates back to the credentials signup fragment
     */
    private void navigateBack() {
        SignupGithubFragmentDirections.GithubToCredentialsAction githubToCredentialsAction = SignupGithubFragmentDirections.githubToCredentialsAction(mNewUser);
        NavHostFragment.findNavController(this)
                .navigate(githubToCredentialsAction);
    }

    /**
     * Goes to the Details fragment
     */
    private void navigateForward() {
        SignupGithubFragmentDirections.GithubToDetailsAction githubToDetailsAction = SignupGithubFragmentDirections.githubToDetailsAction(mNewUser);
        NavHostFragment.findNavController(this)
                .navigate(githubToDetailsAction);
    }

    /**
     * Resets the ViewBinder
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        app = null;
    }

}