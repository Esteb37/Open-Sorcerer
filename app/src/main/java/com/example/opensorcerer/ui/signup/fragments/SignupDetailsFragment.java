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

import com.bumptech.glide.Glide;
import com.example.opensorcerer.databinding.FragmentSignupDetailsBinding;
import com.example.opensorcerer.models.Tools;
import com.example.opensorcerer.models.User;
import com.example.opensorcerer.ui.main.HomeActivity;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;


/**
 * Fragment for adding details to a user's profile
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class SignupDetailsFragment extends Fragment {

    /**Tag for logging*/
    private static final String TAG = "SignupDetailsFragment";

    /**Binder for View Binding*/
    private FragmentSignupDetailsBinding app;

    /**Fragment's context*/
    private Context mContext;

    /**Newly created user for signup*/
    private User mNewUser;

    /**User's selected profile picture*/
    private Bitmap mProfilePicture;

    /**
     * Inflates the fragment's layout
     */
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        app = FragmentSignupDetailsBinding.inflate(inflater, container, false);
        return app.getRoot();

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

        app.buttonNext.setOnClickListener(v -> {

            //Set the imputed text into the user's details
            mNewUser.setBio(Objects.requireNonNull(app.editTextBio.getText()).toString());
            mNewUser.setExperience(Objects.requireNonNull(app.editTextExperience.getText()).toString());
            mNewUser.setName(Objects.requireNonNull(app.editTextName.getText()).toString());

            //Set the user's profile picture
            if(mProfilePicture!=null){

                //Transform the selected profile picture bitmap into a ParseFile
                ParseFile profilePicture  = Tools.bitmapToParseFile(mProfilePicture);

                //Save the profile picture into the database
                profilePicture.saveInBackground((SaveCallback) fe -> {

                    //If the image was saved correctly
                    if(fe==null){

                        //Set the image as the user's profile picture
                        mNewUser.setProfilePicture(profilePicture);
                        navigateForward();
                    } else {
                        fe.printStackTrace();
                    }
                });
            }
        });

        app.buttonSkip.setOnClickListener(v -> navigateToMain());
    }

    /**
     * Hides the input text layout hints on focus
     */
    private void setupEditText(){

        //Set default tags
        app.editTextBio.setHint("Tell everyone about yourself!");
        app.editTextExperience.setHint("Tell us about your projects!");
        app.editTextName.setHint("What's your name?");

        app.editTextBio.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                app.editTextBio.setHint("");
            else
                app.editTextBio.setHint("Tell everyone about yourself!");
        });

        app.editTextExperience.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                app.editTextExperience.setHint("");
            else
                app.editTextExperience.setHint("Tell us about your projects!");
        });

        app.editTextName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                app.editTextName.setHint("");
            else
                app.editTextName.setHint("What's your name?");
        });
    }

    /**
     * Sets up a listener for clicking on the profile picture
     */
    private void setupProfilePictureListener(){
        app.constraintLayoutPicture.setOnClickListener(v ->{

            Intent chooserIntent = Tools.createChooserIntent();

            //Load the pick image process
            chooseProfilePictureActivityLauncher.launch(chooserIntent);
        });
    }

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
                            Glide.with(mContext)
                                    .load(mProfilePicture)
                                    .into(app.imageViewProfilePicture);

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    /**
     * Navigates to the home activity
     */
    private void navigateToMain() {
        Intent i =  new Intent(mContext, HomeActivity.class);

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
        app = null;
    }
}