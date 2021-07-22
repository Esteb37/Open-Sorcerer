package com.example.opensorcerer.ui.main.create;

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
import com.example.opensorcerer.application.OSApplication;
import com.example.opensorcerer.databinding.FragmentCreateProjectBinding;
import com.example.opensorcerer.models.User;

import org.jetbrains.annotations.NotNull;
import org.kohsuke.github.GitHub;

/**
 * Fragment for creating a new project and adding it to the database
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class CreateProjectFragment extends Fragment {

    /**Tag for logging*/
    private static final String TAG = "CreateProjectFragment";

    /**Binder object for ViewBinding*/
    private FragmentCreateProjectBinding app;

    /**Fragment's context*/
    private Context mContext;

    /**Current logged in user*/
    private User mUser;

    /**GitHub API handler*/
    private GitHub mGitHub;


    public CreateProjectFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Inflates the fragment's layout
     */
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        app = com.example.opensorcerer.databinding.FragmentCreateProjectBinding.inflate(inflater,container,false);
        return app.getRoot();
    }

    /**
     * Sets up the fragment's methods
     */
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        getState();

        loadFirstFragment();

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
     * Load the first fragment from the Signup Process
         */
    private void loadFirstFragment() {
        final FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        Fragment fragment = new CreateProjectFirstFragment();
        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
    }

}