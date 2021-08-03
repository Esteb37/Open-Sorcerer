package com.example.opensorcerer.ui.main.profile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import com.example.opensorcerer.R;
import com.example.opensorcerer.databinding.FragmentProfileBinding;
import com.example.opensorcerer.models.Tools;
import com.example.opensorcerer.models.User;
import com.example.opensorcerer.ui.main.MainActivity;

import org.jetbrains.annotations.NotNull;

/**
 * Fragment for displaying a user's profile.
 */
public class ProfileFragment extends androidx.fragment.app.Fragment {

    /**
     * Binder object for ViewBinding
     */
    private FragmentProfileBinding mApp;

    /**
     * Listener for the profile menu's interaction to open and close the drawer
     */
    private final ProfileContentFragment.OnFragmentInteractionListener
            mDrawerListener = new ProfileContentFragment.OnFragmentInteractionListener() {
        @Override
        public void openDrawer() {
            mApp.drawerLy.openDrawer(GravityCompat.END);
        }
    };

    /**
     * Fragment's context
     */
    private Context mContext;

    /**
     * User to view profile
     */
    private User mProfileUser;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public ProfileFragment(User profileUser) {
        mProfileUser = profileUser;
    }

    /**
     * Inflates the layout and sets up view binding
     */
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mApp = FragmentProfileBinding.inflate(inflater, container, false);
        return mApp.getRoot();
    }

    /**
     * Sets up the fragment's methods
     */
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getState();

        setupProfileContent();

        setupDrawer();
    }

    /**
     * Gets the current state for the member variables.
     */
    private void getState() {
        mContext = getContext();

        try {
            requireActivity().findViewById(R.id.bottomNav).setVisibility(View.VISIBLE);
            assert mContext != null;
            ((MainActivity) mContext).hideDetailsFragment();
        } catch (NullPointerException e){
            requireActivity().findViewById(R.id.bottomNavDetails).setVisibility(View.VISIBLE);
        }
    }

    /**
     * Sets up the profile's details content fragment
     */
    private void setupProfileContent() {
        Fragment profileContentFragment = new ProfileContentFragment(mDrawerListener, mProfileUser);
        Tools.loadFragment(mContext, profileContentFragment, R.id.content_frame);
    }

    /**
     * Sets up the profile view's menu drawer
     */
    private void setupDrawer() {
        Tools.loadFragment(mContext, new ProfileDrawerFragment(), R.id.right_drawer);
    }
}