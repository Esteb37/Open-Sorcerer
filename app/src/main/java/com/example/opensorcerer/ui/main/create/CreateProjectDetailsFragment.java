package com.example.opensorcerer.ui.main.create;

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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.example.opensorcerer.R;
import com.example.opensorcerer.application.OSApplication;
import com.example.opensorcerer.databinding.FragmentCreateDetailsBinding;
import com.example.opensorcerer.models.Project;
import com.example.opensorcerer.models.Tools;
import com.example.opensorcerer.models.User;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import org.jetbrains.annotations.NotNull;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.parceler.Parcels;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Fragment for adding primary details to the created project
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class CreateProjectDetailsFragment extends Fragment {

    /**
     * Tag for logging
     */
    private static final String TAG = "CreateProjectDetailsFragment";

    /**
     * Binder object for ViewBinding
     */
    private FragmentCreateDetailsBinding mApp;

    /**
     * Fragment's context
     */
    private Context mContext;

    /**
     * Current logged in user
     */
    private User mUser;

    /**
     * GitHub API handler
     */
    private GitHub mGitHub;

    /**
     * The new project's repo object
     */
    private GHRepository mRepo;

    /**
     * The newly created project
     */
    private Project mNewProject;

    /**
     * New project's logo image
     */
    private Bitmap mProjectLogo;

    /**
     * Activity launcher for choosing a picture from the user's files
     */
    ActivityResultLauncher<Intent> chooseProjectLogoActivityLauncher = registerForActivityResult(
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
                            mProjectLogo = BitmapFactory.decodeStream(inputStream);

                            //Load the profile picture into the placeholder
                            Glide.with(mContext)
                                    .load(mProjectLogo)
                                    .into(mApp.imageViewProjectLogo);

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });


    public CreateProjectDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Inflates the fragment's layout
     */
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mApp = FragmentCreateDetailsBinding.inflate(inflater, container, false);
        return mApp.getRoot();
    }

    /**
     * Sets up the fragment's methods
     */
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getState();

        loadRepoDetails();

        setupButtonListeners();

        setupImageListener();
    }

    /**
     * Gets the current state for the member variables.
     */
    private void getState() {
        mContext = getContext();

        mUser = User.getCurrentUser();

        mGitHub = ((OSApplication) requireActivity().getApplication()).getGitHub();

        mNewProject = Parcels.unwrap(requireArguments().getParcelable("project"));
    }

    /**
     * Populates the inputs with the imported repo's details
     */
    private void loadRepoDetails() {

        //Get the repo object
        mRepo = mNewProject.getRepoObject();

        //Get the repo's information
        String name = mRepo.getName();
        String description = mRepo.getDescription();
        String repo = mRepo.getHtmlUrl().toString();

        //Get the website or set the website to the project's github page
        String website = mRepo.getHomepage();
        if (website == null || website.equals("")) {
            website = repo;
        }
        String finalWebsite = website;

        //Go back to the main thread
        requireActivity().runOnUiThread(() -> {

            //Set the inputs to the repo's information
            mApp.editTextTitle.setText(name);
            mApp.editTextWebsite.setText(finalWebsite);
            mApp.editTextDescription.setText(description);
            mApp.editTextRepository.setText(repo);
        });
    }

    /**
     * Sets up the listeners for the navigation buttons
     */
    private void setupButtonListeners() {
        mApp.buttonNext.setOnClickListener(v -> {

            //Set the project's details from the inputs
            mNewProject.setTitle(mApp.editTextTitle.getText().toString());
            mNewProject.setDescription(mApp.editTextDescription.getText().toString());
            mNewProject.setRepository(mApp.editTextRepository.getText().toString());
            mNewProject.setWebsite(mApp.editTextWebsite.getText().toString());

            //Set the project's manager to this user
            mNewProject.setManager(mUser);

            //Set the project's logo image
            if (mProjectLogo != null) {

                //Transform the selected profile picture bitmap into a ParseFile
                ParseFile logoImage = Tools.bitmapToParseFile(mProjectLogo);

                //Save the logo image into the database
                logoImage.saveInBackground((SaveCallback) fe -> {

                    //If the image was saved correctly
                    if (fe == null) {

                        //Set the image as the user's profile picture
                        mNewProject.setLogoImage(logoImage);
                        navigateForward();
                    } else {
                        fe.printStackTrace();
                    }
                });
            } else {
                navigateForward();
            }
        });

        mApp.buttonBack.setOnClickListener(v -> navigateBackward());
    }

    /**
     * Sets the listener for uploading the logo image
     */
    private void setupImageListener() {
        mApp.constraintLayoutPicture.setOnClickListener(v -> {

            Intent chooserIntent = Tools.createChooserIntent();

            //Load the pick image process
            chooseProjectLogoActivityLauncher.launch(chooserIntent);
        });
    }

    /**
     * Navigates to the create project tags Fragment
     */
    private void navigateForward() {
        final FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        Fragment fragment = new CreateProjectTagsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("project", Parcels.wrap(mNewProject));
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
    }

    /**
     * Navigates to the Create Project import Fragment
     */
    private void navigateBackward() {
        final FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        Fragment fragment = new CreateProjectImportFragment();
        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
    }
}