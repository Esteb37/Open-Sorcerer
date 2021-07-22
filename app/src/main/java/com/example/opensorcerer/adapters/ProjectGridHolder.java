package com.example.opensorcerer.adapters;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.opensorcerer.databinding.ItemGridProjectBinding;
import com.example.opensorcerer.models.Project;
import com.parse.ParseFile;

/**
 * ViewHolder class for Projects in Grid format
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class ProjectGridHolder extends RecyclerView.ViewHolder {

    /**
     * Binder object for ViewBinding
     */
    private final ItemGridProjectBinding mApp;

    /**
     * The Holder's context
     */
    private final Context mContext;

    public ProjectGridHolder(View view, Context context, ItemGridProjectBinding binder) {
        super(view);
        mApp = binder;
        mContext = context;
    }

    /**
     * Populates the view's items with the project's information
     *
     * @param project The project to display
     */
    public void bind(Project project) {

        //Set text details
        mApp.textViewTitle.setText(project.getTitle());
        mApp.textViewDescription.setText(project.getDescription());

        //Dynamically set the title's max lines to avoid word breaking
        mApp.textViewTitle.setMaxLines(project.getTitle().split(" ").length);

        //Load the project's image
        ParseFile image = project.getLogoImage();
        if (image != null) {
            Glide.with(mContext)
                    .load(image.getUrl())
                    .centerInside()
                    .transform(new RoundedCorners(500))
                    .into(mApp.imageViewLogo);
        }
    }
}
