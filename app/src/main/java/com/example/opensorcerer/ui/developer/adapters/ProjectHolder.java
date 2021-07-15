package com.example.opensorcerer.ui.developer.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.opensorcerer.databinding.ItemManagerProjectBinding;
import com.example.opensorcerer.databinding.ItemProjectBinding;
import com.example.opensorcerer.models.Project;
import com.parse.ParseException;
import com.parse.ParseFile;

import java.util.List;
import java.util.StringTokenizer;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class ProjectHolder extends RecyclerView.ViewHolder{


    //View binder
    private final ItemProjectBinding app;

    //Current context
    private final Context mContext;


    public ProjectHolder(View view, Context context, ItemProjectBinding binder,ProjectsAdapter.OnClickListener clickListener) {
        super(view);
        app = binder;
        mContext = context;
        itemView.setOnClickListener(v -> clickListener.onItemClicked(getAdapterPosition()));
    }

    public void bind(Project project) {

        app.tvTitle.setText(project.getTitle());
        app.tvDescription.setText(project.getDescription());

        app.tvTitle.setMaxLines(project.getTitle().split(" ").length);

        try {
            app.tvAuthor.setText(String.format("by %s", project.getManager().fetchIfNeeded().getUsername()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        TextView[] tagViews = {app.tvTag1,app.tvTag2,app.tvTag3};
        List<String> tags = project.getTags();
        for(int i = 0;i<3;i++){
            try {
                tagViews[i].setText(tags.get(i).replace("-"," "));
            } catch(IndexOutOfBoundsException e){
                tagViews[i].setText("");
            }
        }

        TextView[] languageViews = {app.tvLanguage1,app.tvLanguage2,app.tvLanguage3};
        List<String> languages = project.getLanguages();
        for(int i = 0;i<3;i++){
            try {
                languageViews[i].setText(languages.get(i).replace("-"," "));
            } catch(IndexOutOfBoundsException e){
                languageViews[i].setText("");
            }
        }


        ParseFile image = project.getLogoImage();
        if(image != null){
            Glide.with(mContext)
                    .load(image.getUrl())
                    .transform(new RoundedCorners(1000))
                    .into(app.ivImage);
        }
    }
}
