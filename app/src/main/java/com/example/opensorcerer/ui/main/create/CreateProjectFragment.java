package com.example.opensorcerer.ui.main.create;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.opensorcerer.application.OSApplication;
import com.example.opensorcerer.databinding.ActivityCreateBinding;
import com.example.opensorcerer.models.User;
import com.parse.ParseFile;

import org.jetbrains.annotations.NotNull;
import org.kohsuke.github.GitHub;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * Fragment for creating a new project and adding it to the database
 */
@SuppressWarnings("unused")
public class CreateProjectFragment extends Fragment {

    /**Tag for logging*/
    private static final String TAG = "CreateProjectFragment";

    /**Binder object for ViewBinding*/
    private ActivityCreateBinding app;

    /**Fragment's context*/
    private Context mContext;

    /**Current logged in user*/
    private User mUser;

    /**GitHub API handler*/
    private GitHub mGitHub;

    public CreateProjectFragment() {
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
        app = ActivityCreateBinding.inflate(inflater,container,false);
        return app.getRoot();
    }

    /**
     * Sets up the fragment's methods
     */
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getState();

        setupQuickSetupButtonListener();

        setupCreateProjectButtonListener();

        setupImageEditorListener();
    }

    /**
     * Sets up the listener for the "Quick setup" button and populates the form with
     * the project's github information
     */
    private void setupQuickSetupButtonListener() {

        //Create a background thread on the button's click
        /*app.btnQuick.setOnClickListener(v -> new Thread(() -> {

            //Get the repo in user/repo format
            String repoLink = app.etRepo.getText().toString().split("github.com/")[1];
            try {
                Looper.prepare();

                //Get the repository
                GHRepository ghRepo = mGitHub.getRepository(repoLink);

                //Get the repo's information
                String name = ghRepo.getName();
                String readme = ghRepo.getReadme().getHtmlUrl();
                String description = ghRepo.getDescription();

                //Get the tags and languages in comma separated list form
                String tags = ghRepo.listTopics().toString().replace("[","").replace("]","");
                String languages = ghRepo.listLanguages().keySet().toString().replace("[","").replace("]","");

                //Go back to the main thread
                requireActivity().runOnUiThread(() -> {

                    //Set the inputs to the repo's information
                    app.etTitle.setText(name);
                    app.etReadme.setText(readme);
                    app.etShortDescription.setText(description);
                    app.etLanguages.setText(languages);
                    app.etTags.setText(tags);
                });
            } catch (IOException e) {
                Toast.makeText(mContext, "Invalid Repo Link", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }).start());

         */
    }


    /**
     * Sets up a listener for the "Create project" button
     */
    private void setupCreateProjectButtonListener() {

        /*app.btnCreate.setOnClickListener(v -> {

            //Create a new project
            Project project = new Project();

            //Set the project up with the imputed information
            project.setTitle(app.etTitle.getText().toString());
            project.setDescription(app.etShortDescription.getText().toString());
            project.setReadme(app.etReadme.getText().toString());
            project.setManager(mUser);
            project.setRepository(app.etRepo.getText().toString());

            //Set the tags and languages as lists
            List<String> languages = Arrays.asList(app.etLanguages.getText().toString().split(","));
            project.setLanguages(languages);
            List<String> tags = Arrays.asList(app.etTags.getText().toString().split(","));
            project.setTags(tags);

            //Create a new background thread
            new Thread(() -> {

                //Download the selected image from the internet
                ParseFile logoImage = bitmapToParseFile(getBitmapFromURL(app.etImage.getText().toString()));

                //Set the downloaded image into the project's logo image
                project.setLogoImage(logoImage);

                //Save the project
                project.saveInBackground(e -> {
                    if(e==null){

                        //Add the project to the user's list of projects
                        mUser.addProject(project);

                        //Go back to the projects fragment
                        final FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.flContainer,new MyProjectsFragment()).commit();
                    } else {
                        Toast.makeText(mContext, "There was an error creating your project.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                });
            }).start();
        });

         */
    }


    /**
     * Sets up a listener for when the user has finished typing into the Image input
     */
    private void setupImageEditorListener() {
       /* (app.etImage).setOnEditorActionListener(
                (v, actionId, event) -> {

                    //If the user has finished typing, typed enter or down
                    if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                            actionId == EditorInfo.IME_ACTION_DONE ||
                            event != null &&
                                    event.getAction() == KeyEvent.ACTION_DOWN &&
                                    event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        if (event == null || !event.isShiftPressed()) {

                            //Load the image into the preview
                            Glide.with(mContext)
                                    .load(app.etImage.getText().toString())
                                    .fitCenter()
                                    .transform(new RoundedCorners(1000))
                                    .into(app.ivLogo);

                            return true; // consume.
                        }
                    }
                    return false; // pass on to other listeners.
                }
        );
        
        */
    }



    /**
     * Gets the current state for the member variables.
     */
    private void getState() {
        mContext = getContext();

        mUser = User.getCurrentUser();

        mGitHub = ((OSApplication) requireActivity().getApplication()).getGitHub();
    }


    public Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ParseFile bitmapToParseFile(Bitmap imageBitmap){
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        return new ParseFile("logo.png",imageByte);
    }

}