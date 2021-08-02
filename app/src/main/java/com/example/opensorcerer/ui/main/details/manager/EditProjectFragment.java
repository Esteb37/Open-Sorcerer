package com.example.opensorcerer.ui.main.details.manager;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.opensorcerer.databinding.FragmentEditProjectBinding;
import com.example.opensorcerer.models.Project;

import org.jetbrains.annotations.NotNull;

public class EditProjectFragment extends Fragment {

    /**
     * Project being displayed
     */
    private Project mProject;

    /**
     * Binder object for ViewBinding
     */
    private FragmentEditProjectBinding mApp;

    /**
     * Fragment's context
     */
    private Context mContext;

    public EditProjectFragment(Project project) {
        mProject = project;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mApp = FragmentEditProjectBinding.inflate(inflater, container, false);
        return mApp.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getState();
    }

    /**
     * Gets the current state for the member variables.
     */
    private void getState() {
        mContext = getContext();
    }

}