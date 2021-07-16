package com.example.opensorcerer.ui.manager.adapters;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.opensorcerer.databinding.ItemManagerProjectBinding;
import com.example.opensorcerer.models.Project;
import com.parse.ParseFile;

/**
 * ViewHolder class for Projects created by the manager
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class ManagerProjectHolder extends RecyclerView.ViewHolder{

    /**Binder object for ViewBinding*/
    private final ItemManagerProjectBinding app;

    /**The Holder's context*/
    private final Context mContext;

    public ManagerProjectHolder(View view, Context context, ItemManagerProjectBinding binder) {
        super(view);
        app = binder;
        mContext = context;
    }

    /**
     * Populates the view's items with the project's information
     * @param project The project to display
     */
    public void bind(Project project) {

        //Set text details
        app.tvTitle.setText(project.getTitle());
        app.tvDescription.setText(project.getDescription());

        //Dynamically set the title's max lines to avoid word breaking
        app.tvTitle.setMaxLines(project.getTitle().split(" ").length);

        //Load the project's image
        ParseFile image = project.getLogoImage();
        if(image != null){
            Glide.with(mContext)
                    .load(image.getUrl())
                    .centerInside()
                    .into(app.ivImage);
        }
    }
}
