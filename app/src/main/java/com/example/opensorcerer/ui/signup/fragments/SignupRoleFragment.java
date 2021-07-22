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

import com.example.opensorcerer.databinding.FragmentSignupRoleBinding;
import com.example.opensorcerer.models.User;
import com.example.opensorcerer.ui.login.LoginActivity;

import org.jetbrains.annotations.NotNull;

/**
 * Fragment for choosing a user's role
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class SignupRoleFragment extends Fragment {

    /**Tag for logging*/
    private static final String TAG = "SignupRoleFragment";

    /**Binder for ViewBinding*/
    private FragmentSignupRoleBinding mApp;

    /**Fragment's context*/
    private Context mContext;

    /**Newly created user for signup*/
    User mNewUser;

    /**
     * Inflates the fragment
     */
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mApp = FragmentSignupRoleBinding.inflate(inflater, container, false);
        return mApp.getRoot();
    }

    /**
     * Sets up the fragment's methods
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getContext();

        setupButtonListeners();
    }

    /**
     * Sets up the role buttons listeners
     */
    private void setupButtonListeners() {
        mNewUser = new User();

        mApp.buttonDeveloper.setOnClickListener(v -> {
            mNewUser.setRole("developer");
            navigateToSignup();
        });

        mApp.buttonManager.setOnClickListener(v -> {
            mNewUser.setRole("manager");
            navigateToSignup();
        });

        mApp.buttonSignin.setOnClickListener(v -> {
            Intent i = new Intent(mContext, LoginActivity.class);
            startActivity(i);
        });
    }

    /**
     * Navigates forward to the credentials signup screen
     */
    private void navigateToSignup() {
        SignupRoleFragmentDirections.RoleToCredentialsAction roleToCredentialsAction = SignupRoleFragmentDirections.roleToCredentialsAction(mNewUser);
        NavHostFragment.findNavController(this)
                .navigate(roleToCredentialsAction);
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