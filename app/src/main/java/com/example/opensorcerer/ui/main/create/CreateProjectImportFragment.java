package com.example.opensorcerer.ui.main.create;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.opensorcerer.R;
import com.example.opensorcerer.application.OSApplication;
import com.example.opensorcerer.databinding.FragmentCreateImportBinding;
import com.example.opensorcerer.models.Project;
import com.example.opensorcerer.models.Tools;
import com.example.opensorcerer.ui.main.MainActivity;

import org.jetbrains.annotations.NotNull;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.IOException;
import java.util.Objects;

/**
 * Fragment for importing a project from github
 */
public class CreateProjectImportFragment extends Fragment {

    /**
     * Binder object for ViewBinding
     */
    private FragmentCreateImportBinding mApp;

    /**
     * Fragment's context
     */
    private Context mContext;

    /**
     * GitHub API handler
     */
    private GitHub mGitHub;

    /**
     * The project being created
     */
    private Project mNewProject;

    public CreateProjectImportFragment() {
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
        mApp = FragmentCreateImportBinding.inflate(inflater, container, false);
        return mApp.getRoot();
    }

    /**
     * Sets up the fragment's methods
     */
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getState();

        setupImportButtonListener();
    }

    /**
     * Sets up the listener for the "Quick setup" button and populates the form with
     * the project's github information
     */
    private void setupImportButtonListener() {

        //Create a background thread on the button's click
        mApp.buttonImport.setOnClickListener(v -> new Thread(() -> {

            Looper.prepare();

            try {
                //Get the repo in user/repo format
                String repoLink = Tools.getRepositoryName(Objects.requireNonNull(mApp.editTextRepo.getText()).toString());

                try {
                    //Get the repository
                    GHRepository ghRepo = mGitHub.getRepository(repoLink);

                    //Create a new project and add the repo
                    mNewProject = new Project();
                    mNewProject.setRepoObject(ghRepo);

                    navigateForward();
                } catch (IOException e) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(mContext, "Invalid Repo Link", Toast.LENGTH_SHORT).show());
                    e.printStackTrace();
                }

            } catch (ArrayIndexOutOfBoundsException e) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(mContext, "Link must have the format 'github.com/user/project'", Toast.LENGTH_SHORT).show()
                );
            }
        }).start());
    }

    /**
     * Navigates to the Create Project details Fragment
     */
    private void navigateForward() {
        Fragment fragment = new CreateProjectDetailsFragment(mNewProject);
        Tools.navigateToFragment(mContext, fragment, R.id.flContainer, "right_to_left");
    }

    /**
     * Gets the current state for the member variables.
     */
    private void getState() {
        mContext = getContext();

        mGitHub = ((OSApplication) requireActivity().getApplication()).getGitHub();

        ((MainActivity) mContext).hideDetailsFragment();
    }

}