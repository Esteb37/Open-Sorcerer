package com.example.opensorcerer.holders;

import android.content.Context;
import android.view.View;
import android.webkit.URLUtil;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.opensorcerer.adapters.ProjectsGridAdapter;
import com.example.opensorcerer.databinding.ItemGridProjectBinding;
import com.example.opensorcerer.models.Project;
import com.parse.ParseFile;

/**
 * ViewHolder class for Projects in Grid format
 */
public class ProjectGridHolder extends RecyclerView.ViewHolder {

    /**
     * Binder object for ViewBinding
     */
    private final ItemGridProjectBinding mApp;

    /**
     * The Holder's context
     */
    private final Context mContext;

    public ProjectGridHolder(View view, Context context, ItemGridProjectBinding binder, ProjectsGridAdapter.OnClickListener clickListener) {
        super(view);
        mApp = binder;
        mContext = context;
        view.setOnClickListener(v -> clickListener.onItemClicked(getAdapterPosition()));
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

        // Load the project's logo from URL if any
        String imageURL = project.getLogoImageUrl();
        ParseFile imageFile = project.getLogoImage();
        if (imageURL != null) {
            Glide.with(mContext)
                    .load(URLUtil.isValidUrl(imageURL) ? imageURL : imageFile.getUrl())
                    .transform(new RoundedCorners(1000))
                    .into(mApp.imageViewLogo);
        } else if (imageFile != null) {
            Glide.with(mContext)
                    .load(imageFile.getUrl())
                    .transform(new RoundedCorners(1000))
                    .into(mApp.imageViewLogo);
        }
    }
}
