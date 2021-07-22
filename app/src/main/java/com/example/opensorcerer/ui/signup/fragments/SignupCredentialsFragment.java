package com.example.opensorcerer.ui.signup.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.example.opensorcerer.databinding.FragmentSignupCredentialsBinding;
import com.example.opensorcerer.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Fragment for choosing email and password when signing up.
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class SignupCredentialsFragment extends Fragment {

    /**
     * Tag for logging
     */
    private static final String TAG = "SignupCredentialsFragment";

    /**
     * Binder for View Binding
     */
    private FragmentSignupCredentialsBinding mApp;

    /**
     * Fragment's context
     */
    private Context mContext;

    /**
     * Newly created user for signup
     */
    private User mNewUser;

    /**
     * Inflates the fragment's layout
     */
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mApp = FragmentSignupCredentialsBinding.inflate(inflater, container, false);
        return mApp.getRoot();

    }

    /**
     * Sets up the fragment's methods
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getContext();

        //Get the user created in the Role fragment
        mNewUser = SignupCredentialsFragmentArgs.fromBundle(getArguments()).getUser();

        setupButtonListeners();
    }

    /**
     * Sets up the click listeners for the buttons
     */
    private void setupButtonListeners() {

        //Setup "Next" button listener
        mApp.buttonNext.setOnClickListener(v -> {

            //Verify that the email is valid and the passwords match
            if (passwordsMatch()) {

                //Set the credentials into the user
                mNewUser.setUsername(Objects.requireNonNull(mApp.editTextUsername.getText()).toString());
                mNewUser.setPassword(Objects.requireNonNull(mApp.editTextPassword.getText()).toString());

                //Go to the next screen
                navigateForward();
            }
        });

        //Setup "Back" button listener
        mApp.buttonBack.setOnClickListener(v -> navigateBackward());
    }

    /**
     * Goes back to the "role" fragment
     */
    private void navigateBackward() {
        NavDirections credentialsToRoleAction = SignupCredentialsFragmentDirections.credentialsToRoleAction();
        NavHostFragment.findNavController(this)
                .navigate(credentialsToRoleAction);
    }

    /**
     * Goes to the GitHub user fragment
     */
    private void navigateForward() {
        SignupCredentialsFragmentDirections.CredentialsToGithubAction credentialsToGithubAction = SignupCredentialsFragmentDirections.credentialsToGithubAction(mNewUser);
        NavHostFragment.findNavController(this)
                .navigate(credentialsToGithubAction);
    }

    /**
     * @return If the passwords match
     */
    private boolean passwordsMatch() {
        String password = Objects.requireNonNull(mApp.editTextPassword.getText()).toString();
        String confirm = Objects.requireNonNull(mApp.editTextConfirm.getText()).toString();
        if (!(password.equals(confirm))) {
            Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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