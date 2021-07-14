package com.example.opensorcerer.ui.developer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.opensorcerer.databinding.ItemManagerProjectBinding;
import com.example.opensorcerer.databinding.ItemProjectBinding;
import com.example.opensorcerer.models.Project;

import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class ProjectsHolder extends RecyclerView.Adapter<ProjectHolder>{

    List<Project> mProjects;
    Context mContext;

    ItemProjectBinding app;

    public ProjectsHolder(List<Project> projects, Context context) {
        mProjects = projects;
        mContext = context;
    }

    @NonNull
    @NotNull
    @Override
    public ProjectHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        app = ItemProjectBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        View view = app.getRoot();
        return new ProjectHolder(view,mContext, app);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ProjectHolder holder, int position) {
        holder.bind(mProjects.get(position));
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
}
