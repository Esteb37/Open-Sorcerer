package com.example.opensorcerer.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.opensorcerer.R;
import com.example.opensorcerer.databinding.ActivityMainBinding;

import org.kohsuke.github.GitHubBuilder;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ActivityMainBinding app;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(app.getRoot());

        mContext = this;

        new LoginTask().execute();
    }

    class LoginTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            GitHubBuilder builder = new GitHubBuilder();
            return null;
        }
    }


}