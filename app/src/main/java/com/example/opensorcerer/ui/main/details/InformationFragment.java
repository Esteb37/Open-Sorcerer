package com.example.opensorcerer.ui.main.details;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.opensorcerer.R;
import com.example.opensorcerer.application.OSApplication;
import com.example.opensorcerer.databinding.FragmentInformationBinding;
import com.example.opensorcerer.models.Project;
import com.example.opensorcerer.models.User;
import com.parse.ParseException;
import com.parse.ParseFile;

import org.jetbrains.annotations.NotNull;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.parceler.Parcels;

import java.io.IOException;

/**
 * Fragment for displaying a project's general information
 */
@SuppressWarnings("unused")
public class InformationFragment extends Fragment {

    /**Tag for logging*/
    private static final String TAG = "InformationFragment";

    /**Binder object for ViewBinding*/
    private FragmentInformationBinding app;

    /**Fragment's context*/
    private Context mContext;

    /**Current logged in user*/
    private User mUser;

    /**GitHub API handler*/
    private GitHub mGitHub;

    /**Project being displayed*/
    private Project mProject;

    public InformationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Inflates the fragment's layout and sets up ViewBinding
     */
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        app = FragmentInformationBinding.inflate(inflater,container,false);
        return app.getRoot();
    }

    /**
     * Sets up the Fragment's methods
     */
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getState();

        loadProjectDetails();
    }


    /**
     * Gets the current state for the member variables.
     */
    private void getState() {

        mContext = getContext();

        mGitHub = ((OSApplication) requireActivity().getApplication()).getGitHub();

        assert getArguments() != null;
        mProject = Parcels.unwrap(getArguments().getParcelable("project"));
    }


    /**
     * Populates the fragment with the project's details
     */
    private void loadProjectDetails() {
        if(mProject!=null) {

            //Load text details
            app.textViewTitle.setText(mProject.getTitle());
            app.textViewTitle.setMaxLines(mProject.getTitle().split(" ").length);
            app.textViewViews.setText(String.valueOf(mProject.getViewCount()));
            app.textViewLikes.setText(String.valueOf(mProject.getLikeCount()));
            app.textViewTags.setText(mProject.getTags().toString().replace("[", "").replace("]", ""));
            app.textViewLanguages.setText(mProject.getLanguages().toString().replace("[", "").replace("]", ""));

            //Load Manager's name
            try {
                app.textViewAuthor.setText(String.format("by %s", mProject.getManager().fetchIfNeeded().getUsername()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //Load the project's logo
            ParseFile image = mProject.getLogoImage();
            if (image != null) {
                Glide.with(mContext)
                        .load(image.getUrl())
                        .transform(new RoundedCorners(1000))
                        .into(app.imageViewLogo);
                app.progressBar.setVisibility(View.GONE);
            }

            setLikeButton();

            //Load the project's ReadMe file
            loadReadme();
        }
    }

    /**
     * Asynchronous background task for fetching the project's readme
     * Loads the project's ReadMe file and places it into the Markdown viewer
     */
    public void loadReadme(){

        new Thread(() -> {
            try {
                Looper.prepare();

                //Get repository
                String repoLink = mProject.getRepository().split("github.com/")[1];
                GHRepository ghRepo = mGitHub.getRepository(repoLink);

                //Get ReadMe content
                GHContent readme = ghRepo.getReadme();

                //noinspection deprecation
                String content = readme.getContent();

                //Load the content into the markdown viewer
                ((Activity) mContext).runOnUiThread(() -> {
                    app.markdownViewReadme.setMarkDownText(content);
                    app.progressBarReadme.setVisibility(View.GONE);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Sets the like button's color depending on if the user has liked the project
     */
    private void setLikeButton() {
        Drawable unwrappedDrawable = AppCompatResources.getDrawable(mContext, R.drawable.ufi_heart_active);
        assert unwrappedDrawable != null;
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, mProject.isLikedByUser(mUser) ? Color.RED : ContextCompat.getColor(mContext,R.color.darker_blue));
        app.buttonLike.setImageDrawable(wrappedDrawable);
    }
}