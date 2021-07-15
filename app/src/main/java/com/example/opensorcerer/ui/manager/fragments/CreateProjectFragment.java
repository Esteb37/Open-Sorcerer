package com.example.opensorcerer.ui.manager.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.opensorcerer.R;
import com.example.opensorcerer.application.OSApplication;
import com.example.opensorcerer.databinding.FragmentCreateProjectBinding;
import com.example.opensorcerer.models.Project;
import com.example.opensorcerer.models.users.roles.Manager;
import com.parse.ParseFile;
import com.parse.ParseRelation;

import org.jetbrains.annotations.NotNull;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.List;


public class CreateProjectFragment extends Fragment {

    private static final String TAG = "CreateProject";
    private FragmentCreateProjectBinding app;
    private Manager mUser;
    private Context mContext;
    private GitHub mGitHub;

    public CreateProjectFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        app = FragmentCreateProjectBinding.inflate(inflater,container,false);
        return app.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getContext();
        mGitHub = ((OSApplication) getActivity().getApplication()).getGitHub();
        mUser = Manager.getCurrentUser();


        app.btnQuick.setOnClickListener(v -> {
            new Thread(new GetRepoTask()).start();
        });

        app.btnCreate.setOnClickListener(v -> {
            Project project = new Project();
            project.setTitle(app.etTitle.getText().toString());
            project.setDescription(app.etShortDescription.getText().toString());
            project.setReadme(app.etReadme.getText().toString());
            project.setManager(mUser);
            project.setRepository(app.etRepo.getText().toString());
            List<String> languages = Arrays.asList(app.etLanguages.getText().toString().split(","));
            project.setLanguages(languages);
            List<String> tags = Arrays.asList(app.etTags.getText().toString().split(","));
            project.setTags(tags);

            new Thread(() -> {
                ParseFile logoImage = bitmapToParseFile(getBitmapFromURL(app.etImage.getText().toString()));
                project.setLogoImage(logoImage);
                project.saveInBackground(e -> {
                    if(e==null){
                        ParseRelation<Project> projects = mUser.getProjects();
                        projects.add(project);
                        mUser.setProjects(projects);
                        mUser.getHandler().saveInBackground(e1 -> {
                            if(e1==null){
                                final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.flContainer,new MyProjectsFragment()).commit();
                            } else {
                                Log.d(TAG,"Error saving project in user's project list.");
                                e1.printStackTrace();
                            }
                        });

                    } else {
                        Log.d(TAG,"Error saving project.");
                        e.printStackTrace();
                    }
                });
            }).start();
        });

        (app.etImage).setOnEditorActionListener(
                (v, actionId, event) -> {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                            actionId == EditorInfo.IME_ACTION_DONE ||
                            event != null &&
                                    event.getAction() == KeyEvent.ACTION_DOWN &&
                                    event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        if (event == null || !event.isShiftPressed()) {
                            Glide.with(mContext)
                                    .load(app.etImage.getText().toString())
                                    .fitCenter()
                                    .transform(new RoundedCorners(1000))
                                    .into(app.ivLogo);
                            return true; // consume.
                        }
                    }
                    return false; // pass on to other listeners.
                }
        );
    }

    private class GetRepoTask implements Runnable {

        @Override
        public void run() {
            String repoLink = app.etRepo.getText().toString().split("github.com/")[1];
            try {
                Looper.prepare();
                GHRepository ghRepo = mGitHub.getRepository(repoLink);
                app.etTitle.setText(ghRepo.getName());
                app.etReadme.setText(ghRepo.getReadme().getHtmlUrl());
                app.etShortDescription.setText(ghRepo.getDescription());
                String tags = ghRepo.listTopics().toString().replace("[","").replace("]","");;
                String languages = ghRepo.listLanguages().keySet().toString().replace("[","").replace("]","");;
                getActivity().runOnUiThread(() -> {
                    app.etLanguages.setText(languages);
                    app.etTags.setText(tags);
                });
            } catch (IOException e) {
                Toast.makeText(mContext, "Invalid Repo Link", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    public Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ParseFile bitmapToParseFile(Bitmap imageBitmap){
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        ParseFile parseFile = new ParseFile("logo.png",imageByte);
        return parseFile;
    }

}