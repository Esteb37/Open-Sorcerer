package com.example.opensorcerer.ui.signup.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.opensorcerer.databinding.FragmentSignupSecondBinding;
import com.example.opensorcerer.models.users.User;
import com.example.opensorcerer.ui.developer.DeveloperHomeActivity;
import com.example.opensorcerer.ui.manager.ManagerHomeActivity;

import org.jetbrains.annotations.NotNull;

/**
 * Fragment for linking a user to their GitHub account
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class SignupSecondFragment extends Fragment {

    /**Tag for logging*/
    private static final String TAG = "SignupSecondFragment";

    /**Binder for ViewBinding*/
    private FragmentSignupSecondBinding app;

    /**Fragment's context*/
    private Context mContext;

    /**Newly created user for signup*/
    private User mNewUser;

    /**
     * Inflates the fragment
     */
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        app = FragmentSignupSecondBinding.inflate(inflater, container, false);
        return app.getRoot();
    }

    /**
     * Sets up the fragment's methods
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getContext();

        //Get the user created in the First signup fragment
        mNewUser = SignupSecondFragmentArgs.fromBundle(getArguments()).getUser();

        setupButtonListeners();
    }

    /**
     * Sets up the listeners for the buttons
     */
    private void setupButtonListeners() {

        //Setup "Back" button listener
        app.btnBack.setOnClickListener(v -> navigateBack());

        //Setup "Finish" button listener
        app.btnFinish.setOnClickListener(v -> {

            //Get credentials
            mNewUser.setUsername(app.etUsername.getText().toString());
            mNewUser.setGithubToken(app.etToken.getText().toString());

            //Sign the user up into the database
            mNewUser.getHandler().signUpInBackground(e -> {
                if(e==null){
                    navigateToMain(mNewUser.getRole());
                }
                else{
                    e.printStackTrace();
                }
            });
        });
    }

    /**
     * Navigates back to the First signup fragment
     */
    private void navigateBack() {
        SignupSecondFragmentDirections.SecondToFirstAction secondToFirstAction = SignupSecondFragmentDirections.secondToFirstAction(mNewUser);
        NavHostFragment.findNavController(this)
                .navigate(secondToFirstAction);
    }

    /**
     * Resets the ViewBinder
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        app = null;
    }

    /**
     * Navigates to the corresponding home activity depending on the user's role
     */
    private void navigateToMain(String role) {
        Intent i = null;

        //Determine the home activity to navigate to
        if(role.equals("developer")){
            i = new Intent(mContext, DeveloperHomeActivity.class);
        } else if (role.equals("manager")) {
            i = new Intent(mContext, ManagerHomeActivity.class);
        }

        //Navigate to the selected home activity
        startActivity(i);
        requireActivity().finish();
    }

}