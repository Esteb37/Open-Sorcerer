package com.example.opensorcerer.ui.main.details.manager;

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
import com.example.opensorcerer.databinding.FragmentManagerDetailsBinding;
import com.example.opensorcerer.models.Project;
import com.example.opensorcerer.models.Tools;
import com.example.opensorcerer.ui.main.details.external.InformationFragment;

import org.jetbrains.annotations.NotNull;

public class ManagerDetailsFragment extends Fragment {

    /**
     * Project being displayed
     */
    private Project mProject;

    /**
     * Binder object for ViewBinding
     */
    private FragmentManagerDetailsBinding mApp;

    /**
     * Fragment's context
     */
    private Context mContext;

    private Fragment mFragment;

    public ManagerDetailsFragment(Project project) {
        mProject = project;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mApp = FragmentManagerDetailsBinding.inflate(inflater, container, false);
        return mApp.getRoot();
    }

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
        final int actionDetails = R.id.actionPreview;
        final int actionHomepage = R.id.actionEdit;
        final int actionMessage = R.id.actionLikes;
        final int actionShare = R.id.actionShare;

        mApp.bottomNavDetails.setOnItemSelectedListener(item -> {
            mFragment = null;

            // Navigate to a different fragment depending on the item selected
            switch (item.getItemId()) {
                case actionDetails:
                    mFragment = new InformationFragment(mProject);
                    break;

                case actionHomepage:
                    mFragment = new EditProjectFragment(mProject);
                    break;

                case actionMessage:
                    //fragment = new InterestedUsersFragment(mProject);
                    break;

                case actionShare:
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + item.getItemId());
            }

            // Open the selected fragment
            if (item.getItemId() != actionShare) {
                Tools.loadFragment(mContext, mFragment, mApp.flContainerDetailsInternal.getId());
            }
            return true;
        });

        mApp.bottomNavDetails.setSelectedItemId(R.id.actionDetails);
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