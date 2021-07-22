package com.example.opensorcerer.ui.main.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.opensorcerer.databinding.FragmentRightDrawerBinding;
import com.example.opensorcerer.ui.login.LoginActivity;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

/**
 * Fragment for the profile view's menu fragment
 */
public class ProfileDrawerFragment extends androidx.fragment.app.Fragment {

    /**
     * Binder for view binding
     */
    private FragmentRightDrawerBinding mApp;

    /**
     * Inflates the view and sets up view binding
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mApp = FragmentRightDrawerBinding.inflate(inflater, container, false);
        return mApp.getRoot();
    }

    /**
     * Sets up the logout button's listener
     */
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mApp.logoutLayout.setOnClickListener(v ->

                //Logout and return to login
                ParseUser.logOutInBackground(e -> {
                    Intent i = new Intent(getContext(), LoginActivity.class);
                    startActivity(i);
                    requireActivity().finish();
                }));
    }
}