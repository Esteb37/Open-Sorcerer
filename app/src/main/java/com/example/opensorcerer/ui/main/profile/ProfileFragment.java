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
    private FragmentProfileBinding app;

    /**Fragment's context*/
    private Context mContext;

    /**Current logged in user*/
    private User mUser;

    /**GitHub API handler*/
    private GitHub mGitHub;

    /**Fragment pager adapter*/
    FavoritesPagerAdapter mPagerAdapter;

    private Fragment profileContentFragment = null;
    private Fragment profileDrawerFragment = null;


    private final ProfileContentFragment.OnFragmentInteractionListener
            mDrawerListener = new ProfileContentFragment.OnFragmentInteractionListener() {
        @Override
        public void openDrawer() {
            app.drawerLy.openDrawer(GravityCompat.END);
        }

        @Override
        public void closeDrawer() {
            app.drawerLy.closeDrawers();
        }
    };

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        app = FragmentProfileBinding.inflate(inflater,container,false);
        return app.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getState();

        setupDrawer();
    }

    private void setupDrawer() {

        FragmentManager fm = requireActivity().getSupportFragmentManager();
        profileContentFragment = new ProfileContentFragment(mDrawerListener);
        FragmentTransaction t = fm.beginTransaction();
        t.replace(R.id.content_frame, profileContentFragment);
        t.commit();
        profileDrawerFragment = new ProfileDrawerFragment();
        FragmentTransaction r = fm.beginTransaction();
        r.replace(R.id.right_drawer, profileDrawerFragment);
        r.commit();
    }

    /**
     * Gets the current state for the member variables.
     */
    private void getState() {
        mContext = getContext();

        mUser = User.getCurrentUser();

        mGitHub = ((OSApplication) requireActivity().getApplication()).getGitHub();
    }
}