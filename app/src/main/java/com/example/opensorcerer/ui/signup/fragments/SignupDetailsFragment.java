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
import com.example.opensorcerer.models.users.User;
import com.example.opensorcerer.ui.developer.DeveloperHomeActivity;
import com.example.opensorcerer.ui.manager.ManagerHomeActivity;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;


/**
 * Fragment for adding details to a user's profile
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class SignupDetailsFragment extends Fragment {

    /**
     * Tag for logging
     */
    private static final String TAG = "SignupDetailsFragment";

    /**
     * Binder for View Binding
     */
    private FragmentSignupDetailsBinding app;

    /**
     * Fragment's context
     */
    private Context mContext;

    /**
     * Newly created user for signup
     */
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
            mNewUser.setBio(Objects.requireNonNull(app.editTextBio.getText()).toString());
            mNewUser.setExperience(Objects.requireNonNull(app.editTextExperience.getText()).toString());
            mNewUser.setName(Objects.requireNonNull(app.editTextName.getText()).toString());

            if(mProfilePicture!=null){
                //Transform the selected profile picture bitmap into a ParseFile
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                mProfilePicture.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] image = stream.toByteArray();
                ParseFile profilePicture  = new ParseFile("profile_picture.jpeg", image);

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
        app.buttonSkip.setOnClickListener(v -> navigateToMain(mNewUser.getRole()));
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

    /**
     * Goes to the account tags fragment
     */
    private void navigateForward() {
        SignupDetailsFragmentDirections.DetailsToTagsAction detailsToTagsAction = SignupDetailsFragmentDirections.detailsToTagsAction(mNewUser);
        NavHostFragment.findNavController(this)
                .navigate(detailsToTagsAction);
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

            //Prompt the user to pick a file from their device
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("image/*");

            //Prompt the user to pick the folder with the desired image
            Intent pickIntent = new Intent(Intent.ACTION_PICK);
            pickIntent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");

            //Prompt the user to select the image
            Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

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
                            //Get the selected profile picture from the datastream
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
     * Resets the ViewBinder
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        app = null;
    }
}