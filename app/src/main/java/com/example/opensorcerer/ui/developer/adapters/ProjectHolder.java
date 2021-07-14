package com.example.opensorcerer.ui.developer.adapters;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.opensorcerer.databinding.ItemManagerProjectBinding;
import com.example.opensorcerer.databinding.ItemProjectBinding;
import com.example.opensorcerer.models.Project;
import com.parse.ParseFile;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class ProjectHolder extends RecyclerView.ViewHolder{


    //View binder
    private final ItemProjectBinding app;

    //Current context
    private final Context mContext;

    public ProjectHolder(View view, Context context, ItemProjectBinding binder) {
        super(view);
        app = binder;
        mContext = context;
    }

    public void bind(Project project) {

        app.tvTitle.setText(project.getTitle());
        app.tvDescription.setText(project.getDescription());

        ParseFile image = project.getLogoImage();
        if(image != null){
            Glide.with(mContext)
                    .load(image.getUrl())
                    .transform(new RoundedCorners(1000))
                    .into(app.ivImage);
        }
    }
}
