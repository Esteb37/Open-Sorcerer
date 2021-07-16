package com.example.opensorcerer.ui.developer.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.opensorcerer.databinding.ItemProjectBinding;
import com.example.opensorcerer.models.Project;
import com.parse.ParseException;
import com.parse.ParseFile;


import java.util.List;

/**
 * ViewHolder class for Projects in the Developer's timeline
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class ProjectHolder extends RecyclerView.ViewHolder{


    /**Binder object for ViewBinding*/
    private final ItemProjectBinding app;

    /**The Holder's context*/
    private final Context mContext;

    public ProjectHolder(View view, Context context, ItemProjectBinding binder, ProjectsAdapter.OnClickListener clickListener) {
        super(view);
        app = binder;
        mContext = context;
        view.setOnClickListener(v -> clickListener.onItemClicked(getAdapterPosition()));
    }

    /**
     * Populates the view's items with the project's information
     * @param project The project to display
     */
    public void bind(Project project) {

        //Set text details
        app.tvTitle.setText(project.getTitle());
        app.tvDescription.setText(project.getDescription());
        app.tvTitle.setMaxLines(project.getTitle().split(" ").length);

        //Set the project manager's name
        try {
            app.tvAuthor.setText(String.format("by %s", project.getManager().fetchIfNeeded().getUsername()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Show the first three tags
        TextView[] tagViews = {app.tvTag1,app.tvTag2,app.tvTag3};
        List<String> tags = project.getTags();
        for(int i = 0;i<3;i++){
            try {
                tagViews[i].setText(tags.get(i).replace("-"," "));
            } catch(IndexOutOfBoundsException e){
                tagViews[i].setText("");
            }
        }

        //Show the first three languages
        TextView[] languageViews = {app.tvLanguage1,app.tvLanguage2,app.tvLanguage3};
        List<String> languages = project.getLanguages();
        for(int i = 0;i<3;i++){
            try {
                languageViews[i].setText(languages.get(i).replace("-"," "));
            } catch(IndexOutOfBoundsException e){
                languageViews[i].setText("");
            }
        }

        //Load the project's logo
        ParseFile image = project.getLogoImage();
        if(image != null){
            Glide.with(mContext)
                    .load(image.getUrl())
                    .transform(new RoundedCorners(1000))
                    .into(app.ivImage);
        }
    }
}
