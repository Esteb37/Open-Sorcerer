package com.example.opensorcerer.ui.main.details.external;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import com.example.opensorcerer.R;
import com.example.opensorcerer.application.OSApplication;
import com.example.opensorcerer.databinding.FragmentInformationBinding;
import com.example.opensorcerer.models.Project;
import com.example.opensorcerer.models.Tools;
import com.parse.ParseException;
import com.parse.ParseFile;

import org.jetbrains.annotations.NotNull;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.IOException;
import java.util.List;

/**
 * Fragment for displaying a project's general information
 */
public class InformationFragment extends Fragment {


    /**
     * Project being displayed
     */
    private final Project mProject;

    /**
     * Binder object for ViewBinding
     */
    private FragmentInformationBinding mApp;

    /**
     * Fragment's context
     */
    private Context mContext;

    /**
     * GitHub API handler
     */
    private GitHub mGitHub;

    private int mForkCount;

    public InformationFragment(Project project, int forkCount) {
        mProject = project;

        mForkCount = forkCount;
    }

    public InformationFragment(Project project) {
        mProject = project;
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
        mApp = FragmentInformationBinding.inflate(inflater, container, false);
        return mApp.getRoot();
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

        requireActivity().findViewById(R.id.bottomNavDetails).setVisibility(View.VISIBLE);
    }

    /**
     * Populates the fragment with the project's details
     */
    private void loadProjectDetails() {
        if (mProject != null) {

            //Load text details
            mApp.textViewTitle.setText(mProject.getTitle());
            mApp.textViewTitle.setMaxLines(mProject.getTitle().split(" ").length);
            mApp.textViewViews.setText(String.valueOf(mProject.getViewCount()));
            mApp.textViewLikes.setText(String.valueOf(mProject.getLikeCount()));
            mApp.textViewSwipes.setText(String.valueOf(mProject.getSwipeCount()));
            mApp.textViewDescription.setText(mProject.getDescription());

            List<String> tags = mProject.getTags();
            if (tags == null || tags.size() == 0) {
                mApp.groupTags.setVisibility(View.GONE);
            } else {
                mApp.groupTags.setVisibility(View.VISIBLE);
                mApp.textViewTags.setText(Tools.listToString(tags));
                mApp.textViewTags.post(() -> mApp.textViewTags.setMoreMessage(mApp.textViewMoreTags));
            }


            //Load the list of languages to an expandable view
            List<String> languages = mProject.getLanguages();
            if (languages == null || languages.size() == 0) {
                mApp.groupLanguages.setVisibility(View.GONE);
            } else {
                mApp.groupLanguages.setVisibility(View.VISIBLE);
                mApp.textViewLanguages.setText(Tools.listToString(languages));
                mApp.textViewLanguages.post(() -> mApp.textViewLanguages.setMoreMessage(mApp.textViewMoreLanguages));
            }


            //Load Manager's name
            try {
                mApp.textViewAuthor.setText(String.format("by %s", mProject.getManager().fetchIfNeeded().getUsername()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            mApp.textViewForks.setText(String.valueOf(mForkCount));

            loadProjectLogo();

            setLikeButton();

            //Load the project's ReadMe file
            loadReadme();
        }
    }

    /**
     * Loads the project's logo image
     */
    private void loadProjectLogo() {

        // Load the project's logo from URL if any or the image file if no URL is provided
        String imageURL = mProject.getLogoImageUrl();
        ParseFile imageFile = mProject.getLogoImage();
        if (imageURL != null) {
            Tools.loadImageFromURL(mContext, imageURL, mApp.imageViewLogo);
        } else if (imageFile != null) {
            Tools.loadImageFromFile(mContext, imageFile, mApp.imageViewLogo);
        }

        mApp.progressBar.setVisibility(View.GONE);
    }

    /**
     * Loads the project's ReadMe file and places it into the Markdown viewer
     */
    private void loadReadme() {

        new Thread(() -> {
            try {
                Looper.prepare();

                //Get repository
                GHRepository ghRepo = mGitHub.getRepository(mProject.getRepositoryName());

                //Get ReadMe content
                GHContent readme = ghRepo.getReadme();

                //noinspection deprecation
                String content = readme.getContent();

                //Load the content into the markdown viewer
                ((Activity) mContext).runOnUiThread(() -> {
                    mApp.markdownViewReadme.setMarkDownText(content);
                    mApp.progressBarReadme.setVisibility(View.GONE);
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

        DrawableCompat.setTint(wrappedDrawable, mProject.isLikedByUser()
                ? Color.RED
                : ContextCompat.getColor(mContext, R.color.darker_blue));

        mApp.buttonLike.setImageDrawable(wrappedDrawable);
    }

    public void setForkCount(int forkCount) {
        mForkCount = forkCount;
        try {
            mApp.textViewForks.setText(String.valueOf(mForkCount));
        } catch (NullPointerException ignored) {
        }
    }
}