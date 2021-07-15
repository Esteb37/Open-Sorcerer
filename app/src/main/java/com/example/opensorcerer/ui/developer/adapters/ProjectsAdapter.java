package com.example.opensorcerer.ui.developer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.opensorcerer.databinding.ItemProjectBinding;
import com.example.opensorcerer.models.Project;

import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class ProjectsAdapter extends RecyclerView.Adapter<ProjectHolder>{

    List<Project> mProjects;
    Context mContext;

    ItemProjectBinding app;
    ProjectHolder mHolder;

    public interface OnClickListener{
        void onItemClicked(int position);
    }

    private final OnClickListener mClickListener;

    /**
     *
     * @param projects
     * @param context
     */
    public ProjectsAdapter(List<Project> projects, Context context, OnClickListener clickListener) {
        mProjects = projects;
        mContext = context;
        mClickListener = clickListener;
    }

    @NonNull
    @NotNull
    @Override
    public ProjectHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        app = ItemProjectBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        View view = app.getRoot();
        return new ProjectHolder(view,mContext,app,mClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ProjectHolder holder, int position) {
        mHolder = holder;
        mHolder.bind(mProjects.get(position));

    }

    @Override
    public int getItemCount() {
        return mProjects.size();
    }

    public void clear() {
        mProjects.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Project> list) {
        mProjects.addAll(list);
        notifyDataSetChanged();
    }

    public Project getCurrentProject(){
       return  mHolder.getAdapterPosition()>0 ? mProjects.get(mHolder.getAdapterPosition()-1) : mProjects.get(0);
    }
}
