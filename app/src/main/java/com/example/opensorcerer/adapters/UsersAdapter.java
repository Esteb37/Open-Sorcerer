package com.example.opensorcerer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.opensorcerer.databinding.ItemUserBinding;
import com.example.opensorcerer.holders.UserHolder;
import com.example.opensorcerer.models.Project;
import com.example.opensorcerer.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UserHolder> {

    private final List<User> mUsers;
    private final Context mContext;
    private final UsersAdapter.OnClickListener mClickListener;
    private final Project mProject;


    public UsersAdapter(List<User> users, Context context, Project project, UsersAdapter.OnClickListener clickListener) {
        mUsers = users;
        mContext = context;
        mProject = project;
        mClickListener = clickListener;
    }

    @NonNull
    @NotNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ItemUserBinding app = ItemUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        View view = app.getRoot();
        return new UserHolder(view, app, mContext, mProject, mClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UserHolder holder, int position) {
        holder.bind(mUsers.get(position));
    }

    /**
     * Cleans the user list and notifies the adapter
     */
    public void clear() {
        mUsers.clear();
        notifyDataSetChanged();
    }

    /**
     * Adds a list of users to the adapter
     */
    public void addAll(List<User> list) {
        mUsers.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    /**
     * Interface for detecting clicks on the user
     */
    public interface OnClickListener {
        void onItemClicked(int position);
    }
}
