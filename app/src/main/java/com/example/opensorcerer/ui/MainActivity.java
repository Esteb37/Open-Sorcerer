package com.example.opensorcerer.ui;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.opensorcerer.R;

import org.kohsuke.github.GitHubBuilder;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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