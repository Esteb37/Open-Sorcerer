package com.example.opensorcerer.ui.manager.adapters;

import android.content.ClipData;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.opensorcerer.databinding.ItemManagerProjectBinding;
import com.example.opensorcerer.models.Project;
import com.parse.ParseFile;

import org.jetbrains.annotations.NotNull;

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

        /*ParseFile image = project.getBannerImage();
        if(image != null){
            Glide.with(mContext)
                    .load(image.getUrl())
                    .into(app.ivImage);
        }*/
    }
}
