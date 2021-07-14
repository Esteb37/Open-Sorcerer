package com.example.opensorcerer.ui.manager.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.opensorcerer.R;
import com.example.opensorcerer.application.OSApplication;
import com.example.opensorcerer.databinding.FragmentCreateProjectBinding;
import com.example.opensorcerer.models.Project;
import com.example.opensorcerer.models.users.roles.Manager;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.SaveCallback;

import org.jetbrains.annotations.NotNull;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.IOException;


public class CreateProjectFragment extends Fragment {

    private static final String TAG = "CreateProject";
    private FragmentCreateProjectBinding app;
    private Manager mUser;
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

        mContext = getContext();
        mGitHub = ((OSApplication) getActivity().getApplication()).getGitHub();
        mUser = Manager.getCurrentUser();

        app.btnQuick.setOnClickListener(v -> {
            new Thread(new GetRepoTask()).start();
        });

        app.btnCreate.setOnClickListener(v -> {
            Project project = new Project();
            project.setTitle(app.etTitle.getText().toString());
            project.setDescription(app.etShortDescription.getText().toString());
            project.setReadme(app.etReadme.getText().toString());
            project.setManager(mUser);
            project.setRepository(app.etRepo.getText().toString());
            project.saveInBackground(e -> {
                if(e==null){
                    ParseRelation<Project> projects = mUser.getProjects();
                    projects.add(project);
                    mUser.setProjects(projects);
                    mUser.getHandler().saveInBackground(e1 -> {
                        if(e1==null){
                            final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.flContainer,new MyProjectsFragment()).commit();
                        } else {
                            Log.d(TAG,"Error saving project in user's project list.");
                            e1.printStackTrace();
                        }
                    });

                } else {
                    Log.d(TAG,"Error saving project.");
                    e.printStackTrace();
                }
            });
        });
    }

    private class GetRepoTask implements Runnable {

        @Override
        public void run() {
            String repoLink = app.etRepo.getText().toString().split("github.com/")[1];
            try {
                Looper.prepare();
                GHRepository ghRepo = mGitHub.getRepository(repoLink);
                app.etTitle.setText(ghRepo.getName());
                app.etReadme.setText(ghRepo.getReadme().getHtmlUrl());
                app.etShortDescription.setText(ghRepo.getDescription());
            } catch (IOException e) {
                Toast.makeText(mContext, "Invalid Repo Link", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

}