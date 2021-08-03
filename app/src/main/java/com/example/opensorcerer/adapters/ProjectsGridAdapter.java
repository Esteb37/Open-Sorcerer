package com.example.opensorcerer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.opensorcerer.databinding.ItemGridProjectBinding;
import com.example.opensorcerer.holders.ProjectGridHolder;
import com.example.opensorcerer.models.Project;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * RecyclerView Adapter class for Projects in Grid format
 */
public class ProjectsGridAdapter extends RecyclerView.Adapter<ProjectGridHolder> {

    /**
     * The adapter's current context
     */
    private final Context mContext;

    /**
     * The list of projects to display
     */
    private final List<Project> mProjects;

    /**
     * Click listener
     */
    private final OnClickListener mClickListener;

    public ProjectsGridAdapter(List<Project> projects, Context context, OnClickListener clickListener) {
        mProjects = projects;
        mContext = context;
        mClickListener = clickListener;
    }

    /**
     * Inflates the ViewHolder and prepares it for the items
     *
     * @return the inflated ProjectHolder
     */
    @NonNull
    @NotNull
    @Override
    public ProjectGridHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ItemGridProjectBinding mApp = ItemGridProjectBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        View view = mApp.getRoot();
        return new ProjectGridHolder(view, mContext, mApp, mClickListener);
    }

    /**
     * Binds the project to a ViewHolder to display it
     */
    @Override
    public void onBindViewHolder(@NonNull @NotNull ProjectGridHolder holder, int position) {
        holder.bind(mProjects.get(position));
    }

    /**
     * Project list item count getter
     */
    @Override
    public int getItemCount() {
        return mProjects.size();
    }

    /**
     * Cleans the project list and notifies the adapter
     */
    public void clear() {
        mProjects.clear();
        notifyDataSetChanged();
    }

    /**
     * Adds a list of projects to the adapter
     *
     * @param list A list of projects
     */
    public void addAll(List<Project> list) {
        mProjects.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * Interface for detecting clicks on the project
     */
    public interface OnClickListener {
        void onItemClicked(int position);
    }
}
