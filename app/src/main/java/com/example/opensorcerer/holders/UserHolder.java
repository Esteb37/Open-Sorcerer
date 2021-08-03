package com.example.opensorcerer.holders;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.opensorcerer.R;
import com.example.opensorcerer.adapters.UsersAdapter;
import com.example.opensorcerer.databinding.ItemUserBinding;
import com.example.opensorcerer.models.Project;
import com.example.opensorcerer.models.Tools;
import com.example.opensorcerer.models.User;
import com.example.opensorcerer.ui.main.conversations.ConversationFragment;
import com.parse.ParseFile;

import org.jetbrains.annotations.NotNull;

public class UserHolder extends RecyclerView.ViewHolder {

    /**
     * Binder object for ViewBinding
     */
    private final ItemUserBinding mApp;

    /**
     * The Holder's context
     */
    private final Context mContext;

    private final Project mProject;

    public UserHolder(@NonNull @NotNull View itemView, ItemUserBinding app, Context context, Project project, UsersAdapter.OnClickListener clickListener) {
        super(itemView);
        mApp = app;
        mContext = context;
        mProject = project;
        itemView.setOnClickListener(v -> clickListener.onItemClicked(getAdapterPosition()));
    }

    /**
     * Populates the view's items with the user's information
     */
    public void bind(User user) {

        mApp.textViewName.setText(user.getName());
        mApp.textViewUsername.setText(user.getUsername());

        ParseFile profilePicture = user.getProfilePicture();
        if (profilePicture != null) {
            Glide.with(mContext)
                    .load(profilePicture.getUrl())
                    .into(mApp.imageViewProfilePicture);
        }

        if (user.getObjectId().equals(User.getCurrentUser().getObjectId())) {
            mApp.buttonMessage.setVisibility(View.GONE);
        } else {
            mApp.buttonMessage.setVisibility(View.VISIBLE);
            mApp.buttonMessage.setOnClickListener(v ->
                    Tools.navigateToFragment(mContext, new ConversationFragment(user, mProject), R.id.flContainerDetails, "right_to_left"));
        }
    }

}
