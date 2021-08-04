package com.example.opensorcerer.ui.signup.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.opensorcerer.databinding.FragmentSignupDetailsBinding;
import com.example.opensorcerer.models.Tools;
import com.example.opensorcerer.models.User;
import com.example.opensorcerer.ui.main.MainActivity;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;


/**
 * Fragment for adding details to a user's profile
 */
public class SignupDetailsFragment extends Fragment {

    /**
     * Binder for View Binding
     */
    private FragmentSignupDetailsBinding mApp;

    /**
     * Fragment's context
     */
    private Context mContext;

    /**
     * Newly created user for signup
     */
    private User mNewUser;

    /**
     * User's selected profile picture
     */
    private Bitmap mProfilePicture;

    /**
     * Activity launcher for choosing a picture from the user's files
     */
    ActivityResultLauncher<Intent> chooseProfilePictureActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        try {
                            //Get the selected profile picture from the data stream
                            assert data != null;
                            InputStream inputStream = mContext.getContentResolver().openInputStream(data.getData());
                            mProfilePicture = BitmapFactory.decodeStream(inputStream);

                            //Load the profile picture into the placeholder
                            Tools.loadImageFromBitmap(mContext, mProfilePicture, mApp.imageViewProfilePicture);

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    /**
     * Inflates the fragment's layout
     */
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mApp = FragmentSignupDetailsBinding.inflate(inflater, container, false);
        return mApp.getRoot();

    }

    /**
     * Sets up the fragment's methods
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getContext();

        //Get the user created in the Role fragment
        mNewUser = SignupDetailsFragmentArgs.fromBundle(getArguments()).getUser();

        setupButtonListeners();

        setupEditText();

        setupProfilePictureListener();
    }

    /**
     * Sets up the click listeners for the buttons
     */
    private void setupButtonListeners() {

        mApp.buttonNext.setOnClickListener(v -> {

            //Set the imputed text into the user's details
            mNewUser.setBio(Objects.requireNonNull(mApp.editTextBio.getText()).toString());
            mNewUser.setExperience(Objects.requireNonNull(mApp.editTextExperience.getText()).toString());
            mNewUser.setName(Objects.requireNonNull(mApp.editTextName.getText()).toString());

            //Set the user's profile picture
            if (mProfilePicture != null) {

                //Transform the selected profile picture bitmap into a ParseFile
                ParseFile profilePicture = Tools.bitmapToParseFile(mProfilePicture);

                //Save the profile picture into the database
                profilePicture.saveInBackground((SaveCallback) fe -> {

                    //If the image was saved correctly
                    if (fe == null) {

                        //Set the image as the user's profile picture
                        mNewUser.setProfilePicture(profilePicture);
                        navigateForward();
                    } else {
                        fe.printStackTrace();
                    }
                });
            }
        });

        mApp.buttonSkip.setOnClickListener(v -> navigateToMain());
    }

    /**
     * Hides the input text layout hints on focus
     */
    private void setupEditText() {

        //Set default tags
        mApp.editTextBio.setHint("Tell everyone about yourself!");
        mApp.editTextExperience.setHint("Tell us about your projects!");
        mApp.editTextName.setHint("What's your name?");

        mApp.editTextBio.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                mApp.editTextBio.setHint("");
            else
                mApp.editTextBio.setHint("Tell everyone about yourself!");
        });

        mApp.editTextExperience.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                mApp.editTextExperience.setHint("");
            else
                mApp.editTextExperience.setHint("Tell us about your projects!");
        });

        mApp.editTextName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                mApp.editTextName.setHint("");
            else
                mApp.editTextName.setHint("What's your name?");
        });
    }

    /**
     * Sets up a listener for clicking on the profile picture
     */
    private void setupProfilePictureListener() {
        mApp.constraintLayoutPicture.setOnClickListener(v -> {

            Intent chooserIntent = Tools.createChooserIntent();

            //Load the pick image process
            chooseProfilePictureActivityLauncher.launch(chooserIntent);
        });
    }

    /**
     * Navigates to the home activity
     */
    private void navigateToMain() {
        Intent i = new Intent(mContext, MainActivity.class);

        //Navigate to the selected home activity
        startActivity(i);
        requireActivity().finish();
    }

    /**
     * Goes to the account tags fragment
     */
    private void navigateForward() {
        SignupDetailsFragmentDirections.DetailsToTagsAction detailsToTagsAction = SignupDetailsFragmentDirections.detailsToTagsAction(mNewUser);
        NavHostFragment.findNavController(this)
                .navigate(detailsToTagsAction);
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