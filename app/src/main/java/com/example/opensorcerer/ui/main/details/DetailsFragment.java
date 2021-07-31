package com.example.opensorcerer.ui.main.details;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.opensorcerer.R;
import com.example.opensorcerer.databinding.FragmentDetailsBinding;
import com.example.opensorcerer.models.Project;
import com.example.opensorcerer.models.Tools;
import com.example.opensorcerer.ui.main.conversations.ConversationFragment;

import org.jetbrains.annotations.NotNull;

/**
 * Fragment for displaying a project's details
 */
public class DetailsFragment extends Fragment {

    /**
     * Project being displayed
     */
    private Project mProject;

    /**
     * Binder object for ViewBinding
     */
    private FragmentDetailsBinding mApp;

    /**
     * Fragment's context
     */
    private Context mContext;

    public DetailsFragment(Project project) {
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
        mApp = FragmentDetailsBinding.inflate(inflater, container, false);
        return mApp.getRoot();
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
    }

    /**
     * Sets up the bottom navigation bar
     */
    private void setupBottomNavigation() {


        //Ensure that the id's of the navigation items are final for the switch
        final int actionDetails = R.id.actionDetails;
        final int actionHomepage = R.id.actionHomepage;
        final int actionMessage = R.id.actionMessage;

        mApp.bottomNavDetails.setOnItemSelectedListener(item -> {
            Fragment fragment;

            // Navigate to a different fragment depending on the item selected
            switch (item.getItemId()) {
                case actionDetails:
                    fragment = new InformationFragment(mProject);
                    break;

                case actionHomepage:
                    fragment = new HomepageFragment(mProject);
                    break;

                case actionMessage:
                    fragment = new ConversationFragment(mProject);
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + item.getItemId());
            }

            // Open the selected fragment
            Tools.loadFragment(mContext, fragment, mApp.flContainerDetailsInternal.getId());
            return true;
        });

        mApp.bottomNavDetails.setSelectedItemId(R.id.actionDetails);
    }

    /**
     * Updates which project is currently being displayed
     */
    public void updateProject(Project project) {
        mProject = project;
        Tools.loadFragment(mContext, new DetailsFragment(mProject), mApp.flContainerDetailsInternal.getId());
    }

    /**
     * Determines if the current page in the details fragment is the Information page
     */
    public boolean isInformationFragmentVisible() {
        int index = requireActivity().getSupportFragmentManager().getBackStackEntryCount() - 1;
        FragmentManager.BackStackEntry backEntry = requireActivity().getSupportFragmentManager().getBackStackEntryAt(index);
        String tag = backEntry.getName();
        assert tag != null;
        return tag.equals("InformationFragment");
    }

}