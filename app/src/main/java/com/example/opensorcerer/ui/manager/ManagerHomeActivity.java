package com.example.opensorcerer.ui.manager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;

import com.example.opensorcerer.R;
import com.example.opensorcerer.databinding.ActivityManagerHomeBinding;
import com.example.opensorcerer.ui.developer.DeveloperHomeActivity;

import org.kohsuke.github.GitHubBuilder;

public class ManagerHomeActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";
    private ActivityManagerHomeBinding app;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_home);


        app = ActivityManagerHomeBinding.inflate(getLayoutInflater());
        setContentView(app.getRoot());


        mContext = this;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }


    class LoginTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            GitHubBuilder builder = new GitHubBuilder();
            return null;
        }
    }

}