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
import com.example.opensorcerer.databinding.FragmentProfileContentBinding;
import com.example.opensorcerer.models.Tools;
import com.example.opensorcerer.models.User;
import com.example.opensorcerer.ui.main.conversations.ConversationFragment;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Fragment for displaying a user's profile.
 */
public class ProfileContentFragment extends androidx.fragment.app.Fragment {

    /**
     * Listener for the drawer
     */
    private final OnFragmentInteractionListener mListener;

    /**
     * User to show in profile
     */
    private final User mProfileUser;

    /**
     * Binder object for ViewBinding
     */
    private FragmentProfileContentBinding mApp;

    /**
     * Fragment's context
     */
    private Context mContext;

    /**
     * Current logged in user
     */
    private User mUser;

    /**
     * Sets up the listener for the drawer and the user to show
     */
    public ProfileContentFragment(OnFragmentInteractionListener listener, User profileUser) {
        mListener = listener;
        mProfileUser = profileUser;
    }

    /**
     * Inflates the fragment's layout and sets up view binding
     */
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mApp = FragmentProfileContentBinding.inflate(inflater, container, false);
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

        setupMessageButtonListener();
    }

    /**
     * Gets the current state for the member variables.
     */
    private void getState() {
        mContext = getContext();

        mUser = User.getCurrentUser();
    }

    /**
     * Loads the user's information into the profile
     */
    private void loadProfileDetails() {

        //Load text information
        mApp.textViewName.setText(mProfileUser.getName());
        mApp.textViewUsername.setText(String.format("@%s", mProfileUser.getUsername()));
        mApp.textViewBio.setText(mProfileUser.getBio());


        //Load the list of languages to an expandable view
        List<String> languages = mProfileUser.getLanguages();
        if(languages == null || languages.size() == 0){
            mApp.groupLanguages.setVisibility(View.GONE);
        } else {
            mApp.textViewLanguages.setText(Tools.listToString(languages));
            mApp.textViewLanguages.post(() -> mApp.textViewLanguages.setMoreMessage(mApp.textViewMoreLanguages));
        }


        //Load the list of tags to an expandable view
        List<String> interests = mProfileUser.getInterests();
        if(interests == null || interests.size() == 0){
            mApp.groupInterests.setVisibility(View.GONE);
        } else {
            mApp.textViewInterests.setText(Tools.listToString(interests));
            mApp.textViewInterests.post(() -> mApp.textViewInterests.setMoreMessage(mApp.textViewMoreInterests));
        }

        //Load the user's profile picture
        Glide.with(mContext)
                .load(mProfileUser.getProfilePicture().getUrl())
                .into(mApp.imageViewProfilePicture);

        if (!mProfileUser.getObjectId().equals(mUser.getObjectId())) {
            mApp.buttonDrawer.setVisibility(View.GONE);
            mApp.buttonAction.setVisibility(View.GONE);
            mApp.buttonMessage.setVisibility(View.VISIBLE);
            mApp.buttonBack.setVisibility(View.VISIBLE);

            mApp.buttonBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
        }
    }

    /**
     * Sets up the pager view for the profile's projects
     */
    private void setupPagerView() {

        //Set the adapter
        ProjectsPagerAdapter pagerAdapter = new ProjectsPagerAdapter(this, mProfileUser);
        mApp.viewPager.setAdapter(pagerAdapter);

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

    /**
     * Sets up the listener for the "Send message" button
     */
    private void setupMessageButtonListener() {
        mApp.buttonMessage.setOnClickListener(v ->
                Tools.navigateToFragment(mContext, new ConversationFragment(mProfileUser), R.id.flContainer, "right_to_left"));
    }

    /**
     * Interface for listening to the drawer
     */
    public interface OnFragmentInteractionListener {
        void openDrawer();
    }

}