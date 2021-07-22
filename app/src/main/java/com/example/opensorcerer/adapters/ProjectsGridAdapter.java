package com.example.opensorcerer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.opensorcerer.databinding.ItemGridProjectBinding;
import com.example.opensorcerer.models.Project;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * RecyclerView Adapter class for Projects in Grid format
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class ProjectsGridAdapter extends RecyclerView.Adapter<ProjectGridHolder> {

    /**
     * Binder object for ViewBinding
     */
    private ItemGridProjectBinding app;

    /**
     * The adapter's current context
     */
    private final Context mContext;

    /**
     * The list of projects to display
     */
    private final List<Project> mProjects;

    /**
     * The ViewHolder for the project items
     */
    private ProjectCardHolder mHolder;

    public ProjectsGridAdapter(List<Project> projects, Context context) {
        mProjects = projects;
        mContext = context;
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
        app = ItemGridProjectBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        View view = app.getRoot();
        return new ProjectGridHolder(view, mContext, app);
    }

    /**
     * Binds the project to a ViewHolder to display it
     */
    @Override
    public void onBindViewHolder(@NonNull @NotNull ProjectGridHolder holder, int position) {
        holder.bind(mProjects.get(position));
    }

    /**Project list item count getter*/
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
     * @param list A list of projects
     */
    public void addAll(List<Project> list) {
        mProjects.addAll(list);
        notifyDataSetChanged();
    }
}
