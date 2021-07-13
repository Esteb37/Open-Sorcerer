package com.example.opensorcerer.ui.manager;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.opensorcerer.R;
import com.example.opensorcerer.databinding.FragmentCreateProjectBinding;

import org.jetbrains.annotations.NotNull;
import org.kohsuke.github.GitHub;


public class CreateProjectFragment extends Fragment {

    private static final String TAG = "CreateProject";
    private FragmentCreateProjectBinding app;
    private Context mContext;
    private GitHub mGitHub;

    public CreateProjectFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        app = FragmentCreateProjectBinding.inflate(inflater,container,false);
        return app.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}