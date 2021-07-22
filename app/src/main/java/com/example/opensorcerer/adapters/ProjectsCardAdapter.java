package com.example.opensorcerer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.opensorcerer.databinding.ItemCardProjectBinding;
import com.example.opensorcerer.models.Project;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * RecyclerView Adapter class for Projects in linear Card format
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class ProjectsCardAdapter extends RecyclerView.Adapter<ProjectCardHolder>{

    /**Binder object for ViewBinding*/
    private ItemCardProjectBinding app;

    /**The adapter's current context*/
    private final Context mContext;

    /**The list of projects to display*/
    private final List<Project> mProjects;

    /**The ViewHolder for the project items*/
    private ProjectCardHolder mHolder;

    /**
     * Interface for detecting clicks on the project
     */
    public interface OnClickListener{
        void onItemClicked(int position);
    }

    /**Click listener*/
    private final OnClickListener mClickListener;

    /**
     * Interface for detecting double taps on the project
     */
    public interface OnDoubleTapListener{
        void onItemDoubleTap(int position);
    }

    private final OnDoubleTapListener mDoubleTapListener;


    public ProjectsCardAdapter(List<Project> projects, Context context, OnClickListener clickListener, OnDoubleTapListener doubleTapListener) {
        mProjects = projects;
        mContext = context;
        mClickListener = clickListener;
        mDoubleTapListener = doubleTapListener;
    }

    /**
     * Inflates the ViewHolder and prepares it for the items
     *
     * @return the inflated ProjectHolder
     */
    @NonNull
    @NotNull
    @Override
    public ProjectCardHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        app = ItemCardProjectBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        View view = app.getRoot();
        return new ProjectCardHolder(view,mContext,app,mClickListener,mDoubleTapListener);
    }

    /**
     * Binds the project to a ViewHolder to display it
     */
    @Override
    public void onBindViewHolder(@NonNull @NotNull ProjectCardHolder holder, int position) {
        mHolder = holder;
        mHolder.bind(mProjects.get(position));

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

    /**
     * Gets the project that is currently on the user's screen
     * @return The currently viewed project
     */
    public Project getCurrentProject(){
       return  mHolder.getAdapterPosition()>0 ? mProjects.get(mHolder.getAdapterPosition()-1) : mProjects.get(0);
    }
}
