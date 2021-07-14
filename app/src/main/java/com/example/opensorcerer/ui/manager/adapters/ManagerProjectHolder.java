package com.example.opensorcerer.ui.manager.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.opensorcerer.databinding.ItemManagerProjectBinding;
import com.example.opensorcerer.models.Project;
import com.parse.ParseFile;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class ManagerProjectHolder extends RecyclerView.ViewHolder{


    //View binder
    private final ItemManagerProjectBinding app;

    //Current context
    private final Context mContext;

    public ManagerProjectHolder(View view, Context context, ItemManagerProjectBinding binder) {
        super(view);
        app = binder;
        mContext = context;
    }

    public void bind(Project project) {

        app.tvTitle.setText(project.getTitle());
        app.tvDescription.setText(project.getDescription());

        app.tvTitle.setMaxLines(project.getTitle().split(" ").length);

        ParseFile image = project.getLogoImage();
        if(image != null){
            Glide.with(mContext)
                    .load(image.getUrl())
                    .centerInside()
                    .into(app.ivImage);
        }
    }
}
