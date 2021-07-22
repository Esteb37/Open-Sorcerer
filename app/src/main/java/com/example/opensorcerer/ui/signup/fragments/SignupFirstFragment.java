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

import com.example.opensorcerer.databinding.FragmentSignupFirstBinding;
import com.example.opensorcerer.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Fragment for choosing email and password when signing up.
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class SignupFirstFragment extends Fragment {

    /**Tag for logging*/
    private static final String TAG = "SignupFirstFragment";

    /**Binder for View Binding*/
    private FragmentSignupFirstBinding app;

    /**Fragment's context*/
    private Context mContext;

    /**Newly created user for signup*/
    private User mNewUser;

    /**
     * Inflates the fragment's layout
     */
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        app = FragmentSignupFirstBinding .inflate(inflater, container, false);
        return app.getRoot();

    }

    /**
     * Sets up the fragment's methods
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getContext();

        //Get the user created in the Role fragment
        mNewUser = SignupFirstFragmentArgs.fromBundle(getArguments()).getUser();

        setupButtonListeners();
    }

    /**
     * Sets up the click listeners for the buttons
     */
    private void setupButtonListeners() {

        //Setup "Next" button listener
        app.buttonNext.setOnClickListener(v -> {

            //Verify that the email is valid and the passwords match
            if(passwordsMatch()){

                //Set the credentials into the user
                mNewUser.setUsername(Objects.requireNonNull(app.editTextUsername.getText()).toString());
                mNewUser.setPassword(Objects.requireNonNull(app.editTextPassword.getText()).toString());

                //Go to the next screen
                navigateForward();
            }
        });

        //Setup "Back" button listener
        app.buttonBack.setOnClickListener(v -> navigateBackward());
    }

    /**
     * Goes back to the "role" fragment
     */
    private void navigateBackward() {
        NavDirections firstToRoleAction = SignupFirstFragmentDirections.firstToRoleAction();
        NavHostFragment.findNavController(this)
                .navigate(firstToRoleAction);
    }

    /**
     * Goes to the GitHub user fragment
     */
    private void navigateForward() {
        SignupFirstFragmentDirections.FirstToSecondAction firstToSecondAction = SignupFirstFragmentDirections.firstToSecondAction(mNewUser);
        NavHostFragment.findNavController(this)
                .navigate(firstToSecondAction);
    }

    /**
     * @return If the passwords match
     */
    private boolean passwordsMatch() {
        String password = Objects.requireNonNull(app.editTextPassword.getText()).toString();
        String confirm = Objects.requireNonNull(app.editTextConfirm.getText()).toString();
        if(! (password.equals(confirm))){
            Toast.makeText(getContext(),"Passwords do not match",Toast.LENGTH_SHORT).show();
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
        app = null;
    }



}