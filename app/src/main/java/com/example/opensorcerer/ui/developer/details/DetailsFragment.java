package com.example.opensorcerer.ui.developer.details;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.example.opensorcerer.R;
import com.example.opensorcerer.application.OSApplication;
import com.example.opensorcerer.databinding.FragmentDetailsBinding;
import com.example.opensorcerer.models.Project;

import com.example.opensorcerer.models.users.roles.Developer;

import org.jetbrains.annotations.NotNull;
import org.kohsuke.github.GitHub;
import org.parceler.Parcels;

/**
 * Fragment for displaying a project's details
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class DetailsFragment extends Fragment{

    /**Tag for logging*/
    private static final String TAG = "DetailsFragment";

    /**Binder object for ViewBinding*/
    private FragmentDetailsBinding app;

    /**Fragment's context*/
    private Context mContext;

    /**Current logged in user*/
    private Developer mUser;

    /**GitHub API handler*/
    private GitHub mGitHub;

    /**Project being displayed*/
    private Project mProject;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Inflates the fragment and sets up ViewBinding
     */
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        app = FragmentDetailsBinding.inflate(inflater,container,false);
        return app.getRoot();
    }

    /**
     * Sets up the fragment's methods
     */
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getState();

        setupBottomNavigation();
    }

    /**
     * Gets the current state for the member variables.
     */
    private void getState() {

        mContext = getContext();

        mGitHub = ((OSApplication) requireActivity().getApplication()).getGitHub();

        assert getArguments() != null;
        mProject = Parcels.unwrap(getArguments().getParcelable("project"));
    }


    /**
     * Sets up the bottom navigation bar
     */
    private void setupBottomNavigation() {

        final FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        //Ensure that the id's of the navigation items are final for the switch
        final int actionDetails = R.id.actionDetails;
        final int actionGithub = R.id.actionGithub;


        app.bottomNavDetails.setOnItemSelectedListener(item -> {
            Fragment fragment;

            Bundle bundle = new Bundle();
            bundle.putParcelable("project",Parcels.wrap(mProject));

            //Navigate to a different fragment depending on the item selected
            switch(item.getItemId()){
                case actionDetails:
                    fragment = new InformationFragment();
                    break;

                case actionGithub:
                    fragment = new GitHubFragment();
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + item.getItemId());
            }

            fragment.setArguments(bundle);

            //Open the selected fragment
            fragmentManager.beginTransaction().replace(app.flContainerDetailsInternal.getId(),fragment).commit();
            return true;
        });

        app.bottomNavDetails.setSelectedItemId(R.id.actionDetails);
    }
}