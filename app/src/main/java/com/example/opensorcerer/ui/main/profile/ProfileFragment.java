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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.opensorcerer.R;
import com.example.opensorcerer.adapters.FavoritesPagerAdapter;
import com.example.opensorcerer.application.OSApplication;
import com.example.opensorcerer.databinding.FragmentProfileBinding;
import com.example.opensorcerer.models.User;

import org.jetbrains.annotations.NotNull;
import org.kohsuke.github.GitHub;

/**
 * Fragment for displaying a user's profile.
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class ProfileFragment extends androidx.fragment.app.Fragment {


    /**Tag for logging*/
    private static final String TAG = "ProfileFragment";

    /**Binder object for ViewBinding*/
    private FragmentProfileBinding mApp;

    /**Fragment's context*/
    private Context mContext;

    /**Current logged in user*/
    private User mUser;

    /**GitHub API handler*/
    private GitHub mGitHub;

    /**Fragment pager adapter*/
    FavoritesPagerAdapter mPagerAdapter;

    /**Fragment for the profile details*/
    private Fragment profileContentFragment = null;

    /**Fragment for the menu drawer*/
    private Fragment profileDrawerFragment = null;


    /**
     * Listener for the profile menu's interaction to open and close the drawer
     */
    private final ProfileContentFragment.OnFragmentInteractionListener
            mDrawerListener = new ProfileContentFragment.OnFragmentInteractionListener() {
        @Override
        public void openDrawer() {
            mApp.drawerLy.openDrawer(GravityCompat.END);
        }

        @Override
        public void closeDrawer() {
            mApp.drawerLy.closeDrawers();
        }
    };

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Inflates the layout and sets up view binding
     */
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mApp = FragmentProfileBinding.inflate(inflater,container,false);
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

        mUser = User.getCurrentUser();

        mGitHub = ((OSApplication) requireActivity().getApplication()).getGitHub();
    }

    /**
     * Sets up the profile's details content fragment
     */
    private void setupProfileContent(){
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        profileContentFragment = new ProfileContentFragment(mDrawerListener);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_frame, profileContentFragment);
        transaction.commit();
    }

    /**
     * Sets up the profile view's menu drawer
     */
    private void setupDrawer() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        profileDrawerFragment = new ProfileDrawerFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.right_drawer, profileDrawerFragment);
        transaction.commit();
    }
}