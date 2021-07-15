package com.example.opensorcerer.ui.developer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.opensorcerer.R;
import com.example.opensorcerer.databinding.FragmentProjectsBinding;
import com.example.opensorcerer.models.Project;
import com.example.opensorcerer.models.users.roles.Manager;
import com.example.opensorcerer.ui.developer.DetailsFragment;
import com.example.opensorcerer.ui.developer.adapters.ProjectsAdapter;
import com.parse.ParseQuery;

import org.jetbrains.annotations.NotNull;
import org.kohsuke.github.GitHub;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class ProjectsFragment extends Fragment {

    private static final String TAG = "ProjectsFragment";
    private FragmentProjectsBinding app;
    private Context mContext;
    private GitHub mGitHub;
    private Manager mUser;

    private ProjectsAdapter mAdapter;

    private List<Project> mProjects;

    public ProjectsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        app = FragmentProjectsBinding.inflate(inflater,container,false);
        return app.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mUser = Manager.getCurrentUser();

        mContext = getContext();

        mProjects = new ArrayList<>();

        ProjectsAdapter.OnClickListener clickListener = position -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            Bundle bundle = new Bundle();
            bundle.putParcelable("project",Parcels.wrap(mProjects.get(position)));
            Fragment fragment = new DetailsFragment();
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.flContainer,fragment).commit();
        };


        mAdapter = new ProjectsAdapter(mProjects,mContext,clickListener);



        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(app.rvProjects);
        app.rvProjects.setAdapter(mAdapter);
        app.rvProjects.setLayoutManager(new LinearLayoutManager(mContext,RecyclerView.HORIZONTAL,false));

        queryProjects();
    }



    public void queryProjects(){
        ParseQuery<Project> query = ParseQuery.getQuery(Project.class);
        query.addDescendingOrder("createdAt");
        query.findInBackground((projects, e) -> mAdapter.addAll(projects));
    }
}