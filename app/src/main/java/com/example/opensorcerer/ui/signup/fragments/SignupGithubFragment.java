package com.example.opensorcerer.ui.signup.fragments;

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
public class SignupGithubFragment extends Fragment {

    /**
     * Binder for ViewBinding
     */
    private FragmentSignupGithubBinding mApp;

    /**
     * Newly created user for signup
     */
    private User mNewUser;

    /**
     * Inflates the fragment
     */
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mApp = FragmentSignupGithubBinding.inflate(inflater, container, false);
        return mApp.getRoot();
    }

    /**
     * Sets up the fragment's methods
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Get the user created in the credentials signup fragment
        mNewUser = SignupGithubFragmentArgs.fromBundle(getArguments()).getUser();

        setupButtonListeners();
    }

    /**
     * Sets up the listeners for the buttons
     */
    private void setupButtonListeners() {

        //Setup "Back" button listener
        mApp.buttonBack.setOnClickListener(v -> navigateBack());

        //Setup "Finish" button listener
        mApp.buttonFinish.setOnClickListener(v -> {

            //Get credentials
            mNewUser.setGithubToken(Objects.requireNonNull(mApp.editTextToken.getText()).toString());

            //Sign the user up into the database
            mNewUser.getHandler().signUpInBackground(e -> {
                if (e == null) {
                    navigateForward();
                } else {
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
        mApp = null;
    }

}