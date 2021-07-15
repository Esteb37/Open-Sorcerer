package com.example.opensorcerer.ui.developer;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.opensorcerer.application.OSApplication;
import com.example.opensorcerer.databinding.FragmentDetailsBinding;
import com.example.opensorcerer.models.Project;

import com.parse.ParseException;
import com.parse.ParseFile;

import org.jetbrains.annotations.NotNull;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.parceler.Parcels;

import java.io.IOException;


public class DetailsActivity extends AppCompatActivity {

    private FragmentDetailsBinding app;
    private GitHub mGitHub;
    private Project mProject;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = FragmentDetailsBinding.inflate(getLayoutInflater());
        setContentView(app.getRoot());

        mProject = Parcels.unwrap(getIntent().getParcelableExtra("project"));

        mGitHub = ((OSApplication) getApplication()).getGitHub();

        mContext = this;

        app.tvTitle.setText(mProject.getTitle());

        app.tvTitle.setMaxLines(mProject.getTitle().split(" ").length);
        app.tvViews.setText(String.valueOf(mProject.getViewCount()));

        app.tvLikes.setText(String.valueOf(mProject.getLikeCount()));
        try {
            app.tvAuthor.setText(String.format("by %s", mProject.getManager().fetchIfNeeded().getUsername()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        app.tvTags.setText(mProject.getTags().toString().replace("[","").replace("]",""));

        app.tvLanguages.setText(mProject.getLanguages().toString().replace("[","").replace("]",""));

        ParseFile image = mProject.getLogoImage();
        if(image != null){
            Glide.with(mContext)
                    .load(image.getUrl())
                    .transform(new RoundedCorners(1000))
                    .into(app.ivImage);
            app.progressBar.setVisibility(View.GONE);
        }

        new Thread(new GetReadmeTask()).start();

    }

    private class GetReadmeTask implements Runnable {

        @Override
        public void run() {

            try {
                Looper.prepare();
                String repoLink = mProject.getRepository().split("github.com/")[1];
                GHRepository ghRepo = mGitHub.getRepository(repoLink);
                GHContent readme = ghRepo.getReadme();
                runOnUiThread(() -> {
                    try {
                        app.markdownView.setMarkDownText(readme.getContent());
                        app.progressBarReadme.setVisibility(View.GONE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}