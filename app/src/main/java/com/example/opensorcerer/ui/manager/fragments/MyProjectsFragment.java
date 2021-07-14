package com.example.opensorcerer.ui.manager.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.opensorcerer.databinding.FragmentMyProjectsBinding;
import com.example.opensorcerer.models.Project;
import com.example.opensorcerer.models.users.roles.Manager;
import com.example.opensorcerer.ui.manager.adapters.ManagerProjectsAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import org.jetbrains.annotations.NotNull;
import org.kohsuke.github.GitHub;

import java.util.ArrayList;
import java.util.List;

public class MyProjectsFragment extends Fragment {

    private static final String TAG = "CreateProject";
    private FragmentMyProjectsBinding app;
    private Context mContext;
    private GitHub mGitHub;
    private Manager mUser;

    private ManagerProjectsAdapter mAdapter;

    private List<Project> mProjects;

    public MyProjectsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        app = FragmentMyProjectsBinding.inflate(inflater,container,false);
        return app.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mUser = Manager.getCurrentUser();

        mProjects = new ArrayList<>();

        mContext = getContext();

        mAdapter = new ManagerProjectsAdapter(mProjects,mContext);

        app.rvProjects.setAdapter(mAdapter);
        app.rvProjects.setLayoutManager(new GridLayoutManager(mContext,2));

        queryProjects();
    }



    public void queryProjects(){
        ParseQuery<Project> query = ParseQuery.getQuery(Project.class).whereContains("manager",mUser.getObjectId());
        query.addDescendingOrder("createdAt");
        query.findInBackground((projects, e) -> {
            if(e==null){
                mAdapter.addAll(projects);
            } else {
                Log.d(TAG,"Unable to load projects.");
            }
        });
    }
}