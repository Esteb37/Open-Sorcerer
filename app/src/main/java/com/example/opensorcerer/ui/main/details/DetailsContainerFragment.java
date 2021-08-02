package com.example.opensorcerer.ui.main.details;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.opensorcerer.R;
import com.example.opensorcerer.databinding.FragmentDetailsContainerBinding;
import com.example.opensorcerer.models.Project;
import com.example.opensorcerer.models.Tools;
import com.example.opensorcerer.models.User;
import com.example.opensorcerer.ui.main.details.external.ExternalDetailsFragment;
import com.example.opensorcerer.ui.main.details.manager.ManagerDetailsFragment;

import org.jetbrains.annotations.NotNull;

/**
 * Fragment for displaying a project's details
 */
public class DetailsContainerFragment extends Fragment {

    private Project mProject;
    private Context mContext;
    private User mUser;

    private Fragment mDetailsFragment;


    public DetailsContainerFragment(Project project) {
        mProject = project;
    }

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
        FragmentDetailsContainerBinding app = FragmentDetailsContainerBinding.inflate(inflater, container, false);
        return app.getRoot();
    }

    /**
     * Sets up the fragment's methods
     */
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getState();

        loadDetailsFragment();
    }

    private void loadDetailsFragment() {
        if (mProject != null && mProject.isByUser(mUser)) {
            mDetailsFragment = new ManagerDetailsFragment(mProject);
        } else if (mProject != null) {
            mDetailsFragment = new ExternalDetailsFragment(mProject);
        } else {
            mDetailsFragment = new ExternalDetailsFragment(null);
        }

        Tools.loadFragment(mContext, mDetailsFragment, R.id.flContainerDetails);
    }

    public void cleanLayout(){
        requireActivity().getSupportFragmentManager().beginTransaction().remove(mDetailsFragment).commit();
        Log.d("Test","Cleaned layout");
    }

    /**
     * Gets the current state for the member variables.
     */
    private void getState() {
        mContext = getContext();

        mUser = User.getCurrentUser();
    }

    public void updateProject(Project project) {
        mProject = project;

        loadDetailsFragment();
    }

    public boolean isInformationFragmentVisible() {
        if (mProject != null && mProject.isByUser(mUser)) {
            return ((ManagerDetailsFragment) mDetailsFragment).isInformationFragmentVisible();
        } else if (mProject != null){
            return ((ExternalDetailsFragment) mDetailsFragment).isInformationFragmentVisible();
        } else {
            return false;
        }
    }
}