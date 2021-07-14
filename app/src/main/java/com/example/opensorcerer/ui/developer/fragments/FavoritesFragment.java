package com.example.opensorcerer.ui.developer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.opensorcerer.R;
import com.example.opensorcerer.application.OSApplication;
import com.example.opensorcerer.databinding.FragmentCreateProjectBinding;
import com.example.opensorcerer.databinding.FragmentFavoritesBinding;
import com.example.opensorcerer.models.Project;
import com.example.opensorcerer.models.users.roles.Manager;
import com.parse.ParseRelation;

import org.jetbrains.annotations.NotNull;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.IOException;


public class FavoritesFragment extends Fragment {

    private static final String TAG = "FavoritesFragment";
    private FragmentFavoritesBinding app;
    private Manager mUser;
    private Context mContext;
    private GitHub mGitHub;

    public FavoritesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        app = FragmentFavoritesBinding.inflate(inflater,container,false);
        return app.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getContext();
        mGitHub = ((OSApplication) getActivity().getApplication()).getGitHub();
        mUser = Manager.getCurrentUser();
    }



}