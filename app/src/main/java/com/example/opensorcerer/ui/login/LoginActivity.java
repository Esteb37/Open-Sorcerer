package com.example.opensorcerer.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.opensorcerer.R;
import com.example.opensorcerer.databinding.ActivityLoginBinding;
import com.example.opensorcerer.models.users.User;
import com.example.opensorcerer.ui.developer.DeveloperHomeActivity;
import com.example.opensorcerer.ui.manager.ManagerHomeActivity;
import com.parse.ParseUser;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class LoginActivity extends AppCompatActivity {


    private static final String TAG = "LoginActivity";
    private ActivityLoginBinding app;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = this;

        if(ParseUser.getCurrentUser()!=null){
            String role = User.fromParseUser(ParseUser.getCurrentUser()).getRole();
            navigateToMain(role);
        }

        app = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(app.getRoot());

        app.btnLogin.setOnClickListener(v -> {
            String username = app.etUsername.getText().toString();
            String password = app.etPassword.getText().toString();
            ParseUser.logInInBackground(username, password, (user, e) -> {

                //If the login is successful
                if(e==null){
                    String role = User.fromParseUser(ParseUser.getCurrentUser()).getRole();
                    navigateToMain(role);
                } else{
                    Log.d(TAG,"Issue with login",e);

                    //Notify the user that the login was unsuccessful
                    Toast.makeText(mContext,"Username or password is incorrect.",Toast.LENGTH_SHORT).show();
                }
            });
        });

    }

    private void navigateToMain(String role) {
        Intent i = null;
        if(role.equals("developer")){
            i = new Intent(this, DeveloperHomeActivity.class);
        } else if (role.equals("manager")) {
            i = new Intent(this, ManagerHomeActivity.class);
        }
        startActivity(i);
        finish();
    }

}