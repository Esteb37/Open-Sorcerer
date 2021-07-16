package com.example.opensorcerer.ui.manager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.opensorcerer.databinding.ItemManagerProjectBinding;
import com.example.opensorcerer.models.Project;
import com.example.opensorcerer.ui.developer.adapters.ProjectHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * RecyclerView Adapter class for Projects in the Developer's timeline
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class ManagerProjectsAdapter extends RecyclerView.Adapter<ManagerProjectHolder> {

    /**
     * Binder object for ViewBinding
     */
    private ItemManagerProjectBinding app;

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
    private ProjectHolder mHolder;

    public ManagerProjectsAdapter(List<Project> projects, Context context) {
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
    public ManagerProjectHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        app = ItemManagerProjectBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        View view = app.getRoot();
        return new ManagerProjectHolder(view, mContext, app);
    }

    /**
     * Binds the project to a ViewHolder to display it
     */
    @Override
    public void onBindViewHolder(@NonNull @NotNull ManagerProjectHolder holder, int position) {
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
