package com.example.opensorcerer.ui.developer;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.opensorcerer.R;
import com.example.opensorcerer.databinding.ActivityDeveloperHomeBinding;
import com.example.opensorcerer.ui.login.LoginActivity;
import com.parse.ParseUser;

import org.kohsuke.github.GitHubBuilder;

import java.util.Objects;


public class DeveloperHomeActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ActivityDeveloperHomeBinding app;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_home);



        app = ActivityDeveloperHomeBinding.inflate(getLayoutInflater());
        setContentView(app.getRoot());


        mContext = this;

        new LoginTask().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.btnLogout){
            ParseUser.logOutInBackground(e -> {
                if(e == null){
                    Intent i = new Intent(mContext, LoginActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(mContext, "Unable to log out.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            });

        }
        return super.onOptionsItemSelected(item);
    }

    class LoginTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            GitHubBuilder builder = new GitHubBuilder();
            return null;
        }
    }




}