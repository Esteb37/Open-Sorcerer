package com.example.opensorcerer.ui.main.profile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.opensorcerer.R;
import com.example.opensorcerer.adapters.ProjectsPagerAdapter;
import com.example.opensorcerer.application.OSApplication;
import com.example.opensorcerer.databinding.FragmentProfileContentBinding;
import com.example.opensorcerer.models.Tools;
import com.example.opensorcerer.models.User;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;
import org.kohsuke.github.GitHub;

/**
 * Fragment for displaying a user's profile.
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class ProfileContentFragment extends androidx.fragment.app.Fragment  {


    /**Tag for logging*/
    private static final String TAG = "ProfileContentFragment";

    /**Binder object for ViewBinding*/
    private FragmentProfileContentBinding mApp;

    /**Fragment's context*/
    private Context mContext;

    /**Current logged in user*/
    private User mUser;

    /**GitHub API handler*/
    private GitHub mGitHub;

    /**Fragment pager adapter*/
    ProjectsPagerAdapter mPagerAdapter;

    /**Interface for listening to the drawer*/
    public interface OnFragmentInteractionListener {
        void openDrawer();
        void closeDrawer();
    }

    /**Listener for the drawer*/
    private final OnFragmentInteractionListener mListener;

    /**
     * Sets up the listener for the drawer
     */
    public ProfileContentFragment(OnFragmentInteractionListener listener) {
        mListener = listener;
    }

    /**
     * Inflates the fragment's layout and sets up view binding
     */
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mApp = FragmentProfileContentBinding.inflate(inflater,container,false);
        return mApp.getRoot();
    }

    /**
     * Sets up the fragment's methods
     */
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getState();

        loadProfileDetails();

        setupPagerView();

        setDrawerButtonListener();
    }

    /**
     * Gets the current state for the member variables.
     */
    private void getState() {
        mContext = getContext();

        mUser = User.getCurrentUser();

        mGitHub = ((OSApplication) requireActivity().getApplication()).getGitHub();
    }

    /**Loads the user's information into the profile*/
    private void loadProfileDetails() {

        //Load text information
        mApp.textViewName.setText(mUser.getName());
        mApp.textViewUsername.setText(String.format("@%s", mUser.getUsername()));
        mApp.textViewBio.setText(mUser.getBio());

        //Load the list of languages to an expandable view
        mApp.textViewLanguages.setText(Tools.listToString(mUser.getLanguages()));
        mApp.textViewLanguages.post(() -> mApp.textViewLanguages.setMoreMessage(mApp.textViewMoreLanguages));

        //Load the list of tags to an expandable view
        mApp.textViewInterests.setText(Tools.listToString(mUser.getInterests()));
        mApp.textViewInterests.post(() -> mApp.textViewInterests.setMoreMessage(mApp.textViewMoreInterests));

        //Load the user's profile picture
        Glide.with(mContext)
                .load(mUser.getProfilePicture().getUrl())
                .into(mApp.imageViewProfilePicture);
    }

    /**
     * Sets up the pager view for the profile's projects
     */
    private void setupPagerView() {

        //Set the adapter
        mPagerAdapter = new ProjectsPagerAdapter(this);
        mApp.viewPager.setAdapter(mPagerAdapter);

        //Set the tab icons
        new TabLayoutMediator(mApp.tabLayout, mApp.viewPager,
                (tab, position) -> tab.setIcon(position == 0
                        ? R.drawable.ic_dashboard_black_24dp
                        : R.drawable.ufi_heart_active)
        ).attach();
    }

    /**
     * Sets the listener for the menu button
     */
    private void setDrawerButtonListener() {
        mApp.buttonDrawer.setOnClickListener(v -> mListener.openDrawer());
    }

}