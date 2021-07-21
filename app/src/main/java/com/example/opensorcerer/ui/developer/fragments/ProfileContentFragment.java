package com.example.opensorcerer.ui.developer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.opensorcerer.R;
import com.example.opensorcerer.adapters.FavoritesPagerAdapter;
import com.example.opensorcerer.application.OSApplication;
import com.example.opensorcerer.databinding.FragmentProfileContentBinding;
import com.example.opensorcerer.models.users.roles.Developer;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;
import org.kohsuke.github.GitHub;

import java.util.List;

/**
 * Fragment for displaying a user's profile.
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class ProfileContentFragment extends androidx.fragment.app.Fragment  {


    /**Tag for logging*/
    private static final String TAG = "ProfileFragment";

    /**Binder object for ViewBinding*/
    private FragmentProfileContentBinding app;

    /**Fragment's context*/
    private Context mContext;

    /**Current logged in user*/
    private Developer mUser;

    /**GitHub API handler*/
    private GitHub mGitHub;

    /**Fragment pager adapter*/
    FavoritesPagerAdapter mPagerAdapter;

    // create nested interface in ContentFragment
    public interface OnFragmentInteractionListener {
        void openDrawer();
        void closeDrawer();
    }

    private OnFragmentInteractionListener mListener;


    public ProfileContentFragment(OnFragmentInteractionListener listener) {
        mListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        app = FragmentProfileContentBinding.inflate(inflater,container,false);
        return app.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getState();

        loadProfileDetails();

        setupPagerView();

        setDrawerButtonListener();
    }

    private void setDrawerButtonListener() {
        app.buttonDrawer.setOnClickListener(v -> mListener.openDrawer());
    }

    private void setupPagerView() {

        //Set the adapter
        mPagerAdapter = new FavoritesPagerAdapter(this);
        app.viewPager.setAdapter(mPagerAdapter);

        //Set the tab icons
        new TabLayoutMediator(app.tabLayout, app.viewPager,
                (tab, position) -> tab.setIcon(position == 0
                        ? R.drawable.ufi_heart_liked
                        : R.drawable.ic_dashboard_black_24dp)
        ).attach();
    }

    private void loadProfileDetails() {
        app.textViewName.setText(mUser.getName());
        app.textViewUsername.setText(String.format("@%s", mUser.getUsername()));
        app.textViewBio.setText(mUser.getBio());

        app.textViewLanguages.setText(listToString(mUser.getLanguages()));
        app.textViewLanguages.post(() -> app.textViewLanguages.setMoreMessage(app.textViewMoreLanguages));

        app.textViewInterests.setText(listToString(mUser.getInterests()));
        app.textViewInterests.post(() -> app.textViewInterests.setMoreMessage(app.textViewMoreInterests));

        Glide.with(mContext)
                .load(mUser.getProfilePicture().getUrl())
                .into(app.imageViewProfilePicture);
    }





    private String listToString(List<String> list) {
        StringBuilder str = new StringBuilder();
        for(int i = 0;i<list.size();i++){
            String item = list.get(i).trim();
            if(!item.equals("")){
                str.append(item).append(", ");
            }
        }
        return str.substring(0,str.length()-2);
    }


    /**
     * Gets the current state for the member variables.
     */
    private void getState() {
        mContext = getContext();

        mUser = Developer.getCurrentUser();

        mGitHub = ((OSApplication) requireActivity().getApplication()).getGitHub();

    }
}