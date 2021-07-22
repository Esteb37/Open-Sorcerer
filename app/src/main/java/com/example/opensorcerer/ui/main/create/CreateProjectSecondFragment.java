package com.example.opensorcerer.ui.main.create;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.opensorcerer.application.OSApplication;
import com.example.opensorcerer.databinding.FragmentCreateSecondBinding;
import com.example.opensorcerer.models.Project;
import com.example.opensorcerer.models.User;

import org.jetbrains.annotations.NotNull;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.parceler.Parcels;

/**
 * Second fragment for creating a new project and adding it to the database
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class CreateProjectSecondFragment extends Fragment {

    /**Tag for logging*/
    private static final String TAG = "CreateProjectSecondFragment";

    /**Binder object for ViewBinding*/
    private FragmentCreateSecondBinding app;

    /**Fragment's context*/
    private Context mContext;

    /**Current logged in user*/
    private User mUser;

    /**GitHub API handler*/
    private GitHub mGitHub;

    private GHRepository mRepo;

    private Project mNewProject;

    public CreateProjectSecondFragment() {
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
        app = FragmentCreateSecondBinding.inflate(inflater,container,false);
        return app.getRoot();
    }

    /**
     * Sets up the fragment's methods
     */
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getState();

        loadRepoDetails();
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
        if(website.equals("")){
            website = repo;
        }
        String finalWebsite = website;



        //Go back to the main thread
        requireActivity().runOnUiThread(() -> {

            //Set the inputs to the repo's information
            app.editTextTitle.setText(name);
            app.editTextWebsite.setText(finalWebsite);
            app.editTextDescription.setText(description);
            app.editTextRepository.setText(repo);
        });
    }



    /*private void setupCreateProjectButtonListener() {

        app.btnCreate.setOnClickListener(v -> {

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
    }*/



    /*private void setupImageEditorListener() {
       (app.etImage).setOnEditorActionListener(
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
    }*/

      /*//Get the tags and languages in comma separated list form
        try {
            String tags = mRepo.listTopics().toString().replace("[","").replace("]","");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String languages = mRepo.listLanguages().keySet().toString().replace("[","").replace("]","");
        } catch (IOException e) {
            e.printStackTrace();
        }*/
}